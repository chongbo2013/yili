package com.lots.travel.schedule.base;

import android.util.LongSparseArray;

import com.lots.travel.schedule.model.TripDaysInfo;
import com.lots.travel.store.db.Food;
import com.lots.travel.store.db.FoodDao;
import com.lots.travel.store.db.GreenDaoManager;
import com.lots.travel.store.db.Hotel;
import com.lots.travel.store.db.HotelDao;
import com.lots.travel.store.db.Spot;
import com.lots.travel.store.db.SpotDao;
import com.lots.travel.store.db.TripPart;
import com.lots.travel.store.db.TripPartDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * FillManager管理的是实际数据，根据schedule(也就是游记、行程)的id读取其内部数据
 * 服务器按列表存储数据，除此外没有其它字段保证顺序。而实际数据部分不是列表存储的
 * 所以，这里是无法保证数据是按照时间而存储的。但是在数据初始加入还未编辑前可以根
 * 据TripPart的id保证数据的顺序。
 *
 * DataManager中存储的是内存数据，可以选择是否将数据提交到数据库
 */
public class DataManager {
    //schedule
    private Schedule mSchedule;
    //TripPart
    private LinkedHashMap<Long,TripPart> mTripMap;
    //TripPart可以带有的额外数据
    private LongSparseArray<Spot> mSpotArray;
    private LongSparseArray<Hotel> mHotelArray;
    private LongSparseArray<Food> mFoodArray;

    public static DataManager create(Schedule schedule){
        return new DataManager(schedule);
    }

    private DataManager(Schedule schedule){
        mSchedule = schedule;
        mTripMap = new LinkedHashMap<>();
        mSpotArray = new LongSparseArray<>();
        mHotelArray = new LongSparseArray<>();
        mFoodArray = new LongSparseArray<>();
        load();
    }

    private void load(){
        final String scheduleId = mSchedule.getId();
        //加载TripPart
        List<TripPart> parts = GreenDaoManager.getInstance()
                .getTripPartDao()
                .queryBuilder()
                .where(TripPartDao.Properties.ScheduleId.eq(scheduleId))
                .orderAsc(TripPartDao.Properties.Id)
                .list();
        if(parts!=null && parts.size()>0){
            for (int i=0;i<parts.size();i++){
                TripPart part = parts.get(i);
                mTripMap.put(part.getId(),part);
            }
        }

        //加载Spot
        List<Spot> spots = GreenDaoManager.getInstance()
                .getSpotDao()
                .queryBuilder()
                .where(SpotDao.Properties.ScheduleId.eq(scheduleId))
                .list();
        if(spots!=null && spots.size()>0){
            for (int i=0;i<spots.size();i++){
                Spot spot = spots.get(i);
                mSpotArray.put(spot.getViewId(),spot);
            }
        }

        //加载Hotel
        List<Hotel> hotels = GreenDaoManager.getInstance()
                .getHotelDao()
                .queryBuilder()
                .where(HotelDao.Properties.ScheduleId.eq(scheduleId))
                .list();
        if(hotels!=null && hotels.size()>0){
            for (int i=0;i<hotels.size();i++){
                Hotel hotel = hotels.get(i);
                mHotelArray.put(hotel.getId(),hotel);
            }
        }

        //加载Food
        List<Food> foods = GreenDaoManager.getInstance()
                .getFoodDao()
                .queryBuilder()
                .where(FoodDao.Properties.ScheduleId.eq(scheduleId))
                .list();
        if(foods!=null && foods.size()>0){
            for (int i=0;i<foods.size();i++){
                Food food = foods.get(i);
                mFoodArray.put(food.getId(),food);
            }
        }
    }

    public Schedule getSchedule(){
        return mSchedule;
    }

    public int getDayCount(){
        return mSchedule.getCount();
    }

    public void clearAll(){
        mTripMap.clear();
        mSpotArray.clear();
        mHotelArray.clear();
        mFoodArray.clear();
        //清除TripPart
        GreenDaoManager.getInstance().getTripPartDao().deleteAll();
        //清除Spot、Hotel、Food
        GreenDaoManager.getInstance().getSpotDao().detachAll();
        GreenDaoManager.getInstance().getHotelDao().detachAll();
        GreenDaoManager.getInstance().getFoodDao().detachAll();
    }

    //添加TripPart - desc类型
    public long addDesc(int day,boolean saved){
        long partId = TripPart.genId();
        TripPart part = new TripPart();
        part.setId(partId);
        part.setCityId(mSchedule.getCityTo());
        part.setDay(Integer.toString(day));
        part.setStyle(TripPart.STYLE_DESC);
        part.setScheduleId(mSchedule.getId());

        mTripMap.put(partId,part);
        if(saved){
            GreenDaoManager.getInstance().getTripPartDao().insertOrReplace(part);
        }
        return partId;
    }

