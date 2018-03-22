package com.lots.travel.place.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.lots.travel.R;

import com.lots.travel.base.WebViewActivity;
import com.lots.travel.store.db.Hotel;

import com.lots.travel.util.TypeUtil;
import com.lots.travel.widget.CheckableImageView;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;

/**
 * Created by nalanzi on 2017/11/8.
 */

public class StayAdapter extends AbstractLoadAdapter<Hotel> {
    private Context context;
    private RequestOptions requestOptions;
    private OnSelectChangedListener onSelectChangedListener;
    private RecyclerView rvContent;

    private int selectedPos = -1;

    public void setOnSelectChangedListener(OnSelectChangedListener listener){
        onSelectChangedListener = listener;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Holder holder = (Holder) v.getTag();
            int pos = holder.getAdapterPosition();
            Hotel item = getItem(pos);

            if(item==null)
                return;

            if(v.getId()==R.id.tv_detail){
                if(!TextUtils.isEmpty(item.getViewurl()))
                    WebViewActivity.toWeb(context,null,item.getViewurl(),false);
            }else{
                int oldPos = selectedPos;
                selectedPos = pos!=oldPos ? pos:-1;

                //ui变化
                boolean selected = selectedPos==pos;
                holder.ivImg.setChecked(selected);
                if(selected && oldPos!=-1) {
                    Holder holder1 = (Holder) rvContent.findViewHolderForAdapterPosition(oldPos);
                    if(holder1!=null)
                        holder1.ivImg.setChecked(false);
                    else
                        notifyItemChanged(oldPos);
                }

                //触发监听
                if(onSelectChangedListener!=null)
                    onSelectChangedListener.onSelectChanged(selectedPos!=-1);
            }
        }
    };

    public StayAdapter(Context ctx,RecyclerView rv) {
        super(rv);
        context = ctx;
        rvContent = rv;
        requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);
    }

    public Hotel getSelected(){
        return selectedPos==-1 ?
                null:getItem(selectedPos);
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_choose_stay,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        Holder stayHolder = (Holder) holder;
        Hotel item = getItem(position);
        stayHolder.bind(item,selectedPos==position);
    }

    @Override
    public void onRefreshed() {
        selectedPos = -1;
        if(onSelectChangedListener!=null)
            onSelectChangedListener.onSelectChanged(false);
    }

    @Override
    public void onLoaded() {}

    class Holder extends RecyclerView.ViewHolder{
        private CheckableImageView ivImg;
        private TextView tvName;
        private TextView tvScore;//score(main color) scoreDesc scoreType scoreTotle条评价
        private TextView tvLocation;
        private TextView tvTag;
        private TextView tvPrice;

        public Holder(View v) {
            super(v);
            ivImg = (CheckableImageView) v.findViewById(R.id.iv_img);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvScore = (TextView) v.findViewById(R.id.tv_score);
            tvTag = (TextView) v.findViewById(R.id.tv_tag);
            tvLocation = (TextView) v.findViewById(R.id.tv_location);
            tvPrice = (TextView) v.findViewById(R.id.tv_price);

            View tvDetail = v.findViewById(R.id.tv_detail);
            tvDetail.setTag(this);
            tvDetail.setOnClickListener(onClickListener);

            v.setTag(this);
            v.setOnClickListener(onClickListener);
        }

        void bind(Hotel item,boolean selected){
            String url = item.getImg();

            if(!TextUtils.isEmpty(url)){
                Glide.with(context)
                        .load(url)
                        .apply(requestOptions)
                        .into(ivImg);
            }

            tvName.setText(item.getName());

            String score = TextUtils.isEmpty(item.getScore()) ? null:item.getScore()+"分 ";
            //String scoreDesc = item.getScoreDesc();
            String scoreType = item.getScoreType();
            String scoreTotle = TextUtils.isEmpty(item.getScoreTotle()) ? null:item.getScoreTotle()+"条评价";

            String extra = TypeUtil.mergeStrings(/*scoreDesc," ",*/scoreType," ",scoreTotle);
            tvScore.setText(showScore(score,extra));

            tvTag.setText(item.getTag());
            tvLocation.setText(TypeUtil.mergeString(item.getDistance()," · ",item.getZone()));
            tvPrice.setText(showPrice(item.getAvgCost()));
            ivImg.setChecked(selected);
        }

    }

    private SpannableStringBuilder showPrice(String value){
        SpannableStringBuilder strBuilder = new SpannableStringBuilder();
        int textSize,textColor;

        if(TextUtils.isEmpty(value))
            return null;

        strBuilder.append(value);
        int end = strBuilder.length();
        textSize = (int) context.getResources().getDimension(R.dimen.head_small);
        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textColor = ContextCompat.getColor(context,R.color.color_main);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        strBuilder.append(context.getString(R.string.price_extra));
        textSize = (int) context.getResources().getDimension(R.dimen.fonts_tip);
        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),end,strBuilder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textColor = ContextCompat.getColor(context,R.color.color_text);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return strBuilder;
    }

    private SpannableStringBuilder showScore(String value,String extra){
        SpannableStringBuilder strBuilder = new SpannableStringBuilder();

        int textColor;
        int end = 0;
        if(!TextUtils.isEmpty(value)){
            strBuilder.append(value);
            end = strBuilder.length();
            textColor = ContextCompat.getColor(context,R.color.color_main);
            strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if(!TextUtils.isEmpty(extra)){
            strBuilder.append(extra);
            textColor = ContextCompat.getColor(context,R.color.color_text);
            strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return strBuilder;
    }

    public interface OnSelectChangedListener{
        void onSelectChanged(boolean hasSelected);
    }

}
