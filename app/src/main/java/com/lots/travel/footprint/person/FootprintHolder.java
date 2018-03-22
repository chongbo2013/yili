package com.lots.travel.footprint.person;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.footprint.BaseFootprintAdapter;
import com.lots.travel.footprint.BaseHolder;
import com.lots.travel.footprint.CommentableHolder;
import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.footprint.widget.FootprintItem;
import com.lots.travel.footprint.widget.NoteItem;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by nalanzi on 2017/11/20.
 */

class FootprintHolder extends CommentableHolder {

    public static FootprintHolder create(ViewGroup parent, BaseFootprintAdapter adapter){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footprint_person_footprint,parent,false);
        return new FootprintHolder(adapter,v);
    }

    private TextView tvTip;
    private FootprintItem mContent;

    private FootprintHolder(BaseFootprintAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        v.findViewById(R.id.ll_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flatPos = getAdapterPosition();
                int itemPos = adapter.getItemPosition(flatPos);
                Footprint data = adapter.getItem(itemPos);
                if(data==null)
                    return;
                adapter.triggerAddComment(flatPos,data);
            }
        });
        tvTip = (TextView) v.findViewById(R.id.tv_tip);
        mContent = new FootprintItem(adapter);
        mContent.onCreate(this,v);
    }

    @Override
    public void onBind(Footprint t) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());
        tvTip.setText(sdf.format(t.getCreattime()));
        mContent.onBind(this,t);
    }

    @Override
    public void onAddZan(Footprint t){
        mContent.onAddZan(t);
    }

}
