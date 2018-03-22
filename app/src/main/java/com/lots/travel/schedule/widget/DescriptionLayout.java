package com.lots.travel.schedule.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.lots.travel.R;
import com.lots.travel.util.DensityUtil;

import java.io.File;

/**
 * Created by nalanzi on 2017/9/24.
 */
//图文描述、语音录入、视频上传 - 使用TabHost嵌套层次太深
//DescriptionLayout只是layout，不带要数据
//！！！RecyclerView中调用View.GONE还是有小空白空间存在！！！
public class DescriptionLayout extends RelativeLayout implements RadioGroup.OnCheckedChangeListener,View.OnClickListener{
    public static final int IMAGE_MAX_COUNT = 9;

    //content text
    private TextView tvText;
    private RecyclerView rvImages;
    //context audio
    private VoiceRecordView vVoiceRecord;
    //context video
    private RelativeLayout rlVideo;
    private RelativeLayout rlCover;
    private TextView tvVideo;
    private ImageView ivCover;

    private ImagesAdapter imagesAdapter;

    private RadioGroup llTabs;
    private int[] tabs = new int[]{R.id.tab_text,R.id.tab_audio,R.id.tab_video};

    private RequestOptions requestOptions;

    public DescriptionLayout(Context context) {
        this(context,null);
    }

