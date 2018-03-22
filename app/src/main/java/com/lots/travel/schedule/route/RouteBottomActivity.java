package com.lots.travel.schedule.route;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lots.travel.network.model.result.MatchedRoute;
import com.lots.travel.schedule.base.Schedule;
import com.lots.travel.schedule.route.edit.RouteEditScheduleActivity;
import com.lots.travel.schedule.route.match.MatchedRoutePreviewActivity;

/**
 * Created by nalanzi on 2017/12/29.
 */

public class RouteBottomActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        parseIntent(intent);
    }

    private void parseIntent(Intent data){
        boolean back = data.hasExtra(BACK) && data.getBooleanExtra(BACK,false);
        if(back){
            boolean result = data.getBooleanExtra(RESULT,false);
            if(!result) {
                finish();
            }else{
                MatchedRoute matchedRoute = data.getParcelableExtra(MATCHED_ROUTE);
                Schedule schedule = data.getParcelableExtra(SCHEDULE);
                if (matchedRoute!=null)
                    MatchedRoutePreviewActivity.toPreview(RouteBottomActivity.this,schedule,matchedRoute,false);
                else
                    RouteEditScheduleActivity.toEdit(RouteBottomActivity.this,schedule);
                finish();
            }
        }else{
            Intent intent = new Intent(this,RouteChoosePlaceActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }

    public static final String BACK = "Back";
    public static final String RESULT = "Result";
    public static final String MATCHED_ROUTE = "MatchedRoute";
    public static final String SCHEDULE = "Schedule";

    public static void back(Context context, boolean success, Schedule schedule, MatchedRoute matchedRoute){
        if(!success){
            Intent intent = new Intent(context,RouteBottomActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(BACK,true);
            intent.putExtra(RESULT,false);
            context.startActivity(intent);
        }else{
            Intent intent = new Intent(context,RouteBottomActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(BACK,true);
            intent.putExtra(RESULT,true);
            intent.putExtra(SCHEDULE,schedule);
            intent.putExtra(MATCHED_ROUTE,matchedRoute);
            context.startActivity(intent);
        }
    }

}
