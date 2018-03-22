package com.lots.travel.main.info.person;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.lots.travel.R;

import com.lots.travel.widget.RoundImageView;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;

/**
 * Created by nalanzi on 2017/11/24.
 */

public class PersonAlbumAdapter extends AbstractLoadAdapter<Album> {
    private Context mContext;
    private RequestOptions mRequestOptions;
    private OnItemClickListener mOnItemClickListener;

    public PersonAlbumAdapter(Context context,RecyclerView rv) {
        super(rv);
        mContext = context;
        mRequestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mOnItemClickListener==null)
                return;

            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag(R.id.iv_album);
            int itemPos = getItemPosition(holder.getAdapterPosition());
            mOnItemClickListener.onItemClick(itemPos);
        }
    };

    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person_album,parent,false);
        return new Holder(root);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        Album album = getItem(position);
        if(album!=null) {
            Glide.with(mContext)
                    .load(album.getImg())
                    .apply(mRequestOptions)
                    .into((RoundImageView) holder.itemView);
        }
    }

    @Override
    public void onRefreshed() {}

    @Override
    public void onLoaded() {}

    class Holder extends RecyclerView.ViewHolder{

        public Holder(View v) {
            super(v);
            v.setTag(R.id.iv_album,this);
            v.setOnClickListener(onClickListener);
        }

    }

    public interface OnItemClickListener{
        void onItemClick(int itemPos);
    }

}