    public DescriptionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_description,this,true);

        llTabs = (RadioGroup) findViewById(R.id.ll_tabs);
        llTabs.setOnCheckedChangeListener(this);

        tvText = (TextView) findViewById(R.id.tv_text);
        tvText.setOnClickListener(this);

        rvImages = (RecyclerView) findViewById(R.id.rv_images);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvImages.setLayoutManager(layoutManager);
        rvImages.addItemDecoration(new HorizontalSpaceDecoration(DensityUtil.dp2px(context,5)));
        imagesAdapter = new ImagesAdapter();
        rvImages.setAdapter(imagesAdapter);

        vVoiceRecord = (VoiceRecordView) findViewById(R.id.v_voice_record);
        vVoiceRecord.setOnVoiceListener(new VoiceRecordView.OnVoiceListener() {
            @Override
            public void onLengthChanged(int length) {
                if(onActionListener!=null)
                    onActionListener.onAction(AC_ADD_AUDIO,length);
            }
        });

        rlVideo = (RelativeLayout) findViewById(R.id.rl_video);
        rlCover = (RelativeLayout) findViewById(R.id.rl_cover);
        tvVideo = (TextView) findViewById(R.id.tv_video);
        tvVideo.setText(showVideoTip());
        tvVideo.setOnClickListener(this);
        ivCover = (ImageView) findViewById(R.id.iv_cover);
        ivCover.setOnClickListener(this);
        findViewById(R.id.tv_delete_video).setOnClickListener(this);
        findViewById(R.id.iv_play).setOnClickListener(this);

        requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);

        setCurrentItem(1);
    }

    public void setVoiceFilepath(String filepath, long startLength, long totalLength){
        if(filepath!=null && new File(filepath).exists())
            vVoiceRecord.setPhase(1,startLength,totalLength);
        else
            vVoiceRecord.setPhase(0,0,0);
        vVoiceRecord.setFilepath(filepath);
    }

    public void setVoiceControl(AudioRecorder recorder, AudioPlayer player){
        vVoiceRecord.setControl(recorder, player);
    }

    public void resetVoice(){
        vVoiceRecord.reset(true);
    }

    public void setVideoSource(String videoPath,String coverPath){
        if(!TextUtils.isEmpty(videoPath)){
            tvVideo.setVisibility(View.INVISIBLE);
            rlCover.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(coverPath)) {
                Glide.with(getContext())
                        .load(coverPath)
                        .apply(requestOptions)
                        .into(ivCover);
            }
        }else{
            tvVideo.setVisibility(View.VISIBLE);
            rlCover.setVisibility(View.INVISIBLE);
        }
    }

    public boolean dragRecyclerView(MotionEvent ev){
        float x = ev.getRawX();
        float y = ev.getRawY();
        int[] loc = new int[2];
        rvImages.getLocationOnScreen(loc);
        return x>loc[0] && x<loc[0]+rvImages.getWidth()
                && y>loc[1] && y<loc[1]+rvImages.getHeight();
    }

    public void setCurrentItem(int pos){
        llTabs.check(tabs[pos]);
    }

    private void setCurrentItemInternal(int pos){
        if(pos<0 || pos>2)
            return;

        tvText.setVisibility(pos==0 ? View.VISIBLE:View.INVISIBLE);
        rvImages.setVisibility(pos==0 ? View.VISIBLE:View.INVISIBLE);

        vVoiceRecord.setVisibility(pos==1 ? View.VISIBLE:View.INVISIBLE);

        rlVideo.setVisibility(pos==2 ? View.VISIBLE:View.INVISIBLE);
    }

    public void setText(CharSequence text){
        tvText.setText(text);
    }

    public void setImageList(String[] src){
        imagesAdapter.setImageList(src);
    }

    public void setImageList(String[] src, int maxCount){
        imagesAdapter.setImageList(src);
        imagesAdapter.setMaxCount(maxCount);
    }

    public String[] getImageList(){
        return imagesAdapter.images;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        int pos = -1;
        switch (checkedId){
            case R.id.tab_text:
                pos = 0;
                break;

            case R.id.tab_audio:
                pos = 1;
                break;

            case R.id.tab_video:
                pos = 2;
                break;
        }

        setCurrentItemInternal(pos);

        if(pos!=-1 && onActionListener!=null){
            onActionListener.onAction(AC_CHANGE_TAB,pos);
        }
    }

    private SpannableStringBuilder showVideoTip(){
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(getResources().getString(R.string.schedule_desc_video_tip));

        int textColor;
        int end = 8;
        textColor = ContextCompat.getColor(getContext(),R.color.color_text);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        end = 9;
        textColor = ContextCompat.getColor(getContext(),R.color.color_main);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return strBuilder;
    }

    @Override
    public void onClick(View v) {
        if(onActionListener==null)
            return;

        switch (v.getId()){
            case R.id.tv_text:
                onActionListener.onAction(AC_EDIT_TEXT,null);
                break;

            case R.id.tv_video:
                onActionListener.onAction(AC_ADD_VIDEO,null);
                break;

            case R.id.tv_delete_video:
                setVideoSource(null,null);
                onActionListener.onAction(AC_DELETE_VIDEO,null);
                break;

            case R.id.iv_play:
                onActionListener.onAction(AC_PLAY_VIDEO,null);
                break;
        }
    }

    private class ImagesAdapter extends RecyclerView.Adapter<ImageHolder>{
        private int maxCount = IMAGE_MAX_COUNT;
        private String[] images;

        void setImageList(String[] src){
            images = src;
            notifyDataSetChanged();
        }

        void setMaxCount(int n){
            maxCount = Math.max(1,n);
        }

        @Override
        public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_desc_image,parent,false);
            return new ImageHolder(root);
        }

        @Override
        public void onBindViewHolder(ImageHolder holder, int position) {
            int viewType = getItemViewType(position);

            if(viewType==0){
                Glide.with(getContext())
                        .load(images[position])
                        .apply(requestOptions)
                        .into((ImageView) holder.itemView);
            }else if(viewType==1){
                ((ImageView)holder.itemView).setImageResource(R.drawable.img_addmore_desc);
            }

            holder.itemView.setTag(R.id.iv_desc_image,position);
            holder.itemView.setOnClickListener(imageListener);
        }

        @Override
        public int getItemViewType(int position) {
            if(images==null)
                return 1;
            else {
                int size = images.length;
                return size>=maxCount ?
                        0:(position==size ? 1:0);
            }
        }

        @Override
        public int getItemCount() {
            if(images==null)
                return 1;
            else{
                int size = images.length;
                return size>=maxCount ? maxCount:size+1;
            }
        }
    }

    private OnClickListener imageListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(onActionListener==null)
                return;

            int position = (int) v.getTag(R.id.iv_desc_image);
            int viewType = imagesAdapter.getItemViewType(position);
            if(viewType==0){
                onActionListener.onAction(AC_SCAN_IMAGES,position);
            }else if(viewType==1){
                onActionListener.onAction(AC_ADD_IMAGES,getImageList());
            }
        }
    };

    class ImageHolder extends RecyclerView.ViewHolder{

        ImageHolder(View item) {
            super(item);
        }

    }

    private class HorizontalSpaceDecoration extends RecyclerView.ItemDecoration {
        private int spaceHorizontal;

        HorizontalSpaceDecoration(int spaceHorizontal) {
            this.spaceHorizontal = spaceHorizontal;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int index = parent.getChildAdapterPosition(view);
            boolean isLeftEdge = index==0;
            boolean isRightEdge = index==state.getItemCount();

            outRect.left = (int) (spaceHorizontal*1f/2+0.5);
            outRect.top = 0;
            outRect.right = (int) (spaceHorizontal*1f/2+0.5);
            outRect.bottom = 0;

            if(isLeftEdge)
                outRect.left = 0;

            if(isRightEdge)
                outRect.right = 0;
        }

    }

    public static final int AC_ADD_IMAGES = 1;

    public static final int AC_SCAN_IMAGES = 2;

    public static final int AC_EDIT_TEXT = 3;

    public static final int AC_ADD_VIDEO = 4;

    //extra为int
    public static final int AC_CHANGE_TAB = 5;

    public static final int AC_ADD_AUDIO = 6;

    public static final int AC_DELETE_VIDEO = 7;

    public static final int AC_PLAY_VIDEO = 8;

    private OnActionListener onActionListener;

    public void setOnActionListener(OnActionListener listener){
        onActionListener = listener;
    }

    public interface OnActionListener {
        void onAction(int ac, Object extra);
    }

}
