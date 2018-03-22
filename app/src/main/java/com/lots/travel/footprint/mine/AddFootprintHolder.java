package com.lots.travel.footprint.mine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lots.travel.R;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;

/**
 * Created by nalanzi on 2017/11/18.
 */

public class AddFootprintHolder extends AbstractLoadAdapter.HeaderFooterHolder {
    public static AddFootprintHolder create(ViewGroup parent,MineFootprintAdapter adapter){
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footprint_mine_publish,parent,false);
        return new AddFootprintHolder(adapter,root);
    }

    private MineFootprintAdapter mAdapter;

    AddFootprintHolder(MineFootprintAdapter adapter, View v) {
        super(v);
        mAdapter = adapter;
    }

    @Override
    public void onCreate(View v) {
        v.findViewById(R.id.card_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.createFootprint();
            }
        });
    }

    @Override
    public void onBind() {}

    @Override
    public void onDetach() {}

}
