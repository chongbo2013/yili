package com.lots.travel.schedule.note;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.main.info.mine.EditUserInfoActivity;
import com.lots.travel.network.DefaultExceptionHandler;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.SingleAdapter;
import com.lots.travel.network.model.request.AddTrip;
import com.lots.travel.network.model.result.TravelTag;
import com.lots.travel.network.service.CommonService;
import com.lots.travel.network.service.TripService;
import com.lots.travel.schedule.ChooseTripDateActivity;
import com.lots.travel.schedule.SearchPlaceActivity;
import com.lots.travel.schedule.note.edit.NoteEditScheduleActivity;
import com.lots.travel.schedule.widget.ChooseCoverView;
import com.lots.travel.schedule.widget.ChooseTagsView;
import com.lots.travel.schedule.base.Schedule;
import com.lots.travel.store.FileStore;
import com.lots.travel.store.db.ViewCity;

import com.lots.travel.util.Constant;
import com.lots.travel.util.FileUtil;
import com.lots.travel.util.LocateUtil;
import com.lots.travel.util.SosonaOssClient;
import com.lots.travel.util.TakePictureUtil;
import com.lots.travel.widget.ChoosePictureDialog;
import com.lots.travel.widget.ChooseSrcDstLayout;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nalanzi on 2017/9/8.
 */
//日期、地点选取可以后续单独封装出去
public class CreateNoteActivity extends RxBaseActivity implements View.OnClickListener {
    private static final int REQ_CODE_DATE = 0x10;

    private static final int REQ_CODE_PLACE_SRC = 0x11;
    private static final int REQ_CODE_PLACE_DST = 0x12;

    private static final int REQ_CODE_PHOTO = 0x13;

    public static final int REQ_CODE_ALBUM = 0x14;

    private ChooseSrcDstLayout vChooseSrcDst;

    private ChooseCoverView vChooseCover;

    private ChooseTagsView vChooseTags;

    private Date mChooseRangeStart, mChooseRangeEnd;
    private ViewCity mSrcPlace, mDstPlace;
    private String mPictureName;
    private boolean mPortraitChanged;
    private ChoosePictureDialog dlgChoosePicture;

    private SosonaOssClient mOssClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        findViewById(R.id.iv_back).setOnClickListener(this);

        vChooseSrcDst = (ChooseSrcDstLayout) findViewById(R.id.v_choose_src_dst);
        vChooseSrcDst.applyClickListener(this);

        TextView tvSubject = (TextView) findViewById(R.id.tv_theme);
        tvSubject.setText(showSubjectTitle());

        vChooseCover = (ChooseCoverView) findViewById(R.id.v_choose_cover);
        vChooseCover.setOnClickListener(this);
        vChooseCover.setCoverPath(null);

        vChooseTags = (ChooseTagsView) findViewById(R.id.v_choose_tags);

        findViewById(R.id.btn_write).setOnClickListener(this);

        //待修改
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        mChooseRangeEnd = calendar.getTime();

        Calendar cal = Calendar.getInstance();
        cal.setTime(mChooseRangeEnd);
        cal.add(Calendar.DAY_OF_MONTH, -6);
        mChooseRangeStart = cal.getTime();

        String srcCity = LocateUtil.getCity();
        srcCity = TextUtils.isEmpty(srcCity) ? "杭州":srcCity;
        mSrcPlace = new ViewCity();
        mSrcPlace.setId(-1L);
        mSrcPlace.setName(srcCity);

        vChooseSrcDst.setTime(mChooseRangeStart, mChooseRangeEnd);
        vChooseSrcDst.setPlace(
                mSrcPlace !=null ? mSrcPlace.getName():null,
                mDstPlace !=null ? mDstPlace.getName():null);

