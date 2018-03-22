package com.lots.travel.video;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.common.media.ShareableBitmap;
import com.aliyun.qupai.editor.AliyunIThumbnailFetcher;
import com.aliyun.qupai.editor.AliyunThumbnailFetcherFactory;
import com.aliyun.struct.common.AliyunVideoParam;
import com.aliyun.struct.common.ScaleMode;
import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.store.FileStore;
import com.lots.travel.util.FileUtil;
import com.lots.travel.util.TakePictureUtil;
import com.lots.travel.video.edit.editor.EditorActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nalanzi on 2017/12/5.
 */

public class CutCoverActivity extends RxBaseActivity implements View.OnClickListener {
    public static final String OUTPUT_TITLE = "output_title";
    public static final String OUTPUT_COVER_PATH = "output_cover_path";

    private EditText etTitle;
    private TextView tvConfirm;

    private String mVideoPath;
    private AliyunVideoParam mVideoParam;

    private List<ShareableBitmap> mTempBitmaps;
    private AliyunIThumbnailFetcher mThumbnailFetcher;

    private ViewPager coverPager;
    private CoverPagerAdapter mCoverPagerAdapter;

    private String mCoverPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_cut_cover);

        Intent intent = getIntent();
        mVideoPath = intent.getStringExtra(EditorActivity.KEY_VIDEO_PATH);
        mVideoParam = (AliyunVideoParam) intent.getSerializableExtra(EditorActivity.KEY_VIDEO_PARAM);

        findViewById(R.id.iv_back).setOnClickListener(this);

        etTitle = (EditText) findViewById(R.id.et_title);
        tvConfirm = (TextView) findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(this);


        mCoverPagerAdapter = new CoverPagerAdapter();

        coverPager = (ViewPager) findViewById(R.id.cover_pager);
        coverPager.setOffscreenPageLimit(3);
//        coverPager.setPageMargin(DensityUtil.dp2px(this,5));
        coverPager.setPageTransformer(true,new ZoomOutPageTransformer());
        coverPager.setAdapter(mCoverPagerAdapter);

        mTempBitmaps = new ArrayList<>();
        mThumbnailFetcher = AliyunThumbnailFetcherFactory.createThumbnailFetcher();
        requestCovers(mVideoPath,mVideoParam.getOutputWidth(),mVideoParam.getOutputHeight(),15);
    }

    private void requestCovers(String videoPath, int width, int height,int n){
        showProgressDialog();
        mThumbnailFetcher.addVideoSource(videoPath);
        mThumbnailFetcher.setParameters(width, height, AliyunIThumbnailFetcher.CropMode.Mediate, ScaleMode.LB, 30);
        final long interval = mThumbnailFetcher.getTotalDuration()/n;
        mTempBitmaps.clear();
        requestThumbnailImage(0,n,interval);
    }

    private void requestThumbnailImage(final int position,final int count,final long interval){
        if(position>=count){
            dismissProgressDialog();
            mCoverPagerAdapter.setData(mTempBitmaps);
            coverPager.setCurrentItem(mTempBitmaps.size()/2,false);
            mTempBitmaps.clear();
            return;
        }

        mThumbnailFetcher.requestThumbnailImage(new long[]{interval*position},
                new AliyunIThumbnailFetcher.OnThumbnailCompletion() {

                    @Override
                    public void onThumbnailReady(ShareableBitmap frameBitmap, long time) {
                        if (frameBitmap!=null) {
                            mTempBitmaps.add(frameBitmap);
                        }
                        requestThumbnailImage(position+1,count,interval);
                    }

                    @Override
                    public void onError(int errorCode) {
                        requestThumbnailImage(position+1,count,interval);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mThumbnailFetcher!=null){
            mThumbnailFetcher.release();
        }
    }

    @Override
    public void onBackPressed() {
        back(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                back(false);
                break;

            case R.id.tv_confirm:
                int current = coverPager.getCurrentItem();
                int count = mCoverPagerAdapter.getCount();
                if(current>=0 && current<count){
                    ShareableBitmap sBmp = mCoverPagerAdapter.getItem(current);
                    if(sBmp!=null && sBmp.getData()!=null){
                        String coverName = TakePictureUtil.genName();
                        mCoverPath = new FileStore().getCacheImageDir()+File.separator+coverName;
                        if(FileUtil.saveBitmap(new File(mCoverPath),sBmp.getData())){
                            back(true);
                            Toast.makeText(CutCoverActivity.this,R.string.edit_cover_success,Toast.LENGTH_SHORT).show();
                        }else{
                            mCoverPath = null;
                            Toast.makeText(CutCoverActivity.this,R.string.edit_cover_failed,Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }

    private class CoverPagerAdapter extends PagerAdapter {
        private List<ShareableBitmap> mCoverPaths;

        CoverPagerAdapter(){
            mCoverPaths = new ArrayList<>();
        }

        void setData(List<ShareableBitmap> src){
            mCoverPaths.clear();
            if(src!=null && src.size()>0)
                mCoverPaths.addAll(src);
            notifyDataSetChanged();
        }

        ShareableBitmap getItem(int position){
            return mCoverPaths.get(position);
        }

        @Override
        public int getCount() {
            return mCoverPaths.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = (ImageView) LayoutInflater.from(container.getContext()).inflate(R.layout.item_footprint_video_cover,container,false);
            ShareableBitmap sBmp = mCoverPaths.get(position);
            if(sBmp!=null && sBmp.getData()!=null)
                view.setImageBitmap(sBmp.getData());
            container.addView(view);
            return view;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView)object);
        }
    }

    /**
     * 实现的原理是，在当前显示页面放大至原来的MAX_SCALE
     * 其他页面才是正常的的大小MIN_SCALE
     */
    private class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MAX_SCALE = 1.0f;
        private static final float MIN_SCALE = 0.9f;

        @Override
        public void transformPage(View view, float position) {
            //setScaleY只支持api11以上
            if (position < -1){
                view.setScaleX(MIN_SCALE);
                view.setScaleY(MIN_SCALE);
            }
            //a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
            else if (position <= 1) {
                float scaleFactor =  MIN_SCALE+(1-Math.abs(position))*(MAX_SCALE-MIN_SCALE);
                view.setScaleX(scaleFactor);
                //每次滑动后进行微小的移动目的是为了防止在三星的某些手机上出现两边的页面为显示的情况
                if(position>0){
                    view.setTranslationX(-scaleFactor*2);
                }else if(position<0){
                    view.setTranslationX(scaleFactor*2);
                }
                view.setScaleY(scaleFactor);
            }
            else {
                view.setScaleX(MIN_SCALE);
                view.setScaleY(MIN_SCALE);
            }
        }

    }

    private void back(boolean success){
        if(success){
            Intent data = new Intent();
            data.putExtra(OUTPUT_TITLE,etTitle.getText());
            data.putExtra(OUTPUT_COVER_PATH,mCoverPath);
            setResult(Activity.RESULT_OK,data);
        }else {
            setResult(Activity.RESULT_CANCELED);
        }
        finish();
    }

    public static void toCut(Activity context, int requestCode, AliyunVideoParam param, String videoPath, List<String> tempFiles){
        Intent intent = new Intent(context,CutCoverActivity.class);
        intent.putExtra(EditorActivity.KEY_VIDEO_PARAM,param);
        intent.putExtra(EditorActivity.KEY_VIDEO_PATH,videoPath);
        intent.putStringArrayListExtra(EditorActivity.KEY_TEMP_FILE_LIST, (ArrayList<String>) tempFiles);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivityForResult(intent,requestCode);
    }

}
