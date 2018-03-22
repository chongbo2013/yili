package com.lots.travel.main.info.mine;

import android.support.v7.widget.RecyclerView;

import com.lots.travel.main.info.mine.adapter.MineFollowCitiesAdapter;
import com.lots.travel.main.info.mine.model.FollowPlace;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.model.request.GetFollowPlaces;
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

public class MineFollowCitiesFragment extends PagedItemFragment<FollowPlace> {

    @Override
    public void onConfigureDisplay(ConfigureAdapter configureAdapter) {
        super.onConfigureDisplay(configureAdapter);
        configureAdapter.addItemDecoration(new VerticalLinearSpaceItemDecoration(DensityUtil.dp2px(getContext(),10)));
    }

    @Override
    public PageIterator.PageRequest<FollowPlace> createPageRequest() {
        return new PageIterator.PageRequest<FollowPlace>() {
            @Override
            public Single<HttpResult<List<FollowPlace>>> execute(int page) {
                return ServiceGenerator.createService(getContext(), CommonService.class)
                        .getFollowPlaces(new GetFollowPlaces(1,10,GetFollowPlaces.VIEW_TYPE_CITY));
            }
        };
    }

    @Override
    public AbstractLoadAdapter<FollowPlace> createAdapter(RecyclerView rv) {
        return new MineFollowCitiesAdapter(getContext(),rv);
    }

}
