package com.lots.travel.footprint.live;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.footprint.BaseFootprintAdapter;
import com.lots.travel.footprint.BaseHolder;
import com.lots.travel.footprint.CommentableHolder;
import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.footprint.widget.ActiveItem;
import com.lots.travel.footprint.widget.FootprintItem;

/**
 * Created by nalanzi on 2017/11/20.
 */

class FootprintHolder extends CommentableHolder {
    public static FootprintHolder create(ViewGroup parent, BaseFootprintAdapter adapter){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footprint_live_footprint,parent,false);
        return new FootprintHolder(adapter,v);
    }

    private ImageView ivPortrait;
    private TextView tvName,tvProfession;
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
                    case R.id.rl_content:
                        adapter.triggerAddComment(flatPos,data);
                        break;

                    case R.id.iv_portrait:
                        ((LiveFootprintAdapter)adapter).checkUser(data.getUserId());
                        break;
                }
            }
        };

        v.findViewById(R.id.rl_content).setOnClickListener(listener);
        ivPortrait = (ImageView) v.findViewById(R.id.iv_portrait);
        ivPortrait.setOnClickListener(listener);

        tvName = (TextView) v.findViewById(R.id.tv_name);
        tvProfession = (TextView) v.findViewById(R.id.tv_profession);

        mContent = new FootprintItem(adapter);
        mContent.onCreate(this,v);
    }

    @Override
    public void onBind(Footprint t) {
        adapter.loadImage(t.getFaceImg(),ivPortrait);
        tvName.setText(t.getUserName());
        Drawable ico = ContextCompat.getDrawable(adapter.getContext(),"1".equals(t.getSex()) ? R.drawable.ico_male:R.drawable.ico_female);
        ico.setBounds(0,0,ico.getIntrinsicWidth(),ico.getIntrinsicHeight());
        tvName.setCompoundDrawables(null,null,ico,null);
        tvProfession.setText(t.getUserTitle());
        mContent.onBind(this,t);
    }

    @Override
    public void onAddZan(Footprint t){
        mContent.onAddZan(t);
    }

}
