package com.lots.travel.footprint.widget;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.footprint.model.TripData;
import com.lots.travel.main.info.mine.adapter.MineFollowTripsAdapter;
import com.lots.travel.main.info.mine.model.FollowTrip;
import com.lots.travel.util.CommonUtil;
import com.lots.travel.util.TypeUtil;
import com.lots.travel.widget.DestinationLabel;
import com.lots.travel.widget.ItemTypeImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 对应layout_trip
 */
public class TripItem extends BaseItem {
    //非个人时，隐藏三者
    private ImageView ivPortrait;
    private TextView tvName;
    private TextView tvProfession;
    //背景、类型图片、标题
    private ImageView ivCompleted;
    private ImageView ivImg;
    private ItemTypeImageView ivType;
    private TextView tvWord;
    //目的地、出发时间
    private TextView tvSrcGo,tvTimeGo;
    //进度、剩余人数、价格
    private ProgressBar progressBar;
    private TextView tvQuota,tvPrice;
    //目的地标签
    private DestinationLabel vLabel;

    public TripItem(BaseFootprintAdapter adapter) {
        super(adapter);
    }

    @Override
    public void onCreate(BaseHolder holder, View v) {
        ivPortrait = (ImageView) v.findViewById(R.id.iv_portrait);
        tvName = (TextView) v.findViewById(R.id.tv_name);
        tvProfession = (TextView) v.findViewById(R.id.tv_profession);
        ivCompleted = (ImageView) v.findViewById(R.id.iv_completed);
        ivImg = (ImageView) v.findViewById(R.id.iv_img);
        ivType = (ItemTypeImageView) v.findViewById(R.id.iv_type);
        tvWord = (TextView) v.findViewById(R.id.tv_word);
        tvSrcGo = (TextView) v.findViewById(R.id.tv_src_go);
        tvTimeGo = (TextView) v.findViewById(R.id.tv_time_go);
        progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        tvQuota = (TextView) v.findViewById(R.id.tv_quota);
        tvPrice = (TextView) v.findViewById(R.id.tv_price);
        vLabel = (DestinationLabel) v.findViewById(R.id.v_label);
    }

    //个人信息
    private void setUserInfo(Footprint t){
        //头像
        adapter.loadImage(t.getFaceImg(),ivPortrait);
        //名称
        tvName.setText(t.getUserName());
        //性别
        int sexDrawableId = CommonUtil.getSexDrawable(t.getSex());
        if(sexDrawableId!=-1) {
            Drawable sexDrawable = ContextCompat.getDrawable(adapter.getContext(),sexDrawableId);
            sexDrawable.setBounds(0,0,sexDrawable.getIntrinsicWidth(),sexDrawable.getIntrinsicHeight());
            tvName.setCompoundDrawables(null,null,sexDrawable,null);
        }
        //职位
        tvProfession.setText(t.getUserTitle());
    }

    //tripTitle、tripImg、tripType、是否结束
    private void setTripInfo(Footprint t,TripData data,boolean completed){
        //title
        tvWord.setText(t.getTripTitle());
        //封面
        String tripImg = data!=null ? data.getTripImg():null;
        if(TextUtils.isEmpty(tripImg)){
            tripImg = t.getTripImgs();
            String[] strs = TypeUtil.str2arrays(tripImg);
            tripImg = strs.length>0 ? strs[0]:t.getTripVideoImg();
        }
        adapter.loadImage(tripImg,ivImg);
        //type
        if(isTagChecked(t.getTag2()))
            ivType.setImageResource(R.drawable.img_recruit_photograph);
        //结束
        ivCompleted.setVisibility(completed ? View.VISIBLE:View.INVISIBLE);
    }

    private boolean isTagChecked(String value){
        return !TextUtils.isEmpty(value) && value.equals("1");
    }

