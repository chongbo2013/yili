package com.lots.travel.schedule.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.footprint.widget.ImageItemAdapter;
import com.lots.travel.footprint.widget.SpaceItemDecoration;
import com.lots.travel.util.DensityUtil;
import com.lots.travel.util.TypeUtil;
import com.lots.travel.widget.ImageLoader;
import com.lots.travel.widget.PartsLayoutManager;
import com.lots.travel.widget.RoundImageView;
import com.lots.travel.widget.VoiceBar;
import com.lots.travel.widget.VoiceDescView;

/**
 * Created by nalanzi on 2017/11/16.
 */

public class TripPartDisplay extends LinearLayout implements View.OnClickListener {
    private RoundImageView ivCover;
    private RecyclerView rvImages;
    private VoiceDescView vVoiceDesc;
    private TextView tvText;
    private ImageView ivTextControl;

    private ImageItemAdapter mImageAdapter;

    private String mVideoPath;

    private ImageLoader mImageLoader;

    private OnControlListener mOnControlListener;

    public TripPartDisplay(@NonNull Context context) {
        this(context,null);
    }

    public TripPartDisplay(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.layout_trippart_display,this,true);

        ivCover = (RoundImageView) findViewById(R.id.iv_cover);
        rvImages = (RecyclerView) findViewById(R.id.rv_images);
        vVoiceDesc = (VoiceDescView) findViewById(R.id.v_voice_desc);
        tvText = (TextView) findViewById(R.id.tv_text);
        ivTextControl = (ImageView) findViewById(R.id.iv_text_control);
        ivTextControl.setOnClickListener(this);

        int space = DensityUtil.dp2px(context,10);
        rvImages.setLayoutManager(new PartsLayoutManager(context,rvImages, 3, space*2));
        rvImages.addItemDecoration(new SpaceItemDecoration(space));

        ivCover.setOnClickListener(this);
        vVoiceDesc.setOnPlayListener(new VoiceBar.OnPlayListener() {
            @Override
            public void onPlayStart() {
                if(mOnControlListener!=null)
                    mOnControlListener.onPlayVoiceStart();
            }

            @Override
            public void onPlayStop() {
                if(mOnControlListener!=null)
                    mOnControlListener.onPlayVoiceStop();
            }
        });
    }

    public void setImageLoader(ImageLoader loader){
        mImageLoader = loader;
    }

    public void setOnControlListener(OnControlListener listener){
        mOnControlListener = listener;
    }

    public void setVideoImages(String videoPath, String imagePaths){
        if(mImageAdapter==null){
            mImageAdapter = new ImageItemAdapter(mImageLoader);
            rvImages.setAdapter(mImageAdapter);
            mImageAdapter.setOnItemClickListener(new ImageItemAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int pos, String[] images) {
                    processVideoImagesClick(pos);
                }
            });
        }

        String[] images = TypeUtil.str2arrays(imagePaths);
        mVideoPath = videoPath;

        boolean hasVideo = !TextUtils.isEmpty(mVideoPath);

        if(hasVideo) {
            String[] videoPaths = mVideoPath.split(",");
            if(videoPaths.length > 0){
                mVideoPath = videoPaths[0];
            }
        }

        if(hasVideo){
            ivCover.setVisibility(View.VISIBLE);
            ivCover.setTagVisible(true);
            mImageLoader.loadImage(mVideoPath,ivCover);

            if(images!=null && images.length>=2){
                rvImages.setVisibility(View.VISIBLE);
                mImageAdapter.setImageList(0,images);
            }else{
                rvImages.setVisibility(View.GONE);
            }
        }else{
            if(images!=null && images.length>0){
                ivCover.setVisibility(View.VISIBLE);
                ivCover.setTagVisible(false);
                mImageLoader.loadImage(images[0],ivCover);
                if(images.length>=3){
                    rvImages.setVisibility(View.VISIBLE);
                    mImageAdapter.setImageList(1,images);
                }else{
                    rvImages.setVisibility(View.GONE);
                    mImageAdapter.setImageList(1,images);
                }
            }else{
                ivCover.setVisibility(View.GONE);
                rvImages.setVisibility(View.GONE);
            }
        }
    }

    //index=-1为点击images
    private void processVideoImagesClick(int index){
        if(mOnControlListener==null)
            return;

        String[] images = mImageAdapter.getImageList();

        boolean hasVideo = !TextUtils.isEmpty(mVideoPath);
        boolean hasImages = images!=null && images.length>0;

        if(index==-1){
            if(hasVideo){
                mOnControlListener.onPlayVideo();
            }else if(hasImages){
                mOnControlListener.onScanImages(0,images);
            }
        }else{
            mOnControlListener.onScanImages(mImageAdapter.getStartIndex()+index,images);
        }
    }

    public void setVoice(String portrait, int voiceLength){
        if(voiceLength>0){
            vVoiceDesc.setVisibility(View.VISIBLE);
            vVoiceDesc.setPortrait(mImageLoader,portrait);
            vVoiceDesc.setVoice(voiceLength);
        }else{
            vVoiceDesc.setVisibility(View.GONE);
        }
    }

    public void setText(boolean expanded, String text){
        if(TextUtils.isEmpty(text)){
            tvText.setVisibility(View.GONE);
            ivTextControl.setVisibility(View.GONE);
            return;
        }else{
            tvText.setVisibility(View.VISIBLE);
            ivTextControl.setVisibility(View.VISIBLE);
        }

        tvText.setText(text);
        if(expanded){
            tvText.setMaxLines(Integer.MAX_VALUE);
            ivTextControl.setImageResource(R.drawable.arrow_up);
        }else {
            tvText.setMaxLines(3);
            ivTextControl.setImageResource(R.drawable.arrow_down);
        }
    }

    public void reset(){
        vVoiceDesc.reset();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_cover:
                processVideoImagesClick(-1);
                break;

            case R.id.iv_text_control:
                if(mOnControlListener!=null)
                    mOnControlListener.onToggleText();
                break;
        }
    }

    public interface OnControlListener{
        void onPlayVideo();
        void onScanImages(int index,String[] images);
        void onPlayVoiceStart();
        void onPlayVoiceStop();
        void onToggleText();
    }

}
