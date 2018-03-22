package com.lots.travel.schedule.route;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.lots.travel.R;
import com.lots.travel.store.db.Spot;
import com.lots.travel.util.TypeUtil;
import com.lots.travel.widget.CheckableImageView;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by nalanzi on 2017/12/28.
 */

public class ChooseSpotsAdapter extends AbstractLoadAdapter<Spot> {
    private Context mContext;
    private RequestOptions mRequestOptions;
    private List<Spot> mSelectedSpots;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Holder holder = (Holder) v.getTag();
            int itemPosition = getItemPosition(holder.getAdapterPosition());
            Spot spot = getItem(itemPosition);

            switch (v.getId()){
                case R.id.tv_detail:
                    if(mOnItemCallback !=null)
                        mOnItemCallback.onDetail(spot);
                    break;

                case R.id.tv_additional:
                    if(mOnItemCallback !=null)
                        mOnItemCallback.onAdditional(spot);
                    break;

                case R.id.card_spot:
                    boolean selected = mSelectedSpots.contains(spot);
                    if(selected)
                        mSelectedSpots.remove(spot);
                    else
                        mSelectedSpots.add(spot);
                    holder.ivImg.setChecked(!selected);
                    if(mOnItemCallback!=null)
                        mOnItemCallback.onSelectChanged(mSelectedSpots.size());
                    break;
            }
        }
    };

    private OnItemCallback mOnItemCallback;

    public ChooseSpotsAdapter(Context context,RecyclerView rv) {
        super(rv);
        mContext = context;
        mSelectedSpots = new ArrayList<>();
        mRequestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);
    }

    public List<Spot> getSelectedSpots(){
        return mSelectedSpots;
    }

    public void setOnItemCallback(OnItemCallback listener){
        mOnItemCallback = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_spots,parent,false);
        return new Holder(v);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        Spot spot = getItem(position);
        Holder sHolder = (Holder) holder;

        if(spot==null)
            return;

        String url = spot.getViewImg();
        if(!TextUtils.isEmpty(url)){
            Glide.with(mContext)
                    .load(spot.getViewImg())
                    .apply(mRequestOptions)
                    .into(sHolder.ivImg);
        }
        sHolder.ivImg.setChecked(mSelectedSpots.contains(spot));

        sHolder.tvName.setText(spot.getName());
        sHolder.rbScore.setRating(TypeUtil.str2float(spot.getScore()));
        sHolder.tvYwTime.setText(String.format(Locale.getDefault(),
                mContext.getString(R.string.route_choose_spots_yw),TypeUtil.str2float(spot.getYwTime())/60));
        sHolder.tvPercentPeople.setText(spot.getPercentPeople());
        sHolder.tvShortDesc.setText(spot.getShortDesc());
    }

    @Override
    public void onRefreshed() {
        mSelectedSpots.clear();
    }

    @Override
    public void onLoaded() {}

    class Holder extends RecyclerView.ViewHolder{
        private CheckableImageView ivImg;
        private TextView tvName;
        private RatingBar rbScore;
        private TextView tvYwTime;
        private TextView tvPercentPeople;
        private TextView tvShortDesc;
        private TextView tvAdditional;
        private TextView tvDetail;

        public Holder(View v) {
            super(v);
            ivImg = (CheckableImageView) v.findViewById(R.id.iv_img);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            rbScore = (RatingBar) v.findViewById(R.id.rb_score);
            tvYwTime = (TextView) v.findViewById(R.id.tv_yw_time);
            tvPercentPeople = (TextView) v.findViewById(R.id.tv_percent_people);
            tvShortDesc = (TextView) v.findViewById(R.id.tv_short_desc);
            tvAdditional = (TextView) v.findViewById(R.id.tv_additional);
            tvDetail = (TextView) v.findViewById(R.id.tv_detail);

            v.setTag(this);
            tvAdditional.setTag(this);
            tvDetail.setTag(this);
            v.setOnClickListener(mOnClickListener);
            tvAdditional.setOnClickListener(mOnClickListener);
            tvDetail.setOnClickListener(mOnClickListener);
        }

    }

    public interface OnItemCallback {
        void onDetail(Spot spot);
        void onAdditional(Spot spot);
        void onSelectChanged(int n);
    }

}