    //时间、地点
    private void setPlaceTime(TripData data,boolean completed){
        if(data==null)
            return;

        //目的地
        vLabel.setDestination(data.getCityToName());
        //出发地
        tvSrcGo.setText(String.format(Locale.getDefault(),adapter.getContext().getString(R.string.items_trip_place_go),data.getCityFromName()));
        //出发时间
        String str = data.getRecruit_dateFrom();
        if(!TextUtils.isEmpty(str)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
            try {
                Date date = sdf.parse(str);
                sdf = new SimpleDateFormat(adapter.getContext().getString(R.string.items_trip_time_go),Locale.CHINA);
                tvTimeGo.setText(sdf.format(date));
            } catch (ParseException e) {
                Log.e(MineFollowTripsAdapter.class.getName(),e.toString());
            }
        }

        tvSrcGo.setBackgroundResource(completed ? R.drawable.bg_place_go_dis:R.drawable.bg_place_go_en);
        tvTimeGo.setTextColor(ContextCompat.getColor(adapter.getContext(),completed ? R.color.color_text:R.color.color_title_small));
    }

    //报名人数、金额、进度等
    private void setPeopleMoney(TripData data,boolean completed){
        int totalPeople = TypeUtil.str2int(data!=null ? data.getRecruit_people():null);
        int realPeople = TypeUtil.str2int(data!=null ? data.getRecruit_buyCount():null);
        progressBar.setProgress(completed ? 0:(int) (realPeople*1f/totalPeople*100));

        tvQuota.setText(showQuota(completed,totalPeople,realPeople));
        tvPrice.setText(showPrice(completed,TypeUtil.str2float(data!=null ? data.getRecruit_money():null)/100f));
    }

    private SpannableStringBuilder showQuota(boolean completed,int totalPeople,int realPeople){
        SpannableStringBuilder strBuilder = new SpannableStringBuilder();
        int textSize,textColor,end;
        Resources res = adapter.getContext().getResources();

        if(totalPeople==realPeople){
            strBuilder.append("已满员");
            end = strBuilder.length();
            textSize = (int) res.getDimension(R.dimen.fonts_tip);
            textColor = ContextCompat.getColor(adapter.getContext(),completed ? R.color.color_text:R.color.color_title_small);
            strBuilder.setSpan(new AbsoluteSizeSpan(textSize),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return strBuilder;
        }

        strBuilder.append("剩余名额：");
        end = strBuilder.length();
        textSize = (int) res.getDimension(R.dimen.fonts_tip);
        textColor = ContextCompat.getColor(adapter.getContext(),completed ? R.color.color_text:R.color.color_title_small);
        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        strBuilder.append(Integer.toString(totalPeople-realPeople));
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

    private SpannableStringBuilder showPrice(boolean completed,float price){
        int end;
        int textSize,textColor;
        Resources res = adapter.getContext().getResources();

        SpannableStringBuilder strBuilder = new SpannableStringBuilder();

        strBuilder.append("旅费金额：");
        end = strBuilder.length();
        textSize = (int) res.getDimension(R.dimen.fonts_tip);
        textColor = ContextCompat.getColor(adapter.getContext(),completed ? R.color.color_text:R.color.color_title_small);
        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        strBuilder.append(Float.toString(price));
        textSize = (int) res.getDimension(R.dimen.head_medium);
        textColor = ContextCompat.getColor(adapter.getContext(),R.color.color_warn);
        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),end,strBuilder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new StyleSpan(Typeface.BOLD),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        end = strBuilder.length();
        strBuilder.append("元");
        textSize = (int) res.getDimension(R.dimen.fonts_tip);
        textColor = ContextCompat.getColor(adapter.getContext(),completed ? R.color.color_text:R.color.color_title_small);
        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return strBuilder;
    }

    @Override
    public void onBind(BaseHolder holder,Footprint t) {
        if(t==null)
            return;
        TripData data = TextUtils.isEmpty(t.getJson())
                ? null:new Gson().fromJson(t.getJson(),TripData.class);
        boolean completed = data!=null && TypeUtil.str2int(data.getRecruit_status())==FollowTrip.STATUS_COMPLETED;

        setUserInfo(t);
        setTripInfo(t,data,completed);
        setPlaceTime(data,completed);
        setPeopleMoney(data,completed);
    }

}
