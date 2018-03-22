package com.lots.travel.schedule.route.edit;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.base.WebViewActivity;
import com.lots.travel.main.MainActivity;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.model.request.SaveTrip;
import com.lots.travel.network.service.TripService;
import com.lots.travel.place.ChooseComponentsActivity;
import com.lots.travel.place.ChooseStaysActivity;
import com.lots.travel.schedule.base.DataManager;
import com.lots.travel.schedule.base.Schedule;
import com.lots.travel.schedule.base.Topology;
import com.lots.travel.schedule.base.edit.EditScheduleAdapter;
import com.lots.travel.schedule.base.edit.EditTopology;
import com.lots.travel.schedule.base.edit.OnScheduleListener;
import com.lots.travel.schedule.base.move.MoveTopology;
import com.lots.travel.schedule.route.move.RouteMoveScheduleActivity;
import com.lots.travel.schedule.route.preview.RoutePreviewScheduleActivity;
import com.lots.travel.schedule.widget.CircleIndicatorLayout;
import com.lots.travel.schedule.widget.traffic.ChooseTrafficDialog;
import com.lots.travel.store.db.Food;
import com.lots.travel.store.db.Hotel;
import com.lots.travel.store.db.Spot;
import com.lots.travel.store.db.TripPart;
import com.lots.travel.store.db.ViewCity;
import com.lots.travel.util.SosonaOssClient;
import com.lots.travel.util.TypeUtil;
import com.lots.travel.video.RequestRecordActivity;
import com.lots.travel.widget.EditTextActivity;
import com.lots.travel.widget.SwipeItemLayout;
import com.lots.travel.widget.images.Image;
import com.lots.travel.widget.images.ImagePickerActivity;
import com.lots.travel.widget.images.LookUpPictureActivity;

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
 * Created by nalanzi on 2018/1/1.
 */
//1、需要将TripPart部分 text、imgs、sound、soundLen、video、videoImg清除掉
//2、不要把数据库内容清除
public class RouteEditScheduleActivity extends RxBaseActivity implements View.OnClickListener,OnScheduleListener {
    public static final String SCHEDULE = "Schedule";
    public static final String TOPOLOGY = "Topology";

