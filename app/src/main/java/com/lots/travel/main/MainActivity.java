package com.lots.travel.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lots.travel.AccountManager;
import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.main.destination.DestinationFragment;
import com.lots.travel.main.live.LiveFragment;
import com.lots.travel.main.info.mine.MineFragment;
import com.lots.travel.main.trip.TripFragment;
import com.lots.travel.network.DefaultExceptionHandler;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.SingleAdapter;
import com.lots.travel.network.model.request.Empty;
import com.lots.travel.network.model.request.GetRongToken;
import com.lots.travel.network.service.CommonService;
import com.lots.travel.network.service.RongService;
import com.lots.travel.network.service.SpotService;

import com.lots.travel.store.db.GreenDaoManager;
import com.lots.travel.store.db.ViewCity;
import com.lots.travel.util.AreaUtil;
import com.lots.travel.util.LocateUtil;
import com.lots.travel.util.SystemBarCompat;
import com.lots.travel.video.record.Common;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.IOException;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by nalanzi on 2017/9/7.
 */

public class MainActivity extends RxBaseActivity implements RadioGroup.OnCheckedChangeListener{
    private Fragment[] mFragments;
    private int mCurrentIndex = -1;

    private ShortcutPopup mShortcutPopup;

    private DoubleClickExitHelper mDoubleClickHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        SystemBarCompat.fullscreen(this);

        mDoubleClickHelper = new DoubleClickExitHelper(this);
        mDoubleClickHelper.setOnExitListener(new DoubleClickExitHelper.OnExitListener() {
            @Override
            public void onExit() {
                finish();
            }
        });

        mShortcutPopup = new ShortcutPopup(this,findViewById(R.id.rl_content));

        mFragments = new Fragment[]{
                new DestinationFragment(),
                new TripFragment(),
                new LiveFragment(),
                new MineFragment()};

        findViewById(R.id.tab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShortcutPopup.show();
            }
        });

        RadioGroup llTabs = (RadioGroup) findViewById(R.id.ll_tabs);
        llTabs.setOnCheckedChangeListener(this);

        int newIndex = 0;
        int[] ids = new int[]{R.id.tab_destination,R.id.tab_trip,R.id.tab_live,R.id.tab_mine};
        if(savedInstanceState!=null){
            newIndex = savedInstanceState.getInt("currentIndex",0);
        }
        llTabs.check(ids[newIndex]);

        //获取城市
        getAllCities();

        //拷贝assets中的趣拍相关的资源到sd卡
        copyAssets();

        //创建Area表
        AreaUtil.check(this);

        //获取rong token
        ServiceGenerator.createService(this,RongService.class)
                .getToken(new GetRongToken(AccountManager.getInstance().getAccount()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<HttpResult<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull HttpResult<String> result) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
//        OkHttpClient client = new OkHttpClient.Builder().build();
//        FormBody body = new FormBody.Builder()
//                .add("userId",AccountManager.getInstance().getUserId()+"")
//                .add("name",AccountManager.getInstance().getUsername())
//                .add("portraitUri",AccountManager.getInstance().getAccount().getPortraitUrl())
//                .build();
//        client.newCall(
//                new Request.Builder()
//                        .post(body)
//                        .url("http://api.cn.ronghub.com/user/getToken.json")
//                        .build())
//                .enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        e.toString();
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        call.toString();
//                    }
//                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocate(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocate();
    }

    @Override
    protected void onDestroy() {
        mShortcutPopup.dismissDialog();
        super.onDestroy();
        stopLocate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShortcutPopup.processActivityResult(requestCode, resultCode, data);
    }

    private void switchFragment(int newIndex){
        if(newIndex==mCurrentIndex)
            return;

        final int oldIndex = mCurrentIndex;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //隐藏旧的fragment
        if(oldIndex!=-1) {
            transaction.hide(mFragments[oldIndex]);
        }

        //显示新的fragment
        if(!mFragments[newIndex].isAdded())
            transaction.add(R.id.fl_page,mFragments[newIndex]);

        transaction.show(mFragments[newIndex]);
        transaction.commit();

        //懒加载
        mFragments[newIndex].setUserVisibleHint(true);

        mCurrentIndex = newIndex;
    }

    //避免fragment错位的问题
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        outState.putInt("currentIndex",mCurrentIndex);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return mDoubleClickHelper.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId){
            case R.id.tab_destination:
                switchFragment(0);
                break;

            case R.id.tab_trip:
                switchFragment(1);
                break;

            case R.id.tab_live:
                switchFragment(2);
                break;

            case R.id.tab_mine:
                switchFragment(3);
                break;
        }
    }

    private void getAllCities(){
        ServiceGenerator.createService(this,SpotService.class)
                .getCities("2")
                .subscribeOn(Schedulers.io())
                .compose(this.<HttpResult<List<ViewCity>>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new SingleAdapter<HttpResult<List<ViewCity>>>() {
                    @Override
                    public void onSuccess(@NonNull HttpResult<List<ViewCity>> result) {
                        List<ViewCity> viewCities = result.getData();
                        if(viewCities!=null && viewCities.size()>0){
                            GreenDaoManager.getInstance()
                                    .getViewCityDao()
                                    .insertOrReplaceInTx(viewCities);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        new DefaultExceptionHandler().handleException(MainActivity.this,e);
                    }

                });
    }

    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationClientOption;
    private void startLocate(Context context) {
        if (locationClientOption == null) {
            locationClientOption = new AMapLocationClientOption();
            //设置为高精度定位模式
            locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            locationClientOption.setOnceLocation(true);
        }

        if (locationClient == null) {
            locationClient = new AMapLocationClient(context);
            //设置定位监听
            locationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation loc) {
                    LocateUtil.setLatitude(loc.getLatitude());
                    LocateUtil.setLongitude(loc.getLongitude());

                    LocateUtil.setCountry(loc.getCountry());
                    LocateUtil.setProvince(loc.getProvince());
                    LocateUtil.setCity(loc.getCity());
                    LocateUtil.setDistrict(loc.getDistrict());

                    LocateUtil.setDetailAddress(loc.getAddress());
                }
            });
            //设置定位参数
            locationClient.setLocationOption(locationClientOption);
        }

        locationClient.startLocation();
    }

    private void stopLocate() {
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }

        locationClient = null;
        locationClientOption = null;
    }

    private void copyAssets() {
        new Thread(){
            @Override
            public void run() {
                Common.copyAll(MainActivity.this);
            }
        }.start();
    }

}
