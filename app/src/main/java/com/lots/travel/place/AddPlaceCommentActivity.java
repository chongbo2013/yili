package com.lots.travel.place;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.place.model.Place;
import com.lots.travel.place.model.PlaceComment;
import com.lots.travel.place.widget.PlaceProfileView;
import com.lots.travel.schedule.widget.AudioPlayer;
import com.lots.travel.schedule.widget.AudioRecorder;
import com.lots.travel.schedule.widget.DescriptionLayout;
import com.lots.travel.util.FileUtil;
import com.lots.travel.util.TypeUtil;
import com.lots.travel.video.RequestRecordActivity;
import com.lots.travel.video.VideoUtil;
import com.lots.travel.widget.EditTextActivity;
import com.lots.travel.widget.ImageLoader;
import com.lots.travel.widget.images.Image;
import com.lots.travel.widget.images.ImagePickerActivity;
import com.lots.travel.widget.images.LookUpPictureActivity;

import java.util.List;

/**
 * Created by nalanzi on 2018/1/3.
 */

public class AddPlaceCommentActivity extends RxBaseActivity implements View.OnClickListener,ImageLoader,DescriptionLayout.OnActionListener{
    public static final String PLACE = "Place";
    public static final String PLACE_COMMENT = "PlaceComment";

    public static void toComment(Context context, Place place){
        Intent intent = new Intent(context,AddPlaceCommentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(PLACE,place);
        context.startActivity(intent);
    }

    public static final int REQ_ADD_IMAGES = 1;
    public static final int REQ_ADD_VIDEO = 2;
    public static final int REQ_EDIT_TEXT = 3;
    public static final int REQ_SCAN_IMAGES = 4;

    private Place mPlace;
    private PlaceComment mPlaceComment;

    private PlaceProfileView vPlaceProfile;
    private RatingBar ratingBar;
    private DescriptionLayout vDescription;
    private RequestOptions mRequestOptions;

    private AudioPlayer mAudioPlayer;
    private AudioRecorder mAudioRecorder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place_comment);
        initData(getIntent(),savedInstanceState);
        initViews();
    }

    private void initData(Intent data,Bundle savedInstanceState){
        mPlace = data.getParcelableExtra(PLACE);
        if(savedInstanceState!=null){
            mPlace = savedInstanceState.getParcelable(PLACE);
            mPlaceComment = savedInstanceState.getParcelable(PLACE_COMMENT);
        }
        if(mPlaceComment==null)
            mPlaceComment = new PlaceComment();

        mRequestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);

        mAudioRecorder = new AudioRecorder();
        mAudioPlayer = new AudioPlayer();
    }

    private void initViews(){
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_publish).setOnClickListener(this);

        ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(fromUser)
                    mPlaceComment.setRating(ratingBar.getRating());
            }
        });
        ratingBar.setRating(mPlaceComment.getRating());

        vPlaceProfile = (PlaceProfileView) findViewById(R.id.v_place_profile);
        vPlaceProfile.setPlace(this,mPlace);
        vPlaceProfile.setOnDetailListener(this);

        vDescription = (DescriptionLayout) findViewById(R.id.v_description);
        vDescription.setOnActionListener(this);
        vDescription.setVoiceControl(mAudioRecorder,mAudioPlayer);
        vDescription.setCurrentItem(mPlaceComment.getCurrentItem());

        vDescription.setText(mPlaceComment.getText());
        vDescription.setImageList(mPlaceComment.getImages());
        vDescription.setVoiceFilepath(mPlaceComment.getSoundFilepath(),0,mPlaceComment.getSoundLength());
        vDescription.setVideoSource(mPlaceComment.getVideoFilepath(),mPlaceComment.getVideoCover());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PLACE,mPlace);
        outState.putParcelable(PLACE_COMMENT,mPlaceComment);
    }

    @Override
    protected void onDestroy() {
        mAudioPlayer.stop();
        super.onDestroy();
    }

    @Override
    public void loadImage(String url, ImageView target) {
        Glide.with(this)
                .load(url)
                .apply(mRequestOptions)
                .into(target);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;

            case PlaceProfileView.ID_DETAIL:
                ;
                break;

            case R.id.btn_cancel:
                finish();
                break;

            case R.id.btn_publish:
                if(mPlaceComment.getRating()==0){
                    Toast.makeText(this,R.string.place_comment_rating_empty,Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
        }
    }

    @Override
    public void onAction(int ac, Object data) {
        switch (ac){
            case DescriptionLayout.AC_CHANGE_TAB:
                mPlaceComment.setCurrentItem((Integer) data);
                vDescription.setCurrentItem((Integer) data);
                break;

            case DescriptionLayout.AC_ADD_IMAGES:
                ImagePickerActivity.toPick(this, REQ_ADD_IMAGES, 9-mPlaceComment.getImages().length, null);
                break;

            case DescriptionLayout.AC_ADD_VIDEO:
                RequestRecordActivity.toRecord(this,REQ_ADD_VIDEO,true);
                break;

            case DescriptionLayout.AC_EDIT_TEXT:
                EditTextActivity.toEdit(this,REQ_EDIT_TEXT,
                        new EditTextActivity.Config()
                                .text(mPlaceComment.getText())
                                .hint(getString(R.string.schedule_edit_text_hint))
                                .maxLength(100));
                break;

            case DescriptionLayout.AC_SCAN_IMAGES:
                LookUpPictureActivity.toLookUp(this, REQ_SCAN_IMAGES, true, (Integer) data, mPlaceComment.getImages());
                break;

            //语音长度大于0就有效
            case DescriptionLayout.AC_ADD_AUDIO:
                int length = (Integer) data;
                if(mPlaceComment.getSoundLength()!=length){
                    mPlaceComment.setSoundLength(length);
                }
                break;

            case DescriptionLayout.AC_DELETE_VIDEO:
                FileUtil.deleteFilesByPaths(mPlaceComment.getVideoFilepath(),mPlaceComment.getVideoCover());
                mPlaceComment.setVideoCover(null);
                mPlaceComment.setVideoFilepath(null);
                break;

            case DescriptionLayout.AC_PLAY_VIDEO:
                VideoUtil.play(this,mPlaceComment.getVideoFilepath());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode!= Activity.RESULT_OK)
            return;

        switch (requestCode){
            case REQ_ADD_IMAGES:
                List<Image> images = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGES);
                int srcLength = mPlaceComment.getImages().length;
                String[] strs = new String[srcLength+images.size()];
                if(srcLength>0)
                    System.arraycopy(mPlaceComment.getImages(),0,strs,0,srcLength);
                for (int i=0;i<images.size();i++){
                    strs[i+srcLength] = images.get(i).getPath();
                }
                mPlaceComment.setImages(strs);
                vDescription.setImageList(strs,9);
                break;

            case REQ_ADD_VIDEO:
                String videoPath = data.getStringExtra(RequestRecordActivity.VIDEO_PATH);
                String videoCoverPath = data.getStringExtra(RequestRecordActivity.VIDEO_COVER_PATH);
                mPlaceComment.setVideoFilepath(videoPath);
                mPlaceComment.setVideoCover(videoCoverPath);
                vDescription.setVideoSource(videoPath,videoCoverPath);
                break;

            case REQ_EDIT_TEXT:
                String text = data.getStringExtra(EditTextActivity.TEXT);
                mPlaceComment.setText(text);
                vDescription.setText(text);
                break;

            case REQ_SCAN_IMAGES:
                List<String> sPaths = data.getStringArrayListExtra(LookUpPictureActivity.OUTPUT_IMAGE_LIST);
                if(sPaths.size()!=mPlaceComment.getImages().length){
                    String[] aPaths = TypeUtil.list2array(sPaths);
                    mPlaceComment.setImages(aPaths);
                    vDescription.setImageList(aPaths,9);
                }
                break;
        }

    }
}
