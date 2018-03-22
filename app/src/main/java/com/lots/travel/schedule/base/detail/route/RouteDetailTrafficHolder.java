package com.lots.travel.schedule.base.detail.route;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.schedule.base.ScheduleAdapter;
import com.lots.travel.schedule.model.TrafficMode;
import com.lots.travel.store.db.TripPart;

import java.util.Locale;

/**
 * Created by nalanzi on 2017/12/27.
 */

public class RouteDetailTrafficHolder extends RouteDetailHolder {
    public static RouteDetailTrafficHolder create(ViewGroup parent, RouteDetailAdapter adapter){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route_detail_traffic,parent,false);
        return new RouteDetailTrafficHolder(adapter,v);
    }

    public RouteDetailTrafficHolder(ScheduleAdapter adapter, View v) {
        super(adapter, v);
    }

    @Override
    public void onCreate(View v) {
        setLeftIcon(R.drawable.ico_timeline_traffic);
    }

    @Override
    public void onBind() {
        TripPart part = getContent();
        String type = part.getTransport();
        TrafficMode traffic = TrafficMode.getMode(type);
        setLeftIcon(getDrawable(traffic!=null ? traffic.getIcon():R.drawable.ico_timeline_traffic));
        String tip = getTip(part.getDistance(), part.getTransport(), part.getUseHour(), part.getUseMinute());
        ((TextView) itemView).setText(tip);
    }

    @Override
    public void onRelease() {}

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
