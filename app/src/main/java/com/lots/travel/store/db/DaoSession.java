package com.lots.travel.store.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.lots.travel.store.db.Account;
import com.lots.travel.store.db.Area;
import com.lots.travel.store.db.Food;
import com.lots.travel.store.db.Hotel;
import com.lots.travel.store.db.HttpInterface;
import com.lots.travel.store.db.Spot;
import com.lots.travel.store.db.TripPart;
import com.lots.travel.store.db.ViewCity;

import com.lots.travel.store.db.AccountDao;
import com.lots.travel.store.db.AreaDao;
import com.lots.travel.store.db.FoodDao;
import com.lots.travel.store.db.HotelDao;
import com.lots.travel.store.db.HttpInterfaceDao;
import com.lots.travel.store.db.SpotDao;
import com.lots.travel.store.db.TripPartDao;
import com.lots.travel.store.db.ViewCityDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig accountDaoConfig;
    private final DaoConfig areaDaoConfig;
    private final DaoConfig foodDaoConfig;
    private final DaoConfig hotelDaoConfig;
    private final DaoConfig httpInterfaceDaoConfig;
    private final DaoConfig spotDaoConfig;
    private final DaoConfig tripPartDaoConfig;
    private final DaoConfig viewCityDaoConfig;

    private final AccountDao accountDao;
    private final AreaDao areaDao;
    private final FoodDao foodDao;
    private final HotelDao hotelDao;
    private final HttpInterfaceDao httpInterfaceDao;
    private final SpotDao spotDao;
    private final TripPartDao tripPartDao;
    private final ViewCityDao viewCityDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        accountDaoConfig = daoConfigMap.get(AccountDao.class).clone();
        accountDaoConfig.initIdentityScope(type);

        areaDaoConfig = daoConfigMap.get(AreaDao.class).clone();
        areaDaoConfig.initIdentityScope(type);

        foodDaoConfig = daoConfigMap.get(FoodDao.class).clone();
        foodDaoConfig.initIdentityScope(type);

        hotelDaoConfig = daoConfigMap.get(HotelDao.class).clone();
        hotelDaoConfig.initIdentityScope(type);

        httpInterfaceDaoConfig = daoConfigMap.get(HttpInterfaceDao.class).clone();
        httpInterfaceDaoConfig.initIdentityScope(type);

        spotDaoConfig = daoConfigMap.get(SpotDao.class).clone();
        spotDaoConfig.initIdentityScope(type);

        tripPartDaoConfig = daoConfigMap.get(TripPartDao.class).clone();
        tripPartDaoConfig.initIdentityScope(type);

        viewCityDaoConfig = daoConfigMap.get(ViewCityDao.class).clone();
        viewCityDaoConfig.initIdentityScope(type);

        accountDao = new AccountDao(accountDaoConfig, this);
        areaDao = new AreaDao(areaDaoConfig, this);
        foodDao = new FoodDao(foodDaoConfig, this);
        hotelDao = new HotelDao(hotelDaoConfig, this);
        httpInterfaceDao = new HttpInterfaceDao(httpInterfaceDaoConfig, this);
        spotDao = new SpotDao(spotDaoConfig, this);
        tripPartDao = new TripPartDao(tripPartDaoConfig, this);
        viewCityDao = new ViewCityDao(viewCityDaoConfig, this);

        registerDao(Account.class, accountDao);
        registerDao(Area.class, areaDao);
        registerDao(Food.class, foodDao);
        registerDao(Hotel.class, hotelDao);
        registerDao(HttpInterface.class, httpInterfaceDao);
        registerDao(Spot.class, spotDao);
        registerDao(TripPart.class, tripPartDao);
        registerDao(ViewCity.class, viewCityDao);
    }
    
    public void clear() {
        accountDaoConfig.clearIdentityScope();
        areaDaoConfig.clearIdentityScope();
        foodDaoConfig.clearIdentityScope();
        hotelDaoConfig.clearIdentityScope();
        httpInterfaceDaoConfig.clearIdentityScope();
        spotDaoConfig.clearIdentityScope();
        tripPartDaoConfig.clearIdentityScope();
        viewCityDaoConfig.clearIdentityScope();
    }

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public AreaDao getAreaDao() {
        return areaDao;
    }

    public FoodDao getFoodDao() {
        return foodDao;
    }

    public HotelDao getHotelDao() {
        return hotelDao;
    }

    public HttpInterfaceDao getHttpInterfaceDao() {
        return httpInterfaceDao;
    }

    public SpotDao getSpotDao() {
        return spotDao;
    }

    public TripPartDao getTripPartDao() {
        return tripPartDao;
    }

    public ViewCityDao getViewCityDao() {
        return viewCityDao;
    }

}
