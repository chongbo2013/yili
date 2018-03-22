package com.lots.travel.widget.images;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.lots.travel.R;
import com.lots.travel.util.SystemBarCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LookUpPictureActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String IMAGE_LIST = "imageList";
    public static final String IMAGE_ARRAY = "imageArray";
    public static final String INIT_POS = "initPos";
    public static final String EDITABLE = "editable";
    public static final String OUTPUT_IMAGE_LIST = "outputImageList";

    private LookUpAdapter lookUpAdapter;
    private boolean hasChanged;
    private boolean editable;
    private RequestOptions requestOptions;

    private TextView tvIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookup_picture);
        SystemBarCompat.fullscreen(this,findViewById(R.id.rl_main),false);

        tvIndex = (TextView) findViewById(R.id.tv_index);

        requestOptions = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);

        ViewPager rvPictures = (ViewPager) findViewById(R.id.rv_pictures);

        Intent intent = getIntent();
        String[] srcArray = intent.getStringArrayExtra(IMAGE_ARRAY);
        List<String> srcList = intent.getStringArrayListExtra(IMAGE_LIST);
        editable = intent.getBooleanExtra(EDITABLE,false);
        int initPos = intent.getIntExtra(INIT_POS,0);
        if(srcArray!=null)
            lookUpAdapter = new LookUpAdapter(rvPictures,srcArray);
        else if(srcList!=null)
            lookUpAdapter = new LookUpAdapter(rvPictures,srcList);

        rvPictures.setAdapter(lookUpAdapter);
        rvPictures.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position==lookUpAdapter.getCount()-1 && positionOffset<0)//继续向右滑动
                    Toast.makeText(LookUpPictureActivity.this,"已经是最后一张",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageSelected(int position) {
                tvIndex.setText((position+1)+"/"+lookUpAdapter.getCount());
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        rvPictures.setCurrentItem(initPos);
        tvIndex.setText((initPos+1)+"/"+lookUpAdapter.getCount());

        findViewById(R.id.iv_back).setOnClickListener(this);
        View delete = findViewById(R.id.iv_delete);
        if(editable)
            delete.setOnClickListener(this);
        else
            delete.setVisibility(View.INVISIBLE);

        hasChanged = false;
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    public static void toLookUp(Activity context,int requestCode,boolean editable,int initPos,String[] src){
        Intent intent = new Intent(context,LookUpPictureActivity.class);
        intent.putExtra(INIT_POS,initPos);
        intent.putExtra(EDITABLE,editable);
        intent.putExtra(IMAGE_ARRAY, src);
        context.startActivityForResult(intent,requestCode);
    }

    public static void toLookUp(Activity context,int requestCode,boolean editable,int initPos,List<String> src){
        Intent intent = new Intent(context,LookUpPictureActivity.class);
        intent.putExtra(INIT_POS,initPos);
        intent.putExtra(EDITABLE,editable);
        intent.putStringArrayListExtra(IMAGE_LIST, (ArrayList<String>) src);
        context.startActivityForResult(intent,requestCode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                exit();
                break;

            case R.id.iv_delete:
                lookUpAdapter.deleteSnapPicture();
                break;
        }
    }

    private void exit(){
        if(editable) {
            if (hasChanged) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra(OUTPUT_IMAGE_LIST, (ArrayList<String>) lookUpAdapter.getContent());
                setResult(Activity.RESULT_OK, intent);
            } else
                setResult(Activity.RESULT_CANCELED);
        }
        finish();
    }

    private class LookUpAdapter extends PagerAdapter{
        ViewPager lookupPager;
        List<String> sourceList;

        LookUpAdapter(ViewPager lookupPager,String[] src){
            this.lookupPager = lookupPager;

            sourceList = new ArrayList<>();
            if(src==null || src.length==0)
                return;

            Collections.addAll(sourceList, src);
        }

        LookUpAdapter(ViewPager lookupPager,List<String> src){
            this.lookupPager = lookupPager;

            sourceList = new ArrayList<>();
            if(src==null || src.size()==0)
                return;

            sourceList.addAll(src);
        }

        List<String> getContent(){
            return sourceList;
        }

        String getContentString(){
            StringBuilder strBuilder = new StringBuilder();

            for (int i=0;i<sourceList.size();i++){
                if(i!=0)
                    strBuilder.append(",");
                strBuilder.append(sourceList.get(i));
            }

            return strBuilder.toString();
        }

        void deleteSnapPicture(){
            hasChanged = true;
            int pos = lookupPager.getCurrentItem();
            if(pos!=-1){
                sourceList.remove(pos);

                if(sourceList.size()==0)
                    exit();
                else
                    notifyDataSetChanged();
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View root = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_lookup_picture,container,false);
            final SubsamplingScaleImageView ivImage = (SubsamplingScaleImageView) root.findViewById(R.id.iv_image);
            final ProgressBar progressBar = (ProgressBar) root.findViewById(R.id.progress_bar);
            String source = sourceList.get(position);

            progressBar.setVisibility(View.VISIBLE);
            Glide.with(LookUpPictureActivity.this)
                    .asBitmap()
                    .load(source)
                    .apply(requestOptions)
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.INVISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.INVISIBLE);
                            return false;
                        }
                    })
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            ivImage.setImage(ImageSource.bitmap(resource));
                        }
                    });

            container.addView(root);
            return root;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return sourceList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    }

}
