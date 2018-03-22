package com.lots.travel.footprint;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.lots.travel.R;
import com.lots.travel.network.model.result.Comments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by nalanzi on 2018/1/24.
 */

public class FootprintCommentsAdapter extends RecyclerView.Adapter<FootprintCommentsAdapter.Holder> {
    private Context mContext;
    private RequestOptions mRequestOptions;
    private List<Comments.Item> mComments;

    private int mYear,mDay;

    private OnItemClickListener mOnItemClickListener;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mOnItemClickListener==null)
                return;
            Holder holder = (Holder) v.getTag();
            int flatPosition = holder.getAdapterPosition();
            mOnItemClickListener.onItemClick(flatPosition,mComments.get(flatPosition));
        }
    };

    public FootprintCommentsAdapter(Context context){
        mContext = context;
        mRequestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);
        mComments = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mDay = cal.get(Calendar.DAY_OF_YEAR);
    }

    public void setComments(List<Comments.Item> src){
        mComments.clear();
        if(src!=null && src.size()>0){
            mComments.addAll(src);
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footprint_comments,parent,false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Comments.Item data = mComments.get(position);
        if(!TextUtils.isEmpty(data.getFaceImg())){
            Glide.with(mContext)
                    .load(data.getFaceImg())
                    .apply(mRequestOptions)
                    .into(holder.ivPortrait);
        }

        holder.tvName.setText(data.getUserName());
        holder.tvTime.setText(getTime(data.getCreattime()));
        holder.tvContent.setText(getContent(data));

        String str = data.getSex();
        int sexDrawableId = -1;
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
            holder.tvName.setCompoundDrawables(null,null,sexDrawable,null);
        }
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    private String getTime(long mills){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mills);
        int year = cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DAY_OF_YEAR);

        String str;
        if(year==mYear && day==mDay){
            str = "HH:mm";
        }else if(year==mYear){
            str = "MM月dd HH:mm";
        }else{
            str = "yyyy年MM月dd HH:mm";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(str, Locale.CHINA);
        return sdf.format(cal.getTime());
    }

    private SpannableStringBuilder getContent(Comments.Item data){
        SpannableStringBuilder strBuilder = new SpannableStringBuilder();
        int end = 0;
        int textColor;
        //判断是否是回复
        if(!TextUtils.isEmpty(data.getReplyUserName())){
            strBuilder.append(mContext.getString(R.string.footprint_comment_replay));
            end = strBuilder.length();
            textColor = ContextCompat.getColor(mContext,R.color.color_title_small);
            strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textColor = ContextCompat.getColor(mContext,R.color.color_main);
            strBuilder.append(data.getReplyUserName());
            strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            strBuilder.append(mContext.getString(R.string.footprint_comment_replay_colon));
            end = strBuilder.length();
        }
        strBuilder.append(data.getContent());
        textColor = ContextCompat.getColor(mContext,R.color.color_title_small);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return strBuilder;
    }

    class Holder extends RecyclerView.ViewHolder{
        private ImageView ivPortrait;
        private TextView tvName,tvTime,tvContent;

        public Holder(View v) {
            super(v);
            ivPortrait = (ImageView) v.findViewById(R.id.iv_portrait);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvTime = (TextView) v.findViewById(R.id.tv_time);
            tvContent = (TextView) v.findViewById(R.id.tv_content);
            v.setTag(this);
            v.setOnClickListener(mOnClickListener);
        }

    }

    public interface OnItemClickListener{
        void onItemClick(int flatPosition,Comments.Item comment);
    }

}
