package com.lots.travel.schedule.base.edit;

import android.os.Parcel;
import android.os.Parcelable;

import com.lots.travel.schedule.base.Child;
import com.lots.travel.schedule.base.DataManager;
import com.lots.travel.schedule.base.Group;
import com.lots.travel.schedule.base.Topology;
import com.lots.travel.schedule.base.move.MoveTopology;
import com.lots.travel.store.db.TripPart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nalanzi on 2017/12/25.
 * 行程、游记编辑时在结构上是一致的。后期如果需要区分开，写不同的topology
 * 每个item的结构：
 *   date
 *   desc
 *   spot
 *   traffic
 *   hotel
 *   traffic
 *   food
 *   add
 *   traffic
 *   stay
 */
public class EditTopology extends Topology implements Parcelable{

    private EditTopology(){
        super();
    }

    protected EditTopology(Parcel in) {
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

    public static final Creator<EditTopology> CREATOR = new Creator<EditTopology>() {
        @Override
        public EditTopology createFromParcel(Parcel in) {
            return new EditTopology(in);
        }

        @Override
        public EditTopology[] newArray(int size) {
            return new EditTopology[size];
        }
    };

    public static EditTopology create(DataManager manager){
        //创建empty
        EditTopology topology = EditTopology.create(manager.getDayCount());

        //遍历添加，根据当前位置添加
        List<TripPart> parts = manager.getItems();
        for (int i=0;i<parts.size();i++){
            TripPart part = parts.get(i);
            int day = Integer.parseInt(part.getDay());

            long oldPartId = -1;
            Group group = topology.getGroupAt(day-1);
            int childPos;

            switch (part.getStyle()){
                case TripPart.STYLE_DESC:
                    childPos = group.getFlatPosition()+1;
                    oldPartId = topology.setChildContent(childPos,part.getId());
                    break;

                case TripPart.STYLE_SPOT:
                    childPos = group.getFlatPosition()+group.getChildSize()-2;
                    topology.addChild(childPos,group, TYPE_SPOT,part.getId());
                    break;

                case TripPart.STYLE_HOTEL:
                    if("1".equals(part.getHotelIn())) {
                        childPos = group.getFlatPosition()+group.getChildSize()-1;
                        oldPartId = topology.setChildContent(childPos, part.getId());
                    }else{
                        childPos = group.getFlatPosition()+group.getChildSize()-2;
                        topology.addChild(childPos, group, TYPE_HOTEL, part.getId());
                    }
                    break;

                case TripPart.STYLE_FOOD:
                    childPos = group.getFlatPosition()+group.getChildSize()-2;
                    topology.addChild(childPos,group, TYPE_FOOD,part.getId());
                    break;

                case TripPart.STYLE_TRAFFIC:
                    childPos = group.getFlatPosition()+group.getChildSize()-2;
                    topology.addChild(childPos,group, TYPE_TRAFFIC,part.getId());
                    break;
            }

            //删除旧的数据
            if(oldPartId>0)
                manager.remove(oldPartId,true);
        }

        //部分没有traffic的添加traffic
        for (int i=0;i<topology.getGroupCount();i++){
            Group group = topology.getGroupAt(i);

            for (int j=0;j<group.getChildSize();j++){
                Child child = topology.getChildAt(group.getFlatPosition()+j);
                int childType = child.getType();
                //下一个是否为traffic，如果不是添加traffic
                if(childType==TYPE_SPOT
                        || childType==TYPE_HOTEL
                        || childType==TYPE_FOOD){
                    int nextPos = group.getFlatPosition()+j+1;
                    Child nextChild = topology.getChildAt(nextPos);
                    if(nextChild.getType()!=TYPE_TRAFFIC){
                        topology.addChild(nextPos,group,TYPE_TRAFFIC,-1);
                    }
                }
            }

            int lastPos = group.getFlatPosition()+group.getChildSize()-3;
            Child lastComponent = topology.getChildAt(lastPos);
            int lastType = lastComponent.getType();
            if(lastType==TYPE_TRAFFIC){
                //判断是否stay有数据
                Child stay = topology.getChildAt(group.getFlatPosition()+group.getChildSize()-1);
                long stayKey = stay.getTripKey();
                if(stayKey<0){
                    //stay无数据 - 删除traffic
                    long trafficPart = topology.removeChild(lastPos);
                    if(trafficPart>0)
                        manager.remove(trafficPart,true);
                }else{
                    //stay有数据 - traffic添加到stay前面，traffic删除
                    topology.addChild(group.getFlatPosition()+group.getChildSize()-1,group, TYPE_TRAFFIC,lastComponent.getTripKey());
                    topology.removeChild(lastPos);
                }
            }
        }

        return topology;
    }

    public static EditTopology create(int dayCount){
        //创建empty
        EditTopology topology = new EditTopology();

        int flatPosition = 0;
        int childSize;
        Child child;
        Group group;

        for(int i=0;i<dayCount;i++){
            childSize = 0;
            group = new Group(i,0,flatPosition);

            child = new Child(TYPE_DATE,i);
            topology.mChildren.add(child);
            ++flatPosition;
            ++childSize;

            child = new Child(TYPE_DESC,i);
            topology.mChildren.add(child);
            ++flatPosition;
            ++childSize;

            child = new Child(TYPE_ADD,i);
            topology.mChildren.add(child);
            ++flatPosition;
            ++childSize;

            child = new Child(TYPE_STAY,i);
            topology.mChildren.add(child);
            ++flatPosition;
            ++childSize;

            group.setChildSize(childSize);
            topology.mGroups.add(group);
        }

        return topology;
    }

    public List<Long> update(MoveTopology src){
        List<Long> oldKeys = new ArrayList<>();
        //直接在原来的基础上操作，将原来的components、traffic全部删除
        for (int i=mChildren.size()-1;i>=0;i--){
            Child child = mChildren.get(i);
            int type = child.getType();
            if(type==Topology.TYPE_SPOT
                    || type==Topology.TYPE_HOTEL
                    || type==Topology.TYPE_FOOD
                    ||type==Topology.TYPE_TRAFFIC){
                //此处不需要处理返回的key
                removeChild(i);
            }
        }

        //读取MoveTopology中的components、traffics，添加进去
        for (int i=0;i<src.getGroupCount();i++){
            Group srcGroup = src.getGroupAt(i);
            Group dstGroup = getGroupAt(i);

            int basePos = srcGroup.getFlatPosition();
            for (int j=0;j<srcGroup.getChildSize();j++){
                int childPos = basePos+j;
                Child child = src.getChildAt(childPos);
                int childType = child.getType();
                if(childType!=Topology.TYPE_DAY){
                    int insertPos = dstGroup.getFlatPosition()+dstGroup.getChildSize()-2;
                    addChild(insertPos,dstGroup,childType,child.getTripKey());
                }
            }

            //是否有stay，有则将traffic移动到stay前面；否则将traffic删除
            int stayPos = dstGroup.getFlatPosition()+dstGroup.getChildSize()-1;
            Child stay = mChildren.get(stayPos);
            boolean stayNull = stay.getTripKey()==-1;
            boolean hasComponents = dstGroup.getChildSize()>4;

            long oldKey = -1;
            if(stayNull && hasComponents){
                //删除traffic
                oldKeys.add(removeChild(stayPos-2));
            }else if(!stayNull && hasComponents){
                //移动至stay前 - group等位置没有改变
                Child traffic = getChildAt(stayPos-2);
                mChildren.add(stayPos,traffic);
                mChildren.remove(stayPos-2);
            }
//            //不存在components，所以，一定没有traffic，不需要删除
//            else if(!stayNull/* && !hasComponents*/){
//                removeChild(stayPos-2);
//            }
        }

        notifyChildrenChanged();
        return oldKeys;
    }

    public long[] addComponent(int groupPosition,int childType,long tripKey){
        long[] oldKeys = new long[]{-1,-1};
        Group group = getGroupAt(groupPosition);
        int stayPos = group.getFlatPosition()+group.getChildSize()-1;
        Child stay = getChildAt(stayPos);
        boolean stayNull = stay.getTripKey()==-1;
        boolean addFirst = group.getChildSize()==4;

        int insertPos;
        Child child = new Child(childType,groupPosition,tripKey);

        if(addFirst){
            insertPos = stayPos-1;;
            //只需要考虑stay
            addChild(insertPos,group,childType,tripKey);
            if (!stayNull){
                stayPos = group.getFlatPosition()+group.getChildSize()-1;
                addChild(stayPos,group,TYPE_TRAFFIC,-1);
            }
        }else{
            //stay为空不需要添加traffic；stay非空就添加traffic
            if(stayNull){
                insertPos = stayPos-1;
                addChild(insertPos,group,childType,tripKey);
            }else{
                //前面肯定有components，所以有traffic
                insertPos = stayPos-2;
                addChild(insertPos,group,childType,tripKey);
                stayPos = group.getFlatPosition()+group.getChildSize()-1;
                Child traffic = getChildAt(stayPos-1);
                long oldKey = traffic.setTripKey(-1);
                oldKeys[0] = oldKey;
            }
            //判断前面是否有traffic，有就clear，没有就添加
            Child upTraffic = mChildren.get(insertPos-1);
            if(upTraffic.getType()==TYPE_TRAFFIC){
                long oldKey = upTraffic.setTripKey(-1);
                oldKeys[1] = oldKey;
            }else{
                addChild(insertPos,group,TYPE_TRAFFIC,-1);
            }
        }

        return oldKeys;
    }

    public long[] removeComponent(int itemPosition){
        long[] oldKeys = new long[]{-1,-1,-1};
        //将其前面的traffic清空，后面的traffic删除
        Child child = mChildren.get(itemPosition);
        Group group = mGroups.get(child.getGroup());
        boolean isFirst = itemPosition==group.getFlatPosition()+2;

        Child stay = getChildAt(group.getFlatPosition()+group.getChildSize()-1);
        boolean stayNull = stay.getTripKey()==-1;
        int lastPos = group.getFlatPosition()+group.getChildSize()-1;
        lastPos = stayNull ? lastPos-2:lastPos-3;

        boolean isLast = lastPos==itemPosition;
        Child downTraffic;
        Child upTraffic;
        int upPos;
        int downPos;

        if(isFirst && isLast){
            oldKeys[0] = removeChild(itemPosition);
            if(!stayNull){
                downPos = group.getFlatPosition()+group.getChildSize()-2;
                oldKeys[1] = removeChild(downPos);
            }
        }else if(isFirst){
            oldKeys[0] = removeChild(itemPosition);
            downPos = itemPosition;
            oldKeys[1] = removeChild(downPos);
        }else if(isLast){
            oldKeys[0] = removeChild(itemPosition);
            if(!stayNull){
                downPos = group.getFlatPosition()+group.getChildSize()-2;
                downTraffic = mChildren.get(downPos);
                oldKeys[1] = downTraffic.setTripKey(-1);
                notifyChildChanged(downPos);
                upPos = group.getFlatPosition()+group.getChildSize()-4;
            }else
                upPos = group.getFlatPosition()+group.getChildSize()-3;

            upTraffic = mChildren.get(upPos);
            oldKeys[2] = removeChild(upPos);
        }else{
            //清除前一个，删除下一个
            upTraffic = getChildAt(itemPosition-1);
            oldKeys[0] = upTraffic.setTripKey(-1);
            notifyChildChanged(itemPosition-1);

            oldKeys[1] = removeChild(itemPosition+1);
            oldKeys[2] = removeChild(itemPosition);
        }

        return oldKeys;
    }

    public long[] updateStay(int groupPosition,long tripKey){
        long[] oldKeys = new long[]{-1,-1};

        Group group = getGroupAt(groupPosition);
        int stayPos = group.getFlatPosition()+group.getChildSize()-1;
        boolean hasComponents = group.getChildSize()>4;
        //有components，需要考虑traffic
        if(hasComponents){
            Child traffic = getChildAt(stayPos-1);
            //stay前不是traffic - 如果partId=-1，不做任何处理；否则，添加traffic
            //stay前是traffic - 如果partId=-1，删除traffic；否则，清除traffic
            boolean isTraffic = traffic.getType()==TYPE_TRAFFIC;
            boolean isClear = tripKey==-1;
            //处理traffic
            if(!isTraffic && !isClear){
                addChild(stayPos,group,TYPE_TRAFFIC,-1);
            }else if (isTraffic && isClear){
                oldKeys[0] = removeChild(stayPos-1);
            }else if(isTraffic/* && !isClear*/){
                oldKeys[0] = setChildContent(stayPos-1,-1);
            }
        }

        //处理stay
        stayPos = group.getFlatPosition()+group.getChildSize()-1;
        setChildContent(stayPos,tripKey);

        return oldKeys;
    }

}
