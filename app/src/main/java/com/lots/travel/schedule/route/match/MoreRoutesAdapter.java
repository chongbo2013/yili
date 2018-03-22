package com.lots.travel.schedule.route.match;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.lots.travel.R;
import com.lots.travel.network.model.result.MoreRoute;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;

import java.util.Locale;

/**
 * Created by nalanzi on 2018/1/2.
 */

public class MoreRoutesAdapter extends AbstractLoadAdapter<MoreRoute> {
    private Context mContext;
    private RequestOptions mRequestOptions;
    private OnItemClickListener mOnItemClickListener;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mOnItemClickListener==null)
                return;

            Holder holder = (Holder) v.getTag();
            MoreRoute item = getItem(holder.getAdapterPosition());
            mOnItemClickListener.onItemClick(item);
        }
    };

    public MoreRoutesAdapter(Context context,RecyclerView rv) {
        super(rv);
        mContext = context;
        mRequestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more_routes,parent,false);
        return new Holder(root);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        MoreRoute route = getItem(position);
        Holder cHolder = (Holder) holder;

        assert route != null;
        Glide.with(mContext)
                .load(route.getFaceImg())
                .apply(mRequestOptions)
                .into(cHolder.ivPortrait);

        cHolder.tvName.setText(route.getUserName());
        cHolder.tvProfession.setText(route.getTitle());
        cHolder.tvPercentTrust.setText(String.format(Locale.getDefault(),mContext.getString(R.string.route_match_more_trust),route.getKppercent()));
        cHolder.tvPraise.setText(String.format(Locale.getDefault(),"%d",route.getKppeople()));
        cHolder.tvFeature.setText(showFeature(
                mContext.getString(R.string.route_match_more_feature_1),
                String.format(Locale.getDefault(),mContext.getString(R.string.route_match_more_feature_2),route.getDays(),route.getSpotTotles())));
    }

    @Override
    public void onRefreshed() {}

    @Override
    public void onLoaded() {}

    private SpannableStringBuilder showFeature(String s1, String s2){
        SpannableStringBuilder strBuilder = new SpannableStringBuilder();

        int textColor;
        int end = 0;
        if(!TextUtils.isEmpty(s1)){
            strBuilder.append(s1);
            end = strBuilder.length();
            textColor = ContextCompat.getColor(mContext,R.color.color_text);
            strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if(!TextUtils.isEmpty(s2)){
            strBuilder.append(s2);
            textColor = ContextCompat.getColor(mContext,R.color.color_main);
            strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return strBuilder;
    }

    class Holder extends RecyclerView.ViewHolder{
        private ImageView ivPortrait;
        private TextView tvName;
        private TextView tvProfession;
        private TextView tvPercentTrust;
        private TextView tvPraise;
        private TextView tvFeature;

        public Holder(View v) {
            super(v);

            ivPortrait = (ImageView) v.findViewById(R.id.iv_portrait);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvProfession = (TextView) v.findViewById(R.id.tv_profession);
            tvPercentTrust = (TextView) v.findViewById(R.id.tv_percent_trust);
            tvPraise = (TextView) v.findViewById(R.id.tv_praise);
            tvFeature = (TextView) v.findViewById(R.id.tv_feature);

            v.setTag(this);
            v.setOnClickListener(mOnClickListener);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(MoreRoute item);
    }

}
