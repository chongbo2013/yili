package com.lots.travel.footprint;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lots.travel.footprint.model.Footprint;

/**
 * Created by nalanzi on 2017/11/20.
 */

public abstract class BaseHolder extends RecyclerView.ViewHolder{
    protected BaseFootprintAdapter adapter;

    public BaseHolder(BaseFootprintAdapter adapter,View v) {
        super(v);
        this.adapter = adapter;
        onCreate(v);
    }

    /**
     * v - item root
     */
    public abstract void onCreate(View v);
    //itemPos不包含headers部分
    public abstract void onBind(Footprint t);

}
