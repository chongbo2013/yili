package com.lots.travel.widget.images;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.os.Bundle;
import android.os.Parcelable;

import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lots.travel.R;
import com.lots.travel.network.HttpResult;
import com.lots.travel.util.DensityUtil;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;
import com.lots.travel.widget.refresh.PageIterator;
import com.lots.travel.widget.refresh.PagedItemFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;

//数据加载、点击处理、返回选取结果
public class ImagePickerActivity extends AppCompatActivity implements View.OnClickListener,ImagePickerAdapter.OnItemClickListener,ImagePickerAdapter.OnLoadListener{
    private Button btnPreview,btnDone;

    private int mMaxCount;
    private List<Image> mSelectedImages = new ArrayList<>();

    private ImagePickerFragment mImagePickerFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        Intent intent = getIntent();
        List<Image> srcSelectImage = intent.getParcelableArrayListExtra(EXTRA_IMAGES);
        mMaxCount = intent.getIntExtra(EXTRA_COUNT,9);
        if(savedInstanceState!=null){
            srcSelectImage = savedInstanceState.getParcelableArrayList(EXTRA_IMAGES);
            mMaxCount = savedInstanceState.getInt(EXTRA_COUNT);
        }
        if (srcSelectImage!=null && srcSelectImage.size()!=0)
            mSelectedImages.addAll(srcSelectImage);

