package com.lots.travel.widget.refresh;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.lots.travel.R;
import com.lots.travel.base.BaseFragment;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by nalanzi on 2017/11/4.
 */

public abstract class PagedItemFragment<E> extends BaseFragment implements FixedSwipeRefreshLayout.OnRefreshListener {
    private AbstractLoadAdapter<E> itemsAdapter;

    private FixedSwipeRefreshLayout swipeRefresh;
    private RecyclerView rvContent;

    private FrameLayout refreshContainer;
    private View emptyView;
    private View progressBarView;

    private boolean loading = false;
    private boolean noMore = false;

    private boolean forceRefreshing = false;
    private PageIterator<E> pageIterator;

    public abstract PageIterator.PageRequest<E> createPageRequest();

    private RecyclerView.OnScrollListener loadScrollListener = new RecyclerView.OnScrollListener(){
        @Override
        public void onScrolled(RecyclerView rv, int dx, int dy) {

            if(itemsAdapter==null || loading)
                return;

            if(noMore){
                return;
            }

            //上下都没有足够的空间滑动
            if(!rv.canScrollVertically(-1)
                    && !rv.canScrollVertically(1)) {
                return;
            }

            int lastVisible = getLastVisiblePosition(rv);
            if(lastVisible==itemsAdapter.getItemCount()-2){
                loading = true;
                itemsAdapter.onLoading();
                //开始加载数据，数据加载成功以后修改加载状态
                loadData();
            }
        }
    };

    private static final int MSG_LOAD_COMPLETED = 0;
    private static final int MSG_LOAD_FAILED = 1;
    private static final int MSG_REFRESH_COMPLETED = 2;
    private static final int MSG_REFRESH_FAILED = 3;

    private Handler resultHandler = new ResultHandler<>(this);

