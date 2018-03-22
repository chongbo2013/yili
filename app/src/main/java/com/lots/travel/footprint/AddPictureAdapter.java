package com.lots.travel.footprint;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.lots.travel.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nalanzi on 2017/12/15.
 */

public class AddPictureAdapter extends RecyclerView.Adapter<AddPictureAdapter.Holder> {
    private Context context;
    private int maxCount;
    private List<String> imageList;
    private RequestOptions requestOptions;
    private OnItemClickListener onItemClickListener;

    private View.OnClickListener imageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(onItemClickListener==null)
                return;

            AddPictureAdapter.Holder holder = (AddPictureAdapter.Holder) v.getTag(R.id.iv_image);
            int position = holder.getAdapterPosition();
            int viewType = holder.getItemViewType();

            if(viewType==0){
                onItemClickListener.onScanImage(position,imageList);
            }else if(viewType==1){
                onItemClickListener.onAddImage();
            }
        }
    };

    public AddPictureAdapter(Context context,int maxCount){
        this.context = context;
        this.maxCount = Math.max(maxCount,1);

        requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);

        imageList = new ArrayList<>();
    }

    public void setImageList(List<String> src){
        imageList.clear();
        if(src!=null && src.size()>0)
            imageList.addAll(src);
        notifyDataSetChanged();
    }

    public List<String> getImageList(){
        return imageList;
    }

    public int getImageCount(){
        return imageList.size();
    }

    public boolean isEmpty(){
        return imageList.isEmpty();
    }

    public void addImage(String path){
        int pos = imageList.size();
        imageList.add(path);
        notifyItemInserted(pos);
    }

    public void addImages(List<String> src){
        int pos = imageList.size();
        imageList.addAll(src);
        notifyItemRangeChanged(pos,src.size());
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_picture,parent,false);
        return new Holder(root);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        int viewType = holder.getItemViewType();

        if(viewType==0){
            ((ImageView)holder.itemView).setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(context)
                    .load(imageList.get(position))
                    .apply(requestOptions)
                    .into((ImageView) holder.itemView);
        }else if(viewType==1){
            ((ImageView)holder.itemView).setScaleType(ImageView.ScaleType.CENTER);
            ((ImageView)holder.itemView).setImageResource(R.drawable.img_addmore_footprint);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(imageList==null)
            return 1;
        else {
            int size = imageList.size();
            return size>=maxCount ?
                    0:(position==size ? 1:0);
        }
    }

    @Override
    public int getItemCount() {
        if(imageList==null)
            return 1;
        else{
            int size = imageList.size();
            return size>=maxCount ? maxCount:size+1;
        }
    }

    class Holder extends RecyclerView.ViewHolder{

        public Holder(View v) {
            super(v);
            v.setTag(R.id.iv_image,this);
            v.setOnClickListener(imageListener);
        }

    }

    public interface OnItemClickListener{
        void onAddImage();
        void onScanImage(int position,List<String> images);
    }

}
