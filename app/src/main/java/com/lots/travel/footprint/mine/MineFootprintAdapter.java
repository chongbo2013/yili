package com.lots.travel.footprint.mine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.lots.travel.footprint.BaseFootprintAdapter;
import com.lots.travel.footprint.BaseHolder;
import com.lots.travel.footprint.CommentableHolder;
import com.lots.travel.footprint.OnFootprintListener;

import com.lots.travel.footprint.model.Footprint;

import static com.lots.travel.footprint.model.EventStyle.ACTIVE;
import static com.lots.travel.footprint.model.EventStyle.ACTIVE_JOIN;
import static com.lots.travel.footprint.model.EventStyle.DESTINATION;
import static com.lots.travel.footprint.model.EventStyle.FOOTPRINT;
import static com.lots.travel.footprint.model.EventStyle.NOTE;
import static com.lots.travel.footprint.model.EventStyle.TRIP;
import static com.lots.travel.footprint.model.EventStyle.LIVE;

/**
 * Created by nalanzi on 2017/11/18.
 */

public class MineFootprintAdapter extends BaseFootprintAdapter {

    public MineFootprintAdapter(Context context,RecyclerView rv) {
        super(context,rv);
    }

    public void createFootprint(){
        if(mOnFootprintListener!=null && mOnFootprintListener instanceof OnMineFootprintListener)
            ((OnMineFootprintListener) mOnFootprintListener).onCreateFootprint();
    }

    public void chooseState(int pos){
        if(mOnFootprintListener!=null && mOnFootprintListener instanceof OnMineFootprintListener)
            ((OnMineFootprintListener) mOnFootprintListener).onChooseTarget(pos);
    }

    @Override
    public void onAddZan(int flatPosition, boolean zaned) {
        super.onAddZan(flatPosition, zaned);
        RecyclerView.ViewHolder holder = vRecycler.findViewHolderForAdapterPosition(flatPosition);
        if(holder!=null && holder instanceof CommentableHolder){
            CommentableHolder cHolder = (CommentableHolder) holder;
            cHolder.onAddZan(getItem(getItemPosition(flatPosition)));
        }else{
            notifyItemChanged(flatPosition);
        }
    }

    @Override
    public int getDataItemType(int position) {
        Footprint footprint = getItem(position);
        assert footprint != null;
        switch (footprint.getEventStyle()){
            case Footprint.TRIP:
                return TRIP.getValue();

            case Footprint.ACTIVE:
                return ACTIVE.getValue();

            case Footprint.NOTE:
                return NOTE.getValue();

            case Footprint.FOOTPRINT:
                return FOOTPRINT.getValue();

            case Footprint.LIVE:
                return LIVE.getValue();

            case Footprint.DESTINATION:
                return DESTINATION.getValue();

            case Footprint.ACTIVE_JOIN:
                return ACTIVE_JOIN.getValue();
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return TripHolder.create(parent,this);

            case 1:
                return ActiveHolder.create(parent,this);

            case 2:
                return NoteHolder.create(parent,this);

            case 3:
                return FootprintHolder.create(parent,this);

            case 4:
                return VideoHolder.create(parent,this);

            case 5:
                return DestinationHolder.create(parent,this);

            case 6:
                return ActiveHolder.create(parent,this);
        }
        return null;
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseHolder itemHolder = (BaseHolder) holder;
        itemHolder.onBind(getItem(position));
    }

    public interface OnMineFootprintListener extends OnFootprintListener{
        //添加足迹
        void onCreateFootprint();
        //修改状态
        void onChooseTarget(int pos);
    }

}
