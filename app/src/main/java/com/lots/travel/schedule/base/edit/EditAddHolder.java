package com.lots.travel.schedule.base.edit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lots.travel.R;

/**
 * Created by nalanzi on 2017/12/26.
 */

public class EditAddHolder extends EditScheduleHolder implements View.OnClickListener {

    public static EditAddHolder create(ViewGroup parent, EditScheduleAdapter adapter){
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_add,parent,false);
        return new EditAddHolder(adapter,root);
    }

    public EditAddHolder(EditScheduleAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        setLeftIcon(R.drawable.ico_timeline_add);
        v.setOnClickListener(this);
    }

    @Override
    public void onBind() {}

    @Override
    public void onRelease() {}

    @Override
    public void onClick(View v) {
       triggerAddComponents();
    }
}
