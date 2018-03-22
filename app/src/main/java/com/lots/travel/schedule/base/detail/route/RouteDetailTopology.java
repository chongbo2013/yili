package com.lots.travel.schedule.base.detail.route;

import android.os.Parcel;
import android.os.Parcelable;

import com.lots.travel.schedule.base.Child;
import com.lots.travel.schedule.base.DataManager;
import com.lots.travel.schedule.base.Group;
import com.lots.travel.schedule.base.Topology;
import com.lots.travel.store.db.TripPart;

import java.util.List;

/**
 * Created by nalanzi on 2018/1/15.
 */
//date、spot、foot、hotel、stay、traffic
//不显示TripPart！！！
public class RouteDetailTopology extends Topology implements Parcelable{
    private RouteDetailTopology(){
        super();
    }

    protected RouteDetailTopology(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RouteDetailTopology> CREATOR = new Creator<RouteDetailTopology>() {
        @Override
        public RouteDetailTopology createFromParcel(Parcel in) {
            return new RouteDetailTopology(in);
        }

        @Override
        public RouteDetailTopology[] newArray(int size) {
            return new RouteDetailTopology[size];
        }
    };

    public static RouteDetailTopology create(DataManager manager){
        RouteDetailTopology topology = new RouteDetailTopology();
        //创建empty topology
        int count = manager.getDayCount();
        int flatPosition = 0;
        for (int i=0;i<count;i++){
            Group group = new Group(i,1,flatPosition);
            Child child = new Child(Topology.TYPE_DATE,i);
            topology.mChildren.add(child);
            topology.mGroups.add(group);
            flatPosition += 1;
        }

        //依次读取，添加至相应的day中
        List<TripPart> partList = manager.getItems();
        for (int i=0;i<partList.size();i++){
            TripPart part = partList.get(i);
            int index = Integer.parseInt(part.getDay())-1;
            Group group = topology.mGroups.get(index);
            int insertPosition = group.getFlatPosition()+group.getChildSize();
            switch (part.getStyle()){
                case TripPart.STYLE_SPOT:
                    topology.addChild(insertPosition,group,Topology.TYPE_SPOT,part.getId());
                    break;

                case TripPart.STYLE_FOOD:
                    topology.addChild(insertPosition,group,Topology.TYPE_FOOD,part.getId());
                    break;

                case TripPart.STYLE_HOTEL:
                    int childType = "1".equals(part.getHotelIn()) ? Topology.TYPE_STAY:Topology.TYPE_HOTEL;
                    topology.addChild(insertPosition,group,childType,part.getId());
                    break;

                case TripPart.STYLE_TRAFFIC:
                    topology.addChild(insertPosition,group,Topology.TYPE_TRAFFIC,part.getId());
                    break;
            }
        }

        return topology;
    }

}