    //添加TripPart - traffic类型
    public long addTraffic(int day,String mode,int hour,int minute,boolean saved){
        long partId = TripPart.genId();
        TripPart part = new TripPart();
        part.setId(partId);
        part.setScheduleId(mSchedule.getId());
        part.setCityId(mSchedule.getCityTo());
        part.setDay(Integer.toString(day));
        part.setStyle(TripPart.STYLE_TRAFFIC);
        part.setDataKey(null);
        part.setTransport(mode);
        part.setUseHour(Integer.toString(hour));
        part.setUseMinute(Integer.toString(minute));

        mTripMap.put(partId,part);
        if(saved){
            GreenDaoManager.getInstance().getTripPartDao().insertOrReplace(part);
        }
        return partId;
    }

    //添加TripPart - spot类型
    public long addSpot(int day,Spot spot,boolean saved){
        long partId = TripPart.genId();
        TripPart part = new TripPart();
        part.setId(partId);
        part.setScheduleId(mSchedule.getId());
        part.setCityId(mSchedule.getCityTo());
        part.setDay(Integer.toString(day));
        part.setStyle(TripPart.STYLE_SPOT);
        part.setDataKey(Long.toString(spot.getViewId()));

        spot.setScheduleId(mSchedule.getId());
        mSpotArray.put(spot.getViewId(),spot);
        GreenDaoManager.getInstance().getSpotDao().insertOrReplace(spot);

        mTripMap.put(partId,part);
        if(saved){
            GreenDaoManager.getInstance().getTripPartDao().insertOrReplace(part);
        }
        return partId;
    }

    //添加TripPart - hotel类型，酒店和
    public long addHotel(int day,Hotel hotel,boolean hotelIn,boolean saved){
        long partId = TripPart.genId();
        TripPart part = new TripPart();
        part.setId(partId);
        part.setScheduleId(mSchedule.getId());
        part.setCityId(mSchedule.getCityTo());
        part.setDay(Integer.toString(day));
        part.setStyle(TripPart.STYLE_HOTEL);
        part.setHotelIn(hotelIn ? "1":"0");
        part.setDataKey(Long.toString(hotel.getId()));

        hotel.setScheduleId(mSchedule.getId());
        mHotelArray.put(hotel.getId(),hotel);
        GreenDaoManager.getInstance().getHotelDao().insertOrReplace(hotel);

        mTripMap.put(partId,part);
        if(saved){
            GreenDaoManager.getInstance().getTripPartDao().insertOrReplace(part);
        }
        return partId;
    }

    //添加TripPart - food类型
    public long addFood(int day,Food food,boolean saved){
        long partId = TripPart.genId();
        TripPart part = new TripPart();
        part.setId(partId);
        part.setScheduleId(mSchedule.getId());
        part.setCityId(mSchedule.getCityTo());
        part.setDay(Integer.toString(day));
        part.setStyle(TripPart.STYLE_FOOD);
        part.setDataKey(Long.toString(food.getId()));

        food.setScheduleId(mSchedule.getId());
        mFoodArray.put(food.getId(),food);
        GreenDaoManager.getInstance().getFoodDao().insertOrReplace(food);

        mTripMap.put(partId,part);
        if(saved){
            GreenDaoManager.getInstance().getTripPartDao().insertOrReplace(part);
        }
        return partId;
    }

    //更新TripPart，如果只是内存操作，直接对part进行处理即可，不需要调用它
    public boolean update(TripPart part){
        if(part==null)
            return false;

        Long id = part.getId();
        if(id==null || id==-1)
            return false;

        GreenDaoManager.getInstance()
                .getTripPartDao()
                .update(part);
        return true;
    }

    //删除TripPart
    public void remove(long id,boolean saved){
        if(id==-1)
            return;
        TripPart part = mTripMap.remove(id);
        if(saved) {
            GreenDaoManager.getInstance().getTripPartDao().delete(part);
        }
    }

    public void remove(long[] ids,boolean saved){
        if(ids!=null && ids.length>0){
            for (long id:ids){
                remove(id,saved);
            }
        }
    }

    //获取TripPart
    public TripPart getItem(long id){
        return id < 0 ? null:mTripMap.get(id);
    }

    //获取所有item
    public List<TripPart> getItems(){
        List<TripPart> parts = new ArrayList<>();
        for (Map.Entry<Long,TripPart> entry:mTripMap.entrySet()){
            parts.add(entry.getValue());
        }
        Collections.sort(parts, new Comparator<TripPart>() {
            @Override
            public int compare(TripPart lhs, TripPart rhs) {
                if(lhs.getId()<rhs.getId())
                    return -1;
                return 1;
            }
        });
        return parts;
    }

