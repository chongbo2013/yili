package com.lots.travel.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by nalanzi on 2017/10/30.
 */

public class FileUtil {
    private static final int MAX_IMAGE_SIZE = 1280;

    public static Bitmap compress(String srcPath){
        return compress(srcPath,60,60);
    }

    //压缩图片，如果图片小于minSize就放大
    //不需要compress则返回null
    public static Bitmap compress(String srcPath,int minWidth,int minHeight){
        Bitmap ret = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(srcPath, options);
        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;
        if (srcHeight == 0 || srcWidth == 0)
            return null;

        int dstWidth, dstHeight;
        int size = Math.max(srcHeight, srcWidth);
        if (size < MAX_IMAGE_SIZE) {
            if(srcHeight<minHeight && srcWidth<minHeight
                    && minHeight<MAX_IMAGE_SIZE && minWidth<MAX_IMAGE_SIZE){
                //获取bitmap
                options.inJustDecodeBounds = false;
                options.inSampleSize = 1;
                Bitmap bitmap = BitmapFactory.decodeFile(srcPath, options);

                Matrix matrix = new Matrix();
                float scaleWidth = ((float) minWidth / srcWidth);
                float scaleHeight = ((float) minHeight / srcHeight);
                matrix.postScale(scaleWidth, scaleHeight);
                ret = Bitmap.createBitmap(bitmap, 0, 0, srcWidth, srcHeight, matrix, true);
                bitmap.recycle();
            }
            return ret;
        }

        options.inJustDecodeBounds = false;
        if (srcWidth > srcHeight) {
            dstWidth = MAX_IMAGE_SIZE;
            options.inSampleSize = srcWidth / dstWidth;
        } else {
            dstHeight = MAX_IMAGE_SIZE;
            options.inSampleSize = srcHeight / dstHeight;
        }

        ret = BitmapFactory.decodeFile(srcPath, options);
        return ret;
    }

    public static boolean saveBitmap(File file,Bitmap bitmap){
        if (bitmap == null)
            return false;

        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (!bitmap.isRecycled())
                bitmap.recycle();
            if (out != null)
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return true;
    }

    /**
     * 通过Uri获取文件路径
     * 支持图片媒体,文件等
     * <p/>
     * Author qiujuer@live.cn
     *
     * @param uri     Uri
     * @param context Context
     * @return 文件路径
     */
    @SuppressLint({"NewApi"})
    public static String getFilepath(Context context, Uri uri) {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            String authority = uri.getAuthority();
            if ("com.android.externalstorage.documents".equals(authority)) {
                // isExternalStorageDocument
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if ("com.android.providers.downloads.documents".equals(authority)) {
                // isDownloadsDocument
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if ("com.android.providers.media.documents".equals(authority)) {
                // isMediaDocument
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                if (cursor != null) {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (cursor.moveToFirst()) {
                        return cursor.getString(column_index);
                    }
                }
            } catch (Exception e) {
                e.fillInStackTrace();
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static void deleteFilesByPaths(String... paths){
        if(paths!=null && paths.length>0){
            for (String path:paths){
                File file = new File(path);
                if(file.exists())
                    file.delete();
            }
        }
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
     *
     * @param directory
     */
    public static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File child : directory.listFiles()) {
                if (child.isDirectory()) {
                    deleteFilesByDirectory(child);
                }
                child.delete();
            }
        }
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return B/KB/MB/GB
     */
    public static String formatFileSize(long fileS) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 获取目录文件大小
     *
     * @param dir
     * @return
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        if (files != null) {

            for (File file : files) {
                if (file.isFile()) {
                    dirSize += file.length();
                } else if (file.isDirectory()) {
                    dirSize += file.length();
                    dirSize += getDirSize(file); // 递归调用继续统计
                }
            }
        }
        return dirSize;
    }

}
