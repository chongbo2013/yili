package com.lots.travel.footprint.widget;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.footprint.BaseFootprintAdapter;
import com.lots.travel.footprint.BaseHolder;
import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.network.model.request.AddComment;
import com.lots.travel.network.model.request.AddZan;
import com.lots.travel.network.service.CommonService;
import com.lots.travel.store.db.GreenDaoManager;
import com.lots.travel.store.db.ViewCity;
import com.lots.travel.store.db.ViewCityDao;
import com.lots.travel.util.DensityUtil;
import com.lots.travel.util.TypeUtil;
import com.lots.travel.widget.PartsLayoutManager;
import com.lots.travel.widget.RoundImageView;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

/**
 * Created by nalanzi on 2017/11/19.
 */

public class NoteItem extends BaseItem {
    private RoundImageView ivCover;
    private RecyclerView rvImages;
    private TextView tvShortDesc;
    private TextView tvLocation,tvPraise,tvComment;

    private ImageItemAdapter imageAdapter;

    public NoteItem(BaseFootprintAdapter adapter) {
        super(adapter);
    }

    @Override
    public void onCreate(final BaseHolder holder, View v) {
        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int flatPos = holder.getAdapterPosition();
                int itemPos = adapter.getItemPosition(flatPos);
                Footprint data = adapter.getItem(itemPos);

                if(data==null)
                    return;

                switch (v.getId()){
                    case R.id.iv_cover:
                        processClick(data,-1);
                        break;
                    case R.id.tv_comment:
                        adapter.triggerAddComment(flatPos,data);
                        break;
                    case R.id.tv_praise:
                        adapter.triggerAddZan(flatPos, AddZan.DATA_TYPE_FOOTPRINT,data.getId(),
                                data.getHasZan()==1 ? AddZan.ZAN_COMMENT_DELETE:AddZan.ZAN_COMMENT_ADD);
                        break;
                }
            }
        };

        ivCover = (RoundImageView) v.findViewById(R.id.iv_cover);
        ivCover.setOnClickListener(listener);
        rvImages = (RecyclerView) v.findViewById(R.id.rv_images);
        tvShortDesc = (TextView) v.findViewById(R.id.tv_short_desc);
        tvLocation = (TextView) v.findViewById(R.id.tv_location);
        tvPraise = (TextView) v.findViewById(R.id.tv_praise);
        tvPraise.setOnClickListener(listener);
        tvComment = (TextView) v.findViewById(R.id.tv_comment);
        tvComment.setOnClickListener(listener);

        int space = DensityUtil.dp2px(adapter.getContext(),10);
        rvImages.setLayoutManager(new PartsLayoutManager(adapter.getContext(),rvImages, 3, space*2));
        rvImages.addItemDecoration(new SpaceItemDecoration(space));
    }

    /**
     * 有video img与没有video img的处理不同，此处暂时不考虑没有video img的情况
     */
    @Override
    public void onBind(final BaseHolder holder,Footprint data) {
        imageAdapter = new ImageItemAdapter(adapter);
        rvImages.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickListener(new ImageItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos,String[] images) {
                int itemPos = adapter.getItemPosition(holder.getAdapterPosition());
                Footprint data = adapter.getItem(itemPos);
                if(data==null)
                    return;
                processClick(data,pos);
            }
        });

        //如果有video就显示video，否则显示images，在不同情况下的点击事件的处理也是不同的
        String[] images = TypeUtil.str2arrays(data.getTripImgs());
        if(TextUtils.isEmpty(data.getTripVideo())){
            if(images.length==0) {
                ivCover.setVisibility(View.INVISIBLE);
                ivCover.setTagVisible(false);
                rvImages.setVisibility(View.GONE);
            }else{
                ivCover.setVisibility(View.VISIBLE);
                ivCover.setTagVisible(false);
                adapter.loadImage(images[0],ivCover);
                rvImages.setVisibility(images.length>=3 ? View.VISIBLE: View.GONE);
                imageAdapter.setImageList(1,images);
            }
        }else{
            ivCover.setVisibility(View.VISIBLE);
            ivCover.setTagVisible(true);
            String[] videoImages = TypeUtil.str2arrays(data.getTripVideoImg());
            if(videoImages!=null && videoImages.length>0)
                adapter.loadImage(videoImages[0],ivCover);
            if(images.length>=2){
                rvImages.setVisibility(View.VISIBLE);
                imageAdapter.setImageList(0,images);
            }else{
                rvImages.setVisibility(View.GONE);
            }
        }

        String desc = data.getTripTitle();
        tvShortDesc.setText(desc);
        tvShortDesc.setVisibility(TextUtils.isEmpty(desc) ? View.GONE:View.VISIBLE);

        String dst = "";
        try{
            String json = data.getJson();
            JSONObject obj = new JSONObject(json);
            dst = obj.getString("cityToName");;
//            long cityId = obj.getLong("cityTo");
//            List<ViewCity> cities = GreenDaoManager.getInstance()
//                    .getViewCityDao()
//                    .queryBuilder()
//                    .where(ViewCityDao.Properties.Id.eq(cityId))
//                    .list();
//            if(cities!=null && cities.size()>0){
//                ViewCity city = cities.get(0);
//                if(!TextUtils.isEmpty(city.getCName()))
//                    dst = city.getCName()+"-"+city.getName();
//                else
//                    dst = city.getName();
//            }
        }catch (Exception e){
            Log.e(NoteItem.class.getName(),e.toString());
        }

        tvLocation.setVisibility(TextUtils.isEmpty(dst) ? View.INVISIBLE:View.VISIBLE);
        tvLocation.setText(dst);

        Drawable drawable;
        tvComment.setText(String.format(Locale.getDefault(), "%d", data.getCommentTotles()));
        drawable = ContextCompat.getDrawable(adapter.getContext(), data.getHasZan() == 1 ?
                R.drawable.tag_comment : R.drawable.tag_comment);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        tvComment.setCompoundDrawables(drawable, null, null, null);

        tvPraise.setText(String.format(Locale.getDefault(), "%d", data.getZanTotles()));
        drawable = ContextCompat.getDrawable(adapter.getContext(), data.getHasZan() == 1 ?
                R.drawable.tag_praise_en : R.drawable.tag_praise_dis);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        tvPraise.setCompoundDrawables(drawable, null, null, null);
    }

    private void processClick(Footprint data,int imagePos){
        //如果没有video，则查看图片；否则，播放视频或者查看图片
        boolean hasVideo = !TextUtils.isEmpty(data.getTripVideo());
        boolean hasImages = !TextUtils.isEmpty(data.getTripImgs());
        if(!hasVideo && !hasImages)
            return;

        if(imagePos==-1 && hasVideo){
            adapter.triggerPlayVideo(TypeUtil.str2arrays(data.getTripVideo())[0]);
        }else if(imagePos==-1/* && !hasVideo*/){
            adapter.triggerScanImages(0,imageAdapter.getImageList());
        }else if(/*imagePos!=-1 && */hasVideo){
            adapter.triggerScanImages(imagePos,imageAdapter.getImageList());
        }else/* if(imagePos!=-1 && !hasVideo)*/{
            adapter.triggerScanImages(1+imagePos,imageAdapter.getImageList());
        }
    }

    public void onAddZan(Footprint data) {
        tvPraise.setText(String.format(Locale.getDefault(), "%d", data.getZanTotles()));
        Drawable drawable = ContextCompat.getDrawable(adapter.getContext(),
                data.getHasZan()==1 ? R.drawable.tag_praise_en : R.drawable.tag_praise_dis);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        tvPraise.setCompoundDrawables(drawable,null,null,null);
    }

}
