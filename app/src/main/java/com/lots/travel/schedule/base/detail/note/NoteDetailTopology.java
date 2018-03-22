package com.lots.travel.schedule.base.detail.note;

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
//相比route，多了desc部分，并且显示TripPart
public class NoteDetailTopology extends Topology implements Parcelable {
    private NoteDetailTopology(){
        super();
    }

    protected NoteDetailTopology(Parcel in) {
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

    public static final Creator<NoteDetailTopology> CREATOR = new Creator<NoteDetailTopology>() {
        @Override
        public NoteDetailTopology createFromParcel(Parcel in) {
            return new NoteDetailTopology(in);
        }

        @Override
        public NoteDetailTopology[] newArray(int size) {
            return new NoteDetailTopology[size];
        }
    };

    public static NoteDetailTopology create(DataManager manager){
        NoteDetailTopology topology = new NoteDetailTopology();
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
                case TripPart.STYLE_DESC:
                    topology.addChild(insertPosition,group,Topology.TYPE_DESC,part.getId());
                    break;

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
