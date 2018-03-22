package com.lots.travel.footprint.person;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.footprint.BaseFootprintAdapter;
import com.lots.travel.footprint.BaseHolder;
import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.footprint.widget.DestinationItem;
import com.lots.travel.footprint.widget.FootprintItem;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by nalanzi on 2017/11/20.
 */

class DestinationHolder extends BaseHolder {

    public static DestinationHolder create(ViewGroup parent, BaseFootprintAdapter adapter){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footprint_person_destination,parent,false);
        return new DestinationHolder(adapter,v);
    }

    private TextView tvTip;
    private DestinationItem mContent;

    private DestinationHolder(BaseFootprintAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        tvTip = (TextView) v.findViewById(R.id.tv_tip);
        mContent = new DestinationItem(adapter);
        mContent.onCreate(this,v);
    }

    @Override
    public void onBind(Footprint t) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());
        tvTip.setText(sdf.format(t.getCreattime()));
        mContent.onBind(this,t);
    }


}
