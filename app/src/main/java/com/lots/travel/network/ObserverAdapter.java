package com.lots.travel.network;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Observer stub
 */
public abstract class ObserverAdapter<T> implements Observer<T> {

    @Override
    public void onSubscribe(@NonNull Disposable d){}

    @Override
    public abstract void onError(Throwable e);

    @Override
    public abstract void onNext(T t);

    @Override
    public void onComplete(){}
}
