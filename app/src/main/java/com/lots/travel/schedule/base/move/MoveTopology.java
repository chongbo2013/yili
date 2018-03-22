package com.lots.travel.schedule.base.move;

import android.os.Parcel;
import android.os.Parcelable;

import com.lots.travel.schedule.base.Child;
import com.lots.travel.schedule.base.Group;
import com.lots.travel.schedule.base.Topology;
import com.lots.travel.schedule.base.edit.EditTopology;

/**
 * Created by nalanzi on 2017/12/26.
 */

public class MoveTopology extends Topology implements Parcelable {
    private MoveTopology(){
        super();
    }

    protected MoveTopology(Parcel in) {
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

    public static final Creator<MoveTopology> CREATOR = new Creator<MoveTopology>() {
        @Override
        public MoveTopology createFromParcel(Parcel in) {
            return new MoveTopology(in);
        }

        @Override
        public MoveTopology[] newArray(int size) {
            return new MoveTopology[size];
        }
    };

    //只取spot、hotel、food
    public static MoveTopology create(EditTopology src){
        MoveTopology dst = new MoveTopology();

        int flatPos = 0;
        Group srcGroup,dstGroup;
        for (int i=0;i<src.getGroupCount();i++){
            int dstSize = 0;
            srcGroup = src.getGroupAt(i);
            dstGroup = new Group(i,0,flatPos);

            Child dayChild = new Child(TYPE_DAY,i);
            dst.mChildren.add(dayChild);
            ++dstSize;

            //获取spot、food、traffic，按照顺序添加进去
            for (int j=0;j<srcGroup.getChildSize();j++){
                Child srcChild = src.getChildAt(srcGroup.getFlatPosition()+j);
                int srcChildType = srcChild.getType();
                if(srcChildType==TYPE_SPOT
                        ||srcChildType==TYPE_HOTEL
                        ||srcChildType==TYPE_FOOD
                        ||srcChildType==TYPE_TRAFFIC){
                    Child dstChild = srcChild.copy();
                    dstChild.setGroup(srcChild.getGroup());
                    dst.mChildren.add(dstChild);
                    ++dstSize;
                }
            }

            Child lastChild = dst.mChildren.get(dst.mChildren.size()-1);
            int lastType = lastChild.getType();
            if(lastType!=TYPE_TRAFFIC
                    && lastType!=TYPE_DAY){
                Child child = new Child(TYPE_TRAFFIC,i);
                dst.mChildren.add(child);
                ++dstSize;
            }

            dstGroup.setChildSize(dstSize);
            flatPos += dstSize;
            dst.mGroups.add(dstGroup);
        }

        return dst;
    }

    public boolean isGroupLastComponent(int pos){
        Child child = getChildAt(pos);
        Group group = getGroupAt(child.getGroup());
        return pos==group.getFlatPosition()+group.getChildSize()-2;
    }

    //add component、add spot
    public void addComponent(int groupPos,int type,long partId){
        Group group = mGroups.get(groupPos);
        int insertPos = group.getFlatPosition()+group.getChildSize();

        //添加一个child
        addChild(insertPos,group,type,partId);

        //添加一个traffic
        addChild(insertPos+1,group,TYPE_TRAFFIC,-1);
    }

    public long[] deleteComponent(int itemPosition){
        long[] oldKeys = new long[]{-1,-1};

        //删除traffic
        oldKeys[0] = removeChild(itemPosition+1);

        //删除component
        oldKeys[1] = removeChild(itemPosition);
        return oldKeys;
    }

    //待优化
    public void move(int p1,int p2){
        int fromGroup,toGroup;

        Child p2Child = mChildren.get(p2);
        int p2Type = p2Child.getType();
        int p2Group = p2Child.getGroup();

        Child p1Child,p1Traffic;

        p1Child = mChildren.get(p1);
        p1Child = p1Child.copy();
        p1Traffic = mChildren.get(p1+1);
        p1Traffic = p1Traffic.copy();

        fromGroup = p1Child.getGroup();

        if(p2Type==TYPE_DAY){
            if(p1>p2){
                toGroup = p2Group-1;
                p1Child.setGroup(toGroup);
                p1Traffic.setGroup(toGroup);

                mChildren.add(p2,p1Child);
                mChildren.remove(p1+1);
                notifyChildMoved(p1,p2);

                mChildren.add(p2+1,p1Traffic);
                mChildren.remove(p1+2);
                notifyChildMoved(p1+1,p2+1);
            }else{
                toGroup = p2Group;
                p1Child.setGroup(toGroup);
                p1Traffic.setGroup(toGroup);

                mChildren.add(p2+1,p1Child);
                mChildren.remove(p1);
                notifyChildMoved(p1,p2);

                mChildren.add(p2+1,p1Traffic);
                mChildren.remove(p1);
                notifyChildMoved(p1,p2);
            }
        }else{
            toGroup = p2Group;
            if(p1>p2){
                p1Child.setGroup(toGroup);
                p1Traffic.setGroup(toGroup);

                mChildren.add(p2,p1Child);
                mChildren.remove(p1+1);
                notifyChildMoved(p1,p2);

                mChildren.add(p2+1,p1Traffic);
                mChildren.remove(p1+2);
                notifyChildMoved(p1+1,p2+1);
            }else{
                p1Child.setGroup(toGroup);
                p1Traffic.setGroup(toGroup);

                mChildren.add(p2+2,p1Child);
                mChildren.remove(p1);
                notifyChildMoved(p1,p2+1);

                mChildren.add(p2+2,p1Traffic);
                mChildren.remove(p1);
                notifyChildMoved(p1,p2+1);
            }
        }

        //调整group及其位置
        if(fromGroup!=toGroup){
            Group group = mGroups.get(fromGroup);
            group.setChildSize(group.getChildSize()-2);
            for (int i=fromGroup+1;i<mGroups.size();i++){
                Group temp = mGroups.get(i);
                temp.setFlatPosition(temp.getFlatPosition()-2);
            }

            group = mGroups.get(toGroup);
            group.setChildSize(group.getChildSize()+2);
            for (int i=toGroup+1;i<mGroups.size();i++){
                Group temp = mGroups.get(i);
                temp.setFlatPosition(temp.getFlatPosition()+2);
            }
        }
    }

}
