package com.lots.travel.main.info.mine;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lots.travel.main.info.mine.adapter.MineFollowTripsAdapter;
import com.lots.travel.main.info.mine.model.FollowTrip;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.model.request.GetFollowTrips;
import com.lots.travel.network.service.CommonService;
import com.lots.travel.util.DensityUtil;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;
import com.lots.travel.widget.refresh.PageIterator;
import com.lots.travel.widget.refresh.PagedItemFragment;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by nalanzi on 2018/1/12.
 */

public class MineFollowTripsFragment extends PagedItemFragment<FollowTrip> {

    @Override
    public void onConfigureDisplay(ConfigureAdapter configureAdapter) {
        super.onConfigureDisplay(configureAdapter);
        configureAdapter.addItemDecoration(new SpaceItemDecoration(DensityUtil.dp2px(getContext(),5)));
    }

    @Override
    public PageIterator.PageRequest<FollowTrip> createPageRequest() {
        return new PageIterator.PageRequest<FollowTrip>() {
            @Override
            public Single<HttpResult<List<FollowTrip>>> execute(int page) {
                return ServiceGenerator.createService(getContext(), CommonService.class)
                        .getFollowTrips(new GetFollowTrips(page,6));
            }
        };
    }

    @Override
    public AbstractLoadAdapter<FollowTrip> createAdapter(RecyclerView rv) {
        return new MineFollowTripsAdapter(getContext(),rv);
    }

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int mSpace;

        public SpaceItemDecoration(int space){
            mSpace = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            AbstractLoadAdapter adapter = (AbstractLoadAdapter) parent.getAdapter();
            if(adapter==null)
                return;
            int count = adapter.getDataItemCount();
            int pos = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();

            if(pos==count){
                outRect.top = mSpace;
                outRect.bottom = mSpace;
            }else{
                outRect.top = pos==0 ? mSpace:0;
                outRect.bottom = pos==count-1 ? mSpace:0;
            }
        }

    }

}
