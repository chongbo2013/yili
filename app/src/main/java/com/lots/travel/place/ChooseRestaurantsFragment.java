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
import com.lots.travel.store.db.Food;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.model.request.GetCityItems;
import com.lots.travel.network.service.SpotService;
import com.lots.travel.widget.CheckableImageView;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;
import com.lots.travel.widget.refresh.PageIterator;
import com.lots.travel.widget.refresh.SelectItemsAdapter;

import java.util.List;

import io.reactivex.Single;

/**
 * 选取餐厅 - 接口中的food
 */
public class ChooseRestaurantsFragment extends ChooseComponentsFragment<Food> {
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
    public PageIterator.PageRequest<Food> createPageRequest() {
        return new PageIterator.PageRequest<Food>() {
            @Override
            public Single<HttpResult<List<Food>>> execute(int page) {
                GetCityItems params = new GetCityItems();
                params.setCityId(cityId);
                params.setPageNo(page);
                params.setPageSize(10);
                params.setKeyword(getSearchKey().toString());

                return ServiceGenerator.createService(getContext(), SpotService.class)
                                .getFoodListByCity(params);
            }
        };
    }

    @Override
    public AbstractLoadAdapter<Food> createAdapter(RecyclerView rv) {
        RestaurantAdapter adapter = new RestaurantAdapter(getContext(),rv);
        adapter.setOnSelectChangedListener(onSelectChangedListener);
        return adapter;
    }

    @Override
    public void setOnSelectChangedListener(SelectItemsAdapter.OnSelectChangedListener listener) {
        onSelectChangedListener = listener;
    }

    @Override
    public boolean hasSelectedItems() {
        return getAdapter() != null && ((RestaurantAdapter) getAdapter()).hasSelectedItems();
    }

    @Override
    public List<Food> getSelectedItems() {
        return ((RestaurantAdapter)getAdapter()).getSelectedItems();
    }

    public class RestaurantAdapter extends SelectItemsAdapter<Food> {
        private RequestOptions mRequestOptions;

        public RestaurantAdapter(Context context,RecyclerView rv) {
            super(context,rv);

            mRequestOptions = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .priority(Priority.HIGH);
        }


        @Override
        public RecyclerView.ViewHolder onCreateItemHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_choose_restaurant,parent,false);
            return new RestaurantHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            RestaurantHolder restaurantHolder = (RestaurantHolder) holder;
            Food item = getItem(position);

            restaurantHolder.bind(item,isSelected(position));
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
                RestaurantHolder holder = (RestaurantHolder) v.getTag();
                int pos = holder.getAdapterPosition();
                Food item = getItem(pos);

                if(item==null)
                    return;

                if(item.getId()==v.getId()){
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

        class RestaurantHolder extends RecyclerView.ViewHolder {
            private CheckableImageView ivImg;
            private TextView tvName;
            private RatingBar rbScore;
            private TextView tvFoodType;
            private TextView tvArea;
            private TextView tvTag;

            RestaurantHolder(View v) {
                super(v);

                ivImg = (CheckableImageView) v.findViewById(R.id.iv_img);
                tvName = (TextView) v.findViewById(R.id.tv_name);
                rbScore = (RatingBar) v.findViewById(R.id.rb_score);
                tvFoodType = (TextView) v.findViewById(R.id.tv_food_type);
                tvArea = (TextView) v.findViewById(R.id.tv_area);
                tvTag = (TextView) v.findViewById(R.id.tv_tag);

                v.setTag(this);
                v.setOnClickListener(onCheckListener);
            }

            void bind(Food item,boolean selected){
                String url = item.getImg();

                if(!TextUtils.isEmpty(url)){
                    Glide.with(getContext())
                            .load(url)
                            .apply(mRequestOptions)
                            .into(ivImg);
                }

                tvName.setText(item.getName());
                rbScore.setRating(item.getScore());
                tvFoodType.setText(item.getFoodType());
                tvArea.setText(item.getArea());
                tvTag.setText(item.getTag());

                ivImg.setChecked(selected);
            }

        }

    }

}
