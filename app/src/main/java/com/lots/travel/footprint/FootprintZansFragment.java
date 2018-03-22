package com.lots.travel.footprint;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseFragment;
import com.lots.travel.footprint.model.CommentParams;
import com.lots.travel.widget.EmptySupportRecyclerView;

/**
 * Created by nalanzi on 2018/1/24.
 */

public class FootprintZansFragment extends RxBaseFragment{
    private static final String COMMENT_PARAMS = "CommentParams";

    public static FootprintZansFragment create(CommentParams params){
        FootprintZansFragment fragment = new FootprintZansFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(COMMENT_PARAMS,params);
        fragment.setArguments(bundle);
        return fragment;
    }

    private CommentParams mCommentParams;

    private EmptySupportRecyclerView rvContent;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        rvContent.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        });
    }

    @Override
    protected void initData() {

    }

    public void refresh(){}

}
