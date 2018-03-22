package com.lots.travel.footprint.live;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lots.travel.R;
import com.lots.travel.footprint.BaseFootprintAdapter;
import com.lots.travel.footprint.BaseHolder;
import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.footprint.widget.NoteItem;

/**
 * Created by nalanzi on 2017/11/20.
 */

class DestinationHolder extends BaseHolder {
    public static DestinationHolder create(ViewGroup parent, BaseFootprintAdapter adapter){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_destination,parent,false);
        return new DestinationHolder(adapter,v);
    }

    private NoteItem mContent;

    private DestinationHolder(BaseFootprintAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        mContent = new NoteItem(adapter);
        mContent.onCreate(this,v);
    }

    @Override
    public void onBind(Footprint t) {
        mContent.onBind(this,t);
    }
}
