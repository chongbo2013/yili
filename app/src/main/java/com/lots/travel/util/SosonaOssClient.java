package com.lots.travel.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.lots.travel.AccountManager;
import com.lots.travel.store.FileStore;
import com.lots.travel.store.db.Account;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用AK鉴权方式
 */
public class SosonaOssClient {
    private static final String OSS_ACCESS_ID = "LTAI09LjJ7QGwdOm";

    private static final String OSS_ACCESS_SECRET = "gJy4W3dqeut4OCI9lk62xjCH5pYyuN";

    private static final String OSS_ENDPOINT = "oss-cn-qingdao.aliyuncs.com";

    private static final String BUCKET_CHINA_AUDIO = "naan";
    private static final String BUCKET_CHINA_IMAGE = "naan";
    private static final String BUCKET_OVERSEA_AUDIO = "naan";
    private static final String BUCKET_OVERSEA_IMAGE = "naan";

    private static final String FOLDER_DEVELOP = "develop";
    private static final String FOLDER_DISTRIBUTE = "distribute";

    private static final int RES_TYPE_NULL = 0;
    private static final int RES_TYPE_IMAGE = 1;
    private static final int RES_TYPE_VIDEO = 2;
    private static final int RES_TYPE_VOICE = 3;

    private static final String[] RES_SUFFIX = {".png",".PNG","jpg",".JPG",".jpeg",".JPEG","mp4","MP4",".amr",".AMR",".mp3",".MP3"};
    private static final int[] RES_TYPES = {1,1,1,1,1,1,2,2,3,3,3,3};

    //用户头像
    public static final String ITEM_TYPE_ICON = "icon";
    //行程、游记
    public static final String ITEM_TYPE_PRODUCT = "product";
    //足迹
    public static final String ITEM_TYPE_FOOTPRINT = "footprint";
    //招募
    public static final String ITEM_TYPE_RECRUIT = "recruit";
    //评论
    public static final String ITEM_TYPE_COMMENT = "comment";
    //直播
    public static final String ITEM_TYPE_LIVE = "live";
    //实名认证
    public static final String ITEM_TYPE_REAL = "real";
    //凭证
    public static final String ITEM_TYPE_VOUCHER = "voucher";
    //默认
    public static final String ITEM_TYPE_DEFAULT = "default";

    private OSS oss;
    private boolean inDevelop;
    private boolean inChina;
    private String userId;
    //filename、lastModified，如果filename存在且lastModified未变化就表明已经上传
    private SparseArray<Long> uploadCache;
    private SparseArray<String> urlCache;

    public SosonaOssClient(Context context) {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(OSS_ACCESS_ID, OSS_ACCESS_SECRET);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000);
        conf.setSocketTimeout(15 * 1000);
        conf.setMaxConcurrentRequest(5);
        conf.setMaxErrorRetry(2);
        OSSLog.enableLog();

        oss = new OSSClient(context.getApplicationContext(), OSS_ENDPOINT, credentialProvider, conf);

        inDevelop = true;

        Account account = AccountManager.getInstance().getAccount();
        String country = account.getCountry();
        if (TextUtils.isEmpty(country))
            country = LocateUtil.getCountry();
        inChina = TextUtils.isEmpty(country) || "China".equals(country) || "中国".equals(country);

        userId = Long.toString(account.getUserId());

