package com.lots.travel;

import android.content.Intent;
import android.os.Bundle;

import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.login.LoginActivity;
import com.lots.travel.main.MainActivity;
import com.lots.travel.network.SingleAdapter;
import com.lots.travel.util.SystemBarCompat;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nalanzi on 2017/9/7.
 */

public class SplashActivity extends RxBaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SystemBarCompat.fullscreen(this,findViewById(R.id.fl_main),false);

        Single.timer(1500 , TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new SingleAdapter<Long>() {
                    @Override
                    public void onSuccess(@NonNull Long aVoid) {
                        skip();
                    }

                    @Override
                    public void onError(Throwable e) {
                        skip();
                    }
                });
    }

    private void skip(){
        Intent intent = new Intent(this,AccountManager.getInstance().isCurrentValidate()
                ? MainActivity.class:LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

}
