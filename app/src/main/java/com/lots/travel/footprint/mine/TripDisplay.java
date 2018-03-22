package com.lots.travel.footprint.mine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lots.travel.R;
import com.lots.travel.util.DensityUtil;
import com.lots.travel.widget.PartsLayoutManager;
import com.lots.travel.widget.RoundImageView;

/**
 *
 */
public class TripDisplay {
    private RoundImageView ivCover;
    private RecyclerView rvImages;

    private String videoImage;
    private String[] tripImages;

    private int startIndex;
    private ImageAdapter imageAdapter;

    public TripDisplay(Context context, View v){
        ivCover = (RoundImageView) v.findViewById(R.id.iv_cover);
        rvImages = (RecyclerView) v.findViewById(R.id.rv_images);
        rvImages.setLayoutManager(new PartsLayoutManager(context,rvImages,3, DensityUtil.dp2px(context,20)));
        imageAdapter = new ImageAdapter();
        rvImages.setAdapter(imageAdapter);
    }

    public void setContent(String video,String images){
        videoImage = video;

        if(TextUtils.isEmpty(images)) {
            tripImages = null;
        }else{
            tripImages = images.split(",");
        }

        if(TextUtils.isEmpty(videoImage)){
            startIndex = 1;
        }else{
            startIndex = 0;
        }

        rvImages.setVisibility(
                imageAdapter.getItemCount()==0 ? View.GONE:View.VISIBLE);

        imageAdapter.notifyDataSetChanged();
    }

    class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.Holder>{

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trippart_image,parent,false);
            return new Holder(root);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            //((ImageView)holder.itemView)
        }

        @Override
        public int getItemCount() {
            return tripImages==null ? 0:Math.max(tripImages.length-startIndex,0);
        }

        class Holder extends RecyclerView.ViewHolder{

            public Holder(View v) {
                super(v);
            }

        }

    }

}
