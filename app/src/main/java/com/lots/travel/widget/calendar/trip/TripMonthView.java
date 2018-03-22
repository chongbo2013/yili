package com.lots.travel.widget.calendar.trip;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.lots.travel.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//列为7
public class TripMonthView extends View {
    //当前年月
    private Date mMonthDate;

    //时间区间 - 在范围内的enable，闭集合
    private Date mRangeStart,mRangeEnd;

    //选中区间 - 闭集合
    private Date mChooseRangeStart,mChooseRangeEnd;

    private boolean mAutoMeasured;

    private float mCellSize;
    private float mCellChooseSize;

    private int mCellTextColor;
    private int mCellChooseTextColor;
    private int mCellDisableTextColor;

    private Rect mContentRect;

    private Paint mFillPaint;
    private Paint mTextPaint;
    private Paint mDividerPaint;

    private int mTotalCount;
    //首日的星期
    private int mDayOneWeek;

    private RectF mTempRectF;

    public TripMonthView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.TripMonthView);
        mAutoMeasured = a.getBoolean(R.styleable.TripMonthView_autoMeasured,true);
        mCellSize = a.getDimension(R.styleable.TripMonthView_cellSize,100);
        mCellChooseSize = a.getDimension(R.styleable.TripMonthView_cellChooseSize,80);
        float cellTextSize = a.getDimension(R.styleable.TripMonthView_cellTextSize, 24);
        mCellTextColor = a.getColor(R.styleable.TripMonthView_cellTextColor,Color.BLACK);
        mCellChooseTextColor = a.getColor(R.styleable.TripMonthView_cellChooseTextColor,Color.WHITE);
        mCellDisableTextColor = a.getColor(R.styleable.TripMonthView_cellDisableTextColor,Color.GRAY);
        int fillColor = a.getColor(R.styleable.TripMonthView_fillColor, Color.BLUE);

        String strDate = a.getString(R.styleable.TripMonthView_currentMonth);
        if(strDate!=null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
                Date date = sdf.parse(strDate);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                setMonth(cal.getTime());
            } catch (ParseException e) {
                Log.d(TripMonthView.class.getName(), e.toString());
            }
        }
        a.recycle();

        mContentRect = new Rect();

        mFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setColor(fillColor);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(cellTextSize);

        mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDividerPaint.setStyle(Paint.Style.STROKE);
        mDividerPaint.setStrokeWidth(1);
        mDividerPaint.setColor(Color.WHITE);

        mTempRectF = new RectF();
    }

    public void setMonth(Date date){
        mMonthDate = date;

        //当月第一天为周几
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mMonthDate);
        mDayOneWeek = calendar.get(Calendar.DAY_OF_WEEK);
        mTotalCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        requestLayout();
    }

    public void setRange(Date start,Date end){
        mRangeStart = start;
        mRangeEnd = end;
        invalidate();
    }

    public void chooseRange(Date start,Date end){
        mChooseRangeStart = start;
        mChooseRangeEnd = end;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        int row = mMonthDate==null ? 6:(mDayOneWeek -1+ mTotalCount)/7;

        if(mAutoMeasured && widthMode==MeasureSpec.EXACTLY) {
            mCellSize = (widthSize - getPaddingLeft() - getPaddingRight()) / 7;
            mCellChooseSize = mCellSize*.75f;
        }

        mContentRect.set(0,0,(int)(mCellSize*7),(int) (mCellSize*row));

        width = measureWidth(widthMode,widthSize);
        height = measureHeight(heightMode,heightSize);

        setMeasuredDimension(width,height);
    }

    private int measureWidth(int mode,int size){
        int padding = getPaddingLeft()+getPaddingRight();

        switch (mode){
            case MeasureSpec.EXACTLY:
                return size;

            case MeasureSpec.AT_MOST:
                return Math.min(mContentRect.width()+padding,size);

            default:
                return mContentRect.width()+padding;
        }
    }

    private int measureHeight(int mode,int size){
        int padding = getPaddingTop()+getPaddingBottom();

        switch (mode){
            case MeasureSpec.EXACTLY:
                return size;

            case MeasureSpec.AT_MOST:
                return Math.min(mContentRect.height()+padding,size);

            default:
                return mContentRect.height()+padding;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int rl = getPaddingLeft();
        int rt = getPaddingTop();

        int centerX = rl+(getWidth()-rl-getPaddingRight())/2;
        int centerY = rt+(getHeight()-rt-getPaddingBottom())/2;

        mContentRect.offsetTo(centerX-mContentRect.width()/2,centerY-mContentRect.height()/2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mMonthDate==null)
            return;

        canvas.translate(mContentRect.left,mContentRect.top);
        float baseline = getTextBaseline(mTextPaint,0);
        //绘制cell，一律移动到中心
        Calendar cal = Calendar.getInstance();
        cal.setTime(mMonthDate);

        int chooseCount = 0;
        for (int pos = 0; pos< mTotalCount; pos++){
            int flatPos = mDayOneWeek -1+pos;
            int nx = flatPos%7;
            int ny = flatPos/7;
            float dx = (nx+.5f)*mCellSize;
            float dy = (ny+.5f)*mCellSize;
            canvas.translate(dx,dy);

            int drawStyle = CELL_DRAW_NONE;
            int textColor;

            cal.set(Calendar.DAY_OF_MONTH,pos+1);
            Date date = cal.getTime();

            int ret = inRange(date,mRangeStart,mRangeEnd);
            if(ret==0||ret==-1||ret==1){
                boolean hasChoose = true;
                ret = inRange(date,mChooseRangeStart,mChooseRangeEnd);
                if(ret==-1){
                    drawStyle = CELL_DRAW_LEFT;
                    textColor = mCellChooseTextColor;
                }else if(ret==0){
                    drawStyle = CELL_DRAW_WHOLE;
                    textColor = mCellChooseTextColor;
                }else if (ret==1){
                    drawStyle = CELL_DRAW_RIGHT;
                    textColor = mCellChooseTextColor;
                }else {
                    hasChoose = false;
                    textColor = mCellTextColor;
                }
                if(hasChoose && mChooseRangeStart.equals(mChooseRangeEnd))
                    drawStyle = CELL_DRAW_ONE;
                chooseCount++;
            }else{
                drawStyle = CELL_DRAW_NONE;
                textColor = mCellDisableTextColor;
            }

            drawCellBackground(canvas,drawStyle);
            drawCellText(canvas,Integer.toString(pos+1),baseline,textColor);

            canvas.translate(-dx,-dy);
        }

        if(chooseCount>1){
            float dividerLeft = mCellSize*2;
            canvas.drawLine(dividerLeft,0,dividerLeft,mContentRect.height(),mDividerPaint);
            dividerLeft = mCellSize*6;
            canvas.drawLine(dividerLeft,0,dividerLeft,mContentRect.height(),mDividerPaint);
        }

        canvas.translate(-mContentRect.left,-mContentRect.top);
    }

    private static final int CELL_DRAW_NONE = 0;
    private static final int CELL_DRAW_LEFT = 1;
    private static final int CELL_DRAW_WHOLE = 2;
    private static final int CELL_DRAW_RIGHT = 3;
    private static final int CELL_DRAW_ONE = 4;

    private void drawCellBackground(Canvas canvas,int drawStyle){
        switch (drawStyle){
            case CELL_DRAW_LEFT:
                mTempRectF.set(-mCellChooseSize/2,-mCellChooseSize/2,mCellChooseSize/2,mCellChooseSize/2);
                canvas.drawArc(mTempRectF,90,180,false, mFillPaint);
                mTempRectF.set(-1,-mCellChooseSize/2,mCellSize/2,mCellChooseSize/2);
                canvas.drawRect(mTempRectF, mFillPaint);
                break;

            case CELL_DRAW_WHOLE:
                mTempRectF.set(-mCellSize/2,-mCellChooseSize/2,mCellSize/2,mCellChooseSize/2);
                canvas.drawRect(mTempRectF, mFillPaint);
                break;

            case CELL_DRAW_RIGHT:
                mTempRectF.set(-mCellSize/2,-mCellChooseSize/2,1,mCellChooseSize/2);
                canvas.drawRect(mTempRectF, mFillPaint);
                mTempRectF.set(-mCellChooseSize/2,-mCellChooseSize/2,mCellChooseSize/2,mCellChooseSize/2);
                canvas.drawArc(mTempRectF,-90,180,false, mFillPaint);
                break;

            case CELL_DRAW_ONE:
                canvas.drawCircle(0,0,mCellSize/2,mFillPaint);
                break;

            case CELL_DRAW_NONE:
            default:
                break;
        }
    }

    private void drawCellText(Canvas canvas,String day,float baseline,int textColor){
        //测量宽高，移动到最中心绘制
        float x = -mTextPaint.measureText(day)/2;
        mTextPaint.setColor(textColor);
        canvas.drawText(day,x,baseline,mTextPaint);
    }

    private float getTextBaseline(Paint paint,float centerY){
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return centerY-(fontMetrics.bottom+fontMetrics.top)/2;
    }

    private int mDayDown = -1;
    private OnCellListener mOnChooseListener;
    private boolean mCaptureResetDown = false;

    public void setOnCellListener(OnCellListener listener){
        mOnChooseListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction() & MotionEvent.ACTION_MASK;

        if(action==MotionEvent.ACTION_UP && mDayDown==-1 && !mCaptureResetDown)
            return false;

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mCaptureResetDown = mChooseRangeStart!=null && mChooseRangeEnd!=null && !mChooseRangeStart.equals(mChooseRangeEnd);
                if(mCaptureResetDown)
                    return true;

                mDayDown = judgeChooseDay((int) ev.getX(), (int) ev.getY());
                return true;
            }

            case MotionEvent.ACTION_UP:{
                if(mCaptureResetDown){
                    if(mOnChooseListener!=null)
                        mOnChooseListener.onClick(null,true);
                    mCaptureResetDown = false;
                    return true;
                }

                int dayUp = judgeChooseDay((int) ev.getX(), (int) ev.getY());
                if(mDayDown==dayUp){
                    if(mOnChooseListener!=null) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(mMonthDate);
                        cal.set(Calendar.DAY_OF_MONTH,dayUp);
                        mOnChooseListener.onClick(cal.getTime(),false);
                    }
                    return true;
                }
                return false;
            }

        }

        return false;
    }

    private int judgeChooseDay(int x,int y){
        int left,top;

        for (int pos = 0; pos< mTotalCount; pos++){
            int flatPos = mDayOneWeek-1+pos;
            int nx = flatPos%7;
            int ny = flatPos/7;
            left = (int) (mContentRect.left+nx*mCellSize);
            top = (int) (mContentRect.top+ny*mCellSize);

            if(x>=left && x<=left+mCellSize
                    && y>=top && y<=top+mCellSize){

                Calendar cal = Calendar.getInstance();
                cal.setTime(mMonthDate);
                cal.set(Calendar.DAY_OF_MONTH,pos+1);
                if(simpleInRange(cal.getTime(),mRangeStart,mRangeEnd))
                    return pos+1;
                return -1;
            }
        }
        return -1;
    }

    public interface OnCellListener{
        void onClick(Date date,boolean resetTouch);
    }

    // -1 与左边界相等，0在区间内，1与右边界相等
    public static int inRange(Date time,Date from,Date to){
        if(time==null || from==null || to==null)
            return Integer.MAX_VALUE;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

        String strTime = sdf.format(time);
        String strFrom = sdf.format(from);
        String strTo = sdf.format(to);

        int retFrom = strTime.compareTo(strFrom);
        int retTo = strTime.compareTo(strTo);

        if(retFrom<0)
            return Integer.MIN_VALUE;
        else if(retFrom==0)
            return -1;
        else if(retTo<0)
            return 0;
        else if(retTo==0)
            return 1;
        else
            return Integer.MAX_VALUE;
    }

    public boolean simpleInRange(Date time,Date from,Date to){
        if(time==null || from==null || to==null)
            return false;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

        String strTime = sdf.format(time);
        String strFrom = sdf.format(from);
        String strTo = sdf.format(to);

        int retFrom = strTime.compareTo(strFrom);
        int retTo = strTime.compareTo(strTo);
        return retFrom>=0 && retTo<=0;
    }

}
