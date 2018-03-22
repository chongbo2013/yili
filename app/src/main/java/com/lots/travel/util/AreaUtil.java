package com.lots.travel.util;

import android.content.Context;

import com.lots.travel.store.SpStore;
import com.lots.travel.store.db.Area;
import com.lots.travel.store.db.AreaDao;
import com.lots.travel.store.db.GreenDaoManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AreaUtil {
    public static final String SAVE_TO_DB = "saveToDB";

    public static final int PID_PROVINCE = 10;

    public static void check(final Context context){
        if(SpStore.getBoolean(SAVE_TO_DB,false))
            return;

        Single.create(
                new SingleOnSubscribe<Void>() {
                    @Override
                    public void subscribe(@NonNull SingleEmitter<Void> e) throws Exception {
                        StringBuilder stringBuilder = new StringBuilder();
                        InputStream in = null;
                        InputStreamReader reader = null;
                        BufferedReader bf = null;
                        try {
                            in = context.getAssets().open("area.json");
                            reader = new InputStreamReader(in);
                            bf = new BufferedReader(reader);
                            char[] buffer = new char[1024];
                            int n;
                            while ((n=bf.read(buffer)) != -1)
                                stringBuilder.append(buffer,0,n);
                            List<Area> areaList = GsonUtil.list(stringBuilder.toString(), Area.class);
                            GreenDaoManager.getInstance().getAreaDao().detachAll();
                            GreenDaoManager.getInstance().getAreaDao().insertInTx(areaList);
                            SpStore.set(SAVE_TO_DB,true);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }finally {
                            if(bf!=null) {
                                try {
                                    bf.close();
                                }catch (IOException ex){
                                    ex.printStackTrace();
                                }
                            }
                            if(reader!=null) {
                                try {
                                    reader.close();
                                }catch (IOException ex){
                                    ex.printStackTrace();
                                }
                            }
                            if(in!=null) {
                                try {
                                    in.close();
                                }catch (IOException ex){
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public static List<Area> query(int pid){
        return GreenDaoManager.getInstance()
                .getAreaDao()
                .queryBuilder()
                .where(AreaDao.Properties.Pid.eq(pid))
                .list();
    }

}
