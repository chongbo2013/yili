package com.lots.travel.store.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "HTTP_INTERFACE".
*/
public class HttpInterfaceDao extends AbstractDao<HttpInterface, String> {

    public static final String TABLENAME = "HTTP_INTERFACE";

    /**
     * Properties of entity HttpInterface.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Name = new Property(0, String.class, "name", true, "NAME");
        public final static Property Action = new Property(1, String.class, "action", false, "ACTION");
        public final static Property Control = new Property(2, String.class, "control", false, "CONTROL");
    }


    public HttpInterfaceDao(DaoConfig config) {
        super(config);
    }
    
    public HttpInterfaceDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"HTTP_INTERFACE\" (" + //
                "\"NAME\" TEXT PRIMARY KEY NOT NULL ," + // 0: name
                "\"ACTION\" TEXT," + // 1: action
                "\"CONTROL\" TEXT);"); // 2: control
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"HTTP_INTERFACE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, HttpInterface entity) {
        stmt.clearBindings();
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(1, name);
        }
 
        String action = entity.getAction();
        if (action != null) {
            stmt.bindString(2, action);
        }
 
        String control = entity.getControl();
        if (control != null) {
            stmt.bindString(3, control);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, HttpInterface entity) {
        stmt.clearBindings();
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(1, name);
        }
 
        String action = entity.getAction();
        if (action != null) {
            stmt.bindString(2, action);
        }
 
        String control = entity.getControl();
        if (control != null) {
            stmt.bindString(3, control);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public HttpInterface readEntity(Cursor cursor, int offset) {
        HttpInterface entity = new HttpInterface( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // name
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // action
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // control
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, HttpInterface entity, int offset) {
        entity.setName(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setAction(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setControl(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final String updateKeyAfterInsert(HttpInterface entity, long rowId) {
        return entity.getName();
    }
    
    @Override
    public String getKey(HttpInterface entity) {
        if(entity != null) {
            return entity.getName();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(HttpInterface entity) {
        return entity.getName() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
