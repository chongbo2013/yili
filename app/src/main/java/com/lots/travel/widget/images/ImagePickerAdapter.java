package com.lots.travel.widget.images;

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
import com.lots.travel.widget.refresh.AbstractLoadAdapter;

import java.util.List;

/**
 * Created by lWX479187 on 2017/9/25.
 */
//view recycled以后，清除加载
public class ImagePickerAdapter extends AbstractLoadAdapter<Image>{
    private Context mContext;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    private OnClickListener onClickListener;
    private OnLongClickListener onLongClickListener;

    private RequestOptions mRequestOptions;

    private OnLoadListener mOnLoadListener;

    public ImagePickerAdapter(Context context,RecyclerView rv) {
        super(rv);
        mContext = context;
        mRequestOptions = new RequestOptions()
                .centerCrop()
                .priority(Priority.HIGH);
        initListener();
    }

    private void initListener() {
        onClickListener = new OnClickListener() {
            @Override
            public void onClick(int position, long itemId) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(position, itemId);
            }
        };

        onLongClickListener = new OnLongClickListener() {
            @Override
            public boolean onLongClick(int position, long itemId) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onLongClick(position, itemId);
                    return true;
                }
                return false;
            }
        };
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnLoadListener(OnLoadListener listener){
        this.mOnLoadListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_picker,parent,false);
        ImageHolder holder = new ImageHolder(root);
        holder.itemView.setTag(holder);
        holder.itemView.setOnLongClickListener(onLongClickListener);
        holder.itemView.setOnClickListener(onClickListener);
        return holder;
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageHolder iHolder = (ImageHolder) holder;
        int itemPosition = getItemPosition(position);
        Image image = getItem(itemPosition);

        if(image==null)
            return;

        Glide.with(mContext)
                .load(image.getPath())
                .apply(mRequestOptions)
                .into(iHolder.ivImage);

        iHolder.cbPick.setSelected(image.isSelect());
        iHolder.layMask.setVisibility(image.isSelect() ?
                View.VISIBLE : View.GONE);
    }

    @Override
    public void onRefreshed() {
        if(mOnLoadListener!=null)
            mOnLoadListener.onRefreshed();
    }

    @Override
    public void onLoaded() {
        if(mOnLoadListener!=null)
            mOnLoadListener.onLoaded();
    }

    /**
     * 可以共用同一个listener，相对高效
     */
    public static abstract class OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
            onClick(holder.getAdapterPosition(), holder.getItemId());
        }

        public abstract void onClick(int position, long itemId);
    }


    /**
     * 可以共用同一个listener，相对高效
     */
    static abstract class OnLongClickListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
            return onLongClick(holder.getAdapterPosition(), holder.getItemId());
        }

        public abstract boolean onLongClick(int position, long itemId);
    }


    /**
     *
     */
    public interface OnItemClickListener {
        void onItemClick(int position, long itemId);
    }


    interface OnItemLongClickListener {
        void onLongClick(int position, long itemId);
    }

    interface OnLoadListener {
        void onLoaded();
        void onRefreshed();
    }

    private class ImageHolder extends RecyclerView.ViewHolder{
        private ImageView ivImage;
        private View layMask;
        private ImageView cbPick;

        ImageHolder(View itemView) {
            super(itemView);

            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
            layMask = itemView.findViewById(R.id.lay_mask);
            cbPick = (ImageView) itemView.findViewById(R.id.cb_pick);
        }
    }

}
