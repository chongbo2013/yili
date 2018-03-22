package com.lots.travel.footprint;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.lots.travel.R;
import com.lots.travel.footprint.live.LiveFootprintAdapter;
import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.widget.ImageLoader;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;

/**
 * Created by nalanzi on 2017/11/19.
 */

public abstract class BaseFootprintAdapter extends AbstractLoadAdapter<Footprint> implements ImageLoader {
    private Context mContext;
    protected final RecyclerView vRecycler;
    private RequestOptions mRequestOptions;
    protected OnFootprintListener mOnFootprintListener;

    public BaseFootprintAdapter(Context context, RecyclerView rv) {
        super(rv);
        vRecycler = rv;
        mContext = context;
        mRequestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);
    }

    public Context getContext(){
        return mContext;
    }

    public void setOnFootprintListener(OnFootprintListener listener){
        mOnFootprintListener = listener;
    }

    public OnFootprintListener getOnFootprintListener(){
        return mOnFootprintListener;
    }

    public void checkUser(long userId){
        if(mOnFootprintListener!=null){
            mOnFootprintListener.onCheckUser(userId);
        }
    }

    public void triggerPlayVideo(String url){
        if(mOnFootprintListener!=null)
            mOnFootprintListener.onPlayVideo(url);
    }

    public void triggerScanImages(int selected, String[] images){
        if(mOnFootprintListener!=null)
            mOnFootprintListener.onScanImages(selected,images);
    }

    public void triggerAddZan(int flatPosition,String dateTable, String dateKey, String style){
        if(mOnFootprintListener!=null)
            mOnFootprintListener.onAddZan(flatPosition,dateTable, dateKey, style);
    }

    public void triggerAddComment(int flatPosition,Footprint data){
        if(mOnFootprintListener!=null)
            mOnFootprintListener.onAddComment(flatPosition,data);
    }

    public void triggerCheckNote(int flatPosition,String scheduleId){
        if(mOnFootprintListener!=null)
            mOnFootprintListener.onCheckNote(flatPosition,scheduleId);
    }

    public void onAddZan(int flatPosition,boolean zaned){
        int itemPosition = getItemPosition(flatPosition);
        Footprint footprint = getItem(itemPosition);
        if(footprint==null)
            return;
        if(zaned){
            footprint.setHasZan(1);
            footprint.incZan();
        }else{
            footprint.setHasZan(0);
            footprint.decZan();
        }
    }

    public void onAddComment(int flatPosition,int commented,int count){
//        int itemPosition = getItemPosition(flatPosition);
//        Footprint footprint = getItem(itemPosition);
//        if(footprint==null)
//            return;
//        if(commented){
//            footprint.setHasZan(1);
//            footprint.incZan();
//        }else{
//            footprint.setHasZan(0);
//            footprint.decZan();
//        }
//        notifyItemChanged(flatPosition);
    }

    @Override
    public void loadImage(String url, ImageView target){
        Glide.with(mContext)
                .load(url)
                .apply(mRequestOptions)
                .into(target);
    }

    @Override
    public void onRefreshed() {}

    @Override
    public void onLoaded() {}

}
