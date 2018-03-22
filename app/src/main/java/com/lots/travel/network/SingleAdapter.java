package com.lots.travel.network;

import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by nalanzi on 2017/9/23.
 */
//针对rxjava Single的adapter
public abstract class SingleAdapter<T> implements SingleObserver<T> {

    @Override
    public abstract void onSuccess(@NonNull T t);

    @Override
    public void onSubscribe(@NonNull Disposable d){}

    @Override
    public abstract void onError(Throwable e);

}