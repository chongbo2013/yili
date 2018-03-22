package com.lots.travel.footprint;

import android.view.View;

import com.lots.travel.footprint.model.Footprint;

/**
 * Created by nalanzi on 2018/1/6.
 */

public abstract class CommentableHolder extends BaseHolder {

    public CommentableHolder(BaseFootprintAdapter adapter, View v) {
        super(adapter, v);
    }

    public abstract void onAddZan(Footprint t);

}
