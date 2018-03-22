package com.lots.travel.schedule.model;

import com.lots.travel.R;

/**
 * Created by nalanzi on 2017/11/9.
 */

public enum TrafficMode {
    AIRPLANE("飞机",R.drawable.ico_traffic_airplane),
    BUS("公交",R.drawable.ico_traffic_bus),
    CYCLING("骑车",R.drawable.ico_traffic_cycling),
    DRIVE("开车",R.drawable.ico_traffic_drive),
    SHIP("乘船",R.drawable.ico_traffic_ship),
    SUBWAY("地铁",R.drawable.ico_traffic_subway),
    WALK("步行",R.drawable.ico_traffic_walk);

    private String name;
    private int icon;

    TrafficMode(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName(){
        return name;
    }

    public int getIcon(){
        return icon;
    }

    public static TrafficMode getMode(String name){
        if(name==null)
            return null;

        for (TrafficMode mode:TrafficMode.values()){
            if(mode.getName().equals(name))
                return mode;
        }

        return null;
    }

}
