package com.lots.travel.footprint.live;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.lots.travel.footprint.BaseFootprintAdapter;
import com.lots.travel.footprint.BaseHolder;
import com.lots.travel.footprint.CommentableHolder;
import com.lots.travel.footprint.OnFootprintListener;
import com.lots.travel.footprint.model.Footprint;

import java.util.ArrayList;
import java.util.List;

import static com.lots.travel.footprint.model.EventStyle.ACTIVE;
import static com.lots.travel.footprint.model.EventStyle.ACTIVE_JOIN;
import static com.lots.travel.footprint.model.EventStyle.DESTINATION;
import static com.lots.travel.footprint.model.EventStyle.FOOTPRINT;
import static com.lots.travel.footprint.model.EventStyle.NOTE;
import static com.lots.travel.footprint.model.EventStyle.TRIP;
import static com.lots.travel.footprint.model.EventStyle.LIVE;

/**
 * Created by nalanzi on 2017/11/20.
 */

public class LiveFootprintAdapter extends BaseFootprintAdapter {

    public LiveFootprintAdapter(Context context,RecyclerView rv) {
        super(context,rv);
    }

    @Override
    public void onAddZan(int flatPosition, boolean zaned) {
        super.onAddZan(flatPosition, zaned);
        RecyclerView.ViewHolder holder = vRecycler.findViewHolderForAdapterPosition(flatPosition);
        if(holder!=null && holder instanceof CommentableHolder){
            CommentableHolder cHolder = (CommentableHolder) holder;
            Footprint t = getItem(getItemPosition(flatPosition));
            cHolder.onAddZan(t);
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
                return LiveHolder.create(parent,this);

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

    @Override
    public void onRefreshed(List<Footprint> srcData) {
        if(srcData==null || srcData.size()==0) {
            super.onRefreshed(srcData);
            return;
        }

        List<Footprint> src = new ArrayList<>();
        for (int i=0;i<srcData.size();i++){
            Footprint item = srcData.get(i);
            String eventStyle = item.getEventStyle();
            if("note".equals(eventStyle)
                    || "youji".equals(eventStyle)
                    || "zhibo".equals(eventStyle))
                src.add(item);
        }

        super.onRefreshed(src);
    }

    @Override
    public int addAll(int position,List<Footprint> items) {
        if(items==null || items.size()==0)
            return 0;

        List<Footprint> src = new ArrayList<>();
        for (int i=0;i<items.size();i++){
            Footprint item = items.get(i);
            String eventStyle = item.getEventStyle();
            if("note".equals(eventStyle)
                    || "youji".equals(eventStyle)
                    || "zhibo".equals(eventStyle))
                src.add(item);
        }

        return super.addAll(position,src);
    }

}