        uploadCache = new SparseArray<>();
        urlCache = new SparseArray<>();
    }

    public String uploadImage(Context context,String type,String path,boolean compress){
        if(isNetworkResource(path))
            return path;

        //如果是uri，转换为路径
        //如果文件不存在，不上传
        File uploadFile = null;
        if(path.startsWith("file://")
                ||path.startsWith("content://")){
            String cPath = FileUtil.getFilepath(context,Uri.parse(path));
            if(cPath!=null)
                uploadFile = new File(cPath);
        }else{
            uploadFile = new File(path);
        }
        if(uploadFile==null || !uploadFile.exists()){
            Log.e(SosonaOssClient.class.getName(),path+",文件不存在");
            return null;
        }

        //压缩图片
        if(compress) {
            path = compressImage(uploadFile.getAbsolutePath());
        }
        //上传
        return uploadImage(type,path,new File(path).getName());
    }

    public String uploadVideo(Context context,String type,String path){
        if(isNetworkResource(path))
            return path;

        //如果是uri，转换为路径
        //如果文件不存在，不上传
        File uploadFile = null;
        if(path.startsWith("file://")
                ||path.startsWith("content://")){
            String cPath = FileUtil.getFilepath(context,Uri.parse(path));
            if(cPath!=null)
                uploadFile = new File(cPath);
        }else{
            uploadFile = new File(path);
        }
        if(uploadFile==null || !uploadFile.exists()){
            Log.e(SosonaOssClient.class.getName(),path+",文件不存在");
            return null;
        }

        return uploadAudio(type,uploadFile.getAbsolutePath(),uploadFile.getName());
    }

    //批量上传 - 根据key来获取上传后的url
    public List<SosonaOssClient.Item> upload(Context context,List<Item> itemList,SparseArray<String> urlMap){
        int key;
        List<SosonaOssClient.Item> failedItems = new ArrayList<>();

        if(itemList==null || itemList.size()==0){
            return failedItems;
        }

        for (int i=0;i<itemList.size();i++){
            SosonaOssClient.Item item = itemList.get(i);
            //如果为空，不处理
            if(TextUtils.isEmpty(item.path))
                continue;
            //如果为http、https，不需要上传
            if(isNetworkResource(item.path)){
                if(urlMap!=null)
                    urlMap.put(item.path.hashCode(),item.path);
                continue;
            }
            int type = getResourceType(item.path);
            //如果资源类型未知，不处理
            if(type==RES_TYPE_NULL)
                continue;
            //如果是uri，转换为路径
            //如果文件不存在，不上传
            File uploadFile = null;
            if(item.path.startsWith("file://")
                    ||item.path.startsWith("content://")){
                String cPath = FileUtil.getFilepath(context,Uri.parse(item.path));
                if(cPath!=null)
                    uploadFile = new File(cPath);
            }else{
                uploadFile = new File(item.path);
            }
            if(uploadFile==null || !uploadFile.exists()){
                Log.e(SosonaOssClient.class.getName(),item.path+",文件不存在");
                continue;
            }

            //计算资源key
            key = item.path.hashCode();

            //在缓存中，表明其已经上传过了，不需要再次上传
            Long lastModified = uploadCache.get(key);
            if(lastModified!=null && lastModified==uploadFile.lastModified()){
                String url = urlCache.get(key);
                if(!TextUtils.isEmpty(url)){
                    if(urlMap!=null)
                        urlMap.put(key,url);
                    continue;
                }
            }

            String url;
            switch (type){
                case RES_TYPE_IMAGE:
                    //压缩图片
                    item.path = compressImage(uploadFile.getAbsolutePath());
                    //上传
                    url = uploadImage(item.type,item.path,new File(item.path).getName());
                    if(url==null){
                        failedItems.add(item);
                    }else{
                        uploadCache.put(key,uploadFile.lastModified());
                        urlCache.put(key,url);
                        if(urlMap!=null)
                            urlMap.put(key,url);
                    }
                    break;

                case RES_TYPE_VIDEO:
                    url = uploadAudio(item.type,uploadFile.getAbsolutePath(),uploadFile.getName());
                    if(url==null){
                        failedItems.add(item);
                    }else{
                        uploadCache.put(key,uploadFile.lastModified());
                        urlCache.put(key,url);
                        if(urlMap!=null)
                            urlMap.put(key,url);
                    }
                    break;

                case RES_TYPE_VOICE:
                    //amr转mp3
                    item.path = transAmrToMp3(uploadFile.getAbsolutePath());
                    url = uploadAudio(item.type,item.path,new File(item.path).getName());
                    if(url==null){
                        failedItems.add(item);
                    }else{
                        uploadCache.put(key,uploadFile.lastModified());
                        urlCache.put(key,url);
                        if(urlMap!=null)
                            urlMap.put(key,url);
                    }
                    break;
            }
        }

        return failedItems;
    }

    //是否为网络资源，网络资源显然不需要上传
    private boolean isNetworkResource(String filepath){
        return (filepath.startsWith("http")
                || filepath.startsWith("HTTP")
                || filepath.startsWith("https")
                || filepath.startsWith("HTTPS"));
    }

    private int getResourceType(String filepath){
        for (int i=0;i<RES_SUFFIX.length;i++){
            if(filepath.endsWith(RES_SUFFIX[i]))
                return RES_TYPES[i];
        }
        return RES_TYPE_NULL;
    }

    private String compressImage(String filepath){
        //压缩图片
        Bitmap bitmap = FileUtil.compress(filepath);
        //不需要压缩则不处理；经过压缩则将其缓存至临时文件夹
        if(bitmap!=null){
            File file = new File(filepath);
            String dstPath = new FileStore().getCacheImageDir()+File.separator+"compressed_"+file.getName();
            File dstFile = new File(dstPath);
            if(FileUtil.saveBitmap(dstFile,bitmap)){
                return dstPath;
            }else if(dstFile.exists()){
                dstFile.delete();
            }
        }
        return filepath;
    }

    private String uploadImage(String itemType,String filepath,String filename){
        String bucket = inChina ? BUCKET_CHINA_IMAGE : BUCKET_OVERSEA_IMAGE;
        //开发环境/用户id/功能名/时间戳(非必须)/文件名
        String key = (inDevelop ? FOLDER_DEVELOP:FOLDER_DISTRIBUTE)+File.separator+userId+File.separator+itemType+File.separator+filename;
        if(uploadFile(bucket,key,filepath))
            return oss.presignPublicObjectURL(bucket,key);
        return null;
    }

    private String transAmrToMp3(String filepath){
        return filepath;
    }

    private String uploadAudio(String itemType,String filepath,String filename){
        String bucket = inChina ? BUCKET_CHINA_AUDIO : BUCKET_OVERSEA_AUDIO;
        //开发环境/用户id/功能名/时间戳(非必须)/文件名
        String key = (inDevelop ? FOLDER_DEVELOP:FOLDER_DISTRIBUTE)+File.separator+userId+File.separator+itemType+File.separator+filename;
        if(uploadFile(bucket,key,filepath))
            return oss.presignPublicObjectURL(bucket,key);
        return null;
    }

    private boolean uploadFile(String bucket,String name,String filepath) {
        PutObjectRequest put = new PutObjectRequest(bucket,name,filepath);
        try {
            oss.putObject(put);
        } catch (ClientException | ServiceException e) {
            Log.e(SosonaOssClient.class.getName(),"uploadFile失败"+filepath);
            return false;
        }
        return true;
    }

    public static class Item{
        //功能分类：icon、product等等
        String type;
        String path;

        public Item(String type,String path){
            this.type = type;
            this.path = path;
        }
    }

}
