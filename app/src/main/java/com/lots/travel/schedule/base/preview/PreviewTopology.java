package com.lots.travel.schedule.base.preview;

import android.os.Parcel;
import android.os.Parcelable;

import com.lots.travel.schedule.base.Child;
import com.lots.travel.schedule.base.DataManager;
import com.lots.travel.schedule.base.Group;
import com.lots.travel.schedule.base.Topology;
import com.lots.travel.schedule.base.edit.EditTopology;
import com.lots.travel.store.db.TripPart;

import java.util.List;

/**
 * Created by nalanzi on 2017/12/26.
 */

public class PreviewTopology extends Topology implements Parcelable {
    private PreviewTopology(){
        super();
    }

    protected PreviewTopology(Parcel in) {
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

    public static final Creator<PreviewTopology> CREATOR = new Creator<PreviewTopology>() {
        @Override
        public PreviewTopology createFromParcel(Parcel in) {
            return new PreviewTopology(in);
        }

        @Override
        public PreviewTopology[] newArray(int size) {
            return new PreviewTopology[size];
        }
    };

    public static PreviewTopology create(EditTopology src){
        PreviewTopology dst = new PreviewTopology();

        //依次添加除了add以外的所有元素添加进去，如果TripPart部分为null就不要添加
        int dstFlatPos = 0;
        Group srcGroup,dstGroup;

        for (int i=0;i<src.getGroupCount();i++){
            srcGroup = src.getGroupAt(i);

            int srcFlatPos = srcGroup.getFlatPosition();
            int dstSize = 0;

            for (int j=0;j<srcGroup.getChildSize();j++){
                Child srcChild = src.getChildAt(srcFlatPos+j);

                srcChild = srcChild.copy();
                srcChild.setExtraData(null);

                int type = srcChild.getType();
                if(type==Topology.TYPE_DATE ||
                        (type!=Topology.TYPE_ADD && srcChild.getTripKey()!=-1)){
                    dst.mChildren.add(srcChild);
                    ++dstSize;
                }
            }

            dstGroup = new Group(i,dstSize,dstFlatPos);
            dstFlatPos += dstSize;
            dst.mGroups.add(dstGroup);
        }

        return dst;
    }

    public static PreviewTopology create(DataManager manager){
        PreviewTopology topology = new PreviewTopology();
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
