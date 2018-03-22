package com.lots.travel.schedule;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lots.travel.R;
import com.lots.travel.base.BaseActivity;
import com.lots.travel.util.TimeUtil;
import com.lots.travel.widget.calendar.trip.ChooseDateRangeView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nalanzi on 2017/9/22.
 */

public class ChooseTripDateActivity extends BaseActivity implements View.OnClickListener{
    //选中的区间
    private Date rangeStartDate, rangeEndDate;
    //当前的选择
    private Date choseStartDate, choseEndDate;

    private CheckedTextView rbDateGo,rbDateBack;
    private TextView tvDateGo,tvDateBack;

    private ChooseDateRangeView chooseRangeView;

    private AlertDialog resetDialog;

    private FrameLayout flSticky;
    private TextView tvSticky;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parseIntent(getIntent(),savedInstanceState);
        setContentView(R.layout.activity_choose_trip_date);

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_reset).setOnClickListener(this);

        chooseRangeView = (ChooseDateRangeView) findViewById(R.id.range_date_view);
        chooseRangeView.setRange(rangeStartDate,rangeEndDate);
        chooseRangeView.setOnChooseListener(new ChooseDateRangeView.OnChooseListener() {
            @Override
            public void onChoose(int state, Date start, Date end) {
                switch (state){
                    case ChooseDateRangeView.STATE_RESET:
                        resetChoose();
                        break;

                    case ChooseDateRangeView.STATE_CHOOSING:
                        choseStartDate = start;
                        rbDateGo.setChecked(false);
                        rbDateBack.setChecked(true);
                        break;

                    case ChooseDateRangeView.STATE_COMPLETED:
                        choseStartDate = start;
                        choseEndDate = end;
                        setDateText(start, end);
                        break;
                }
            }

            @Override
            public void onLessThanStart() {
                Toast.makeText(ChooseTripDateActivity.this,R.string.schedule_trip_date_lessback,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResetTouch() {
                if(resetDialog==null){
                    resetDialog = new AlertDialog.Builder(ChooseTripDateActivity.this)
                            .setMessage(R.string.schedule_trip_date_retry)
                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    resetChoose();
                                }
                            })
                            .setNegativeButton(R.string.cancel,null)
                            .create();
                }
                resetDialog.show();
            }

        });

        rbDateGo = (CheckedTextView) findViewById(R.id.rb_date_go);
        rbDateGo.setOnClickListener(this);
        rbDateBack = (CheckedTextView) findViewById(R.id.rb_date_back);
        rbDateBack.setOnClickListener(this);
        rbDateGo.setChecked(true);
        rbDateBack.setChecked(false);

        tvDateGo = (TextView) findViewById(R.id.tv_date_go);
        tvDateBack = (TextView) findViewById(R.id.tv_date_back);

        findViewById(R.id.btn_confirm).setOnClickListener(this);

        flSticky = (FrameLayout) findViewById(R.id.fl_sticky);
        tvSticky = (TextView) findViewById(R.id.tv_sticky);
        chooseRangeView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                //获取rv item的top所在的屏幕坐标
                LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
                int firstVisiblePos = layoutManager.findLastVisibleItemPosition();

                //计算出当前应该显示值
                Date date = chooseRangeView.findDateForPosition(firstVisiblePos);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月", Locale.getDefault());
                tvSticky.setText(sdf.format(date));
            }
        });

        //根据保存的数据进行界面还原
        if(choseStartDate!=null && choseEndDate!=null){
            chooseRangeView.chooseRange(choseStartDate,choseEndDate);
            int months = TimeUtil.getBetweenMonths(choseStartDate,rangeEndDate);
            chooseRangeView.scrollToPosition(months);
            setDateText(choseStartDate, choseEndDate);
            rbDateGo.setChecked(false);
            rbDateBack.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                toBack(true);
                break;

            case R.id.tv_reset:
                resetChoose();
                break;

            case R.id.rb_date_go:
                if(chooseRangeView.getChooseState()==ChooseDateRangeView.STATE_COMPLETED)
                    resetChoose();
                break;

            case R.id.rb_date_back:
                if(chooseRangeView.getChooseState()==ChooseDateRangeView.STATE_RESET)
                    Toast.makeText(ChooseTripDateActivity.this,R.string.schedule_trip_date_nogo,Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_confirm:
                if(choseStartDate ==null)
                    Toast.makeText(this,R.string.schedule_trip_date_nogo,Toast.LENGTH_SHORT).show();
                else if(choseEndDate ==null)
                    Toast.makeText(this,R.string.schedule_trip_date_noback,Toast.LENGTH_SHORT).show();
                else
                    toBack(false);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (resetDialog!=null && resetDialog.isShowing()){
            resetDialog.dismiss();
            resetDialog = null;
        }
    }

    @Override
    public void onBackPressed() {
        toBack(true);
    }

    private void resetChoose(){
        choseStartDate = null;
        choseEndDate = null;
        //rb变化
        rbDateGo.setChecked(true);
        rbDateBack.setChecked(false);
        //tv变化
        setDateText(rangeStartDate, rangeEndDate);
        //range变化
        chooseRangeView.reset();
    }

    private void setDateText(Date start,Date end){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        if(start!=null){
            tvDateGo.setText(sdf.format(start));
        }
        if(end!=null){
            tvDateBack.setText(sdf.format(end));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(RANGE_START_DATE, rangeStartDate);
        outState.putSerializable(RANGE_END_DATE, rangeEndDate);
        outState.putSerializable(CHOSE_START_DATE, choseStartDate);
        outState.putSerializable(CHOSE_END_DATE, choseEndDate);
    }

    private void parseIntent(Intent intent, Bundle savedInstanceState){
        rangeStartDate = (Date) intent.getSerializableExtra(RANGE_START_DATE);
        rangeEndDate = (Date) intent.getSerializableExtra(RANGE_END_DATE);
        choseStartDate = (Date) intent.getSerializableExtra(CHOSE_START_DATE);;
        choseEndDate = (Date) intent.getSerializableExtra(CHOSE_END_DATE);

        if(savedInstanceState!=null){
            rangeStartDate = (Date) savedInstanceState.getSerializable(RANGE_START_DATE);
            rangeEndDate = (Date) savedInstanceState.getSerializable(RANGE_END_DATE);
            choseStartDate = (Date) savedInstanceState.getSerializable(CHOSE_START_DATE);;
            choseEndDate = (Date) savedInstanceState.getSerializable(CHOSE_END_DATE);;
        }

        if(rangeStartDate==null || rangeEndDate==null){
            Log.e(ChooseTripDateActivity.class.getName(),"日期选取区间为空");
            finish();
        }
    }

    public static final String CHOSE_START_DATE = "ChoseStartDate";
    public static final String CHOSE_END_DATE = "ChoseEndDate";
    public static final String RANGE_START_DATE = "RangeStartDate";
    public static final String RANGE_END_DATE = "RangeEndDate";

    public static void toChoose(Activity context,int requestCode, Date choseStart, Date choseEnd){
        Date rangeStart,rangeEnd;

        Calendar cal = Calendar.getInstance();
        cal.set(1970,1,1);
        rangeStart = cal.getTime();

        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        rangeEnd = cal.getTime();

        toChoose(context, requestCode, choseStart, choseEnd, rangeStart, rangeEnd);
    }

    public static void toChoose(Activity context,int requestCode, Date choseStart, Date choseEnd,Date rangeStart,Date rangeEnd){
        Intent intent = new Intent(context,ChooseTripDateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(CHOSE_START_DATE,choseStart);
        intent.putExtra(CHOSE_END_DATE,choseEnd);
        intent.putExtra(RANGE_START_DATE,rangeStart);
        intent.putExtra(RANGE_END_DATE,rangeEnd);
        context.startActivityForResult(intent,requestCode);
    }



    public void toBack(boolean cancel){
        if(cancel){
            setResult(Activity.RESULT_CANCELED);
        }else{
            Intent data = new Intent();
            data.putExtra(CHOSE_START_DATE, choseStartDate);
            data.putExtra(CHOSE_END_DATE, choseEndDate);
            setResult(Activity.RESULT_OK,data);
        }
        finish();
    }

}
