package com.lots.travel.video;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.aliyun.common.utils.StorageUtils;
import com.aliyun.struct.common.AliyunVideoParam;
import com.aliyun.struct.common.CropKey;
import com.aliyun.struct.common.ScaleMode;
import com.aliyun.struct.common.VideoQuality;
import com.aliyun.struct.recorder.CameraType;
import com.aliyun.struct.recorder.FlashType;
import com.aliyun.struct.snap.AliyunSnapVideoParam;

import com.lots.travel.video.edit.editor.EditorActivity;
import com.lots.travel.video.importer.ImportMediaActivity;
import com.lots.travel.video.record.AliyunVideoRecorder;
import com.lots.travel.video.record.Common;

import java.io.File;
import java.util.List;

/**
 * 设置参数，跳转到AliyunVideoRecorder页面
 * record以后，返回地址，然后跳转至edit页面
 */
public class RequestRecordActivity extends AppCompatActivity {
    public static final String VIDEO_TITLE = "VideoTitle";
    public static final String VIDEO_PATH = "VideoPath";
    public static final String VIDEO_COVER_PATH = "VideoCoverPath";

    public static final String EXTRA_NEED_COVER = "NeedCover";
    public static final String EXTRA_TO_IMPORT = "ToImport";

    private static final int REQ_RECORD = 1;
    private static final int REQ_IMPORT = 2;
    private static final int REQ_EDIT = 3;
    private static final int REQ_CUT_COVER = 4;

    private String mVideoTitle;
    private String mVideoPath;
    private String mVideoCoverPath;

    private AliyunVideoParam mVideoParam;
    private String mProjectJsonPath;
    private List<String> mTempFilePaths;

    private boolean mNeedCover;

    public static void toRecord(Activity context,int requestCode,boolean needCover){
        Intent intent = new Intent(context,RequestRecordActivity.class);
        intent.putExtra(EXTRA_NEED_COVER,needCover);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        mNeedCover = getIntent().getBooleanExtra(EXTRA_NEED_COVER,true);
        record();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQ_RECORD:
                if(resultCode== Activity.RESULT_OK) {
                    mVideoParam = (AliyunVideoParam) data.getSerializableExtra(EditorActivity.KEY_VIDEO_PARAM);
                    mProjectJsonPath = data.getStringExtra(EditorActivity.KEY_PROJECT_JSON_PATH);
                    mTempFilePaths = data.getStringArrayListExtra(EditorActivity.KEY_TEMP_FILE_LIST);

                    EditorActivity.startActivity(this,REQ_EDIT,mVideoParam,mProjectJsonPath,mTempFilePaths);
                }else {
                    if(data!=null &&
                            data.getBooleanExtra(EXTRA_TO_IMPORT,false)){
                        toImport();
                    }else {
                        setResult(Activity.RESULT_CANCELED);
                        finish();
                    }
                }
                break;

            case REQ_IMPORT:
                if(resultCode==Activity.RESULT_OK){
                    mVideoParam = (AliyunVideoParam) data.getSerializableExtra(EditorActivity.KEY_VIDEO_PARAM);
                    mProjectJsonPath = data.getStringExtra(EditorActivity.KEY_PROJECT_JSON_PATH);
                    EditorActivity.startActivity(this,REQ_EDIT,mVideoParam,mProjectJsonPath,mTempFilePaths);
                }else{
                    record();
                }
                break;

            case REQ_EDIT:
                if(mNeedCover){
                    if(resultCode==Activity.RESULT_OK){
                        mVideoPath = data.getStringExtra(EditorActivity.KEY_VIDEO_PATH);
                        CutCoverActivity.toCut(this,REQ_CUT_COVER,mVideoParam,mVideoPath,mTempFilePaths);
                    }else{
                        record();
                    }
                }else{
                    if(resultCode==Activity.RESULT_OK){
                        Intent ret = new Intent();
                        mVideoPath = data.getStringExtra(EditorActivity.KEY_VIDEO_PATH);
                        ret.putExtra(VIDEO_PATH,mVideoPath);
                        setResult(Activity.RESULT_OK,ret);
                    }else{
                        setResult(Activity.RESULT_CANCELED);
                    }
                    deleteTempFiles(mTempFilePaths);
                    finish();
                }
                break;

            case REQ_CUT_COVER:
                if(resultCode==Activity.RESULT_OK){
                    mVideoTitle = data.getStringExtra(CutCoverActivity.OUTPUT_TITLE);
                    mVideoCoverPath = data.getStringExtra(CutCoverActivity.OUTPUT_COVER_PATH);
                    Intent ret = new Intent();
                    ret.putExtra(VIDEO_TITLE,mVideoTitle);
                    ret.putExtra(VIDEO_PATH,mVideoPath);
                    ret.putExtra(VIDEO_COVER_PATH,mVideoCoverPath);
                    setResult(Activity.RESULT_OK,ret);
                }else{
                    setResult(Activity.RESULT_CANCELED);
                }
                deleteTempFiles(mTempFilePaths);
                finish();
        }
    }

    private void record(){
        AliyunSnapVideoParam recordParam = new AliyunSnapVideoParam.Builder()
                .setResulutionMode(AliyunSnapVideoParam.RESOLUTION_540P)
                .setRatioMode(AliyunSnapVideoParam.RATIO_MODE_9_16)
                .setRecordMode(AliyunSnapVideoParam.RECORD_MODE_AUTO)
                .setFilterList(getRecordFilters())
                .setBeautyLevel(80)
                .setBeautyStatus(true)
                .setCameraType(CameraType.BACK)
                .setFlashType(FlashType.AUTO)
                .setNeedClip(true)
                .setMaxDuration(30000)
                .setMinDuration(2000)
                .setVideQuality(VideoQuality.HD)
                .setGop(5)
                .setVideoBitrate(0)
                /**
                 * 裁剪参数
                 */
                .setMinVideoDuration(4000)
                .setMaxVideoDuration(29 * 1000)
                .setMinCropDuration(3000)
                .setFrameRate(25)
                .setCropMode(ScaleMode.PS)
                .build();
        AliyunVideoRecorder.startRecord(this,REQ_RECORD,recordParam);
    }

    private void toImport(){
        Intent intent = new Intent(this,ImportMediaActivity.class);
        intent.putExtra(CropKey.VIDEO_RATIO, AliyunSnapVideoParam.RATIO_MODE_9_16);
        intent.putExtra(CropKey.VIDEO_SCALE,ScaleMode.PS);
        intent.putExtra(CropKey.VIDEO_QUALITY , VideoQuality.HD);
        intent.putExtra(CropKey.VIDEO_FRAMERATE,25);
        intent.putExtra(CropKey.VIDEO_GOP,5);
        intent.putExtra(CropKey.VIDEO_BITRATE, 0);
        startActivityForResult(intent,REQ_IMPORT);
    }

    private String[] getRecordFilters(){
        String path = StorageUtils.getCacheDirectory(this).getAbsolutePath() + Common.QU_NAME + File.separator;
        return new String[]{
                null,
                path + "filter/chihuang",
                path + "filter/fentao",
                path + "filter/hailan",
                path + "filter/hongrun",
                path + "filter/huibai",
                path + "filter/jingdian",
                path + "filter/maicha",
                path + "filter/nonglie",
                path + "filter/rourou",
                path + "filter/shanyao",
                path + "filter/xianguo",
                path + "filter/xueli",
                path + "filter/yangguang",
                path + "filter/youya",
                path + "filter/zhaoyang"
        };
    }

    private void deleteTempFiles(List<String> tempPaths) {
        if (tempPaths != null) {
            for (String path : tempPaths) {
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

}
