package com.lots.travel.store;

import android.os.Environment;
import android.util.Log;

import com.lots.travel.util.FileUtil;

import java.io.File;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * Created by nalanzi on 2017/9/7.
 */
//sd 卡
public class FileStore {

    private static final String ROOT = File.separator+"sosona";

    private static final String CACHE = File.separator+"cache";

    private File mCacheDir;

    public FileStore() {
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File external = Environment.getExternalStorageDirectory();

            File root = new File(external+ROOT);
            if(!root.exists())
                root.mkdir();

            mCacheDir = new File(root, CACHE);
            if(!mCacheDir.exists())
                mCacheDir.mkdir();
        }else
            Log.d(FileStore.class.getName(),"The sd card is not detected.");
    }

    public File getCacheDir(){
        return mCacheDir;
    }

    public File getCacheVoiceDir(){
        File file = new File(mCacheDir, "/voice");
        if(!file.exists())
            file.mkdir();
        return file;
    }

    public File getCacheVideoDir(){
        File file = new File(mCacheDir, "/video");
        if(!file.exists())
            file.mkdir();
        return file;
    }

    public File getCacheImageDir(){
        File file = new File(mCacheDir, "image");
        if(!file.exists())
            file.mkdir();
        return file;
    }

    public long getCacheSize(){
        return FileUtil.getDirSize(mCacheDir);
    }

}
