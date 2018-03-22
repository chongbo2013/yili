package com.lots.travel.main.info.mine;

import android.content.Intent;
import android.view.View;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseFragment;
import com.lots.travel.base.WebViewActivity;
import com.lots.travel.main.info.mine.adapter.MineCenterAdapter;
import com.lots.travel.util.Constant;
import com.lots.travel.widget.LinearItemsLayout;

/**
 * Created by nalanzi on 2017/9/26.
 */

public class MineCenterFragment extends RxBaseFragment implements LinearItemsLayout.OnItemClickListener{

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine_center;
    }

    @Override
    protected void initView(View root) {
        LinearItemsLayout itemsLayout = (LinearItemsLayout) root.findViewById(R.id.items_layout);
        itemsLayout.removeAllViews();
        MineCenterAdapter adapter = new MineCenterAdapter(getActivity());
        itemsLayout.setOnItemClickListener(this);
        itemsLayout.setAdapter(adapter);
    }

    @Override
    protected void initData() {}

    @Override
    public void onItemClick(int type) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        switch (type){
            case MineCenterAdapter.ITEM_ORDER:
                WebViewActivity.toWeb(getContext(),getString(R.string.mine_content_order),Constant.PATH_MINE_ORDER,false);
                break;

            case MineCenterAdapter.ITEM_TRIP:
                WebViewActivity.toWeb(getContext(),getString(R.string.mine_content_trip), Constant.PATH_MINE_TRIP,false);
                break;

            case MineCenterAdapter.ITEM_ACTIVITY:
                WebViewActivity.toWeb(getContext(),getString(R.string.mine_content_activity),Constant.PATH_MINE_ACTIVITY,false);
                break;

            case MineCenterAdapter.ITEM_CROW_FUNDING:
//                WebViewActivity.toWeb(getContext(),getString(R.string.mine_content_crowd_funding),,false);
                break;

            case MineCenterAdapter.ITEM_REPLAY:
//                intent.setClass(getActivity(), MyRewardActivity.class);
//                toCut(intent);
                break;

            case MineCenterAdapter.ITEM_WALLET:
//                intent.setClass(getActivity(), MyWalletActivity.class);
//                toCut(intent);
                break;

            case MineCenterAdapter.ITEM_FAVORITE:
                intent.setClass(getActivity(), MineFollowItemsActivity.class);
                startActivity(intent);
                break;

            case MineCenterAdapter.ITEM_WANT_DST:
                intent.setClass(getActivity(), MineFollowPlacesActivity.class);
                startActivity(intent);
                break;

            case MineCenterAdapter.ITEM_FOLLOW:
                intent.setClass(getActivity(), MineFollowPeoplesActivity.class);
                startActivity(intent);
                break;

            case MineCenterAdapter.ITEM_LABEL:
                intent.setClass(getActivity(), MineTravelTagsActivity.class);
                startActivity(intent);
                break;

            case MineCenterAdapter.ITEM_GROUP:
//                intent.setClass(getActivity(), MyGroupActivity.class);
//                toCut(intent);
                break;
        }

    }
}