    //获取Spot
    public Spot getSpot(long key){
        return key < 0 ? null:mSpotArray.get(key);
    }

    //获取Hotel
    public Hotel getHotel(long key){
        return key < 0 ? null:mHotelArray.get(key);
    }

    //获取Food
    public Food getFood(long key){
        return key < 0 ? null:mFoodArray.get(key);
    }

    //删除数据库中的spot、hotel in、food
    public void clearComponentsInDb(){
        //清除所有Spot、Food、HotelIn、Traffic
        List<TripPart> partList = GreenDaoManager.getInstance()
                .getTripPartDao()
                .queryBuilder()
                .whereOr(TripPartDao.Properties.Style.eq(TripPart.STYLE_SPOT),
                        TripPartDao.Properties.Style.eq(TripPart.STYLE_FOOD),
                        TripPartDao.Properties.Style.eq(TripPart.STYLE_TRAFFIC))
                .list();

        List<TripPart> hotelPartList = GreenDaoManager.getInstance()
                .getTripPartDao()
                .queryBuilder()
                .where(TripPartDao.Properties.Style.eq(TripPart.STYLE_HOTEL),TripPartDao.Properties.HotelIn.eq("0"))
                .list();

        partList.addAll(hotelPartList);

        GreenDaoManager.getInstance()
                .getTripPartDao()
                .deleteInTx(partList);
    }

    //将TripPart存储至数据库
    public void saveToDb(List<TripPart> tripParts){
        GreenDaoManager.getInstance()
                .getTripPartDao()
                .insertInTx(tripParts);
    }

    public static Schedule createSchedule(TripDaysInfo tripInfo){
        //清除数据库
        GreenDaoManager.getInstance().getTripPartDao().deleteAll();
        GreenDaoManager.getInstance().getSpotDao().deleteAll();
        GreenDaoManager.getInstance().getHotelDao().deleteAll();
        GreenDaoManager.getInstance().getFoodDao().deleteAll();
        //创建Schedule
        String id = tripInfo.getBaseId();
        TripDaysInfo.BaseInfo baseInfo = tripInfo.getBaseInfo();
        Schedule schedule = new Schedule();
        schedule.setId(tripInfo.getBaseId());
        schedule.setCount(tripInfo.getDays());
        schedule.setStartTime(baseInfo.getCreattime());
        schedule.setCityTo(baseInfo.getCityTo(),baseInfo.getCityToName());
        schedule.setCityToImg(baseInfo.getImg());
        schedule.setTravelTags(baseInfo.getTravelTag());
        schedule.setTravelNeeds(baseInfo.getTravelNeed());
        //获取每天的信息
        List<TripDaysInfo.DayInfo> daysInfo = tripInfo.getDayInfos();
        for (int i=0;i<daysInfo.size();i++){
            TripDaysInfo.DayInfo dayInfo = daysInfo.get(i);
            //获取该天的相关信息
            List<TripDaysInfo.TripPartInfo> partsInfo = dayInfo.getInfo();
            for (int j=0;j<partsInfo.size();j++){
                TripDaysInfo.TripPartInfo partInfo = partsInfo.get(j);
                TripPart tripPart = TripPart.create(partInfo);
                //id是递增的，保证了读取时的顺序
                tripPart.setId(TripPart.genId());
                tripPart.setScheduleId(id);
                //为spot、hotel、food类型，但是ComponentInfo为空，直接丢掉 - 因为此处是loadAll，显然是服务器存储的数据出错了
                TripDaysInfo.ComponentInfo componentInfo = partInfo.getInfo();
                String infoStyle = partInfo.getStyle();
                if((infoStyle.equals(TripPart.STYLE_SPOT)
                        || infoStyle.equals(TripPart.STYLE_HOTEL)
                        || infoStyle.equals(TripPart.STYLE_FOOD))
                        && componentInfo==null){
                    continue;
                }
                //将TripPart存储至数据库
                GreenDaoManager.getInstance().getTripPartDao().insertOrReplace(tripPart);
                //如果携带额外数据，将其存储至数据库
                switch (infoStyle){
                    case TripPart.STYLE_SPOT:
                        Spot spot = Spot.create(componentInfo);
                        spot.setScheduleId(id);
                        GreenDaoManager.getInstance().getSpotDao().insertOrReplace(spot);
                        break;

                    case TripPart.STYLE_HOTEL:
                        Hotel hotel = Hotel.create(componentInfo);
                        hotel.setScheduleId(id);
                        GreenDaoManager.getInstance().getHotelDao().insertOrReplace(hotel);
                        break;

                    case TripPart.STYLE_FOOD:
                        Food food = Food.create(componentInfo);
                        food.setScheduleId(id);
                        GreenDaoManager.getInstance().getFoodDao().insertOrReplace(food);
                        break;
                }
            }
        }
        return schedule;
    }

}
