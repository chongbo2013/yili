package com.lots.travel.widget.refresh;

import android.content.Context;
import android.support.v4.util.ArraySet;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.lots.travel.widget.refresh.AbstractLoadAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by nalanzi on 2017/11/2.
 */

public abstract class SelectItemsAdapter<T> extends AbstractLoadAdapter<T> {
    private TreeMap<Integer,T> selectItems;
    private OnSelectChangedListener onSelectedChangedListener;

    public SelectItemsAdapter(Context context,RecyclerView rv) {
        super(rv);
        selectItems = new TreeMap<>();
    }

    public void setSelected(Integer pos,T item,boolean selected){
        if(selected)
            selectItems.put(pos,item);
        else
            selectItems.remove(pos);
    }

    public boolean isSelected(Integer pos){
        return selectItems.containsKey(pos);
    }

    @Override
    public void onRefreshed() {
        selectItems.clear();
        triggerSelectChangedListener();
    }

    public void setOnSelectChangedListener(OnSelectChangedListener listener){
        onSelectedChangedListener = listener;
    }

    protected void triggerSelectChangedListener(){
        if(onSelectedChangedListener!=null)
            onSelectedChangedListener.onSelectChanged();
    }

    public boolean hasSelectedItems(){
        return selectItems.size()!=0;
    }

    public List<T> getSelectedItems(){
        List<T> items = new ArrayList<>();

        for (Map.Entry<Integer, T> entry : selectItems.entrySet()) {
            items.add(entry.getValue());
        }
        return items;
    }

    public interface OnSelectChangedListener{
        void onSelectChanged();
    }

}
