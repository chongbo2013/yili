package com.lots.travel.footprint.person;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.lots.travel.footprint.AddFootprintCommentActivity;
import com.lots.travel.footprint.OnFootprintListener;
import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.footprint.FootprintTimelineDecoration;
import com.lots.travel.main.info.person.PersonMainActivity;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.SingleAdapter;
import com.lots.travel.network.model.request.AddZan;
import com.lots.travel.network.model.request.GetComments;
import com.lots.travel.network.model.request.GetFootprint;
import com.lots.travel.network.service.CommonService;
import com.lots.travel.network.service.FootprintService;
import com.lots.travel.schedule.note.detail.MineNoteDetailActivity;
import com.lots.travel.video.VideoUtil;
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
 * Created by nalanzi on 2017/11/20.
 */

public class PersonFootprintFragment extends PagedItemFragment<Footprint> implements OnFootprintListener {
    public static final String EXTRA_USER_ID = "userId";
    public static final int REQ_COMMENT = 1;
    public static final int REQ_NOTE_DETAIL = 2;

    private long mUserId;
    private PersonFootprintAdapter mFootprintAdapter;
    private int mTargetPosition = -1;
    private String mType;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        mUserId = bundle.getLong(EXTRA_USER_ID);
    }

    public void setSearchType(String type){
        mType = type;
    }

    public void startSearch(String type){
        if((mType==null&&type==null)
                || (mType!=null&&mType.equals(type)))
            return;
        forceRefresh();
    }

    @Override
    public void onConfigureDisplay(ConfigureAdapter configureAdapter) {
        super.onConfigureDisplay(configureAdapter);
        configureAdapter.addItemDecoration(new FootprintTimelineDecoration(getContext(),0.5f));
    }

    @Override
    public PageIterator.PageRequest<Footprint> createPageRequest() {
        return new PageIterator.PageRequest<Footprint>() {
            @Override
            public Single<HttpResult<List<Footprint>>> execute(int page) {
                GetFootprint params = new GetFootprint();
                params.setPageNo(page);
                params.setPageSize(10);
                params.setQueryType("private");
                params.setTargetId(mUserId);
                params.setEventStyle(mType);

                return ServiceGenerator.createService(getContext(),FootprintService.class)
                        .getFootprint(params);
            }
        };
    }

    @Override
    public AbstractLoadAdapter<Footprint> createAdapter(RecyclerView rv) {
        mFootprintAdapter = new PersonFootprintAdapter(getContext(),rv);
        mFootprintAdapter.setOnFootprintListener(this);
        return mFootprintAdapter;
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
    public void onAddZan(final int flatPosition, String dateTable, String dateKey, final String style) {
        AddZan params = new AddZan();
        params.setDataTable(dateTable);
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
                        mFootprintAdapter.onAddZan(flatPosition,zaned);
                    }

                    @Override
                    public void onError(Throwable e) {}
                });
    }

    @Override
    public void onAddComment(int flatPosition,Footprint data) {
        mTargetPosition = flatPosition;
        AddFootprintCommentActivity.toComment(this,REQ_COMMENT,data);
    }

    @Override
    public void onCheckNote(int flatPosition,String scheduleId) {
        mTargetPosition = flatPosition;
        MineNoteDetailActivity.toDetail(this,REQ_NOTE_DETAIL,scheduleId);
    }

    @Override
    public void onCheckUser(long userId) {
        PersonMainActivity.toPerson(getContext(),userId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=Activity.RESULT_OK || mTargetPosition<0)
            return;

        switch (requestCode){
            case REQ_COMMENT: {
                int commentCount = data.getIntExtra(AddFootprintCommentActivity.COMMENT_COUNT,0);
                int zanCount = data.getIntExtra(AddFootprintCommentActivity.ZAN_COUNT,0);
                int itemPosition = mFootprintAdapter.getItemPosition(mTargetPosition);
                Footprint item = mFootprintAdapter.getItem(itemPosition);
                if (item==null)
                    return;
                if(commentCount!=item.getCommentTotles()
                        || zanCount!=item.getZanTotles()){
                    item.setZanTotles(zanCount);
                    item.setCommentTotles(commentCount);
                    mFootprintAdapter.notifyItemChanged(mTargetPosition);
                }
                break;
            }

            case REQ_NOTE_DETAIL:{
                ;
                break;
            }
        }

        mTargetPosition = -1;
    }

}
