package com.lots.travel.footprint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.model.request.AddFootprint;
import com.lots.travel.network.service.FootprintService;
import com.lots.travel.store.FileStore;
import com.lots.travel.util.DensityUtil;
import com.lots.travel.util.LocateUtil;
import com.lots.travel.util.SosonaOssClient;
import com.lots.travel.util.TakePictureUtil;
import com.lots.travel.widget.ChoosePictureDialog;
import com.lots.travel.widget.GridSpaceItemDecoration;
import com.lots.travel.widget.WholeGridLayoutManager;
import com.lots.travel.widget.images.Image;
import com.lots.travel.widget.images.ImagePickerActivity;
import com.lots.travel.widget.images.LookUpPictureActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nalanzi on 2017/12/15.
 */

public class PublishImagesActivity extends RxBaseActivity implements View.OnClickListener {
    public static final int REQ_ADD_PHOTO = 0;

    public static final int REQ_ADD_ALBUM = 1;

    public static final int REQ_SCAN = 2;

    public static final int REQ_LOCATE = 3;

    public static final int REQ_TARGETS = 4;

    private static final int MAX_IMAGE_COUNT = 9;

    private EditText etText;
    private AddPictureAdapter mPictureAdapter;
    private ChoosePictureDialog dlgChoosePicture;

    private TextView tvAddress;
    private Switch sLocation;
    private Switch sTargets;

    private String mPictureName;
    private SosonaOssClient mOssClient;

    private double mLatitude;
    private double mLongitude;
    private String mDetailAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_images);

        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_publish).setOnClickListener(this);
