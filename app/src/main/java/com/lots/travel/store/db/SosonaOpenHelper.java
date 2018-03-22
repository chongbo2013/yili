package com.lots.travel.store.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.lots.travel.network.ObtainInterfaceFileTask;

import org.greenrobot.greendao.database.Database;

/**
 * Created by nalanzi on 2017/11/1.
 */

public class SosonaOpenHelper extends DaoMaster.DevOpenHelper {

    public SosonaOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);

        ObtainInterfaceFileTask.reset();
    }
}
