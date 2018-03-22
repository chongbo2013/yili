package com.lots.travel.util;

import android.content.Context;

import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.main.MainActivity;
import com.lots.travel.network.DefaultExceptionHandler;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.SingleAdapter;
import com.lots.travel.network.service.SpotService;
import com.lots.travel.store.db.GreenDaoManager;
import com.lots.travel.store.db.ViewCity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nalanzi on 2018/1/19.
 */
//初始时加载一些必要的数据
public class LoadDataUtil {
    private static final String HAS_LOADED = "HasLoaded";

    public static void load(RxBaseActivity context){

    }



}