//        findViewById(R.id.ll_location).setOnClickListener(this);
//        findViewById(R.id.ll_targets).setOnClickListener(this);

        etText = (EditText) findViewById(R.id.et_text);

        RecyclerView rvImages = (RecyclerView) findViewById(R.id.rv_images);
        rvImages.setNestedScrollingEnabled(false);
        rvImages.setLayoutManager(new WholeGridLayoutManager(this,3));
        rvImages.addItemDecoration(new GridSpaceItemDecoration(3, DensityUtil.dp2px(this,5),DensityUtil.dp2px(this,5)));
        mPictureAdapter = new AddPictureAdapter(this,MAX_IMAGE_COUNT);
        rvImages.setAdapter(mPictureAdapter);

        mPictureAdapter.setOnItemClickListener(new AddPictureAdapter.OnItemClickListener() {
            @Override
            public void onAddImage() {
                dlgChoosePicture.show();
            }

            @Override
            public void onScanImage(int position, List<String> images) {
                LookUpPictureActivity.toLookUp(PublishImagesActivity.this, REQ_SCAN, true, position, images);
            }
        });

        dlgChoosePicture = new ChoosePictureDialog(this, new ChoosePictureDialog.OnChooseListener() {
            @Override
            public void onTakePhoto() {
                mPictureName = TakePictureUtil.genName();
                TakePictureUtil.takeFromCamera(PublishImagesActivity.this,REQ_ADD_PHOTO,new FileStore().getCacheImageDir().getAbsolutePath(),mPictureName);
            }

            @Override
            public void onTakeAlbum() {
                ImagePickerActivity.toPick(PublishImagesActivity.this,REQ_ADD_ALBUM,MAX_IMAGE_COUNT-mPictureAdapter.getImageCount(),null);
            }
        });

        tvAddress = (TextView) findViewById(R.id.tv_address);
        sLocation = (Switch) findViewById(R.id.s_location);
        sTargets = (Switch) findViewById(R.id.s_targets);
        sLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mLatitude = LocateUtil.getLatitude();
                    mLongitude = LocateUtil.getLongitude();
                    mDetailAddress = LocateUtil.getDetailAddress();
                    tvAddress.setText(mDetailAddress);
                }else{
                    tvAddress.setText(R.string.footprint_publish_location);
                }
            }
        });

        mOssClient = new SosonaOssClient(this);

        boolean fromPhoto = getIntent().getBooleanExtra(EXTRA_FROM_PHOTO,true);
        if(fromPhoto) {
            mPictureName = TakePictureUtil.genName();
            TakePictureUtil.takeFromCamera(PublishImagesActivity.this, REQ_ADD_PHOTO, new FileStore().getCacheImageDir().getAbsolutePath(), mPictureName);
        }else{
            ImagePickerActivity.toPick(PublishImagesActivity.this,REQ_ADD_ALBUM,MAX_IMAGE_COUNT,null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!= Activity.RESULT_OK)
            return;

        switch (requestCode){
            case REQ_ADD_ALBUM: {
                List<Image> imageList = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGES);
                if(imageList!=null && imageList.size()>0){
                    List<String> src = new ArrayList<>();
                    for (int i=0;i<imageList.size();i++){
                        src.add(imageList.get(i).getPath());
                    }
                    mPictureAdapter.addImages(src);
                }
                break;
            }

            case REQ_ADD_PHOTO: {
                String srcPath = new FileStore().getCacheImageDir().getAbsolutePath() + File.separator + mPictureName;
                mPictureAdapter.addImage(srcPath);
                break;
            }

            case REQ_SCAN:
                List<String> images = data.getStringArrayListExtra(LookUpPictureActivity.OUTPUT_IMAGE_LIST);
                mPictureAdapter.setImageList(images);
                break;
        }
    }

    private static String EXTRA_FROM_PHOTO = "fromPhoto";

    public static void toPublish(Context context,boolean fromPhoto){
        Intent intent = new Intent(context,PublishImagesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(EXTRA_FROM_PHOTO,fromPhoto);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                finish();
                break;

            case R.id.tv_publish:
                publish();
                break;

            case R.id.ll_location:
                ;
                break;

            case R.id.ll_targets:
                ;
                break;
        }
    }

    void publish(){
        final String text = etText.getText().toString();
        final List<String> images = mPictureAdapter.getImageList();
        if(TextUtils.isEmpty(text.trim()) && images.size()==0) {
            Toast.makeText(this,R.string.footprint_publish_empty_tip,Toast.LENGTH_SHORT).show();
            return;
        }
        final boolean openLocation = sLocation.isChecked();
        final boolean openTargets = sTargets.isChecked();

        Single.create(
                new SingleOnSubscribe<String>() {
                    @Override
                    public void subscribe(@io.reactivex.annotations.NonNull SingleEmitter<String> e) throws Exception {
                        StringBuilder imagesBuilder = new StringBuilder();
                        if(images.size()>0) {
                            SparseArray<String> urlMap = new SparseArray<>();
                            List<SosonaOssClient.Item> uploadItems = new ArrayList<>();
                            for (int i=0;i<images.size();i++){
                                uploadItems.add(new SosonaOssClient.Item(SosonaOssClient.ITEM_TYPE_FOOTPRINT,images.get(i)));
                            }
                            //上传文件
                            List<SosonaOssClient.Item> failedItems = mOssClient.upload(PublishImagesActivity.this,uploadItems, urlMap);
                            if (failedItems != null && failedItems.size() > 0)
                                e.onError(new RuntimeException("部分文件上传失败"));
                            for (int i=0;i<images.size();i++){
                                String path = images.get(i);
                                String url = !TextUtils.isEmpty(path) ? urlMap.get(path.hashCode()):null;
                                if(TextUtils.isEmpty(url))
                                    continue;
                                if(i!=0)
                                    imagesBuilder.append(",");
                                imagesBuilder.append(url);
                            }
                        }

                        //调用请求
                        AddFootprint params = new AddFootprint();
                        params.setType("1");
                        params.setContent(text);
                        params.setPics(imagesBuilder.toString());
                        if(openLocation) {
                            params.setLocation(mLatitude,mLongitude);
                            params.setGpsAddress(mDetailAddress);
                        }
                        params.setXzshow(openTargets ? "1":"0");
                        try{
                            HttpResult<String> result = ServiceGenerator.createService(PublishImagesActivity.this, FootprintService.class)
                                    .addFootprint(params)
                                    .blockingGet();
                            e.onSuccess(result.getData());
                        }catch (Exception exception){
                            e.onError(exception);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        showProgressDialog();
                    }

                    @Override
                    public void onSuccess(@NonNull String s) {
                        dismissProgressDialog();
                        Toast.makeText(PublishImagesActivity.this,R.string.footprint_publish_success,Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dismissProgressDialog();
                        Toast.makeText(PublishImagesActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
