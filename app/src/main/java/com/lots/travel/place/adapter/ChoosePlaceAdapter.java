package com.lots.travel.place.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.lots.travel.R;
import com.lots.travel.place.model.Place;
import com.lots.travel.place.widget.PlaceProfileView;
import com.lots.travel.widget.ImageLoader;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;

/**
 * Created by nalanzi on 2017/9/3.
 */

public class ChoosePlaceAdapter extends AbstractLoadAdapter<Place> implements ImageLoader {
    private Activity mContext;
    private RecyclerView mRecyclerView;
    private int mCheckedPosition;

    private RequestOptions mRequestOptions;

    private OnCheckChangedListener mOnCheckChangedListener;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Holder holder = (Holder) v.getTag();
            int currentPosition = holder.getAdapterPosition();
            Place place = getItem(getItemPosition(currentPosition));

            switch (v.getId()){
                case PlaceProfileView.ID_DETAIL:
                    //WebViewActivity.toWeb(mContext,place.getSpotName(),);
                    break;

                case R.id.card_place: {
                    //点击的是自己 - 只是变化自身
                    if(mCheckedPosition==currentPosition){
                        mCheckedPosition = -1;
                        holder.vPlaceProfile.setChecked(false);
                    }
                    //点击的是新的 - 新旧变化
                    else{
                        //之前没有选中的
                        if(mCheckedPosition==-1){
                            mCheckedPosition = currentPosition;
                            holder.vPlaceProfile.setChecked(true);
                        }
                        //之前有选中的
                        else{
                            Holder oldHolder = (Holder) mRecyclerView.findViewHolderForAdapterPosition(mCheckedPosition);
                            if(oldHolder!=null)
                                oldHolder.vPlaceProfile.setChecked(false);
                            else
                                notifyItemChanged(mCheckedPosition);
                            holder.vPlaceProfile.setChecked(true);
                            mCheckedPosition = currentPosition;
                        }
                    }
                    if(mOnCheckChangedListener!=null)
                        mOnCheckChangedListener.onChecked(mCheckedPosition!=-1,mCheckedPosition!=-1 ? place:null);
                    break;
                }
            }
        }
    };

    public ChoosePlaceAdapter(Activity context,RecyclerView rv){
        super(rv);
        mContext = context;
        mRecyclerView = rv;
        mCheckedPosition = -1;

        mRequestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);
    }

    public Place getChooseItem(){
        return mCheckedPosition!=-1 ? getItem(mCheckedPosition):null;
    }

    public void setOnCheckChangedListener(OnCheckChangedListener listener){
        mOnCheckChangedListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_place,parent,false);
        return new Holder(root);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        Holder cHolder = (Holder) holder;
        Place place = getItem(position);
        cHolder.vPlaceProfile.setPlace(this,place);
        cHolder.vPlaceProfile.setChecked(position==mCheckedPosition);
    }

    @Override
    public void onRefreshed() {
        mCheckedPosition = -1;
        if(mOnCheckChangedListener!=null)
            mOnCheckChangedListener.onChecked(false,null);
    }

    @Override
    public void onLoaded() {}

    @Override
    public void loadImage(String url, ImageView target) {
        Glide.with(mContext)
                .load(url)
                .apply(mRequestOptions)
                .into(target);
    }

    private class Holder extends RecyclerView.ViewHolder{
        PlaceProfileView vPlaceProfile;

        Holder(View v){
            super(v);

            vPlaceProfile = (PlaceProfileView) v.findViewById(R.id.v_place_profile);
            vPlaceProfile.setDetailTag(this);
            vPlaceProfile.setOnDetailListener(mOnClickListener);

            v.setTag(this);
            v.setOnClickListener(mOnClickListener);
        }
    }

    public interface OnCheckChangedListener{
        void onChecked(boolean checked,Place place);
    }

}
