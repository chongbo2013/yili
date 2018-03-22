package com.lots.travel.footprint;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lots.travel.R;
import com.lots.travel.footprint.model.CommentParams;
import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.network.model.result.Comments;

/**
 * Created by nalanzi on 2018/1/24.
 */

public class AddFootprintCommentFragment extends Fragment{
    private static final String COMMENT_PARAMS = "CommentParams";

    public static AddFootprintCommentFragment create(CommentParams params){
        AddFootprintCommentFragment fragment = new AddFootprintCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(COMMENT_PARAMS,params);
        fragment.setArguments(bundle);
        return fragment;
    }

    private CommentParams mCommentParams;

    private ViewPager vPager;
    private TabLayout tabNav;
    private FragmentStatePagerAdapter mAdapter;

    private FootprintZansFragment mZansFragment;
    private FootprintCommentsFragment mCommentsFragment;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        mCommentParams = bundle.getParcelable(COMMENT_PARAMS);
    }

    public void refreshComments(int count){
        mCommentParams.setCommentTotles(count);
        TabLayout.Tab tab = tabNav.getTabAt(1);
        if(tab==null)
            return;
        tab.setText(String.format("评论(%s)", count));
        mCommentsFragment.refresh();
    }

    public void refreshZans(int count){
        mCommentParams.setZanTotles(count);
        TabLayout.Tab tab = tabNav.getTabAt(0);
        if(tab==null)
            return;
        tab.setText(String.format("赞(%s)", count));
        mZansFragment.refresh();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_footprint_comment,container,false);
        vPager = (ViewPager) root.findViewById(R.id.v_pager);
        tabNav = (TabLayout) root.findViewById(R.id.tab_nav);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mAdapter==null){
            mZansFragment = FootprintZansFragment.create(mCommentParams);
            mCommentsFragment = FootprintCommentsFragment.create(mCommentParams);

            vPager.setAdapter(mAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    switch (position) {
                        case 0:
                            return mZansFragment;

                        case 1:
                            return mCommentsFragment;

                    }
                    return null;
                }

                @Override
                public int getCount() {
                    return 2;
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    switch (position) {
                        case 0:
                            return String.format("赞(%s)", mCommentParams.getZanTotles());
                        case 1:
                            return String.format("评论(%s)", mCommentParams.getCommentTotles());
                    }
                    return null;
                }
            });
            tabNav.setupWithViewPager(vPager);
            vPager.setCurrentItem(1);
        }else{
            vPager.setAdapter(mAdapter);
        }
    }

    public interface OnChooseTargetListener{
        void onChooseTarget(Comments.Item target);
    }

}
