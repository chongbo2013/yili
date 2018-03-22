package com.lots.travel.footprint.mine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lots.travel.R;
import com.lots.travel.footprint.BaseFootprintAdapter;
import com.lots.travel.footprint.BaseHolder;
import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.footprint.model.TripData;
import com.lots.travel.footprint.widget.TripItem;
import com.lots.travel.widget.DestinationLabel;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by nalanzi on 2017/11/18.
 */
class TripHolder extends BaseHolder {

    public static TripHolder create(ViewGroup parent,BaseFootprintAdapter adapter){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footprint_mine_trip,parent,false);
        return new TripHolder(adapter,v);
    }

    private TextView tvTip;
    private TripItem mContent;

    private TripHolder(BaseFootprintAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        v.findViewById(R.id.tv_target).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flatPosition = getAdapterPosition();
                int itemPosition = adapter.getItemPosition(flatPosition);
                Footprint data = adapter.getItem(itemPosition);
                if(data==null)
                    return;

                if(v.getId()==R.id.tv_target){
                    ((MineFootprintAdapter)adapter).chooseState(getAdapterPosition());
                }
            }
        });

        tvTip = (TextView) v.findViewById(R.id.tv_tip);
        mContent = new TripItem(adapter);
        mContent.onCreate(this,v);
    }

    @Override
    public void onBind(Footprint data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());
        tvTip.setText(sdf.format(data.getCreattime()));
        mContent.onBind(this,data);
    }

}
