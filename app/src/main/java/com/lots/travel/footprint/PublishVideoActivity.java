package com.lots.travel.footprint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.common.media.ShareableBitmap;
import com.aliyun.qupai.editor.AliyunIThumbnailFetcher;
import com.aliyun.qupai.editor.AliyunThumbnailFetcherFactory;
import com.aliyun.struct.common.ScaleMode;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.model.request.AddFootprint;
import com.lots.travel.network.service.FootprintService;
import com.lots.travel.store.FileStore;
import com.lots.travel.util.FileUtil;
import com.lots.travel.util.LocateUtil;
import com.lots.travel.util.SosonaOssClient;
import com.lots.travel.util.TakePictureUtil;
import com.lots.travel.video.RequestRecordActivity;
import com.lots.travel.video.VideoUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nalanzi on 2017/12/15.
 */

public class PublishVideoActivity extends RxBaseActivity implements View.OnClickListener {
    private static final int REQ_RECORD = 0;

    private EditText etText;

    private TextView tvAddress;
    private Switch sLocation;
    private Switch sTargets;
    private ImageView ivVideo;

    private double mLatitude;
    private double mLongitude;
    private String mDetailAddress;

    private SosonaOssClient mOssClient;
    private String mVideoPath;
    private String mVideoCoverPath;

    private RequestOptions mRequestOptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_video);

        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_publish).setOnClickListener(this);

        etText = (EditText) findViewById(R.id.et_text);

        tvAddress = (TextView) findViewById(R.id.tv_address);
        sLocation = (Switch) findViewById(R.id.s_location);
        sTargets = (Switch) findViewById(R.id.s_targets);
        sLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mLatitude = LocateUtil.getLatitude();
                    mLongitude = LocateUtil.getLongitude();
                    mDetailAddress = LocateUtil.getDetailAddress();
                    tvAddress.setText(mDetailAddress);
                }else{
                    tvAddress.setText(R.string.footprint_publish_location);
                }
            }
        });
        ivVideo = (ImageView) findViewById(R.id.iv_video);
        ivVideo.setOnClickListener(this);

        mRequestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);

        mOssClient = new SosonaOssClient(this);
        RequestRecordActivity.toRecord(this,REQ_RECORD,true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQ_RECORD && resultCode!=Activity.RESULT_OK)
            finish();

        if(resultCode!=Activity.RESULT_OK)
            return;

        switch (requestCode){
            case REQ_RECORD:
                mVideoPath = data.getStringExtra(RequestRecordActivity.VIDEO_PATH);
                mVideoCoverPath = data.getStringExtra(RequestRecordActivity.VIDEO_COVER_PATH);
                Glide.with(this)
                        .load(mVideoCoverPath)
                        .apply(mRequestOptions)
                        .into(ivVideo);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                finish();
                break;

            case R.id.tv_publish:
                publish();
                break;

            case R.id.ll_location:
                ;
                break;

            case R.id.ll_targets:
                ;
                break;

            case R.id.iv_video:
                if(!TextUtils.isEmpty(mVideoPath))
                    VideoUtil.play(this,mVideoPath);
                break;
        }
    }

    void publish(){
        final String text = etText.getText().toString();
        final boolean openLocation = sLocation.isChecked();
        final boolean openTargets = sTargets.isChecked();

        Single.create(
                new SingleOnSubscribe<String>() {
                    @Override
                    public void subscribe(@io.reactivex.annotations.NonNull SingleEmitter<String> e) throws Exception {
                        String coverUrl = mOssClient.uploadImage(PublishVideoActivity.this,SosonaOssClient.ITEM_TYPE_FOOTPRINT,mVideoCoverPath,false);
                        String videoUrl = mOssClient.uploadVideo(PublishVideoActivity.this,SosonaOssClient.ITEM_TYPE_FOOTPRINT,mVideoPath);

                        if(coverUrl==null || videoUrl==null){
                            e.onError(new RuntimeException("部分文件上传失败"));
                        }

                        //调用请求
                        AddFootprint params = new AddFootprint();
                        params.setType("2");
                        params.setContent(text);
                        params.setVideo(videoUrl);
                        params.setVideoimg(coverUrl);
                        if(openLocation) {
                            params.setLocation(mLatitude,mLongitude);
                            params.setGpsAddress(mDetailAddress);
                        }
                        params.setXzshow(openTargets ? "1":"0");
                        try{
                            HttpResult<String> result = ServiceGenerator.createService(PublishVideoActivity.this, FootprintService.class)
                                    .addFootprint(params)
                                    .blockingGet();
                            e.onSuccess(result.getData());
                        }catch (Exception exception){
                            e.onError(exception);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        showProgressDialog();
                    }

                    @Override
                    public void onSuccess(@NonNull String s) {
                        dismissProgressDialog();
                        Toast.makeText(PublishVideoActivity.this,R.string.footprint_publish_success,Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dismissProgressDialog();
                        Toast.makeText(PublishVideoActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void toVideo(Context context){
        Intent intent = new Intent(context,PublishVideoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

}
