package com.lots.travel.footprint.mine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lots.travel.R;
import com.lots.travel.footprint.BaseFootprintAdapter;
import com.lots.travel.footprint.BaseHolder;
import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.footprint.widget.ActiveItem;

/**
 * Created by nalanzi on 2017/11/18.
 */

class ActiveHolder extends BaseHolder {

    public static ActiveHolder create(ViewGroup parent, BaseFootprintAdapter adapter){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footprint_mine_active,parent,false);
        return new ActiveHolder(adapter,v);
    }

    private ActiveItem mContent;

    private ActiveHolder(BaseFootprintAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        v.findViewById(R.id.tv_target).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MineFootprintAdapter)adapter).chooseState(getAdapterPosition());
            }
        });
        mContent = new ActiveItem(adapter);
        mContent.onCreate(this,v);
    }

    @Override
    public void onBind(Footprint data) {
        mContent.onBind(this,data);
    }

}
