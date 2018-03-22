package com.lots.travel.main.info.mine.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import com.google.gson.Gson;
import com.lots.travel.R;
import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.main.info.mine.model.FollowTrip;
import com.lots.travel.util.CommonUtil;
import com.lots.travel.util.Constant;
import com.lots.travel.util.TypeUtil;
import com.lots.travel.widget.DestinationLabel;
import com.lots.travel.widget.ItemTypeImageView;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nalanzi on 2018/1/23.
 */

public class MineFollowTripsAdapter extends AbstractLoadAdapter<FollowTrip> {
    private Context mContext;
    private RequestOptions mRequestOptions;

    public MineFollowTripsAdapter(Context context,RecyclerView rv) {
        super(rv);
        mContext = context;
        mRequestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mine_follow_trips,parent,false);
        return new Holder(v);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        FollowTrip f = getItem(position);
        if(f==null)
            return;
        FollowTrip.Data data = TextUtils.isEmpty(f.getJson())
                ? null:new Gson().fromJson(f.getJson(),FollowTrip.Data.class);
        boolean completed = data!=null && TypeUtil.str2int(data.getRecruit_status())==FollowTrip.STATUS_COMPLETED;

        Holder cHolder = (Holder) holder;
        cHolder.setUserInfo(f);
        cHolder.setTripInfo(f,data,completed);
        cHolder.setPlaceTime(data,completed);
        cHolder.setPeopleMoney(data,completed);
    }

    private boolean isTagChecked(String value){
        return !TextUtils.isEmpty(value) && value.equals("1");
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
    public void onRefreshed() {}

    @Override
    public void onLoaded() {}

    class Holder extends RecyclerView.ViewHolder{
        private ImageView ivPortrait;
        private TextView tvName;
        private TextView tvProfession;
        //背景、类型图片、标题
        private ImageView ivImg;
        private ImageView ivCompleted;
        private ItemTypeImageView ivType;
        private TextView tvWord;
        //目的地、出发时间
        private TextView tvSrcGo,tvTimeGo;
        //进度、剩余人数、价格
        private ProgressBar progressBar;
        private TextView tvQuota,tvPrice;
        //目的地标签
        private DestinationLabel vLabel;

        public Holder(View v) {
            super(v);
            ivPortrait = (ImageView) v.findViewById(R.id.iv_portrait);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvProfession = (TextView) v.findViewById(R.id.tv_profession);
            ivImg = (ImageView) v.findViewById(R.id.iv_img);
            ivCompleted = (ImageView) v.findViewById(R.id.iv_completed);
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
        private void setUserInfo(FollowTrip f){
            //头像
            loadImage(f.getFaceImg(),ivPortrait);
            //名称
            tvName.setText(f.getUserName());
            //性别
            int sexDrawableId = CommonUtil.getSexDrawable(f.getSex());
            if(sexDrawableId!=-1) {
                Drawable sexDrawable = ContextCompat.getDrawable(mContext,sexDrawableId);
                sexDrawable.setBounds(0,0,sexDrawable.getIntrinsicWidth(),sexDrawable.getIntrinsicHeight());
                tvName.setCompoundDrawables(null,null,sexDrawable,null);
            }
            //职位
            tvProfession.setText(f.getTitle());
        }

        //tripTitle、tripImg、tripType、是否结束
        private void setTripInfo(FollowTrip f,FollowTrip.Data data,boolean completed){
            //title
            tvWord.setText(f.getTripTitle());
            //封面
            String tripImg = data!=null ? data.getTripImg():null;
            if(TextUtils.isEmpty(tripImg)){
                tripImg = f.getTripImgs();
                String[] strs = TypeUtil.str2arrays(tripImg);
                tripImg = strs.length>0 ? strs[0]:f.getTripVideoImg();
            }
            loadImage(tripImg,ivImg);
            //type
            if(isTagChecked(f.getTag2()))
                ivType.setImageResource(R.drawable.img_recruit_photograph);
            //结束
            ivCompleted.setVisibility(completed ? View.VISIBLE:View.INVISIBLE);
        }

        //时间、地点
        private void setPlaceTime(FollowTrip.Data data,boolean completed){
            if(data==null)
                return;

            //目的地
            vLabel.setDestination(data.getCityToName());
            //出发地
            tvSrcGo.setText(String.format(Locale.getDefault(),mContext.getString(R.string.items_trip_place_go),data.getCityFromName()));
            //出发时间
            String str = data.getRecruit_dateFrom();
            if(!TextUtils.isEmpty(str)){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
                try {
                    Date date = sdf.parse(str);
                    sdf = new SimpleDateFormat(mContext.getString(R.string.items_trip_time_go),Locale.CHINA);
                    tvTimeGo.setText(sdf.format(date));
                } catch (ParseException e) {
                    Log.e(MineFollowTripsAdapter.class.getName(),e.toString());
                }
            }

            tvSrcGo.setBackgroundResource(completed ? R.drawable.bg_place_go_dis:R.drawable.bg_place_go_en);
            tvTimeGo.setTextColor(ContextCompat.getColor(mContext,completed ? R.color.color_text:R.color.color_title_small));
        }

        //报名人数、金额、进度等
        private void setPeopleMoney(FollowTrip.Data data,boolean completed){
            int totalPeople = TypeUtil.str2int(data!=null ? data.getRecruit_people():null);
            int realPeople = TypeUtil.str2int(data!=null ? data.getRecruit_buyCount():null);
            progressBar.setProgress(completed ? 0:(int) (realPeople*1f/totalPeople*100));

            tvQuota.setText(showQuota(completed,totalPeople,realPeople));
            tvPrice.setText(showPrice(completed,TypeUtil.str2float(data!=null ? data.getRecruit_money():null)/100f));
        }

        private SpannableStringBuilder showQuota(boolean completed,int totalPeople,int realPeople){
            SpannableStringBuilder strBuilder = new SpannableStringBuilder();
            int textSize,textColor,end;
            Resources res = mContext.getResources();

            if(totalPeople==realPeople){
                strBuilder.append("已满员");
                end = strBuilder.length();
                textSize = (int) res.getDimension(R.dimen.fonts_tip);
                textColor = ContextCompat.getColor(mContext,completed ? R.color.color_text:R.color.color_title_small);
                strBuilder.setSpan(new AbsoluteSizeSpan(textSize),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return strBuilder;
            }

            strBuilder.append("剩余名额：");
            end = strBuilder.length();
            textSize = (int) res.getDimension(R.dimen.fonts_tip);
            textColor = ContextCompat.getColor(mContext,completed ? R.color.color_text:R.color.color_title_small);
            strBuilder.setSpan(new AbsoluteSizeSpan(textSize),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            strBuilder.append(Integer.toString(totalPeople-realPeople));
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

        private SpannableStringBuilder showPrice(boolean completed,float price){
            int end;
            int textSize,textColor;
            Resources res = mContext.getResources();

            SpannableStringBuilder strBuilder = new SpannableStringBuilder();

            strBuilder.append("旅费金额：");
            end = strBuilder.length();
            textSize = (int) res.getDimension(R.dimen.fonts_tip);
            textColor = ContextCompat.getColor(mContext,completed ? R.color.color_text:R.color.color_title_small);
            strBuilder.setSpan(new AbsoluteSizeSpan(textSize),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            strBuilder.append(Float.toString(price));
            textSize = (int) res.getDimension(R.dimen.head_medium);
            textColor = ContextCompat.getColor(mContext,R.color.color_warn);
            strBuilder.setSpan(new AbsoluteSizeSpan(textSize),end,strBuilder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            strBuilder.setSpan(new StyleSpan(Typeface.BOLD),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            end = strBuilder.length();
            strBuilder.append("元");
            textSize = (int) res.getDimension(R.dimen.fonts_tip);
            textColor = ContextCompat.getColor(mContext,completed ? R.color.color_text:R.color.color_title_small);
            strBuilder.setSpan(new AbsoluteSizeSpan(textSize),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return strBuilder;
        }

    }

}
