package com.lots.travel.main.info.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.lots.travel.R;
import com.lots.travel.base.WebViewActivity;
import com.lots.travel.main.info.mine.model.FollowPeople;
import com.lots.travel.main.info.mine.model.FollowPlace;
import com.lots.travel.util.Constant;
import com.lots.travel.util.TypeUtil;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;

import java.util.Locale;

/**
 * Created by nalanzi on 2018/1/22.
 */

public class MineFollowSpotsAdapter extends AbstractLoadAdapter<FollowPlace> {
    private Context mContext;
    private RequestOptions mRequestOptions;

    public MineFollowSpotsAdapter(Context context,RecyclerView rv) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mine_follow_spots,parent,false);
        return new Holder(v);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        FollowPlace data = getItem(position);
        if(data==null)
            return;
        Holder cHolder = (Holder) holder;
        String url = data.getViewImg();
        if(!TextUtils.isEmpty(url)) {
            url = url.startsWith("http") ? url:Constant.PATH_OSS_IMAGE+url;
            Glide.with(mContext)
                    .load(url)
                    .apply(mRequestOptions)
                    .into(cHolder.ivImg);
        }

        String str;
        cHolder.tvName.setText(data.getName());
        str = data.getScore();
        if(TextUtils.isEmpty(str)){
            cHolder.rbScore.setVisibility(View.INVISIBLE);
        }else{
            cHolder.rbScore.setVisibility(View.VISIBLE);
            cHolder.rbScore.setRating(TypeUtil.str2float(str));
        }

        float ywTime = TypeUtil.str2float(data.getYwTime())/60;
        cHolder.tvYwTime.setText(ywTime==0 ?
                "" : String.format(Locale.getDefault(), mContext.getString(R.string.spot_yw_time),ywTime));

        cHolder.tvPercentPeople.setText(data.getPercentPeople());

        str = data.getShortDesc();
        if(TextUtils.isEmpty(str)){
            cHolder.tvShortDesc.setVisibility(View.GONE);
        }else{
            cHolder.tvShortDesc.setVisibility(View.VISIBLE);
            cHolder.tvShortDesc.setText(data.getShortDesc());
        }
    }

    @Override
    public void onRefreshed() {}

    @Override
    public void onLoaded() {}

    private View.OnClickListener mOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Holder holder = (Holder) v.getTag();
            int flatPosition = holder.getAdapterPosition();
            int itemPosition = getItemPosition(flatPosition);
            FollowPlace data = getItem(itemPosition);
            if(data!=null && !TextUtils.isEmpty(data.getViewurl())){
                WebViewActivity.toWeb(mContext,null,data.getViewurl(),false);
            }
        }
    };

    class Holder extends RecyclerView.ViewHolder{
        private ImageView ivImg;
        private TextView tvName;
        private RatingBar rbScore;
        private TextView tvYwTime;
        private TextView tvPercentPeople;
        private TextView tvShortDesc;

        public Holder(View v) {
            super(v);
            ivImg = (ImageView) v.findViewById(R.id.iv_img);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            rbScore = (RatingBar) v.findViewById(R.id.rb_score);
            tvYwTime = (TextView) v.findViewById(R.id.tv_yw_time);
            tvPercentPeople = (TextView) v.findViewById(R.id.tv_percent_people);
            tvShortDesc = (TextView) v.findViewById(R.id.tv_short_desc);
            TextView tvDetail = (TextView) v.findViewById(R.id.tv_detail);
            tvDetail.setTag(this);
            tvDetail.setOnClickListener(mOnClickListener);
        }

        void bind(FollowPeople data){

        }

    }

}
