package com.lots.travel.widget.calendar.trip;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lots.travel.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nalanzi on 2017/9/21.
 */

public class ChooseDateRangeView extends RecyclerView implements TripMonthView.OnCellListener{
    private Date mRangeStart,mRangeEnd;
    private Date mChooseRangeStart,mChooseRangeEnd;

    private Adapter mMonthAdapter;

    public static final int STATE_RESET = 0;
    public static final int STATE_CHOOSING = 1;
    public static final int STATE_COMPLETED = 2;
    private int mChooseState;

    private OnChooseListener mOnChooseListener;

    public ChooseDateRangeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true));
    }

    public void setRange(Date from,Date to){
        mRangeStart = from;
        mRangeEnd = to;
        mMonthAdapter = new MonthAdapter();
        setAdapter(mMonthAdapter);
    }

    public void chooseRange(Date from,Date to){
        mChooseState = STATE_COMPLETED;
        mChooseRangeStart = from;
        mChooseRangeEnd = to;
        mMonthAdapter.notifyDataSetChanged();
    }

    public void setOnChooseListener(OnChooseListener listener){
        mOnChooseListener = listener;
    }

    public int getChooseState(){
        return mChooseState;
    }

    public Date findDateForPosition(int pos){
        Calendar cal = Calendar.getInstance();
        cal.setTime(mRangeEnd);
        cal.set(Calendar.DAY_OF_MONTH,1);
        cal.add(Calendar.MONTH,-pos);
        return cal.getTime();
    }

    public void reset(){
        mChooseState = STATE_RESET;
        mChooseRangeStart = null;
        mChooseRangeEnd = null;
        mMonthAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(Date date,boolean resetTouch) {
        if(resetTouch){
            if(mOnChooseListener!=null)
                mOnChooseListener.onResetTouch();
            return;
        }

        if(mChooseState==STATE_RESET){
            mChooseState = STATE_CHOOSING;
            mChooseRangeStart = date;
            mChooseRangeEnd = date;
            mMonthAdapter.notifyDataSetChanged();
        }else if(mChooseState==STATE_CHOOSING){
            if(date.compareTo(mChooseRangeStart)<=0) {
                if(mOnChooseListener!=null) mOnChooseListener.onLessThanStart();
                return;
            }

            mChooseState = STATE_COMPLETED;
            mChooseRangeEnd = date;
            mMonthAdapter.notifyDataSetChanged();
        }

        if(mOnChooseListener!=null)
            mOnChooseListener.onChoose(mChooseState,mChooseRangeStart,mChooseRangeEnd);
    }

    private class MonthAdapter extends Adapter<Holder>{
        int itemCount;

        MonthAdapter(){
            Calendar cal = Calendar.getInstance();
            cal.setTime(mRangeStart);
            int startYear = cal.get(Calendar.YEAR);
            int startMonth = cal.get(Calendar.MONTH);

            cal.setTime(mRangeEnd);
            int endYear = cal.get(Calendar.YEAR);
            int endMonth = cal.get(Calendar.MONTH);

            itemCount = 11-startMonth+Math.abs((endYear-startYear+1))*12-(11-endMonth);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_choose_date_range,parent,false);
            return new Holder(root);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(mRangeEnd);
            cal.set(Calendar.DAY_OF_MONTH,1);
            cal.add(Calendar.MONTH,-position);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月", Locale.getDefault());
            Date date = cal.getTime();
            holder.tvTitle.setText(sdf.format(date));
            holder.monthView.setMonth(date);
            holder.monthView.setRange(mRangeStart,mRangeEnd);
            holder.monthView.chooseRange(mChooseRangeStart,mChooseRangeEnd);
        }

        @Override
        public int getItemCount() {
            return itemCount;
        }
    }

    class Holder extends ViewHolder{
        TextView tvTitle;
        TripMonthView monthView;

        Holder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            monthView = (TripMonthView) itemView.findViewById(R.id.month_view);
            monthView.setOnCellListener(ChooseDateRangeView.this);
        }
    }

    public interface OnChooseListener{
        void onChoose(int state, Date start, Date end);
        void onLessThanStart();
        void onResetTouch();
    }

}
