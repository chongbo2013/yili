package com.lots.travel.main.info.mine.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.lots.travel.R;

import com.lots.travel.footprint.widget.ImageItemAdapter;
import com.lots.travel.footprint.widget.NoteItem;
import com.lots.travel.footprint.widget.SpaceItemDecoration;
import com.lots.travel.main.info.mine.model.FollowNote;
import com.lots.travel.network.model.request.AddZan;
import com.lots.travel.util.CommonUtil;
import com.lots.travel.util.DensityUtil;
import com.lots.travel.util.TypeUtil;
import com.lots.travel.widget.ImageLoader;
import com.lots.travel.widget.PartsLayoutManager;
import com.lots.travel.widget.RoundImageView;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;

import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by nalanzi on 2018/2/5.
 */

public class MineFollowNotesAdapter extends AbstractLoadAdapter<FollowNote> implements ImageLoader{
    private Context mContext;
    private RecyclerView mRecyclerView;
    private RequestOptions mRequestOptions;
    private OnFollowNotesListener mOnFollowNotesListener;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Holder holder = (Holder) v.getTag(v.getId());
            int flatPos = holder.getAdapterPosition();
            int itemPos = getItemPosition(flatPos);
            FollowNote data = getItem(itemPos);

            if(data==null)
                return;

