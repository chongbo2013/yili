package com.lots.travel.footprint.live;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.footprint.BaseFootprintAdapter;
import com.lots.travel.footprint.BaseHolder;
import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.footprint.widget.LiveItem;

/**
 * Created by nalanzi on 2017/11/20.
 */

class LiveHolder extends BaseHolder {
    public static LiveHolder create(ViewGroup parent, BaseFootprintAdapter adapter){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footprint_live_live,parent,false);
        return new LiveHolder(adapter,v);
    }

    private ImageView ivPortrait;
    private TextView tvName,tvProfession;
    private LiveItem mContent;

    private LiveHolder(BaseFootprintAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        ivPortrait = (ImageView) v.findViewById(R.id.iv_portrait);
        ivPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition();
                pos = adapter.getItemPosition(pos);
                Footprint data = adapter.getItem(pos);
                if(data!=null)
                    ((LiveFootprintAdapter)adapter).checkUser(data.getUserId());
            }
        });

        tvName = (TextView) v.findViewById(R.id.tv_name);
        tvProfession = (TextView) v.findViewById(R.id.tv_profession);

        mContent = new LiveItem(adapter);
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
}
