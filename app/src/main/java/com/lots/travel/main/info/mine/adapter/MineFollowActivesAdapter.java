package com.lots.travel.main.info.mine.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.lots.travel.R;
import com.lots.travel.main.info.mine.model.FollowActive;
import com.lots.travel.main.info.mine.model.FollowTrip;
import com.lots.travel.util.Constant;
import com.lots.travel.widget.ItemTypeImageView;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nalanzi on 2018/1/23.
 */
//判断是否结束
public class MineFollowActivesAdapter extends AbstractLoadAdapter<FollowActive> {
    private Context mContext;
    private RequestOptions mRequestOptions;

    public MineFollowActivesAdapter(Context context,RecyclerView rv) {
        super(rv);
        mContext = context;
        mRequestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);
    }

    private void loadImage(String url,ImageView target){
        if(!TextUtils.isEmpty(url)) {
            url = url.startsWith("http") ? url: Constant.PATH_OSS_IMAGE+url;
            Glide.with(mContext)
                    .load(url)
                    .apply(mRequestOptions)
                    .into(target);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mine_follow_actives,parent,false);
        return new Holder(v);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        FollowActive f = getItem(position);
        if(f==null)
            return;

        Holder cHolder = (Holder) holder;
        cHolder.setUserInfo(f);
        cHolder.setActiveInfo(f);
        cHolder.setExtraInfo(f);
    }

    @Override
    public void onRefreshed() {}

    @Override
    public void onLoaded() {}

    class Holder extends RecyclerView.ViewHolder{
        private ImageView ivPortrait;
        private TextView tvName;
        private TextView tvProfession;

        private ImageView ivImg;
        private ImageView ivCompleted;
        private ItemTypeImageView ivType;
        private TextView tvWord;

        private TextView tvTimeGo,tvTimeGoDetail,tvType;
        private ProgressBar progressBar;
        private TextView tvQuota,tvPrice;

        public Holder(View v) {
            super(v);
            ivPortrait = (ImageView) v.findViewById(R.id.iv_portrait);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvProfession = (TextView) v.findViewById(R.id.tv_profession);
            ivImg = (ImageView) v.findViewById(R.id.iv_img);
            ivCompleted = (ImageView) v.findViewById(R.id.iv_completed);
            ivType = (ItemTypeImageView) v.findViewById(R.id.iv_type);
            tvWord = (TextView) v.findViewById(R.id.tv_word);
            tvTimeGo = (TextView) v.findViewById(R.id.tv_time_go);
            tvTimeGoDetail = (TextView) v.findViewById(R.id.tv_time_go_detail);
            tvType = (TextView) v.findViewById(R.id.tv_type);
            progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
            tvQuota = (TextView) v.findViewById(R.id.tv_quota);
            tvPrice = (TextView) v.findViewById(R.id.tv_price);
        }

        private void setUserInfo(FollowActive f){
            loadImage(f.getFaceImg(),ivPortrait);
            tvName.setText(f.getUserName());
            int sexDrawableId = -1;
            String str = f.getSex();
            if(!TextUtils.isEmpty(str)){
                if (str.equals("1")){
                    sexDrawableId = R.drawable.ico_male;
                } else if(str.equals("2")) {
                    sexDrawableId = R.drawable.ico_female;
                }
            }
            if(sexDrawableId!=-1) {
                Drawable sexDrawable = ContextCompat.getDrawable(mContext,sexDrawableId);
                sexDrawable.setBounds(0,0,sexDrawable.getIntrinsicWidth(),sexDrawable.getIntrinsicHeight());
                tvName.setCompoundDrawables(null,null,sexDrawable,null);
            }
            tvProfession.setText(f.getTitle());
        }

        //title、image、type、completed
        private void setActiveInfo(FollowActive f){
            tvWord.setText(f.getTriptitle());
            loadImage(f.getTripimg(),ivImg);
            ivCompleted.setVisibility(f.getStatus()==FollowActive.STATUS_COMPLETED ? View.VISIBLE:View.INVISIBLE);

            int bgId = -1;
            int tagId = -1;
            switch (f.getActivityStyle()){
                case "周末自驾":
                    tagId = R.drawable.ico_active_drive;
                    bgId = R.color.bg_active_drive;
                    break;

                case "摄影约拍":
                    tagId = R.drawable.ico_active_photograph;
                    bgId = R.color.bg_active_photograph;
                    break;

                case "结伴运动":
                    tagId = R.drawable.ico_active_sport;
                    bgId = R.color.bg_active_sport;
                    break;

                case "美食/拼餐":
                    tagId = R.drawable.ico_active_foods;
                    bgId = R.color.bg_active_foods;
                    break;

                case "游戏开黑":
                    tagId = R.drawable.ico_active_games;
                    bgId = R.color.bg_active_games;
                    break;

                case "交友聚会":
                    tagId = R.drawable.ico_active_friends;
                    bgId = R.color.bg_active_friends;
                    break;

                case "结伴逛街":
                    tagId = R.drawable.ico_active_shopping;
                    bgId = R.color.bg_active_shopping;
                    break;
            }
            Drawable drawable = tvType.getBackground();
            if(bgId!=-1 && drawable!=null && drawable instanceof GradientDrawable) {
                GradientDrawable bgDrawable = (GradientDrawable) drawable;
                bgDrawable.setColor(ContextCompat.getColor(mContext,bgId));
            }
            if(tagId!=-1){
                ivType.setImageResource(tagId);
            }
        }

        //time、progress、quota、price
        private void setExtraInfo(FollowActive f){
            boolean completed = f.getStatus()==FollowActive.STATUS_COMPLETED;
            if(!TextUtils.isEmpty(f.getDateFrom())){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                try {
                    Date date = sdf.parse(f.getDateFrom());
                    sdf = new SimpleDateFormat(mContext.getString(R.string.items_active_time_go), Locale.CHINA);
                    tvTimeGoDetail.setTextColor(ContextCompat.getColor(mContext,completed ? R.color.color_text:R.color.color_title_small));
                    tvTimeGoDetail.setText(sdf.format(date));
                } catch (ParseException e) {
                    Log.e(MineFollowActivesAdapter.class.getName(),e.toString());
                }
            }
            //图片、tag颜色
            int totalPeople = f.getPeople();
            int realPeople = f.getBuyCount();
            tvType.setText(f.getActivityStyle());
            tvQuota.setText(showPeople(realPeople,completed));
            tvPrice.setText(showPrice(f.getMoneyStyle(),completed));
            //进度
            progressBar.setProgress((int) (realPeople*1f/totalPeople*100));
        }
    }

    private SpannableStringBuilder showPeople(int n,boolean completed){
        int end;
        int textSize,textColor;
        Resources res = mContext.getResources();

        SpannableStringBuilder strBuilder = new SpannableStringBuilder();

        strBuilder.append("报名人数：");
        end = strBuilder.length();
        textSize = (int) res.getDimension(R.dimen.fonts_tip);
        textColor = ContextCompat.getColor(mContext,completed ? R.color.color_text:R.color.color_title_small);
        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        strBuilder.append(Integer.toString(n));
        textSize = (int) res.getDimension(R.dimen.head_medium);
        textColor = ContextCompat.getColor(mContext,R.color.color_warn);
        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),end,strBuilder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new StyleSpan(Typeface.BOLD),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        end = strBuilder.length();
        strBuilder.append("人");
        textSize = (int) res.getDimension(R.dimen.fonts_tip);
        textColor = ContextCompat.getColor(mContext,completed ? R.color.color_text:R.color.color_title_small);
        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return strBuilder;
    }

    private SpannableStringBuilder showPrice(String moneyStyle,boolean completed){
        int end;
        int textSize,textColor;
        Resources res = mContext.getResources();

        SpannableStringBuilder strBuilder = new SpannableStringBuilder();

        strBuilder.append("活动费用：");
        end = strBuilder.length();
        textSize = (int) res.getDimension(R.dimen.fonts_tip);
        textColor = ContextCompat.getColor(mContext,completed ? R.color.color_text:R.color.color_title_small);
        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        strBuilder.append(moneyStyle);
        textSize = (int) res.getDimension(R.dimen.head_medium);
        textColor = ContextCompat.getColor(mContext,R.color.color_warn);
        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),end,strBuilder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new StyleSpan(Typeface.BOLD),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return strBuilder;
    }

}