    public static void toEdit(Context context, Schedule schedule){
        Intent intent = new Intent(context,RouteEditScheduleActivity.class);
        intent.putExtra(SCHEDULE,schedule);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    //添加components
    public static final int REQ_ADD_COMPONENTS = 1;
    //添加入住酒店
    public static final int REQ_ADD_STAY = 2;
    //编辑文本
    public static final int REQ_EDIT_TEXT = 3;
    //添加图片
    public static final int REQ_ADD_IMAGES = 4;
    //图片预览 - 可以删除图片
    public static final int REQ_SCAN_IMAGES = 5;
    //编辑components
    public static final int REQ_EDIT_COMPONENTS = 6;
    //添加视频
    public static final int REQ_ADD_VIDEO = 7;

    private Schedule mSchedule;
    private DataManager mDataManager;
    private EditTopology mTopology;
    private EditScheduleAdapter mScheduleAdapter;

    private int mActivePosition = -1;
    private SosonaOssClient mOssClient;

    private AlertDialog dlgTrafficDelete;
    private ChooseTrafficDialog dlgChooseTraffic;
    private CircleIndicatorLayout mCircleIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_edit_schedule);
        initData(getIntent(),savedInstanceState);
        initViews();
    }

    private void initData(Intent data,Bundle savedInstanceState){
        mSchedule = data.getParcelableExtra(SCHEDULE);
        if(savedInstanceState!=null){
            mSchedule = savedInstanceState.getParcelable(SCHEDULE);
            mTopology = savedInstanceState.getParcelable(TOPOLOGY);
        }
        mDataManager = DataManager.create(mSchedule);
        if(mTopology==null)
            mTopology = EditTopology.create(mDataManager);

        mScheduleAdapter = new EditScheduleAdapter(this,mTopology,mDataManager,new RouteEditHolderFactory());
        mScheduleAdapter.setOnScheduleListener(this);

        mOssClient = new SosonaOssClient(this);
    }

    private void initViews(){
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_edit).setOnClickListener(this);
        findViewById(R.id.btn_preview).setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);

        RecyclerView rvContent = (RecyclerView) findViewById(R.id.rv_content);
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        rvContent.setAdapter(mScheduleAdapter);
        rvContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
                int flatPosition = layoutManager.findFirstVisibleItemPosition();
                int itemPosition = mScheduleAdapter.getItemPosition(flatPosition);
                int range = mScheduleAdapter.getGroupPosition(itemPosition);
                if(range!=-1)
                    mCircleIndicator.checkToPosition(range);
            }
        });
        rvContent.addItemDecoration(new RouteEditTimelineDecoration(this,0.5f));
        rvContent.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));

        mCircleIndicator = (CircleIndicatorLayout) findViewById(R.id.circle_indicator);
        mCircleIndicator.setCircleCount(mScheduleAdapter.getGroupCount());
        mCircleIndicator.checkToPosition(0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SCHEDULE,mSchedule);
        outState.putParcelable(TOPOLOGY,mTopology);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_edit:
                RouteMoveScheduleActivity.toMove(this,REQ_EDIT_COMPONENTS,mDataManager.getSchedule(),mTopology);
                break;

            case R.id.btn_preview:
                RoutePreviewScheduleActivity.toPreview(this,mDataManager.getSchedule(),mTopology);
                break;

            case R.id.btn_save:
                saveRoute();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode!= Activity.RESULT_OK)
            return;

        final int itemPosition = mScheduleAdapter.getItemPosition(mActivePosition);
        if(itemPosition<0 && requestCode!=REQ_EDIT_COMPONENTS)
            return;

        switch (requestCode){
            case REQ_ADD_COMPONENTS:
                //从intent获取数据
                List<Spot> spots = data.getParcelableArrayListExtra(ChooseComponentsActivity.EXTRA_SPOTS);
                List<Food> foods = data.getParcelableArrayListExtra(ChooseComponentsActivity.EXTRA_RESTAURANTS);
                List<Hotel> hotels = data.getParcelableArrayListExtra(ChooseComponentsActivity.EXTRA_HOTELS);

                //创建TripPart，每个Spot会创建两个Child，但是只有一个TripPart
                mScheduleAdapter.addSpots(itemPosition,spots);
                mScheduleAdapter.addHotels(itemPosition,hotels);
                mScheduleAdapter.addFoods(itemPosition,foods);
                break;

            case REQ_ADD_STAY:
                //从intent获取stay数据，进行替换
                Hotel stay = data.getParcelableExtra(ChooseStaysActivity.EXTRA_STAYS);
                if(stay!=null){
                    mScheduleAdapter.setStayContent(itemPosition,stay);
                }
                break;

            case REQ_EDIT_TEXT:{
                String text = data.getStringExtra(EditTextActivity.TEXT);
                TripPart part = mScheduleAdapter.getItemContent(itemPosition);
                part.setTripDesc(text);
                mScheduleAdapter.updateItemContent(itemPosition,part,true);
                break;
            }

            case REQ_ADD_IMAGES:{
                List<Image> images = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGES);
                TripPart part = mScheduleAdapter.getItemContent(itemPosition);
                part.setTripImgs(TypeUtil.images2str(part.getTripImgs(),images,9));
                mScheduleAdapter.updateItemContent(itemPosition,part,true);
                break;
            }

            case REQ_SCAN_IMAGES:{
                List<String> images = data.getStringArrayListExtra(LookUpPictureActivity.OUTPUT_IMAGE_LIST);
                TripPart part = mScheduleAdapter.getItemContent(itemPosition);
                part.setTripImgs(TypeUtil.images2str(images));
                mScheduleAdapter.updateItemContent(itemPosition,part,true);
                break;
            }

            case REQ_EDIT_COMPONENTS:{
                MoveTopology srcTopology = data.getParcelableExtra(RouteMoveScheduleActivity.OUTPUT_TOPOLOGY);
                //重新建立NoteManager，读取所有数据库数据
                mDataManager = DataManager.create(mDataManager.getSchedule());
                mScheduleAdapter.update(mDataManager,srcTopology);
                break;
            }

            case REQ_ADD_VIDEO:{
                String videoPath = data.getStringExtra(RequestRecordActivity.VIDEO_PATH);
                String videoCoverPath = data.getStringExtra(RequestRecordActivity.VIDEO_COVER_PATH);
                TripPart part = mScheduleAdapter.getItemContent(itemPosition);
                part.setTripVideo(videoPath);
                part.setTripVideoImg(videoCoverPath);
                mScheduleAdapter.updateItemContent(itemPosition,part,true);
                break;
            }
        }
    }

    @Override
    public void onAddDesc(int pos) {
        int itemPosition = mScheduleAdapter.getItemPosition(pos);
        mScheduleAdapter.setDescContent(itemPosition);
    }

    @Override
    public void onEditText(int pos, String text) {
        mActivePosition = pos;
        EditTextActivity.toEdit(this,REQ_EDIT_TEXT,
                new EditTextActivity.Config()
                        .text(text)
                        .hint(getString(R.string.schedule_edit_text_hint))
                        .maxLength(100));
    }

    @Override
    public void onAddAudio(int pos, int length) {}

    @Override
    public void onAddVideo(int pos) {
        mActivePosition = pos;
        RequestRecordActivity.toRecord(this,REQ_ADD_VIDEO,true);
    }

    @Override
    public void onScanImages(int pos, String images, int imagePos) {
        if(!TextUtils.isEmpty(images)) {
            mActivePosition = pos;
            LookUpPictureActivity.toLookUp(this, REQ_SCAN_IMAGES, true, imagePos, TypeUtil.str2arrays(images));
        }
    }

    @Override
    public void onAddImages(int pos) {
        mActivePosition = pos;
        TripPart tripPart = mScheduleAdapter.getItemContent(pos);
        String[] images = TypeUtil.str2arrays(tripPart.getTripImgs());
        ImagePickerActivity.toPick(this, REQ_ADD_IMAGES, 9-images.length, null);
    }

    @Override
    public void onScanDetails(String url) {
        if(!TextUtils.isEmpty(url))
            WebViewActivity.toWeb(this,null,url,false);
    }

    @Override
    public void onAddComponents(int pos) {
        mActivePosition = pos;
        int itemType = mScheduleAdapter.getItemViewType(pos);

        Long dstId = mScheduleAdapter.getDstId();
        if(itemType== Topology.TYPE_STAY){
            ChooseStaysActivity.toChoose(this,REQ_ADD_STAY,dstId);
        }else{
            ChooseComponentsActivity.toChoose(this,REQ_ADD_COMPONENTS,dstId);
        }
    }

    @Override
    public void onDeleteComponent(int pos) {
        int itemPosition = mScheduleAdapter.getItemPosition(pos);
        mScheduleAdapter.removeComponent(itemPosition);
    }

    @Override
    public void onAddTraffic(int pos) {
        mActivePosition = pos;
        if(dlgChooseTraffic==null){
            dlgChooseTraffic = new ChooseTrafficDialog(this);
            dlgChooseTraffic.setOnChooseListener(new ChooseTrafficDialog.OnChooseListener() {
                @Override
                public void onChoose(String mode, int hour, int minute) {
                    int itemPosition = mScheduleAdapter.getItemPosition(mActivePosition);
                    mScheduleAdapter.setTrafficContent(itemPosition,mode,hour,minute);
                }
            });
        }
        dlgChooseTraffic.show();
    }

    @Override
    public void onDeleteTraffic(int pos) {
        mActivePosition = pos;
        if(dlgTrafficDelete==null){
            dlgTrafficDelete = new AlertDialog.Builder(this)
                    .setTitle(null)
                    .setMessage(R.string.schedule_traffic_delete)
                    .setCancelable(false)
                    .setNegativeButton(R.string.cancel,null)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int itemPosition = mScheduleAdapter.getItemPosition(mActivePosition);
                            mScheduleAdapter.clearTrafficContent(itemPosition);
                        }
                    })
                    .create();
        }
        dlgTrafficDelete.show();
    }

    @Override
    protected void onDestroy() {
        if(dlgTrafficDelete!=null)
            dlgTrafficDelete.dismiss();

        if(dlgChooseTraffic!=null)
            dlgChooseTraffic.dismiss();

        if(mScheduleAdapter!=null
                && mScheduleAdapter.getAudioPlayer()!=null)
            mScheduleAdapter.getAudioPlayer().stop();

        super.onDestroy();
    }

    private void saveRoute(){
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
                        List<SosonaOssClient.Item> failedItems = mOssClient.upload(RouteEditScheduleActivity.this,uploadItems,urlMap);
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
                            HttpResult<String> result = ServiceGenerator.createService(RouteEditScheduleActivity.this, TripService.class)
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
                        Toast.makeText(RouteEditScheduleActivity.this,R.string.route_edit_success,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RouteEditScheduleActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        dismissProgressDialog();
                        Toast.makeText(RouteEditScheduleActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
