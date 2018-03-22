package com.lots.travel.main.info.mine;

import android.support.v7.widget.RecyclerView;

import com.lots.travel.footprint.AddFootprintCommentActivity;
import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.main.info.mine.adapter.MineFollowNotesAdapter;
import com.lots.travel.main.info.mine.model.FollowNote;
import com.lots.travel.main.info.person.PersonMainActivity;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.SingleAdapter;
import com.lots.travel.network.model.request.AddZan;
import com.lots.travel.network.model.request.GetFollowNotes;
import com.lots.travel.network.service.CommonService;
import com.lots.travel.util.DensityUtil;
import com.lots.travel.video.VideoUtil;
import com.lots.travel.widget.VerticalLinearSpaceItemDecoration;
import com.lots.travel.widget.images.LookUpPictureActivity;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;
import com.lots.travel.widget.refresh.PageIterator;
import com.lots.travel.widget.refresh.PagedItemFragment;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nalanzi on 2018/1/12.
 */

public class MineFollowNotesFragment extends PagedItemFragment<FollowNote> implements MineFollowNotesAdapter.OnFollowNotesListener {
    private static final int REQ_COMMENT = 1;
    private MineFollowNotesAdapter mAdapter;
    private int mTargetPosition;

    @Override
    public void onConfigureDisplay(ConfigureAdapter configureAdapter) {
        super.onConfigureDisplay(configureAdapter);
        configureAdapter.addItemDecoration(new VerticalLinearSpaceItemDecoration(DensityUtil.dp2px(getContext(),10)));
    }

    @Override
    public PageIterator.PageRequest<FollowNote> createPageRequest() {
        return new PageIterator.PageRequest<FollowNote>() {
            @Override
            public Single<HttpResult<List<FollowNote>>> execute(int page) {
                return ServiceGenerator.createService(getContext(), CommonService.class)
                        .getFollowNotes(new GetFollowNotes(page,6));
            }
        };
    }

    @Override
    public AbstractLoadAdapter<FollowNote> createAdapter(RecyclerView rv) {
        mAdapter = new MineFollowNotesAdapter(rv);
        mAdapter.setOnFollowNotesListener(this);
        return mAdapter;
    }

    @Override
    public void onPlayVideo(String url) {
        VideoUtil.play(getActivity(),url);
    }

    @Override
    public void onScanImages(int selected, String[] images) {
        LookUpPictureActivity.toLookUp(getActivity(), 0, false, selected, images);
    }

    @Override
    public void onAddZan(final int flatPosition, String dateKey, final String style) {
        AddZan params = new AddZan();
        params.setDataTable(AddZan.DATA_TYPE_NOTE);
        params.setDataKey(dateKey);
        params.setStyle(style);
        ServiceGenerator.createService(getContext(), CommonService.class)
                .addZan(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleAdapter<HttpResult<String>>() {
                    @Override
                    public void onSuccess(@NonNull HttpResult<String> result) {
                        boolean zaned = AddZan.ZAN_COMMENT_ADD.equals(style);
                        mAdapter.onAddZan(flatPosition,zaned);
                    }

                    @Override
                    public void onError(Throwable e) {}
                });
    }

    @Override
    public void onAddComment(int flatPosition, FollowNote data) {
        mTargetPosition = flatPosition;
        AddFootprintCommentActivity.toComment(this,REQ_COMMENT,data);
    }

    @Override
    public void onCheckNote(int flatPosition, String scheduleId) {

    }

    @Override
    public void onCheckUser(long userId) {
        PersonMainActivity.toPerson(getContext(),userId);
    }
}
