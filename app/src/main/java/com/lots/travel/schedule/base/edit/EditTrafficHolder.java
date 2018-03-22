package com.lots.travel.schedule.base.edit;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.schedule.model.TrafficMode;
import com.lots.travel.store.db.TripPart;

import java.util.Locale;

/**
 * Created by nalanzi on 2017/11/5.
 */

public class EditTrafficHolder extends EditScheduleHolder implements View.OnClickListener{

    public static EditTrafficHolder create(ViewGroup parent, EditScheduleAdapter adapter){
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_traffic,parent,false);
        return new EditTrafficHolder(adapter,root);
    }

    public EditTrafficHolder(EditScheduleAdapter adapter, View v) {
        super(adapter, v);
    }

    private TextView tvMode;

    @Override
    public void onCreate(View v) {
        tvMode = (TextView) v.findViewById(R.id.tv_mode);
        tvMode.setOnClickListener(this);
        v.findViewById(R.id.tv_delete).setOnClickListener(this);
    }

    @Override
    public void onBind() {
        TripPart tripPart = getContent();
        if(tripPart==null) {
            setLeftIcon(R.drawable.ico_timeline_traffic);
            tvMode.setText("");
            return;
        }

        String type = tripPart.getTransport();
        TrafficMode traffic = TrafficMode.getMode(type);
        setLeftIcon(getDrawable(traffic!=null ? traffic.getIcon():R.drawable.ico_timeline_traffic));
        String tip = getTip(tripPart.getDistance(), tripPart.getTransport(), tripPart.getUseHour(), tripPart.getUseMinute());
        tvMode.setText(tip);
    }

    @Override
    public void onRelease() {}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_mode:
                triggerAddTraffic();
                break;

            case R.id.tv_delete:
                triggerDeleteTraffic();
                break;
        }
    }

    private String getTip(String dis,String mode,String hour,String minute){
        try{
            float vDis = Float.parseFloat(dis);
            int vHour = Integer.parseInt(hour);
            int vMinute = Integer.parseInt(minute);

            StringBuilder strBuilder = new StringBuilder();
            if(Float.compare(vDis,0)!=0){
                strBuilder
                        .append("距离")
                        .append(String.format(Locale.getDefault(),"%.02f",vDis))
                        .append("km");
                if(vHour!=0 || vMinute!=0)
                    strBuilder.append("，");
            }
            if(!TextUtils.isEmpty(mode)){
                strBuilder.append(mode);
            }
            if(vHour!=0 || vMinute!=0){
                strBuilder.append("约");
                if(vHour>0) {
                    strBuilder.append(vHour).append("小时");
                }
                strBuilder.append(vMinute).append("分钟");
            }

            return strBuilder.toString();
        }catch (Exception e){
            return "";
        }
    }

}
