package com.lots.travel.footprint.mine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lots.travel.R;
import com.lots.travel.footprint.BaseFootprintAdapter;
import com.lots.travel.footprint.BaseHolder;
import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.footprint.widget.LiveItem;

/**
 * Created by nalanzi on 2017/11/18.
 */

public class VideoHolder extends BaseHolder {

    public static VideoHolder create(ViewGroup parent, BaseFootprintAdapter adapter){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footprint_mine_video,parent,false);
        return new VideoHolder(adapter,v);
    }

    private LiveItem mContent;

    private VideoHolder(BaseFootprintAdapter adapter, View v) {
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

        mContent = new LiveItem(adapter);
        mContent.onCreate(this,v);
    }

    @Override
    public void onBind(Footprint data) {
        mContent.onBind(this,data);
    }

}
