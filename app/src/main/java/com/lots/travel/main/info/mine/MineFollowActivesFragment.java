package com.lots.travel.main.info.mine;

import android.support.v7.widget.RecyclerView;

import com.lots.travel.main.info.mine.adapter.MineFollowActivesAdapter;
import com.lots.travel.main.info.mine.model.FollowActive;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.model.request.GetFollowActives;
import com.lots.travel.network.service.CommonService;
import com.lots.travel.util.DensityUtil;
import com.lots.travel.widget.VerticalLinearSpaceItemDecoration;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;
import com.lots.travel.widget.refresh.PageIterator;
import com.lots.travel.widget.refresh.PagedItemFragment;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by nalanzi on 2018/1/12.
 */
/*
  GradientDrawable myGrad = (GradientDrawable)view.getBackground();
  myGrad.setColor(color);
  根据不同的activeStyle绘制背景
 */
public class MineFollowActivesFragment extends PagedItemFragment<FollowActive> {

    @Override
    public void onConfigureDisplay(ConfigureAdapter configureAdapter) {
        super.onConfigureDisplay(configureAdapter);
        configureAdapter.addItemDecoration(new VerticalLinearSpaceItemDecoration(DensityUtil.dp2px(getContext(),10)));
    }

    @Override
    public PageIterator.PageRequest<FollowActive> createPageRequest() {
        return new PageIterator.PageRequest<FollowActive>() {
            @Override
            public Single<HttpResult<List<FollowActive>>> execute(int page) {
                return ServiceGenerator.createService(getContext(), CommonService.class)
                        .getFollowActives(new GetFollowActives(page,6));
            }
        };
    }

    @Override
    public AbstractLoadAdapter<FollowActive> createAdapter(RecyclerView rv) {
        return new MineFollowActivesAdapter(getContext(),rv);
    }

}
