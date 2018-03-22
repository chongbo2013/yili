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

public class MineFollowCitiesAdapter extends AbstractLoadAdapter<FollowPlace> {
    private Context mContext;
    private RequestOptions mRequestOptions;

    public MineFollowCitiesAdapter(Context context, RecyclerView rv) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mine_follow_cities,parent,false);
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

        cHolder.tvName.setText(data.getName());
        if(data.getYwDay()>0)
            cHolder.tvYwDay.setText(String.format(Locale.getDefault(),mContext.getResources().getString(R.string.place_yw_day),data.getYwDay()));

        String strTags = data.getTag();
        cHolder.tvTags[0].setVisibility(View.INVISIBLE);
        cHolder.tvTags[1].setVisibility(View.INVISIBLE);
        cHolder.tvTags[2].setVisibility(View.INVISIBLE);
        if(!TextUtils.isEmpty(strTags)){
            String[] tags = strTags.split(" ");
            int tagsLength = Math.min(tags.length,3);
            for (int i=0;i<tagsLength;i++) {
                cHolder.tvTags[i].setVisibility(View.VISIBLE);
                cHolder.tvTags[i].setText(tags[i]);
            }
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
        private TextView tvYwDay;
        private TextView[] tvTags;

        public Holder(View v) {
            super(v);
            ivImg = (ImageView) v.findViewById(R.id.iv_img);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvYwDay = (TextView) v.findViewById(R.id.tv_yw_day);

            tvTags = new TextView[3];
            tvTags[0] = (TextView) v.findViewById(R.id.tv_tag_1);
            tvTags[1] = (TextView) v.findViewById(R.id.tv_tag_2);
            tvTags[2] = (TextView) v.findViewById(R.id.tv_tag_3);

            TextView tvDetail = (TextView) v.findViewById(R.id.tv_detail);
            tvDetail.setTag(this);
            tvDetail.setOnClickListener(mOnClickListener);
        }

    }

}
