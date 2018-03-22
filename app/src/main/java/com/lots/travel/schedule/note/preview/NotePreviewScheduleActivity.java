package com.lots.travel.schedule.note.preview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.main.MainActivity;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.model.request.SaveTrip;
import com.lots.travel.network.service.TripService;
import com.lots.travel.schedule.base.DataManager;
import com.lots.travel.schedule.base.Schedule;
import com.lots.travel.schedule.base.edit.EditTopology;
import com.lots.travel.schedule.base.preview.PreviewScheduleAdapter;
import com.lots.travel.schedule.base.preview.PreviewTopology;
import com.lots.travel.schedule.widget.CircleIndicatorLayout;
import com.lots.travel.store.db.TripPart;
import com.lots.travel.util.SosonaOssClient;
import com.lots.travel.util.SystemBarCompat;
import com.lots.travel.util.TypeUtil;
import com.lots.travel.widget.WindowInsertsToolbarLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nalanzi on 2017/12/27.
 */

public class NotePreviewScheduleActivity extends RxBaseActivity implements View.OnClickListener {
    public static final String SCHEDULE = "Schedule";
    public static final String TOPOLOGY = "Topology";
    public static final String SOURCE_TOPOLOGY = "SourceTopology";

    public static void toPreview(Activity context, Schedule schedule, EditTopology topology){
        Intent intent = new Intent(context,NotePreviewScheduleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(SCHEDULE,schedule);
        intent.putExtra(SOURCE_TOPOLOGY,topology);
        context.startActivity(intent);
    }

    private TextView tvTitle;
    private WindowInsertsToolbarLayout toolbarContainer;
    private Drawable toolbarBackground;
    private CircleIndicatorLayout circleIndicator;
    private NotePreviewHeaderHolder hHeader;

    private int[] mTempLocation = new int[2];

    private PreviewTopology mTopology;
    private DataManager mDataManager;
    private PreviewScheduleAdapter mScheduleAdapter;

    private SosonaOssClient mOssClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_preview_schedule);
        SystemBarCompat.fullscreen(this);
        initData(getIntent(),savedInstanceState);
        initViews();
    }

    private void initData(Intent data,Bundle savedInstanceState){
        Schedule schedule = data.getParcelableExtra(SCHEDULE);
        if(savedInstanceState!=null){
            schedule = savedInstanceState.getParcelable(SCHEDULE);
            mTopology = savedInstanceState.getParcelable(TOPOLOGY);
        }
        if(mTopology==null){
            EditTopology sourceTopology = data.getParcelableExtra(SOURCE_TOPOLOGY);
            mTopology = PreviewTopology.create(sourceTopology);
        }
        mDataManager = DataManager.create(schedule);

        mScheduleAdapter = new PreviewScheduleAdapter(this,mTopology,mDataManager,new NotePreviewHolderFactory());
        mOssClient = new SosonaOssClient(this);
    }

    private void initViews(){
        toolbarContainer = (WindowInsertsToolbarLayout) findViewById(R.id.toolbar_container);
        toolbarBackground = toolbarContainer.getBackground().mutate();

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(mScheduleAdapter.getDstName());

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_share).setOnClickListener(this);
        findViewById(R.id.btn_edit).setOnClickListener(this);
        findViewById(R.id.btn_publish).setOnClickListener(this);

        circleIndicator = (CircleIndicatorLayout) findViewById(R.id.circle_indicator);
        circleIndicator.setCircleCount(mScheduleAdapter.getGroupCount());
        circleIndicator.checkToPosition(0);

        RecyclerView rvContent = (RecyclerView) findViewById(R.id.rv_content);
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        rvContent.setAdapter(mScheduleAdapter);
        rvContent.addItemDecoration(new NotePreviewTimelineDecoration(this,.5f));

        rvContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                if (toolbarBackground==null)
                    return;

                LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
                int flatPosition = layoutManager.findFirstVisibleItemPosition();

                if(flatPosition!=0){
                    toolbarBackground.setAlpha(255);
                    tvTitle.setAlpha(1);
                    circleIndicator.setVisibility(View.VISIBLE);
                }else{
                    hHeader.itemView.getLocationOnScreen(mTempLocation);
                    int headerTop = mTempLocation[1];
                    toolbarContainer.getLocationOnScreen(mTempLocation);
                    int toolbarTop = mTempLocation[1];
                    int heightAbs = Math.abs(hHeader.itemView.getHeight()-toolbarContainer.getHeight());
                    if(heightAbs==0)
                        return;
                    float ratio = Math.min(1,(toolbarTop-headerTop)*1f/heightAbs);
                    tvTitle.setAlpha(ratio);
                    toolbarBackground.setAlpha((int) (255*ratio));
                    circleIndicator.setVisibility(ratio==1 ? View.VISIBLE:View.INVISIBLE);
                }

                if(circleIndicator.getVisibility()!=View.VISIBLE)
                    return;
                //获取range
                circleIndicator.getLocationOnScreen(mTempLocation);
                int indicatorTop = mTempLocation[1];
                final int childCount = rv.getChildCount();
                for (int i=0;i<childCount;i++){
                    View child = rv.getChildAt(i);
                    child.getLocationOnScreen(mTempLocation);
                    int childTop = mTempLocation[1]-layoutManager.getTopDecorationHeight(child);
                    int childBottom = childTop+child.getHeight()+layoutManager.getBottomDecorationHeight(child);
                    if(childTop<=indicatorTop && childBottom>=indicatorTop){
                        int childFlatPosition = rv.getChildAdapterPosition(child);
                        int childItemPosition = mScheduleAdapter.getItemPosition(childFlatPosition);
                        int range = mScheduleAdapter.getGroupPosition(childItemPosition);
                        if(range!=-1)
                            circleIndicator.checkToPosition(range);
                        return;
                    }
                }
            }
        });

        //添加头部
        hHeader = NotePreviewHeaderHolder.create(rvContent,mScheduleAdapter);
        mScheduleAdapter.addHeader(mScheduleAdapter.genHeaderFooterTypes(), hHeader);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                back();
                break;

            case R.id.iv_share:
                ;
                break;

            case R.id.btn_edit:
                back();
                break;

            case R.id.btn_publish:
                publishNote();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back(){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mScheduleAdapter!=null
                && mScheduleAdapter.getAudioPlayer()!=null){
            mScheduleAdapter.getAudioPlayer().stop();
        }
    }

    private void publishNote(){
        Single.create(
                new SingleOnSubscribe<String>() {
                    @Override
                    public void subscribe(@io.reactivex.annotations.NonNull SingleEmitter<String> e) throws Exception {
                        List<TripPart> tripParts = mScheduleAdapter.getAllItemContent();
                        //上传文件 - 文件上传成功以后再提交
                        List<SosonaOssClient.Item> uploadItems = new ArrayList<>();
                        for (int i=0;i<tripParts.size();i++){
                            TripPart part = tripParts.get(i);
                            part = part.copy();
                            tripParts.set(i,part);
                            //语音 - 长度不为0才代表存在语音文件，才能上传
                            int soundLen = TypeUtil.str2int(part.getTripSoundLen());
                            if(soundLen>0)
                                uploadItems.add(new SosonaOssClient.Item(SosonaOssClient.ITEM_TYPE_PRODUCT,part.getTripSound()));
                            //视频及其封面
                            uploadItems.add(new SosonaOssClient.Item(SosonaOssClient.ITEM_TYPE_PRODUCT,part.getTripVideo()));
                            uploadItems.add(new SosonaOssClient.Item(SosonaOssClient.ITEM_TYPE_PRODUCT,part.getTripVideoImg()));
                            //图片
                            String[] images = TypeUtil.str2arrays(part.getTripImgs());
                            if(images!=null && images.length>0){
                                for (String image:images)
                                    uploadItems.add(new SosonaOssClient.Item(SosonaOssClient.ITEM_TYPE_PRODUCT,image));
                            }
                        }

                        SparseArray<String> urlMap = new SparseArray<>();
                        List<SosonaOssClient.Item> failedItems = mOssClient.upload(NotePreviewScheduleActivity.this,uploadItems,urlMap);
                        if(failedItems!=null && failedItems.size()>0)
                            e.onError(new RuntimeException("部分文件上传失败"));

                        //修改路径为url
                        for (int i=0;i<tripParts.size();i++){
                            TripPart part = tripParts.get(i);
                            //视频
                            String path = part.getTripVideo();
                            String url = !TextUtils.isEmpty(path) ? urlMap.get(path.hashCode()):null;
                            if(!TextUtils.isEmpty(url)){
                                part.setTripVideo(url);
                            }
                            path = part.getTripVideoImg();
                            url = !TextUtils.isEmpty(path) ? urlMap.get(part.hashCode()):null;
                            if(!TextUtils.isEmpty(url)){
                                part.setTripVideoImg(url);
                            }
                            //语音
                            path = part.getTripSound();
                            url = !TextUtils.isEmpty(path) ? urlMap.get(part.hashCode()):null;
                            if(!TextUtils.isEmpty(url)){
                                part.setTripSound(url);
                            }
                            //图片
                            String[] images = TypeUtil.str2arrays(part.getTripImgs());
                            if(images!=null && images.length>0){
                                StringBuilder strBuilder = new StringBuilder();
                                for (int j=0;j<images.length;j++){
                                    path = images[j];
                                    url = !TextUtils.isEmpty(path) ? urlMap.get(path.hashCode()):null;
                                    if(TextUtils.isEmpty(url))
                                        continue;
                                    if(j!=0)
                                        strBuilder.append(",");
                                    strBuilder.append(url);
                                }
                                part.setTripImgs(strBuilder.toString());
                            }
                        }

                        SaveTrip params = new SaveTrip();
                        params.setBaseId(mScheduleAdapter.getScheduleId());
                        params.setStatus("0");
                        params.setDayInfos(tripParts);

                        try{
                            HttpResult<String> result = ServiceGenerator.createService(NotePreviewScheduleActivity.this, TripService.class)
                                    .saveTripInfo(params)
                                    .blockingGet();
                            e.onSuccess(result.getData());
                        }catch (Exception exception){
                            e.onError(exception);
                        }
                    }})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        showProgressDialog();
                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull String aBoolean) {
                        dismissProgressDialog();
                        Toast.makeText(NotePreviewScheduleActivity.this,R.string.note_edit_success,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NotePreviewScheduleActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        dismissProgressDialog();
                        Toast.makeText(NotePreviewScheduleActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
