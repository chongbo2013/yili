package com.lots.travel.store.db;

import android.content.Context;

import com.lots.travel.R;

/**
 * Created by nalanzi on 2017/9/16.
 */

public class GreenDaoManager {
    private static volatile GreenDaoManager mInstance;

    private GreenDaoManager() {}

    public static GreenDaoManager getInstance() {
        if (mInstance == null) {
            synchronized (GreenDaoManager.class) {
                if (mInstance == null) {
                    mInstance = new GreenDaoManager();
                }
            }
        }
        return mInstance;
    }

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    public void init(Context context) {
        DaoMaster.DevOpenHelper helper = new SosonaOpenHelper(context, "sosona", null);
        mDaoMaster = new DaoMaster(helper.getWritableDb());
        mDaoSession = mDaoMaster.newSession();
    }

    public void release() {
        if (mDaoMaster != null) {
            mDaoMaster.getDatabase().close();
        }

        if (mDaoSession != null) {
            mDaoSession.clear();
        }
    }

    public HttpInterfaceDao getHttpInterfaceDao() {
        return mDaoSession == null ? null : mDaoSession.getHttpInterfaceDao();
    }

    public AccountDao getAccountDao() {
        return mDaoSession == null ? null : mDaoSession.getAccountDao();
    }

    public ViewCityDao getViewCityDao(){
        return mDaoSession == null ? null : mDaoSession.getViewCityDao();
    }

    public TripPartDao getTripPartDao(){
        return mDaoSession == null ? null : mDaoSession.getTripPartDao();
    }

    public FoodDao getFoodDao(){
        return mDaoSession == null ? null : mDaoSession.getFoodDao();
    }

    public SpotDao getSpotDao(){
        return mDaoSession == null ? null : mDaoSession.getSpotDao();
    }

    public HotelDao getHotelDao(){
        return mDaoSession == null ? null : mDaoSession.getHotelDao();
    }

    public AreaDao getAreaDao(){
        return mDaoSession == null ? null : mDaoSession.getAreaDao();
    }

    DaoSession getSession() {
        return mDaoSession;
    }

    DaoSession getNewSession() {
        if (mDaoMaster == null) {
            return null;
        }
        if (mDaoSession != null) {
            mDaoSession.clear();
        }
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }

}
