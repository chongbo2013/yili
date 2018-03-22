package com.lots.travel.video;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.lots.travel.BuildConfig;

import java.io.File;

/**
 * Created by nalanzi on 2017/12/5.
 */

public class VideoUtil {

    public static void play(Activity context,String videoPath){
        Uri videoUri;
        File videoFile = new File(videoPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if(videoPath.startsWith("file")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                videoUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", videoFile);
            else
                videoUri  = Uri.fromFile(videoFile);
        }else{
            videoUri = Uri.parse(videoPath);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(videoUri, "video/*");

        context.startActivity(intent);
    }

}
