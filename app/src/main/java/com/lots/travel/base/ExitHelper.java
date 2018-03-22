package com.lots.travel.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.lots.travel.TravelApplication;

/**
 * Created by nalanzi on 2017/10/14.
 */

public class ExitHelper {
    private static final String ACTION_EXIT = "com.lots.travel.exit";

    private Activity activity;

    private BroadcastReceiver exitBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(ACTION_EXIT.equals(intent.getAction()))
                activity.finish();
        }
    };

    public ExitHelper(Activity activity){
        this.activity = activity;
    }

    public void register(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_EXIT);
        LocalBroadcastManager.getInstance(activity).registerReceiver(exitBroadcastReceiver,filter);
    }

    public void unregister(){
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(exitBroadcastReceiver);
    }

    public static void exit(){
        LocalBroadcastManager.getInstance(TravelApplication.getInstance())
                .sendBroadcast(new Intent(ACTION_EXIT));
    }

}
