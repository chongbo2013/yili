package com.lots.travel.widget.refresh;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;

import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lots.travel.R;

import java.util.List;
import java.util.concurrent.Executors;

public abstract class PagedItemFragment_Old<E> extends Fragment implements FixedSwipeRefreshLayout.OnRefreshListener {
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

    private View rootView;

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
            if(lastVisible==itemsAdapter.getItemCount()-1){
                loading = true;
                itemsAdapter.onLoading();
                //开始加载数据，数据加载成功以后修改加载状态
                loadData();
            }
        }
    };

    private Handler retHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0){
                loading = false;
                List<E> data = (List<E>) msg.obj;
                if(data==null || data.isEmpty()){
                    noMore = true;
                    itemsAdapter.onNoMore();
                }else{
                    itemsAdapter.onIdle();
                    itemsAdapter.onLoadCompleted(data);
                }
            }else if(msg.what==1){
                itemsAdapter.onRefreshed((List<E>) msg.obj);

                if(swipeRefresh.isRefreshing())
                    swipeRefresh.setRefreshing(false);

                if(forceRefreshing) {
                    progressBarView.setVisibility(View.GONE);
                }
            }
        }
    };

    //替换为rxjava
    private void loadData(){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List<E> data = pageIterator.next();
                Message.obtain(retHandler,0,data).sendToTarget();
            }
        });
    }

    private void refreshData(){
        Executors.newSingleThreadExecutor().execute(new Runnable() {

            @Override
            public void run() {
                pageIterator.resetNotReplaceRequest();
                List<E> data = pageIterator.next();
                Message.obtain(retHandler,1,data).sendToTarget();
            }
        });
    }

    /**
     * 强制重新刷新，此时PageIterator重新获取
     * 例如，查询景点，最开始使用的是查询条件是全国，之后修改查询条件为某特定区域，此时需要调用forceRefresh重新获取PageIterator
     * ！！！如果在Activity中调用该方法，必须在onResume()调用以后。
     */
    public void forceRefresh(){
        //重新获取
        forceRefreshing = true;
        emptyView.setVisibility(View.INVISIBLE);
        pageIterator.reset(createPageRequest());
        //swipeRefresh.setRefreshing(true);
        progressBarView.setVisibility(View.VISIBLE);
        refreshData();
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pageIterator = new PageIterator<>(createPageRequest());

        //refresh
        swipeRefresh = (FixedSwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.turquoise);
        swipeRefresh.setOnRefreshListener(this);

        //recyclerview
        rvContent = (RecyclerView) view.findViewById(R.id.rv_content);
        rvContent.setLayoutManager(new LinearLayoutManager(getActivity()));

        refreshContainer = (FrameLayout) view.findViewById(R.id.refresh_container);
        emptyView = view.findViewById(R.id.refresh_default_empty);
        progressBarView = view.findViewById(R.id.refresh_default_progressbar);

        //adapter、empty
        itemsAdapter = createAdapter();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView==null){
            rootView = inflater.inflate(R.layout.fragment_paged_item, container, false);
        }else{
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    @Override
    public void onDestroy() {
        rvContent.removeOnScrollListener(loadScrollListener);
        super.onDestroy();
    }

    public abstract AbstractLoadAdapter<E> createAdapter();

    public AbstractLoadAdapter<E> getAdapter(){
        return itemsAdapter;
    }

    public void onConfigureDisplay(ConfigureAdapter configureAdapter){}

    public class ConfigureAdapter {

        public ConfigureAdapter setColorSchemeResources(@ColorRes int... colorResIds){
            swipeRefresh.setColorSchemeResources(colorResIds);
            return this;
        }

        public ConfigureAdapter setEmptyView(View view,FrameLayout.LayoutParams lp){
            if(emptyView!=null){
                refreshContainer.removeView(emptyView);
            }
            refreshContainer.addView(view,lp);
            emptyView = view;
            return this;
        }

        public ConfigureAdapter setProgressView(View view,FrameLayout.LayoutParams lp){
            if(progressBarView!=null){
                refreshContainer.removeView(progressBarView);
            }
            refreshContainer.addView(view,lp);
            progressBarView = view;
            return this;
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
