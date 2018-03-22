/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.lots.travel.video.edit.http;

public interface HttpCallback<T> {
    void onSuccess(T result);
    void onFailure(Throwable e);
}