        mOssClient = new SosonaOssClient(this);
        dlgChoosePicture = new ChoosePictureDialog(this, new ChoosePictureDialog.OnChooseListener() {
            @Override
            public void onTakePhoto() {
                mPictureName = TakePictureUtil.genName();
                TakePictureUtil.takeFromCamera(CreateNoteActivity.this,REQ_CODE_PHOTO,new FileStore().getCacheImageDir().getAbsolutePath(),mPictureName);
            }

            @Override
            public void onTakeAlbum() {
                TakePictureUtil.takeFromAlbum(CreateNoteActivity.this,REQ_CODE_ALBUM,getResources().getString(R.string.info_album_title));
            }
        });

        requestTravelTags();
    }

    private void requestTravelTags(){
        ServiceGenerator.createService(this, CommonService.class)
                .getTravelTags()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleAdapter<HttpResult<List<TravelTag>>>() {
                    @Override
                    public void onSuccess(@NonNull HttpResult<List<TravelTag>> result) {
                        vChooseTags.setTags(result.getData());
                    }

                    @Override
                    public void onError(Throwable e) {}
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;

            case ChooseSrcDstLayout.ID_TIME_BACK:
            case ChooseSrcDstLayout.ID_TIME_GO:
                ChooseTripDateActivity.toChoose(this,REQ_CODE_DATE, mChooseRangeStart, mChooseRangeEnd);
                break;

            case ChooseSrcDstLayout.ID_PLACE_SRC:
                SearchPlaceActivity.toSearch(this,REQ_CODE_PLACE_SRC);
                break;

            case ChooseSrcDstLayout.ID_PLACE_DST:
                SearchPlaceActivity.toSearch(this,REQ_CODE_PLACE_DST);
                break;

            case ChooseCoverView.CHANGE:
            case ChooseCoverView.UPLOAD:
                dlgChoosePicture.show();
                break;

            case R.id.btn_write:{
                if(mChooseRangeStart ==null || mChooseRangeEnd ==null){
                    Toast.makeText(this,R.string.note_destination_notime,Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mSrcPlace ==null || TextUtils.isEmpty(mSrcPlace.getName())
                        || mDstPlace ==null || TextUtils.isEmpty(mDstPlace.getName())){
                    Toast.makeText(this,R.string.note_destination_noplace,Toast.LENGTH_SHORT).show();
                    return;
                }
                List<TravelTag> tags = vChooseTags.getCheckedTags();
                if(tags==null || tags.size()==0){
                    Toast.makeText(this,R.string.note_destination_notheme,Toast.LENGTH_SHORT).show();
                    return;
                }

                requestCreateTrip();
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode!= Activity.RESULT_OK) {
            //if(requestCode!=REQ_CODE_PHOTO)
                return;
        }

        switch (requestCode){
            case REQ_CODE_DATE:{
                mChooseRangeStart = (Date) data.getSerializableExtra(ChooseTripDateActivity.CHOSE_START_DATE);
                mChooseRangeEnd = (Date) data.getSerializableExtra(ChooseTripDateActivity.CHOSE_END_DATE);
                vChooseSrcDst.setTime(mChooseRangeStart, mChooseRangeEnd);
                break;
            }

            case REQ_CODE_PLACE_SRC:{
                mSrcPlace = data.getParcelableExtra(SearchPlaceActivity.EXTRA_PLACE);
                vChooseSrcDst.setPlace(mSrcPlace !=null ? mSrcPlace.getName():null,true);
                break;
            }

            case REQ_CODE_PLACE_DST:{
                mDstPlace = data.getParcelableExtra(SearchPlaceActivity.EXTRA_PLACE);
                vChooseSrcDst.setPlace(mDstPlace !=null ? mDstPlace.getName():null,false);
                break;
            }

            case REQ_CODE_PHOTO:{
                String srcPath = new FileStore().getCacheImageDir().getAbsolutePath() + File.separator + mPictureName;
                vChooseCover.setCoverPath(srcPath);
                mPortraitChanged = true;
                break;
            }

            case REQ_CODE_ALBUM:{
                String srcPath = FileUtil.getFilepath(this, data.getData());
                vChooseCover.setCoverPath(srcPath);
                mPortraitChanged = true;
                break;
            }
        }
    }

    private SpannableStringBuilder showSubjectTitle(){
        int end;
        int textSize,textColor;

        SpannableStringBuilder strBuilder = new SpannableStringBuilder();

        strBuilder.append(getResources().getString(R.string.note_destination_theme_1));
        end = strBuilder.length();
        textSize = (int) getResources().getDimension(R.dimen.head_small);
        textColor = ContextCompat.getColor(this,R.color.color_title_big);
        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),0,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        strBuilder.append(getResources().getString(R.string.note_destination_theme_2));
        textSize = (int) getResources().getDimension(R.dimen.fonts_tip);
        textColor = ContextCompat.getColor(this,R.color.color_title_big);
        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),end,strBuilder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return strBuilder;
    }

    private static final long MILLS_DAY = 1000*60*60*24L;
    private int getDifferenceDays(Date start, Date end) {
        int count = (int)((end.getTime() - start.getTime())/MILLS_DAY);
        count = Math.abs(count)+1;
        return count;
    }

    private void requestCreateTrip(){
        final String[] coverUrl = new String[1];
        Single.create(
                new SingleOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull SingleEmitter<String> e) throws Exception {
                        //上传封面
                        if(mPortraitChanged) {
                            String path = vChooseCover.getCoverPath();
                            if(path!=null) {
                                coverUrl[0] = mOssClient.uploadImage(CreateNoteActivity.this, SosonaOssClient.ITEM_TYPE_PRODUCT, path,true);
                            }
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        final int days = getDifferenceDays(mChooseRangeStart, mChooseRangeEnd);

                        AddTrip params = new AddTrip();
                        params.setStyle("note");
                        params.setCityFrom(mSrcPlace.getId()==-1 ? mSrcPlace.getName(): mSrcPlace.getId()+"");
                        params.setCityTo(mDstPlace.getId()==-1 ? mDstPlace.getName(): mDstPlace.getId()+"");
                        params.setDays(days+"");
                        params.setDateFrom(sdf.format(mChooseRangeStart));
                        params.setDateTo(sdf.format(mChooseRangeEnd));
                        params.setXcshow(1+"");//仅自己可见
                        params.setTravelTag(vChooseTags.getCheckedTagsString());
                        params.setImg(coverUrl[0]);

                        HttpResult<String> result = ServiceGenerator.createService(CreateNoteActivity.this,TripService.class)
                                .addTrip(params)
                                .blockingGet();

                        e.onSuccess(result.getData());
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleAdapter<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        showProgressDialog();

                    }

                    @Override
                    public void onSuccess(@NonNull String id) {
                        dismissProgressDialog();

                        if(TextUtils.isEmpty(id)){
                            Toast.makeText(CreateNoteActivity.this,"游记id错误",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Long placeId;
                        Schedule schedule = new Schedule();
                        schedule.setId(id);
                        schedule.setCount(getDifferenceDays(mChooseRangeStart, mChooseRangeEnd));
                        schedule.setStartTime(mChooseRangeStart.getTime());
                        placeId = mSrcPlace.getId();
                        schedule.setCityFrom(placeId==null||placeId==-1 ? null:Long.toString(placeId), mSrcPlace.getName());
                        placeId = mDstPlace.getId();
                        schedule.setCityTo(placeId==null||placeId==-1 ? null:Long.toString(placeId), mDstPlace.getName());
                        schedule.setCountryFrom(mSrcPlace.getCountry(), mSrcPlace.getCName());
                        schedule.setCountryTo(mDstPlace.getCountry(), mDstPlace.getCName());
                        schedule.setTravelTags(vChooseTags.getCheckedTagsString());
                        String url = mDstPlace.getViewImg();
                        if(!TextUtils.isEmpty(url)) {
                            url = url.startsWith("http") ? url: Constant.PATH_OSS_IMAGE+url;
                            schedule.setCityToImg(url);
                        }

                        NoteEditScheduleActivity.toEdit(CreateNoteActivity.this,schedule);
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        new DefaultExceptionHandler().handleException(CreateNoteActivity.this,e);
                    }
                });
    }

}
