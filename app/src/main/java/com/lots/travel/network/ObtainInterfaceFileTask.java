package com.lots.travel.network;

import android.content.res.AssetManager;

import com.lots.travel.TravelApplication;
import com.lots.travel.store.SpStore;
import com.lots.travel.store.db.GreenDaoManager;
import com.lots.travel.store.db.HttpInterface;
import com.lots.travel.store.db.HttpInterfaceDao;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nalanzi on 2017/9/16.
 */
public class ObtainInterfaceFileTask {
    private static final String INTERFACE_VERSION = "interface_version";

    //暂时没有在app中从服务器获取，而是直接放在了assets目录
    //h5_share_domain没有action
    public void execute(){
        int currentVersion = SpStore.getInt(INTERFACE_VERSION,-1);
        if(currentVersion>0)
            return;

        //从assets
        Observable.create(new ObservableOnSubscribe<Void>(){

            @Override
            public void subscribe(@NonNull ObservableEmitter<Void> emitter) throws Exception {
                //读取http_interfaces.txt文件
                AssetManager assetManager = TravelApplication.getInstance().getAssets();

                InputStream in = null;
                BufferedReader reader = null;
                try{
                    in = assetManager.open("http_interfaces.json");
                    InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
                    reader = new BufferedReader(inputStreamReader);
                    char[] buffer = new char[1024];
                    int n;
                    StringBuilder strBuilder = new StringBuilder();
                    while ((n=reader.read(buffer))!=-1){
                        strBuilder.append(buffer,0,n);
                    }

                    JSONObject rootObj = new JSONObject(strBuilder.toString());
                    JSONObject dataObj = rootObj.getJSONObject("data");
                    Iterator<String> iterator = dataObj.keys();

                    List<HttpInterface> httpInterfaceList = new ArrayList<>();
                    while(iterator.hasNext()){
                        HttpInterface itemData = new HttpInterface();
                        String name =  iterator.next();
                        JSONObject itemObj = dataObj.getJSONObject(name);
                        itemData.setName(name);
                        try {
                            itemData.setAction(itemObj.getString("action"));
                        }catch (JSONException e){
                            itemData.setAction(null);
                        }
                        itemData.setControl(itemObj.getString("control"));
                        httpInterfaceList.add(itemData);
                    }

                    HttpInterfaceDao dao = GreenDaoManager.getInstance().getHttpInterfaceDao();
                    if(dao!=null){
                        try{
                            dao.insertOrReplaceInTx(httpInterfaceList);
                        }catch(Exception e){
                            String str = e.toString();
                        }
                    }

                    SpStore.set(INTERFACE_VERSION,56);
                }catch(IOException | JSONException e){
                    e.printStackTrace();
                } finally{
                    if(in!=null){
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(reader != null){
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }).subscribeOn(Schedulers.io()).subscribe();

    }

    public static void reset(){
        SpStore.set(ObtainInterfaceFileTask.INTERFACE_VERSION,-1);
    }

}
