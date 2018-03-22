package com.lots.travel.footprint.live;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lots.travel.R;
import com.lots.travel.footprint.BaseFootprintAdapter;
import com.lots.travel.footprint.BaseHolder;
import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.footprint.widget.ActiveItem;

/**
 * Created by nalanzi on 2017/11/20.
 */

class ActiveHolder extends BaseHolder {
    public static ActiveHolder create(ViewGroup parent, BaseFootprintAdapter adapter){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_active,parent,false);
        return new ActiveHolder(adapter,v);
    }

    private ActiveItem mContent;

    private ActiveHolder(BaseFootprintAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        mContent = new ActiveItem(adapter);
        mContent.onCreate(this,v);
    }

    @Override
    public void onBind(Footprint t) {
        mContent.onBind(this,t);
    }
}