        if(mMaxCount==0)
            finish();

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);

        btnPreview = (Button) findViewById(R.id.btn_preview);
        btnPreview.setOnClickListener(this);
        btnDone = (Button) findViewById(R.id.btn_done);
        btnDone.setOnClickListener(this);

        mImagePickerFragment = new ImagePickerFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_container,mImagePickerFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_IMAGES, (ArrayList<? extends Parcelable>) mSelectedImages);
        outState.putInt(EXTRA_COUNT,mMaxCount);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mImagePickerFragment.setUserVisibleHint(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                toBack(false);
                break;

            case R.id.btn_preview:
                List<String> imagePaths = new ArrayList<>();
                for (Image image:mSelectedImages){
                    imagePaths.add(image.getPath());
                }
                LookUpPictureActivity.toLookUp(this,0,false,0,imagePaths);
                break;

            case R.id.btn_done:
                toBack(true);
                break;

            case R.id.tv_cancel:
                toBack(false);
                break;
        }
    }

    @Override
    public void onItemClick(int position, long itemId) {
        handleSelectChange(position);
    }

    private void handleSelectSizeChange(int size) {
        if (size > 0) {
            btnPreview.setEnabled(true);
            btnDone.setEnabled(true);
            btnDone.setText(String.format("%s(%s)", getResources().getString(R.string.image_done), size));
        } else {
            btnPreview.setEnabled(false);
            btnDone.setEnabled(false);
            btnDone.setText(getResources().getString(R.string.image_done));
        }
    }

    private void handleSelectChange(int position) {
        Image image = mImagePickerFragment.getItem(position);
        if(image == null)
            return;

        if (image.isSelect()) {
            image.setSelect(false);
            mSelectedImages.remove(image);
            mImagePickerFragment.updateItem(position);
        } else {
            if (mSelectedImages.size() == mMaxCount) {
                Toast.makeText(this, "图片选择数量超过上限", Toast.LENGTH_SHORT).show();
            } else {
                image.setSelect(true);
                mSelectedImages.add(image);
                mImagePickerFragment.updateItem(position);
            }
        }
        handleSelectSizeChange(mSelectedImages.size());
    }

    @Override
    public void onLoaded() {
        handleSelectSizeChange(mSelectedImages.size());
    }

    @Override
    public void onRefreshed() {
        //改变界面状态
        handleSelectSizeChange(0);
    }

    public static class ImagePickerFragment extends PagedItemFragment<Image>{
        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.MINI_THUMB_MAGIC,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        private ImagePickerActivity mHost;
        private ImagePickerAdapter mImagePickerAdapter;
        private ImagePickerAdapter.OnItemClickListener mOnItemClickListener;
        private ImagePickerAdapter.OnLoadListener mOnLoadListener;

        private int mOffset = 0;
        private int mPageSize = 32;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            mHost = (ImagePickerActivity) context;
            mOnItemClickListener = (ImagePickerAdapter.OnItemClickListener) context;
            mOnLoadListener = (ImagePickerAdapter.OnLoadListener) context;
        }

        public Image getItem(int position){
            int itemPosition = mImagePickerAdapter.getItemPosition(position);
            return mImagePickerAdapter.getItem(itemPosition);
        }

        public void updateItem(int position){
            if(position<mImagePickerAdapter.getItemCount())
                mImagePickerAdapter.notifyItemChanged(position);
        }

        @Override
        public void onConfigureDisplay(ConfigureAdapter configureAdapter) {
            super.onConfigureDisplay(configureAdapter);
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int itemType = getAdapter().getItemViewType(position);
                    return itemType!=0 ? 4:1;
                }
            });
            configureAdapter.setLayoutManager(layoutManager);
            configureAdapter.addItemDecoration(new SpaceGridItemDecoration( DensityUtil.dp2px(getContext(), .5f)));
        }

        @Override
        public void onRefresh() {
            mOffset = 0;
            //清空选取的图片
            mHost.mSelectedImages.clear();
            super.onRefresh();
        }

        @Override
        public PageIterator.PageRequest<Image> createPageRequest() {
            return new PageIterator.PageRequest<Image>() {
                @Override
                public Single<HttpResult<List<Image>>> execute(int page) {
                    return Single.create(new SingleOnSubscribe<HttpResult<List<Image>>>() {
                        @Override
                        public void subscribe(@NonNull SingleEmitter<HttpResult<List<Image>>> e) throws Exception {
                            //从ContentProvider获取所有图片资源
                            ContentResolver resolver = getContext().getContentResolver();
                            Cursor cursor = resolver.query(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    IMAGE_PROJECTION,
                                    null,
                                    null,
                                    IMAGE_PROJECTION[2]+" desc limit "+mPageSize+" offset "+mOffset,
                                    null);

                            if(cursor==null){
                                e.onError(new RuntimeException("Cursor查询出错"));
                                return;
                            }

                            HttpResult<List<Image>> result = new HttpResult<>();

                            try{
                                final ArrayList<Image> images = new ArrayList<>();
                                int count = cursor.getCount();
                                mOffset += count;
                                if(count>0){
                                    cursor.moveToFirst();
                                    do {
                                        String path = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                                        String name = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                                        long dateTime = cursor.getLong(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                                        int id = cursor.getInt(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                                        String thumbPath = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                                        String bucket = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));

                                        Image image = new Image();
                                        image.setPath(path);
                                        image.setName(name);
                                        image.setDate(dateTime);
                                        image.setId(id);
                                        image.setThumbPath(thumbPath);
                                        image.setFolderName(bucket);
                                        images.add(image);

                                        //如果是被选中的图片
                                        if (mHost.mSelectedImages.size() > 0) {
                                            for (Image i : mHost.mSelectedImages) {
                                                if (i.getPath().equals(image.getPath())) {
                                                    image.setSelect(true);
                                                }
                                            }
                                        }
                                    } while (cursor.moveToNext());

                                    //删除掉不存在的，在于用户选择了相片，又去相册删除
                                    if (mHost.mSelectedImages.size() > 0) {
                                        List<Image> rs = new ArrayList<>();
                                        for (Image i : mHost.mSelectedImages) {
                                            File f = new File(i.getPath());
                                            if (!f.exists()) {
                                                rs.add(i);
                                            }
                                        }
                                        mHost.mSelectedImages.removeAll(rs);
                                    }
                                }
                                result.setData(images);
                                e.onSuccess(result);
                            }finally{
                                cursor.close();
                            }
                        }
                    });
                }
            };
        }

        @Override
        public AbstractLoadAdapter<Image> createAdapter(RecyclerView rv) {
            mImagePickerAdapter = new ImagePickerAdapter(getContext(),rv);
            mImagePickerAdapter.setOnItemClickListener(mOnItemClickListener);
            mImagePickerAdapter.setOnLoadListener(mOnLoadListener);
            return mImagePickerAdapter;
        }

    }

    public static final String EXTRA_COUNT = "maxCount";
    public static final String EXTRA_IMAGES = "selectedImages";
    private void toBack(boolean success){
        if(success){
            Intent data = new Intent();
            data.putParcelableArrayListExtra(EXTRA_IMAGES, (ArrayList<? extends Parcelable>) mSelectedImages);
            setResult(Activity.RESULT_OK,data);
        }else
            setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public static void toPick(Activity context,int requestCode,int maxCount,List<Image> data){
        Intent intent = new Intent(context,ImagePickerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(EXTRA_COUNT,maxCount);
        intent.putParcelableArrayListExtra(EXTRA_IMAGES, (ArrayList<? extends Parcelable>) data);
        context.startActivityForResult(intent,requestCode);
    }

}
