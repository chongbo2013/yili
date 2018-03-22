package com.lots.travel.main.info.mine.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.lots.travel.R;
import com.lots.travel.main.info.mine.model.FollowPeople;
import com.lots.travel.store.db.Hotel;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;

/**
 * Created by nalanzi on 2018/1/11.
 */

public class MineFollowPeoplesAdapter extends AbstractLoadAdapter<FollowPeople> {
    private Context mContext;
    private RequestOptions mRequestOptions;

    public MineFollowPeoplesAdapter(Context context,RecyclerView rv) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mine_follow_peoples,parent,false);
        return new Holder(v);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        FollowPeople people = getItem(position);
        Holder cHolder = (Holder) holder;

        if(people==null)
            return;

        Glide.with(mContext)
                .load(people.getFaceImg())
                .apply(mRequestOptions)
                .into(cHolder.ivPortrait);

        cHolder.tvName.setText(people.getUserName());
        cHolder.tvProfession.setText(people.getTitle());

        String str = people.getSex();
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
            cHolder.tvName.setCompoundDrawables(null,null,sexDrawable,null);
        }
    }

    @Override
    public void onRefreshed() {}

    @Override
    public void onLoaded() {}

    class Holder extends RecyclerView.ViewHolder{
        private ImageView ivPortrait;
        private TextView tvName,tvProfession;

        public Holder(View v) {
            super(v);
            ivPortrait = (ImageView) v.findViewById(R.id.iv_portrait);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvProfession = (TextView) v.findViewById(R.id.tv_profession);
        }

    }

}
