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
import com.lots.travel.store.db.Hotel;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.model.request.GetCityItems;
import com.lots.travel.network.service.SpotService;
import com.lots.travel.util.TypeUtil;
import com.lots.travel.widget.CheckableImageView;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;
import com.lots.travel.widget.refresh.PageIterator;
import com.lots.travel.widget.refresh.SelectItemsAdapter;

import java.util.List;

import io.reactivex.Single;

/**
 * 选取酒店，对应接口中的hotel，hotelIn=false，stay对应的是hotelIn=true
 */

public class ChooseHotelsFragment extends ChooseComponentsFragment<Hotel> {
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
    public PageIterator.PageRequest<Hotel> createPageRequest() {
        return new PageIterator.PageRequest<Hotel>() {
            @Override
            public Single<HttpResult<List<Hotel>>> execute(int page) {
                GetCityItems params = new GetCityItems();
                params.setCityId(cityId);
                params.setPageNo(page);
                params.setPageSize(10);
                params.setKeyword(getSearchKey().toString());
                return ServiceGenerator.createService(getContext(), SpotService.class)
                        .getHotelListByCity(params);
            }
        };
    }

    @Override
    public AbstractLoadAdapter<Hotel> createAdapter(RecyclerView rv) {
        HotelAdapter adapter = new HotelAdapter(getContext(),rv);
        adapter.setOnSelectChangedListener(onSelectChangedListener);
        return adapter;
    }

    @Override
    public void setOnSelectChangedListener(SelectItemsAdapter.OnSelectChangedListener listener) {
        onSelectChangedListener = listener;
    }

    @Override
    public boolean hasSelectedItems() {
        return getAdapter() != null && ((HotelAdapter)getAdapter()).hasSelectedItems();
    }

    @Override
    public List<Hotel> getSelectedItems() {
        return ((HotelAdapter)getAdapter()).getSelectedItems();
    }

    private class HotelAdapter extends SelectItemsAdapter<Hotel> {
        private RequestOptions mRequestOptions;

        HotelAdapter(Context context,RecyclerView rv) {
            super(context,rv);

            mRequestOptions = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .priority(Priority.HIGH);
        }

        @Override
        public RecyclerView.ViewHolder onCreateItemHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_choose_hotel,parent,false);
            return new HotelHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            HotelHolder hotelHolder = (HotelHolder) holder;
            Hotel item = getItem(position);
            hotelHolder.bind(item,isSelected(position));
        }

        @Override
        public void onRefreshed() {
            super.onRefreshed();
        }

        @Override
        public void onLoaded() {}

        private View.OnClickListener onCheckListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HotelHolder holder = (HotelHolder) v.getTag();
                int pos = holder.getAdapterPosition();
                Hotel item = getItem(pos);

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

        class HotelHolder extends RecyclerView.ViewHolder{
            private CheckableImageView ivImg;
            private TextView tvName;
            private RatingBar rbScore;
            private TextView tvScoreType;
            private TextView tvTag;
            private TextView tvLocation;

            HotelHolder(View v) {
                super(v);

                ivImg = (CheckableImageView) v.findViewById(R.id.iv_img);
                tvName = (TextView) v.findViewById(R.id.tv_name);
                rbScore = (RatingBar) v.findViewById(R.id.rb_score);
                tvScoreType = (TextView) v.findViewById(R.id.tv_score_type);
                tvTag = (TextView) v.findViewById(R.id.tv_tag);
                tvLocation = (TextView) v.findViewById(R.id.tv_location);

                View tvDetail = v.findViewById(R.id.tv_detail);
                tvDetail.setTag(this);
                tvDetail.setOnClickListener(onCheckListener);

                v.setTag(this);
                v.setOnClickListener(onCheckListener);
            }

            void bind(Hotel item,boolean selected){
                String url = item.getImg();

                if(!TextUtils.isEmpty(url)){
                    Glide.with(getContext())
                            .load(url)
                            .apply(mRequestOptions)
                            .into(ivImg);
                }

                tvName.setText(item.getName());

                float score = TypeUtil.str2float(item.getScore());
                rbScore.setRating(score);
                tvScoreType.setText(item.getScoreType());
                tvTag.setText(item.getTag());
                tvLocation.setText(TypeUtil.mergeString(item.getDistance()," · ",item.getZone()));
                ivImg.setChecked(selected);
            }

        }

    }

}
