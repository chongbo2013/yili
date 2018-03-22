package com.lots.travel.footprint.widget;

import android.view.View;

import com.lots.travel.footprint.BaseFootprintAdapter;
import com.lots.travel.footprint.BaseHolder;
import com.lots.travel.footprint.model.Footprint;

/**
 * Created by nalanzi on 2017/11/19.
 */

public abstract class BaseItem {
    protected BaseFootprintAdapter adapter;

    public BaseItem(BaseFootprintAdapter adapter){
        this.adapter = adapter;
    }

    public abstract void onCreate(BaseHolder holder,View v);

    public abstract void onBind(BaseHolder holder,Footprint data);

}
