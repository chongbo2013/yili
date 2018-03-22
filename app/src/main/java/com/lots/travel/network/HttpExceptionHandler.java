package com.lots.travel.network;

import android.content.Context;

import com.lots.travel.base.BaseActivity;

/**
 * Created by nalanzi on 2017/10/14.
 */

public interface HttpExceptionHandler {
    void handleException(BaseActivity context, Throwable ex);
}
