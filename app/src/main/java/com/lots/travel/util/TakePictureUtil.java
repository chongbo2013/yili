package com.lots.travel.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.lots.travel.BuildConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by nalanzi on 2017/12/14.
 */

public class TakePictureUtil {

    public static String genName(){
        return UUID.randomUUID().toString()+"_"+new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date())+".jpg";
    }

    public static void takeFromCamera(Activity context, int requestCode, String path, String photoName){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File tempFile = new File(path,photoName);
        Uri imgUri;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            imgUri = FileProvider.getUriForFile(context, "com.lots.travel.provider", tempFile);
        else
            imgUri  = Uri.fromFile(tempFile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        context.startActivityForResult(intent,requestCode);
    }

    public static void takeFromAlbum(Activity context,int requestCode,String title){
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
        }
        context.startActivityForResult(Intent.createChooser(intent, title), requestCode);
    }

    public static void clipPhoto(Activity context, int requestCode, String in, Uri out, int outputX, int outputY) {
        File tempFile = new File(in);
        Uri inUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            inUri = FileProvider.getUriForFile(context, "com.lots.travel.provider", tempFile);
        else
            inUri  = Uri.fromFile(tempFile);

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, out);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        context.startActivityForResult(intent, requestCode);
    }

}
