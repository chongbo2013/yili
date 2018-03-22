package com.lots.travel.footprint.widget;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lots.travel.R;
import com.lots.travel.footprint.BaseFootprintAdapter;
import com.lots.travel.footprint.BaseHolder;
import com.lots.travel.footprint.model.ActiveData;
import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.main.info.mine.adapter.MineFollowActivesAdapter;
import com.lots.travel.main.info.mine.model.FollowActive;
import com.lots.travel.util.TypeUtil;
import com.lots.travel.widget.ItemTypeImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nalanzi on 2017/11/19.
 */

public class ActiveItem extends BaseItem {
    private ImageView ivPortrait;
    private TextView tvName;
    private TextView tvProfession;

    private ImageView ivCompleted;
    private ImageView ivImg;
    private ItemTypeImageView ivType;
    private TextView tvWord;

    private TextView tvTimeGo,tvTimeGoDetail,tvType;
    private ProgressBar progressBar;
    private TextView tvQuota,tvPrice;

    public ActiveItem(BaseFootprintAdapter adapter) {
        super(adapter);
    }

    @Override
    public void onCreate(BaseHolder holder, View v) {
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

    private void setUserInfo(Footprint t){
        adapter.loadImage(t.getFaceImg(),ivPortrait);
        tvName.setText(t.getUserName());
        int sexDrawableId = -1;
        String str = t.getSex();
        if(!TextUtils.isEmpty(str)){
            if (str.equals("1")){
                sexDrawableId = R.drawable.ico_male;
            } else if(str.equals("2")) {
                sexDrawableId = R.drawable.ico_female;
            }
        }
        if(sexDrawableId!=-1) {
            Drawable sexDrawable = ContextCompat.getDrawable(adapter.getContext(),sexDrawableId);
            sexDrawable.setBounds(0,0,sexDrawable.getIntrinsicWidth(),sexDrawable.getIntrinsicHeight());
            tvName.setCompoundDrawables(null,null,sexDrawable,null);
        }
        tvProfession.setText(t.getUserTitle());
    }

    //title、image、type、completed
    private void setActiveInfo(Footprint f,ActiveData data,boolean completed){
        tvWord.setText(f.getTripTitle());

        if(data==null)
            return;

        adapter.loadImage(data.getTripImg(), ivImg);
        ivCompleted.setVisibility(completed ? View.VISIBLE:View.INVISIBLE);

        int bgId = -1;
        int tagId = -1;
        switch (data.getActivityStyle()){
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
            bgDrawable.setColor(ContextCompat.getColor(adapter.getContext(),bgId));
        }
        if(tagId!=-1){
            ivType.setImageResource(tagId);
        }
    }

    //time、progress、quota、price
    private void setExtraInfo(Footprint f,ActiveData data,boolean completed){
        if(data==null)
            return;

        if(!TextUtils.isEmpty(data.getDateFrom())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
            try {
                Date date = sdf.parse(data.getDateFrom());
                sdf = new SimpleDateFormat(adapter.getContext().getString(R.string.items_active_time_go), Locale.CHINA);
                tvTimeGoDetail.setTextColor(ContextCompat.getColor(adapter.getContext(),completed ? R.color.color_text:R.color.color_title_small));
                tvTimeGoDetail.setText(sdf.format(date));
            } catch (ParseException e) {
                Log.e(MineFollowActivesAdapter.class.getName(),e.toString());
            }
        }
        //图片、tag颜色
        int totalPeople = TypeUtil.str2int(data.getPeople());
        int realPeople = TypeUtil.str2int(data.getBuyCount());
        tvType.setText(data.getActivityStyle());
        tvQuota.setText(showPeople(realPeople,completed));
        tvPrice.setText(showPrice(data.getMoneyStyle(),completed));
        //进度
        progressBar.setProgress((int) (realPeople*1f/totalPeople*100));
    }

    private SpannableStringBuilder showPeople(int n, boolean completed){
        int end;
        int textSize,textColor;
        Resources res = adapter.getContext().getResources();

        SpannableStringBuilder strBuilder = new SpannableStringBuilder();

        strBuilder.append("报名人数：");
        end = strBuilder.length();
        textSize = (int) res.getDimension(R.dimen.fonts_tip);
        textColor = ContextCompat.getColor(adapter.getContext(),completed ? R.color.color_text:R.color.color_title_small);
        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        strBuilder.append(Integer.toString(n));
        textSize = (int) res.getDimension(R.dimen.head_medium);
        textColor = ContextCompat.getColor(adapter.getContext(),R.color.color_warn);
        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),end,strBuilder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new StyleSpan(Typeface.BOLD),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        end = strBuilder.length();
        strBuilder.append("人");
        textSize = (int) res.getDimension(R.dimen.fonts_tip);
        textColor = ContextCompat.getColor(adapter.getContext(),completed ? R.color.color_text:R.color.color_title_small);
        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return strBuilder;
    }

    private SpannableStringBuilder showPrice(String moneyStyle,boolean completed){
        int end;
        int textSize,textColor;
        Resources res = adapter.getContext().getResources();

        SpannableStringBuilder strBuilder = new SpannableStringBuilder();

        strBuilder.append("活动费用：");
        end = strBuilder.length();
        textSize = (int) res.getDimension(R.dimen.fonts_tip);
        textColor = ContextCompat.getColor(adapter.getContext(),completed ? R.color.color_text:R.color.color_title_small);
        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        strBuilder.append(moneyStyle);
        textSize = (int) res.getDimension(R.dimen.head_medium);
        textColor = ContextCompat.getColor(adapter.getContext(),R.color.color_warn);
        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),end,strBuilder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new StyleSpan(Typeface.BOLD),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return strBuilder;
    }

    @Override
    public void onBind(BaseHolder holder,Footprint f) {
        if(f==null)
            return;
        ActiveData data = null;
        boolean completed = false;
        if(f.getJson()!=null) {
            data = new Gson().fromJson(f.getJson(), ActiveData.class);
            int status = TypeUtil.str2int(data.getActivityStatus());
            completed = status==FollowActive.STATUS_COMPLETED;
        }

        setUserInfo(f);
        setActiveInfo(f,data,completed);
        setExtraInfo(f,data,completed);
    }

}
