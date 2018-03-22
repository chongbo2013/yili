package com.lots.travel.footprint;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseFragment;
import com.lots.travel.footprint.model.CommentParams;
import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.model.request.GetComments;
import com.lots.travel.network.model.result.Comments;
import com.lots.travel.network.service.CommonService;
import com.lots.travel.util.DensityUtil;
import com.lots.travel.widget.EmptySupportRecyclerView;
import com.lots.travel.widget.VerticalDividerDecoration;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nalanzi on 2018/1/24.
 */

public class FootprintCommentsFragment extends RxBaseFragment{
    private static final String COMMENT_PARAMS = "CommentParams";

    public static FootprintCommentsFragment create(CommentParams params){
        FootprintCommentsFragment fragment = new FootprintCommentsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(COMMENT_PARAMS,params);
        fragment.setArguments(bundle);
        return fragment;
    }

    private CommentParams mCommentParams;

    private EmptySupportRecyclerView rvContent;
    private FootprintCommentsAdapter mAdapter;

    private AddFootprintCommentFragment.OnChooseTargetListener mOnChooseTargetListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnChooseTargetListener = (AddFootprintCommentFragment.OnChooseTargetListener) context;
        Bundle bundle = getArguments();
        mCommentParams = bundle.getParcelable(COMMENT_PARAMS);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_footprint_comments;
    }

    @Override
    protected void initView(View root) {
        TextView tvEmpty = (TextView) root.findViewById(R.id.tv_empty);
        rvContent = (EmptySupportRecyclerView) root.findViewById(R.id.rv_content);
        rvContent.setEmptyView(tvEmpty);
        rvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new FootprintCommentsAdapter(getContext());
        rvContent.setAdapter(mAdapter);
        rvContent.addItemDecoration(
                new VerticalDividerDecoration(
                    DensityUtil.dp2px(getContext(),0.5f),
                    ContextCompat.getColor(getContext(),R.color.color_divider)
                )
        );
        mAdapter.setOnItemClickListener(new FootprintCommentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int flatPosition, Comments.Item comment) {
                mOnChooseTargetListener.onChooseTarget(comment);
            }
        });
    }

    @Override
    protected void initData() {
        requestComments();
    }

    public void refresh(){
        requestComments();
    }

    private void requestComments(){
        GetComments params = new GetComments();
        params.setPageNo(1);
        params.setPageSize(10);
        params.setDataTable(mCommentParams.getEventStyle());
        params.setDataKey(mCommentParams.getId());
        params.setEventType(GetComments.COMMENT_EVENT_PL);
        ServiceGenerator.createService(getContext(), CommonService.class)
                .getComments(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<HttpResult<Comments>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new SingleObserver<HttpResult<Comments>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {}

                    @Override
                    public void onSuccess(@NonNull HttpResult<Comments> result) {
                        Comments data = result.getData();
                        if(data!=null)
                            mAdapter.setComments(data.getList());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {}
                });
    }

}
