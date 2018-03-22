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
 * DAO for table "ACCOUNT".
*/
public class AccountDao extends AbstractDao<Account, Long> {

    public static final String TABLENAME = "ACCOUNT";

    /**
     * Properties of entity Account.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property UserId = new Property(0, Long.class, "userId", true, "_id");
        public final static Property Secret = new Property(1, String.class, "secret", false, "SECRET");
        public final static Property Realname = new Property(2, String.class, "realname", false, "REALNAME");
        public final static Property AuthStatus = new Property(3, String.class, "authStatus", false, "AUTH_STATUS");
        public final static Property Interests = new Property(4, String.class, "interests", false, "INTERESTS");
        public final static Property MobilePhone = new Property(5, String.class, "mobilePhone", false, "MOBILE_PHONE");
        public final static Property PortraitUrl = new Property(6, String.class, "portraitUrl", false, "PORTRAIT_URL");
        public final static Property Username = new Property(7, String.class, "username", false, "USERNAME");
        public final static Property Sex = new Property(8, String.class, "sex", false, "SEX");
        public final static Property Birthday = new Property(9, String.class, "birthday", false, "BIRTHDAY");
        public final static Property Emotion = new Property(10, String.class, "emotion", false, "EMOTION");
        public final static Property Height = new Property(11, int.class, "height", false, "HEIGHT");
        public final static Property Weight = new Property(12, int.class, "weight", false, "WEIGHT");
        public final static Property Company = new Property(13, String.class, "company", false, "COMPANY");
        public final static Property Profession = new Property(14, String.class, "profession", false, "PROFESSION");
        public final static Property School = new Property(15, String.class, "school", false, "SCHOOL");
        public final static Property Department = new Property(16, String.class, "department", false, "DEPARTMENT");
        public final static Property Country = new Property(17, String.class, "country", false, "COUNTRY");
        public final static Property Province = new Property(18, String.class, "province", false, "PROVINCE");
        public final static Property City = new Property(19, String.class, "city", false, "CITY");
        public final static Property District = new Property(20, String.class, "district", false, "DISTRICT");
        public final static Property Mail = new Property(21, String.class, "mail", false, "MAIL");
        public final static Property Qq = new Property(22, String.class, "qq", false, "QQ");
        public final static Property Tags = new Property(23, String.class, "tags", false, "TAGS");
        public final static Property Follow = new Property(24, int.class, "follow", false, "FOLLOW");
        public final static Property Fans = new Property(25, int.class, "fans", false, "FANS");
        public final static Property WxUser = new Property(26, boolean.class, "wxUser", false, "WX_USER");
    }


    public AccountDao(DaoConfig config) {
        super(config);
    }
    
    public AccountDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ACCOUNT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: userId
                "\"SECRET\" TEXT," + // 1: secret
                "\"REALNAME\" TEXT," + // 2: realname
                "\"AUTH_STATUS\" TEXT," + // 3: authStatus
                "\"INTERESTS\" TEXT," + // 4: interests
                "\"MOBILE_PHONE\" TEXT," + // 5: mobilePhone
                "\"PORTRAIT_URL\" TEXT," + // 6: portraitUrl
                "\"USERNAME\" TEXT," + // 7: username
                "\"SEX\" TEXT," + // 8: sex
                "\"BIRTHDAY\" TEXT," + // 9: birthday
                "\"EMOTION\" TEXT," + // 10: emotion
                "\"HEIGHT\" INTEGER NOT NULL ," + // 11: height
                "\"WEIGHT\" INTEGER NOT NULL ," + // 12: weight
                "\"COMPANY\" TEXT," + // 13: company
                "\"PROFESSION\" TEXT," + // 14: profession
                "\"SCHOOL\" TEXT," + // 15: school
                "\"DEPARTMENT\" TEXT," + // 16: department
                "\"COUNTRY\" TEXT," + // 17: country
                "\"PROVINCE\" TEXT," + // 18: province
                "\"CITY\" TEXT," + // 19: city
                "\"DISTRICT\" TEXT," + // 20: district
                "\"MAIL\" TEXT," + // 21: mail
                "\"QQ\" TEXT," + // 22: qq
                "\"TAGS\" TEXT," + // 23: tags
                "\"FOLLOW\" INTEGER NOT NULL ," + // 24: follow
                "\"FANS\" INTEGER NOT NULL ," + // 25: fans
                "\"WX_USER\" INTEGER NOT NULL );"); // 26: wxUser
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ACCOUNT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Account entity) {
        stmt.clearBindings();
 
        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(1, userId);
        }
 
        String secret = entity.getSecret();
        if (secret != null) {
            stmt.bindString(2, secret);
        }
 
        String realname = entity.getRealname();
        if (realname != null) {
            stmt.bindString(3, realname);
        }
 
        String authStatus = entity.getAuthStatus();
        if (authStatus != null) {
            stmt.bindString(4, authStatus);
        }
 
        String interests = entity.getInterests();
        if (interests != null) {
            stmt.bindString(5, interests);
        }
 
        String mobilePhone = entity.getMobilePhone();
        if (mobilePhone != null) {
            stmt.bindString(6, mobilePhone);
        }
 
        String portraitUrl = entity.getPortraitUrl();
        if (portraitUrl != null) {
            stmt.bindString(7, portraitUrl);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(8, username);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(9, sex);
        }
 
        String birthday = entity.getBirthday();
        if (birthday != null) {
            stmt.bindString(10, birthday);
        }
 
        String emotion = entity.getEmotion();
        if (emotion != null) {
            stmt.bindString(11, emotion);
        }
        stmt.bindLong(12, entity.getHeight());
        stmt.bindLong(13, entity.getWeight());
 
        String company = entity.getCompany();
        if (company != null) {
            stmt.bindString(14, company);
        }
 
        String profession = entity.getProfession();
        if (profession != null) {
            stmt.bindString(15, profession);
        }
 
        String school = entity.getSchool();
        if (school != null) {
            stmt.bindString(16, school);
        }
 
        String department = entity.getDepartment();
        if (department != null) {
            stmt.bindString(17, department);
        }
 
        String country = entity.getCountry();
        if (country != null) {
            stmt.bindString(18, country);
        }
 
        String province = entity.getProvince();
        if (province != null) {
            stmt.bindString(19, province);
        }
 
        String city = entity.getCity();
        if (city != null) {
            stmt.bindString(20, city);
        }
 
        String district = entity.getDistrict();
        if (district != null) {
            stmt.bindString(21, district);
        }
 
        String mail = entity.getMail();
        if (mail != null) {
            stmt.bindString(22, mail);
        }
 
        String qq = entity.getQq();
        if (qq != null) {
            stmt.bindString(23, qq);
        }
 
        String tags = entity.getTags();
        if (tags != null) {
            stmt.bindString(24, tags);
        }
        stmt.bindLong(25, entity.getFollow());
        stmt.bindLong(26, entity.getFans());
        stmt.bindLong(27, entity.getWxUser() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Account entity) {
        stmt.clearBindings();
 
        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(1, userId);
        }
 
        String secret = entity.getSecret();
        if (secret != null) {
            stmt.bindString(2, secret);
        }
 
        String realname = entity.getRealname();
        if (realname != null) {
            stmt.bindString(3, realname);
        }
 
        String authStatus = entity.getAuthStatus();
        if (authStatus != null) {
            stmt.bindString(4, authStatus);
        }
 
        String interests = entity.getInterests();
        if (interests != null) {
            stmt.bindString(5, interests);
        }
 
        String mobilePhone = entity.getMobilePhone();
        if (mobilePhone != null) {
            stmt.bindString(6, mobilePhone);
        }
 
        String portraitUrl = entity.getPortraitUrl();
        if (portraitUrl != null) {
            stmt.bindString(7, portraitUrl);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(8, username);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(9, sex);
        }
 
        String birthday = entity.getBirthday();
        if (birthday != null) {
            stmt.bindString(10, birthday);
        }
 
        String emotion = entity.getEmotion();
        if (emotion != null) {
            stmt.bindString(11, emotion);
        }
        stmt.bindLong(12, entity.getHeight());
        stmt.bindLong(13, entity.getWeight());
 
        String company = entity.getCompany();
        if (company != null) {
            stmt.bindString(14, company);
        }
 
        String profession = entity.getProfession();
        if (profession != null) {
            stmt.bindString(15, profession);
        }
 
        String school = entity.getSchool();
        if (school != null) {
            stmt.bindString(16, school);
        }
 
        String department = entity.getDepartment();
        if (department != null) {
            stmt.bindString(17, department);
        }
 
        String country = entity.getCountry();
        if (country != null) {
            stmt.bindString(18, country);
        }
 
        String province = entity.getProvince();
        if (province != null) {
            stmt.bindString(19, province);
        }
 
        String city = entity.getCity();
        if (city != null) {
            stmt.bindString(20, city);
        }
 
        String district = entity.getDistrict();
        if (district != null) {
            stmt.bindString(21, district);
        }
 
        String mail = entity.getMail();
        if (mail != null) {
            stmt.bindString(22, mail);
        }
 
        String qq = entity.getQq();
        if (qq != null) {
            stmt.bindString(23, qq);
        }
 
        String tags = entity.getTags();
        if (tags != null) {
            stmt.bindString(24, tags);
        }
        stmt.bindLong(25, entity.getFollow());
        stmt.bindLong(26, entity.getFans());
        stmt.bindLong(27, entity.getWxUser() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Account readEntity(Cursor cursor, int offset) {
        Account entity = new Account( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // userId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // secret
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // realname
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // authStatus
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // interests
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // mobilePhone
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // portraitUrl
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // username
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // sex
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // birthday
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // emotion
            cursor.getInt(offset + 11), // height
            cursor.getInt(offset + 12), // weight
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // company
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // profession
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // school
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // department
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // country
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // province
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // city
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // district
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // mail
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // qq
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // tags
            cursor.getInt(offset + 24), // follow
            cursor.getInt(offset + 25), // fans
            cursor.getShort(offset + 26) != 0 // wxUser
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Account entity, int offset) {
        entity.setUserId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSecret(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setRealname(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAuthStatus(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setInterests(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setMobilePhone(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPortraitUrl(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setUsername(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setSex(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setBirthday(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setEmotion(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setHeight(cursor.getInt(offset + 11));
        entity.setWeight(cursor.getInt(offset + 12));
        entity.setCompany(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setProfession(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setSchool(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setDepartment(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setCountry(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setProvince(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setCity(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setDistrict(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setMail(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setQq(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setTags(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setFollow(cursor.getInt(offset + 24));
        entity.setFans(cursor.getInt(offset + 25));
        entity.setWxUser(cursor.getShort(offset + 26) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Account entity, long rowId) {
        entity.setUserId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Account entity) {
        if(entity != null) {
            return entity.getUserId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Account entity) {
        return entity.getUserId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