            switch (v.getId()){
                case R.id.iv_portrait:
                    checkUser(data.getUserId());
                    break;

                case R.id.iv_cover:
                    processClick(holder,data,-1);
                    break;
                case R.id.tv_comment:
                    triggerAddComment(flatPos,data);
                    break;
                case R.id.tv_praise:
                    triggerAddZan(flatPos,data.getDataKey(),
                            data.getHasZan()==1 ? AddZan.ZAN_COMMENT_DELETE:AddZan.ZAN_COMMENT_ADD);
                    break;
            }
        }
    };

    private void processClick(Holder holder,FollowNote data,int imagePos){
        //如果没有video，则查看图片；否则，播放视频或者查看图片
        boolean hasVideo = !TextUtils.isEmpty(data.getTripVideo());
        boolean hasImages = !TextUtils.isEmpty(data.getTripImgs());
        if(!hasVideo && !hasImages)
            return;

        if(imagePos==-1 && hasVideo){
            triggerPlayVideo(TypeUtil.str2arrays(data.getTripVideo())[0]);
        }else if(imagePos==-1/* && !hasVideo*/){
            triggerScanImages(0,holder.imageAdapter.getImageList());
        }else if(/*imagePos!=-1 && */hasVideo){
            triggerScanImages(imagePos,holder.imageAdapter.getImageList());
        }else/* if(imagePos!=-1 && !hasVideo)*/{
            triggerScanImages(1+imagePos,holder.imageAdapter.getImageList());
        }
    }

    public MineFollowNotesAdapter(RecyclerView rv) {
        super(rv);
        mContext = rv.getContext();
        mRecyclerView = rv;
        mRequestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);
    }

    public void setOnFollowNotesListener(OnFollowNotesListener listener){
        mOnFollowNotesListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mine_follow_notes,parent,false);
        return new Holder(v);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Holder cHolder = (Holder) holder;
        FollowNote f = getItem(position);
        if(f==null)
            return;

        loadImage(f.getFaceImg(),cHolder.ivPortrait);
        cHolder.tvName.setText(f.getUserName());

        Drawable sexIco = null;
        int sexIcoId = CommonUtil.getSexDrawable(f.getSex());
        if(sexIcoId!=-1) {
            sexIco = ContextCompat.getDrawable(mContext, sexIcoId);
            sexIco.setBounds(0,0,sexIco.getIntrinsicWidth(),sexIco.getIntrinsicHeight());
        }
        cHolder.tvName.setCompoundDrawables(null,null,sexIco,null);

        cHolder.tvProfession.setText(f.getTitle());

        //content部分
        cHolder.imageAdapter = new ImageItemAdapter(this);
        cHolder.rvImages.setAdapter(cHolder.imageAdapter);
        cHolder.imageAdapter.setOnItemClickListener(new ImageItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos,String[] images) {
                int itemPos = getItemPosition(cHolder.getAdapterPosition());
                FollowNote data = getItem(itemPos);
                if(data==null)
                    return;
                processClick(cHolder,data,pos);
            }
        });

        //如果有video就显示video，否则显示images，在不同情况下的点击事件的处理也是不同的
        String[] images = TypeUtil.str2arrays(f.getTripImgs());
        if(TextUtils.isEmpty(f.getTripVideo())){
            if(images.length==0) {
                cHolder.ivCover.setVisibility(View.INVISIBLE);
                cHolder.ivCover.setTagVisible(false);
                cHolder.rvImages.setVisibility(View.GONE);
            }else{
                cHolder.ivCover.setVisibility(View.VISIBLE);
                cHolder.ivCover.setTagVisible(false);
                loadImage(images[0],cHolder.ivCover);
                cHolder.rvImages.setVisibility(images.length>=3 ? View.VISIBLE: View.GONE);
                cHolder.imageAdapter.setImageList(1,images);
            }
        }else{
            cHolder.ivCover.setVisibility(View.VISIBLE);
            cHolder.ivCover.setTagVisible(true);
            String[] videoImages = TypeUtil.str2arrays(f.getTripVideoImg());
            if(videoImages!=null && videoImages.length>0)
                loadImage(videoImages[0],cHolder.ivCover);
            if(images.length>=2){
                cHolder.rvImages.setVisibility(View.VISIBLE);
                cHolder.imageAdapter.setImageList(0,images);
            }else{
                cHolder.rvImages.setVisibility(View.GONE);
            }
        }

        String desc = f.getTripTitle();
        cHolder.tvShortDesc.setText(desc);
        cHolder.tvShortDesc.setVisibility(TextUtils.isEmpty(desc) ? View.GONE:View.VISIBLE);

        String dst = "";
        try{
            String json = f.getJson();
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

        cHolder.tvLocation.setVisibility(TextUtils.isEmpty(dst) ? View.INVISIBLE:View.VISIBLE);
        cHolder.tvLocation.setText(dst);

        Drawable drawable;
        cHolder.tvComment.setText(String.format(Locale.getDefault(), "%d", f.getCommentTotles()));
        drawable = ContextCompat.getDrawable(mContext, f.getHasZan() == 1 ?
                R.drawable.tag_comment : R.drawable.tag_comment);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        cHolder.tvComment.setCompoundDrawables(drawable, null, null, null);

        cHolder.tvPraise.setText(String.format(Locale.getDefault(), "%d", f.getZanTotles()));
        drawable = ContextCompat.getDrawable(mContext, f.getHasZan() == 1 ?
                R.drawable.tag_praise_en : R.drawable.tag_praise_dis);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        cHolder.tvPraise.setCompoundDrawables(drawable, null, null, null);
    }

    public void checkUser(long userId){
        if(mOnFollowNotesListener!=null){
            mOnFollowNotesListener.onCheckUser(userId);
        }
    }

    public void triggerPlayVideo(String url){
        if(mOnFollowNotesListener!=null)
            mOnFollowNotesListener.onPlayVideo(url);
    }

    public void triggerScanImages(int selected, String[] images){
        if(mOnFollowNotesListener!=null)
            mOnFollowNotesListener.onScanImages(selected,images);
    }

    public void triggerAddZan(int flatPosition,String dateKey, String style){
        if(mOnFollowNotesListener!=null)
            mOnFollowNotesListener.onAddZan(flatPosition,dateKey, style);
    }

    public void triggerAddComment(int flatPosition,FollowNote data){
        if(mOnFollowNotesListener!=null)
            mOnFollowNotesListener.onAddComment(flatPosition,data);
    }

    public void triggerCheckNote(int flatPosition,String scheduleId){
        if(mOnFollowNotesListener!=null)
            mOnFollowNotesListener.onCheckNote(flatPosition,scheduleId);
    }

    public void onAddZan(int flatPosition, boolean zaned) {
        int itemPosition = getItemPosition(flatPosition);
        FollowNote f = getItem(itemPosition);
        if(f==null)
            return;
        if(zaned){
            f.setHasZan(1);
            f.setZanTotles(f.getZanTotles()+1);
        }else{
            f.setHasZan(0);
            f.setZanTotles(f.getZanTotles()-1);
        }

        RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForAdapterPosition(flatPosition);
        if(holder!=null && holder instanceof Holder){
            Holder cHolder = (Holder) holder;
            cHolder.onAddZan(getItem(getItemPosition(flatPosition)));
        }else{
            notifyItemChanged(flatPosition);
        }
    }

    @Override
    public void onRefreshed() {}

    @Override
    public void onLoaded() {}

    @Override
    public void loadImage(String url, ImageView target) {
        Glide.with(mContext)
                .load(url)
                .apply(mRequestOptions)
                .into(target);
    }

    class Holder extends RecyclerView.ViewHolder{
        private ImageView ivPortrait;
        private TextView tvName,tvProfession;
        private RoundImageView ivCover;
        private RecyclerView rvImages;
        private TextView tvShortDesc;
        private TextView tvLocation,tvPraise,tvComment;

        private ImageItemAdapter imageAdapter;

        public Holder(View v) {
            super(v);

            ivPortrait = (ImageView) v.findViewById(R.id.iv_portrait);
            ivPortrait.setTag(R.id.iv_portrait,this);
            ivPortrait.setOnClickListener(mOnClickListener);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvProfession = (TextView) v.findViewById(R.id.tv_profession);

            ivCover = (RoundImageView) v.findViewById(R.id.iv_cover);
            ivCover.setTag(R.id.iv_cover,this);
            ivCover.setOnClickListener(mOnClickListener);
            rvImages = (RecyclerView) v.findViewById(R.id.rv_images);
            tvShortDesc = (TextView) v.findViewById(R.id.tv_short_desc);
            tvLocation = (TextView) v.findViewById(R.id.tv_location);
            tvPraise = (TextView) v.findViewById(R.id.tv_praise);
            tvPraise.setTag(R.id.tv_praise,this);
            tvPraise.setOnClickListener(mOnClickListener);
            tvComment = (TextView) v.findViewById(R.id.tv_comment);
            tvComment.setTag(R.id.tv_comment,this);
            tvComment.setOnClickListener(mOnClickListener);

            int space = DensityUtil.dp2px(mContext,10);
            rvImages.setLayoutManager(new PartsLayoutManager(mContext,rvImages, 3, space*2));
            rvImages.addItemDecoration(new SpaceItemDecoration(space));
        }

        public void onAddZan(FollowNote f) {
            tvPraise.setText(String.format(Locale.getDefault(), "%d", f.getZanTotles()));
            Drawable drawable = ContextCompat.getDrawable(mContext,
                    f.getHasZan()==1 ? R.drawable.tag_praise_en : R.drawable.tag_praise_dis);
            drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
            tvPraise.setCompoundDrawables(drawable,null,null,null);
        }
    }

    public interface OnFollowNotesListener{
        //播放视频
        void onPlayVideo(String url);
        //查看images
        void onScanImages(int selected,String[] images);
        //点赞
        void onAddZan(int flatPosition,String dateKey,String style);
        //评论
        void onAddComment(int flatPosition,FollowNote data);
        //查看游记
        void onCheckNote(int flatPosition,String scheduleId);
        //查看用户
        void onCheckUser(long userId);
    }

}
