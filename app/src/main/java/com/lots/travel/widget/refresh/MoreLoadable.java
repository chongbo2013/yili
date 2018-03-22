package com.lots.travel.widget.refresh;

import java.util.List;

/**
 * Created by nalanzi on 2017/9/22.
 */
public interface MoreLoadable<E> {

    //Footer部分(加载中、no more)不需要显示
    void onIdle();

    //正在加载，Footer显示
    void onLoading();

    //显示no more view
    void onNoMore();

    //需要将新数据append原来的数据中
    void onLoadCompleted(List<E> newData);

    //正在刷新 - adapter不需要该接口
    //void onRefreshing();

    //需要将原先的数据清除，然后换上新的数据
    void onRefreshed(List<E> newData);
}
