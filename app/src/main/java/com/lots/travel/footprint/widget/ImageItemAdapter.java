package com.lots.travel.footprint.widget;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lots.travel.R;
import com.lots.travel.widget.ImageLoader;

/**
 * Created by nalanzi on 2017/11/19.
 */

public class ImageItemAdapter extends RecyclerView.Adapter<ImageItemAdapter.Holder> {
    private String[] mImages;
    private ImageLoader mImageLoader;

    private int mStartIndex;
    private OnItemClickListener mOnItemClickListener;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Holder holder = (Holder) v.getTag(R.id.iv_image);
            if(mOnItemClickListener !=null)
                mOnItemClickListener.onItemClick(holder.getAdapterPosition(),mImages);
        }
    };

    public ImageItemAdapter(ImageLoader loader){
        mImageLoader = loader;
    }

    public void setImageList(int startIndex,String[] src){
        mStartIndex = startIndex;
        mImages = src;
        notifyDataSetChanged();
    }

    public String[] getImageList(){
        return mImages;
    }

    public int getStartIndex(){
        return mStartIndex;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_item,parent,false);
        return new Holder(root);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        mImageLoader.loadImage(mImages[position+mStartIndex], (ImageView) holder.itemView);
    }

    @Override
    public int getItemCount() {
        return mImages==null ? 0:mImages.length-mStartIndex;
    }

    class Holder extends RecyclerView.ViewHolder{

        public Holder(View v) {
            super(v);
            v.setTag(R.id.iv_image,this);
            v.setOnClickListener(mOnClickListener);
        }

    }

    public interface OnItemClickListener{
        void onItemClick(int pos,String[] images);
    }

}
