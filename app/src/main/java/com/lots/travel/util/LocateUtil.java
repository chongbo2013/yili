package com.lots.travel.util;

import com.lots.travel.store.SpStore;

/**
 * Created by nalanzi on 2017/12/15.
 */

public class LocateUtil {
    public static final String LONGITUDE = "longitude";

    public static final String LATITUDE = "latitude";

    public static final String COUNTRY = "country";

    public static final String PROVINCE = "province";

    public static final String CITY = "city";

    public static final String DISTRICT = "district";

    public static final String DETAIL_ADDRESS = "detailAddress";

    public static void setLongitude(double longitude){
        SpStore.set(LONGITUDE,Double.toString(longitude));
    }

    public static double getLongitude(){
        try{
            String value = SpStore.getString(LONGITUDE,"0");
            return Double.parseDouble(value);
        }catch (NumberFormatException e){
            return 0;
        }
    }

    public static void setLatitude(double latitude){
        SpStore.set(LATITUDE,Double.toString(latitude));
    }

    public static double getLatitude(){
        try{
            String value = SpStore.getString(LATITUDE,"0");
            return Double.parseDouble(value);
        }catch (NumberFormatException e){
            return 0;
        }
    }

    public static void setCountry(String country){
        SpStore.set(COUNTRY,country);
    }

    public static String getCountry(){
        return SpStore.getString(COUNTRY,null);
    }

    public static void setProvince(String province){
        SpStore.set(PROVINCE,province);
    }

    public static String getProvince(){
        return SpStore.getString(PROVINCE,null);
    }

    public static void setCity(String city){
        SpStore.set(CITY,city);
    }

    public static String getCity(){
        return SpStore.getString(CITY,null);
    }

    public static void setDistrict(String district){
        SpStore.set(DISTRICT,district);
    }

    public static String getDistrict(){
        return SpStore.getString(DISTRICT,null);
    }

    public static void setDetailAddress(String address){
        SpStore.set(DETAIL_ADDRESS,address);
    }

    public static String getDetailAddress(){
        return SpStore.getString(DETAIL_ADDRESS,null);
    }

}
