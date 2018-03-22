package com.lots.travel.footprint.mine;

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
import com.lots.travel.footprint.CommentableHolder;
import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.footprint.widget.NoteItem;
import com.lots.travel.util.CommonUtil;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by nalanzi on 2017/11/18.
 */

class NoteHolder extends CommentableHolder {

    public static NoteHolder create(ViewGroup parent, BaseFootprintAdapter adapter){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footprint_mine_note,parent,false);
        return new NoteHolder(adapter,v);
    }

    private TextView tvTip,tvTitle;
    private ImageView ivPortrait;
    private TextView tvName,tvProfession;
    private NoteItem mContent;

    private NoteHolder(BaseFootprintAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flatPosition = getAdapterPosition();
                int itemPosition = adapter.getItemPosition(flatPosition);
                Footprint data = adapter.getItem(itemPosition);
                if(data==null)
                    return;

                switch (v.getId()){
                    case R.id.rl_content:
                        adapter.triggerCheckNote(flatPosition,data.getId());
                        break;
                    case R.id.tv_target:
                        ((MineFootprintAdapter)adapter).chooseState(getAdapterPosition());
                        break;
                    case R.id.iv_portrait:
                        adapter.checkUser(data.getUserId());
                        break;
                }
            }
        };

        v.findViewById(R.id.rl_content).setOnClickListener(listener);
        tvTip = (TextView) v.findViewById(R.id.tv_tip);
        tvTitle = (TextView) v.findViewById(R.id.tv_title);

        ivPortrait = (ImageView) v.findViewById(R.id.iv_portrait);
        ivPortrait.setOnClickListener(listener);
        tvName = (TextView) v.findViewById(R.id.tv_name);
        tvProfession = (TextView) v.findViewById(R.id.tv_profession);

        mContent = new NoteItem(adapter);
        mContent.onCreate(this,v);
    }

    @Override
    public void onBind(Footprint t) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm",Locale.getDefault());
        tvTip.setText(sdf.format(t.getCreattime()));
        tvTitle.setText(String.format(Locale.getDefault(), adapter.getContext().getString(R.string.footprint_note_title),t.getTripTitle()));

        adapter.loadImage(t.getFaceImg(),ivPortrait);
        tvName.setText(t.getUserName());
        Drawable sexIco = null;
        int sexIcoId = CommonUtil.getSexDrawable(t.getSex());
        if(sexIcoId!=-1) {
            sexIco = ContextCompat.getDrawable(adapter.getContext(), sexIcoId);
            sexIco.setBounds(0,0,sexIco.getIntrinsicWidth(),sexIco.getIntrinsicHeight());
        }
        tvName.setCompoundDrawables(null,null,sexIco,null);
        tvProfession.setText(t.getUserTitle());

        mContent.onBind(this,t);
    }

    @Override
    public void onAddZan(Footprint t) {
        mContent.onAddZan(t);
    }

}
