package com.lots.travel;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.alivc.player.AliVcMediaPlayer;
import com.aliyun.vodplayer.media.AliyunPlayAuth;
import com.aliyun.vodplayer.media.AliyunVodPlayer;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.util.SosonaOssClient;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
 * Created by nalanzi on 2017/9/2.
 */

public class DemoActivity extends RxBaseActivity {
    private static final String TAG = DemoActivity.class.getName();
//    private SosonaOssClient ossClient;
    private AliyunVodPlayer mPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        final SurfaceView vSurface = (SurfaceView) findViewById(R.id.v_surface);
        vSurface.getHolder().addCallback(new SurfaceHolder.Callback() {
            public void surfaceCreated(SurfaceHolder holder) {
                holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
                holder.setKeepScreenOn(true);
                Log.d(TAG, "AlivcPlayer onSurfaceCreated." + mPlayer);

                // Important: surfaceView changed from background to front, we need reset surface to mediaplayer.
                // 对于从后台切换到前台,需要重设surface;部分手机锁屏也会做前后台切换的处理
                if (mPlayer != null) {
                    mPlayer.setDisplay(vSurface.getHolder());
                }

                Log.d(TAG, "AlivcPlayeron SurfaceCreated over.");
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
                Log.d(TAG, "onSurfaceChanged is valid ? " + holder.getSurface().isValid());
                if (mPlayer != null)
                    mPlayer.setDisplay(holder);
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.d(TAG, "onSurfaceDestroy.");
            }
        });


        mPlayer = new AliyunVodPlayer(this);
        AliyunPlayAuth.AliyunPlayAuthBuilder authBuilder = new AliyunPlayAuth.AliyunPlayAuthBuilder();
        authBuilder.setVid("http://play-lss.qupaicloud.com/zhongyoulive/zhongyoulive-23FDN.flv");
        authBuilder.setPlayAuth("1496936320-0-2156-0f6802ea3e82e66ecd107d402868218a");
        mPlayer.prepareAsync(authBuilder.build());
        mPlayer.enableNativeLog();







//        ossClient = new SosonaOssClient(DemoActivity.this);
//        findViewById(R.id.btn_upload).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final String imagePath = Environment.getExternalStorageDirectory()
//                        +"/MagazineUnlock/magazine-unlock-03-2.3.808-_ea7272562cb9461eba2b083fbe47e642.jpg";
//                if(!new File(imagePath).exists()){
//                    Toast.makeText(DemoActivity.this,"文件不存在",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                Single.create(
//                        new SingleOnSubscribe<String>() {
//                            @Override
//                            public void subscribe(@NonNull SingleEmitter<String> e) throws Exception {
//                                List<SosonaOssClient.Item> items = new ArrayList<>();
//                                items.add(new SosonaOssClient.Item(SosonaOssClient.ITEM_TYPE_ICON,imagePath));
//                                SparseArray<String> urlMap = new SparseArray<>();
//                                List<SosonaOssClient.Item> failedItems = ossClient.upload(DemoActivity.this,items,urlMap);
//                                e.onSuccess("成功");
//                            }
//                        })
//                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new SingleObserver<String>() {
//                            @Override
//                            public void onSubscribe(@NonNull Disposable d) {}
//
//                            @Override
//                            public void onSuccess(@NonNull String s) {
//                                s.toString();
//                            }
//
//                            @Override
//                            public void onError(@NonNull Throwable e) {
//                                e.toString();
//                            }
//                        });
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        mPlayer.stop();
        mPlayer.release();
        super.onDestroy();
    }
}