    //替换为rxjava
    private void loadData(){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<E> data = pageIterator.next();
                    Message.obtain(resultHandler,MSG_LOAD_COMPLETED,data).sendToTarget();
                } catch (Exception e) {
                    Message.obtain(resultHandler,MSG_LOAD_FAILED,e).sendToTarget();
                }
            }
        });
    }

    private void refreshData(){
        Executors.newSingleThreadExecutor().execute(new Runnable() {

            @Override
            public void run() {
                try {
                    List<E> data = pageIterator.next();
                    Message.obtain(resultHandler,MSG_REFRESH_COMPLETED,data).sendToTarget();
                } catch (Exception e) {
                    Message.obtain(resultHandler,MSG_REFRESH_FAILED,e).sendToTarget();
                }
            }
        });
    }

    /**
     * 强制重新刷新，此时PageIterator重新获取
     * 例如，查询景点，最开始使用的是查询条件是全国，之后修改查询条件为某特定区域，此时需要调用forceRefresh重新获取PageIterator
     * ！！！如果在Activity中调用该方法，必须在onResume()调用以后。
     */
    public void forceRefresh(){
        noMore = false;
        //重新获取
        forceRefreshing = true;
        swipeRefresh.setAvoidRefresh(true);
        emptyView.setVisibility(View.INVISIBLE);
        pageIterator.reset(createPageRequest());
        //swipeRefresh.setRefreshing(true);
        progressBarView.setVisibility(View.VISIBLE);
        refreshData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_paged_item;
    }

    @Override
    protected void initView(View root) {
        pageIterator = new PageIterator<>(createPageRequest());

        //refresh
        swipeRefresh = (FixedSwipeRefreshLayout) root.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.turquoise);
        swipeRefresh.setOnRefreshListener(this);

        //recyclerview
        rvContent = (RecyclerView) root.findViewById(R.id.rv_content);
        rvContent.setLayoutManager(new LinearLayoutManager(getActivity()));

        refreshContainer = (FrameLayout) root.findViewById(R.id.refresh_container);
        emptyView = root.findViewById(R.id.refresh_default_empty);
        progressBarView = root.findViewById(R.id.refresh_default_progressbar);

        //adapter、empty
        itemsAdapter = createAdapter(rvContent);
        if(itemsAdapter!=null){
            rvContent.setAdapter(itemsAdapter);

            itemsAdapter.registerAdapterDataObserver(emptyObserver);
            emptyObserver.onChanged();

            itemsAdapter.onIdle();
        }
        //自定义
        onConfigureDisplay(new ConfigureAdapter());

        rvContent.addOnScrollListener(loadScrollListener);
    }

    @Override
    protected void initData() {
        forceRefresh();
    }

    @Override
    public void onDestroy() {
        if(rvContent!=null)
            rvContent.removeOnScrollListener(loadScrollListener);

        resultHandler.removeMessages(MSG_LOAD_COMPLETED);
        resultHandler.removeMessages(MSG_LOAD_FAILED);
        resultHandler.removeMessages(MSG_REFRESH_COMPLETED);
        resultHandler.removeMessages(MSG_REFRESH_FAILED);
        super.onDestroy();
    }

    public abstract AbstractLoadAdapter<E> createAdapter(RecyclerView rv);

    public AbstractLoadAdapter<E> getAdapter(){
        return itemsAdapter;
    }

    public void onConfigureDisplay(ConfigureAdapter configureAdapter){}

    @Override
    public void onRefresh() {
        noMore = false;
        pageIterator.resetNotReplaceRequest();
        refreshData();
    }

    public class ConfigureAdapter {

        public ConfigureAdapter setColorSchemeResources(@ColorRes int... colorResIds){
            swipeRefresh.setColorSchemeResources(colorResIds);
            return this;
        }

        public ConfigureAdapter setEmptyView(View view, FrameLayout.LayoutParams lp){
            if(emptyView!=null){
                refreshContainer.removeView(emptyView);
            }
            refreshContainer.addView(view,lp);
            emptyView = view;
            return this;
        }

        public ConfigureAdapter setProgressView(View view, FrameLayout.LayoutParams lp){
            if(progressBarView!=null){
                refreshContainer.removeView(progressBarView);
            }
            refreshContainer.addView(view,lp);
            progressBarView = view;
            return this;
        }

        public ConfigureAdapter addItemDecoration(RecyclerView.ItemDecoration decoration){
            rvContent.addItemDecoration(decoration);
            return this;
        }

        public void setRefreshEnabled(boolean enabled){
            swipeRefresh.setEnabled(enabled);
        }

        public void setLayoutManager(RecyclerView.LayoutManager layoutManager){
            rvContent.setLayoutManager(layoutManager);
        }

        public void addOnScrollListener(RecyclerView.OnScrollListener listener){
            rvContent.addOnScrollListener(listener);
        }

    }

    private int getLastVisiblePosition(RecyclerView rv) {
        int position;
        if (rv.getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) rv.getLayoutManager()).findLastVisibleItemPosition();
        } else if (rv.getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) rv.getLayoutManager()).findLastVisibleItemPosition();
        } else if (rv.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) rv.getLayoutManager();
            int[] lastPositions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = rv.getLayoutManager().getItemCount() - 1;
        }
        return position;
    }

    private int getMaxPosition(int[] positions) {
        int maxPosition = Integer.MIN_VALUE;
        for (int position : positions) {
            maxPosition = Math.max(maxPosition, position);
        }
        return maxPosition;
    }

    private RecyclerView.AdapterDataObserver emptyObserver = new RecyclerView.AdapterDataObserver() {

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            changed();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            changed();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            changed();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            changed();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            changed();
        }

        @Override
        public void onChanged() {
            changed();
        }

    };

    private static class ResultHandler<E> extends Handler{
        private WeakReference<PagedItemFragment<E>> host;

        ResultHandler(PagedItemFragment<E> host){
            this.host = new WeakReference<>(host);
        }

        @Override
        public void handleMessage(Message msg) {
            PagedItemFragment<E> fragment = host.get();
            if(fragment==null)
                return;

            switch(msg.what){
                case MSG_LOAD_COMPLETED: {
                    fragment.loading = false;
                    List<E> data = (List<E>) msg.obj;
                    if(data==null || data.isEmpty()){
                        fragment.noMore = true;
                        fragment.itemsAdapter.onNoMore();
                    }else{
                        fragment.itemsAdapter.onIdle();
                        fragment.itemsAdapter.onLoadCompleted(data);
                    }
                }
                break;

                case MSG_REFRESH_COMPLETED: {
                    fragment.swipeRefresh.setAvoidRefresh(false);
                    fragment.itemsAdapter.onRefreshed((List<E>) msg.obj);

                    if(fragment.swipeRefresh.isRefreshing())
                        fragment.swipeRefresh.setRefreshing(false);

                    if(fragment.forceRefreshing) {
                        fragment.progressBarView.setVisibility(View.GONE);
                    }
                }
                break;

                case MSG_LOAD_FAILED: {
                    fragment.loading = false;
                    fragment.itemsAdapter.onIdle();
                    fragment.itemsAdapter.onLoadCompleted(null);
                    Context context = fragment.getContext();
                    if(context!=null)
                        Toast.makeText(context,"加载失败",Toast.LENGTH_SHORT).show();
                }
                break;

                case MSG_REFRESH_FAILED: {
                    fragment.swipeRefresh.setAvoidRefresh(false);
                    if(fragment.swipeRefresh.isRefreshing())
                        fragment.swipeRefresh.setRefreshing(false);
                    if(fragment.forceRefreshing) {
                        fragment.progressBarView.setVisibility(View.GONE);
                    }
                    Context context = fragment.getContext();
                    if(context!=null)
                        Toast.makeText(context,"刷新失败",Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

    }

    private void changed(){
        if(itemsAdapter != null && emptyView != null){
            if(itemsAdapter.getDataItemCount() == 0){
                emptyView.setVisibility(View.VISIBLE);
            }else{
                emptyView.setVisibility(View.GONE);
            }

        }
    }

}
