package com.lots.travel.place;

import android.content.Context;
import android.os.Bundle;
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
import com.lots.travel.base.WebViewActivity;
import com.lots.travel.network.model.request.GetCityItems;
import com.lots.travel.network.model.request.GetPlaces;
import com.lots.travel.store.db.Spot;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.service.SpotService;
import com.lots.travel.util.TypeUtil;
import com.lots.travel.widget.CheckableImageView;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;
import com.lots.travel.widget.refresh.PageIterator;
import com.lots.travel.widget.refresh.SelectItemsAdapter;

import java.util.List;
import java.util.Locale;

import io.reactivex.Single;

/**
 * Created by nalanzi on 2017/10/15.
 */

public class ChooseSpotsFragment extends ChooseComponentsFragment<Spot> {
    private long cityId;
    private SelectItemsAdapter.OnSelectChangedListener onSelectChangedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if(bundle!=null)
            cityId = bundle.getLong(ChooseComponentsActivity.EXTRA_CITY_ID);
    }

    @Override
    public PageIterator.PageRequest<Spot> createPageRequest() {
        return new PageIterator.PageRequest<Spot>() {
            @Override
            public Single<HttpResult<List<Spot>>> execute(int page) {
                GetCityItems params = new GetCityItems();
                params.setCityId(cityId);
                params.setPageNo(page);
                params.setPageSize(10);
                params.setKeyword(getSearchKey().toString());

                return ServiceGenerator.createService(getContext(), SpotService.class)
                                .getSpotListByCity(params);
            }
        };
    }

    @Override
    public AbstractLoadAdapter<Spot> createAdapter(RecyclerView rv) {
        SpotAdapter spotAdapter = new SpotAdapter(getContext(),rv);
        spotAdapter.setOnSelectChangedListener(onSelectChangedListener);
        return spotAdapter;
    }

    @Override
    public void setOnSelectChangedListener(SelectItemsAdapter.OnSelectChangedListener listener) {
        onSelectChangedListener = listener;
    }

    @Override
    public boolean hasSelectedItems() {
        return getAdapter() != null && ((SpotAdapter)getAdapter()).hasSelectedItems();
    }

    @Override
    public List<Spot> getSelectedItems() {
        return ((SpotAdapter)getAdapter()).getSelectedItems();
    }

    private class SpotAdapter extends SelectItemsAdapter<Spot> {
        private RequestOptions mRequestOptions;

        SpotAdapter(Context context,RecyclerView rv) {
            super(context,rv);

            mRequestOptions = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .priority(Priority.HIGH);
        }


        @Override
        public RecyclerView.ViewHolder onCreateItemHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_choose_spot,parent,false);
            return new SpotHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            SpotHolder spotHolder = (SpotHolder) holder;
            Spot item = getItem(position);
            spotHolder.bind(item,isSelected(position));
        }

        @Override
        public void onRefreshed() {
            super.onRefreshed();
        }

        @Override
        public void onLoaded() {
//            StringBuilder strBuilder = new StringBuilder();
//            for(int i=0;i<getDataItemCount();i++){
//                Spot spot = getItem(i);
//                strBuilder.append("i：").append(spot.getName()).append("\n");
//            }
//            Log.e("！！！！！！！！！！",strBuilder.toString());
        }

        private View.OnClickListener onCheckListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpotHolder holder = (SpotHolder) v.getTag();
                int pos = holder.getAdapterPosition();
                Spot item = getItem(pos);

                if(item==null)
                    return;

                if(v.getId()==R.id.tv_detail){
                    if(!TextUtils.isEmpty(item.getViewurl()))
                        WebViewActivity.toWeb(getContext(),null,item.getViewurl(),false);
                }else{
                    //改变数据
                    boolean old = isSelected(pos);
                    setSelected(pos,item,!old);

                    //ui变化
                    holder.ivImg.setChecked(!old);
                    //触发监听
                    triggerSelectChangedListener();
                }
            }
        };

        class SpotHolder extends RecyclerView.ViewHolder{
            private CheckableImageView ivImg;
            private TextView tvName;
            private RatingBar rbScore;
            private TextView tvYwTime;
            private TextView tvShortDesc;
            private TextView tvPercentPeople;

            SpotHolder(View v) {
                super(v);

                ivImg = (CheckableImageView) v.findViewById(R.id.iv_img);
                tvName = (TextView) v.findViewById(R.id.tv_name);
                rbScore = (RatingBar) v.findViewById(R.id.rb_score);
                tvYwTime = (TextView) v.findViewById(R.id.tv_yw_time);
                tvShortDesc = (TextView) v.findViewById(R.id.tv_short_desc);
                tvPercentPeople = (TextView) v.findViewById(R.id.tv_percent_people);

                View tvDetail = v.findViewById(R.id.tv_detail);
                tvDetail.setTag(this);
                tvDetail.setOnClickListener(onCheckListener);

                v.setTag(this);
                v.setOnClickListener(onCheckListener);
            }

            void bind(Spot item,boolean selected){
                String url = item.getViewImg();

                if(!TextUtils.isEmpty(url)){
                    Glide.with(getContext())
                            .load(url)
                            .apply(mRequestOptions)
                            .into(ivImg);
                }

                tvName.setText(item.getName());
                rbScore.setRating(TypeUtil.str2float(item.getScore()));

                float ywTime = TypeUtil.str2float(item.getYwTime())/60;
                tvYwTime.setText(ywTime==0 ?
                        "" : String.format(Locale.getDefault(), getString(R.string.spot_yw_time),ywTime));

                tvPercentPeople.setText(item.getPercentPeople());
                tvShortDesc.setText(item.getShortDesc());

                ivImg.setChecked(selected);
            }

        }

    }

}
