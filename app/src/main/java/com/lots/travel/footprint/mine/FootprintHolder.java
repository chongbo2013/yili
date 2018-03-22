package com.lots.travel.footprint.mine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.footprint.BaseFootprintAdapter;
import com.lots.travel.footprint.BaseHolder;
import com.lots.travel.footprint.CommentableHolder;
import com.lots.travel.footprint.live.LiveFootprintAdapter;
import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.footprint.widget.FootprintItem;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by nalanzi on 2017/11/18.
 */

class FootprintHolder extends CommentableHolder {

    public static FootprintHolder create(ViewGroup parent, BaseFootprintAdapter adapter){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footprint_mine_footprint,parent,false);
        return new FootprintHolder(adapter,v);
    }

    private TextView tvTip;
    private FootprintItem mContent;

    private FootprintHolder(BaseFootprintAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flatPos = getAdapterPosition();
                int itemPos = adapter.getItemPosition(flatPos);
                Footprint data = adapter.getItem(itemPos);
                if(data==null)
                    return;

                switch (v.getId()){
                    case R.id.ll_content:
                        adapter.triggerAddComment(flatPos,data);
                        break;
                    case R.id.tv_target:
                        ((MineFootprintAdapter)adapter).chooseState(getAdapterPosition());
                        break;
                }
            }
        };
        v.findViewById(R.id.ll_content).setOnClickListener(listener);
        v.findViewById(R.id.tv_target).setOnClickListener(listener);
        tvTip = (TextView) v.findViewById(R.id.tv_tip);
        mContent = new FootprintItem(adapter);
        mContent.onCreate(this,v);
    }

    @Override
    public void onBind(Footprint data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());
        tvTip.setText(sdf.format(data.getCreattime()));
        mContent.onBind(this,data);
    }

    @Override
    public void onAddZan(Footprint t){
        mContent.onAddZan(t);
    }

}
