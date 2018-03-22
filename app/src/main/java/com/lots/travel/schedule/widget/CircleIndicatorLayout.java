package com.lots.travel.schedule.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.util.DensityUtil;

import java.util.Locale;

/**
 * Created by nalanzi on 2017/9/13.
 */
//写游记页面，顶部圆形sticky head
public class CircleIndicatorLayout extends RecyclerView {
    private int circleRadius;
    private Drawable circleBackgroundDrawable;

    private float circleTextSize;
    private int circleTextColor;

    private int circleCount;
    private int circlePadding;

    private int checkedPosition;

    private CircleAdapter circleAdapter;

    public CircleIndicatorLayout(Context context) {
        this(context,null);
    }

    public CircleIndicatorLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleIndicatorLayout);
        circleRadius = (int) a.getDimension(R.styleable.CircleIndicatorLayout_circleRadius,50);
        circleTextSize = a.getDimension(R.styleable.CircleIndicatorLayout_circleTextSize,18);
        circleTextColor = a.getColor(R.styleable.CircleIndicatorLayout_circleTextColor, Color.WHITE);
        circleBackgroundDrawable = a.getDrawable(R.styleable.CircleIndicatorLayout_circleBackgroundDrawable);
        circleCount = a.getInt(R.styleable.CircleIndicatorLayout_circleCount,0);
        circlePadding = (int) a.getDimension(R.styleable.CircleIndicatorLayout_circlePadding,20);
        checkedPosition = a.getInt(R.styleable.CircleIndicatorLayout_checkedPosition,0);
        a.recycle();

        CircleLayoutManager layoutManager = new CircleLayoutManager();
        setLayoutManager(layoutManager);
        circleAdapter = new CircleAdapter();
        setAdapter(circleAdapter);

        layoutManager.scrollToPosition(checkedPosition);
    }

    public void setCircleCount(int count){
        circleCount = count;
        getAdapter().notifyDataSetChanged();
    }

    public void checkToPosition(int position){
        checkedPosition = position;

        scrollToPosition(position);
    }

    private class CircleLayoutManager extends LayoutManager{
        private int scrollPosition;
        private int scrollOffset;

        CircleLayoutManager(){
            scrollPosition = -1;
            setAutoMeasureEnabled(true);
        }

        private int fillToRight(Recycler recycler,int position,int offsetLeft){
            //获取
            View view = recycler.getViewForPosition(position);

            if (view instanceof RadioButton){
                ((RadioButton)view).setChecked(position==checkedPosition);
            }

            //测量
            int sizeSpec = MeasureSpec.makeMeasureSpec(circleRadius*2,MeasureSpec.EXACTLY);
            view.measure(sizeSpec,sizeSpec);
            addView(view);
            //layout
            view.layout(offsetLeft+circlePadding, getPaddingTop()
                    , offsetLeft+circlePadding+view.getMeasuredWidth(), getPaddingTop()+view.getMeasuredHeight());
            return view.getRight()+circlePadding;
        }

        private int fillToLeft(Recycler recycler,int position,int offsetRight){
            //获取
            View view = recycler.getViewForPosition(position);

            if (view instanceof RadioButton){
                ((RadioButton)view).setChecked(position==checkedPosition);
            }

            //测量
            int sizeSpec = MeasureSpec.makeMeasureSpec(circleRadius*2,MeasureSpec.EXACTLY);
            view.measure(sizeSpec,sizeSpec);
            addView(view,0);
            //layout
            view.layout(offsetRight-circlePadding-view.getMeasuredWidth(), getPaddingTop()
                    , offsetRight-circlePadding, getPaddingTop()+view.getMeasuredHeight());
            return view.getLeft()-circlePadding;
        }

        @Override
        public void onLayoutChildren(Recycler recycler, State state) {
            int itemCount = getItemCount();

            int edgeLeft = getPaddingLeft();
            int edgeRight = getWidth()-getPaddingRight();

            //找到第一个在edge内的view作为anchor view
            int anchorPosition = 0;
            int anchorOffset = getPaddingLeft();

            if(scrollPosition!=-1 && !state.isMeasuring() && !state.isPreLayout()){
                anchorPosition = scrollPosition;
                anchorOffset = scrollOffset;
                scrollPosition = -1;
            }else{
                for (int i=0;i<getChildCount();i++){
                    View child = getChildAt(i);
                    int childViewLeft = child.getLeft()-circlePadding;
                    if(childViewLeft>=edgeLeft
                            && childViewLeft<=edgeRight){
                        anchorPosition = getChildAdapterPosition(child);
                        anchorOffset = childViewLeft;
                        break;
                    }
                }
            }

            detachAndScrapAttachedViews(recycler);

            //fillToRight
            int layoutPosition = anchorPosition;
            int layoutOffset = anchorOffset;
            while(layoutPosition<itemCount
                    && layoutOffset<edgeRight){
                layoutOffset = fillToRight(recycler,layoutPosition,layoutOffset);
                layoutPosition++;
            }

            //fillToLeft
            layoutPosition = anchorPosition-1;
            layoutOffset = anchorOffset;
            while(layoutPosition>=0
                    && layoutOffset>edgeLeft){
                layoutOffset = fillToLeft(recycler,layoutPosition,layoutOffset);
                layoutPosition--;
            }
        }

        @Override
        public int scrollHorizontallyBy(int dx, Recycler recycler, State state) {
            int itemCount = getItemCount();
            if(getChildCount()==0||itemCount==0)
                return 0;

            //进行移动，然后fill，如果超过边界就回来一点，然后recycle
            offsetChildrenHorizontal(-dx);

            int edgeLeft = getPaddingLeft();
            int edgeRight = getWidth()-getPaddingRight();

            View childView = getChildAt(0);
            int anchorPosition = getChildAdapterPosition(childView);
            int anchorOffset = childView.getLeft()-circlePadding;
            //fillToLeft
            int layoutPosition = anchorPosition-1;
            int layoutOffset = anchorOffset;
            while(layoutPosition>=0
                    && layoutOffset>edgeLeft){
                layoutOffset = fillToLeft(recycler,layoutPosition,layoutOffset);
                layoutPosition--;
            }

            childView = getChildAt(getChildCount()-1);
            anchorPosition = getChildAdapterPosition(childView);
            anchorOffset = childView.getRight()+circlePadding;
            //fillToRight
            layoutPosition = anchorPosition+1;
            layoutOffset = anchorOffset;
            while(layoutPosition<itemCount
                    && layoutOffset<edgeRight){
                layoutOffset = fillToRight(recycler,layoutPosition,layoutOffset);
                layoutPosition++;
            }

            //如果超过就往回走
            int backDx = 0;
            int rangeLeft = getChildAt(0).getLeft()-circlePadding;
            int rangeRight = getChildAt(getChildCount()-1).getRight()+circlePadding;

            if(rangeLeft>getPaddingLeft())
                backDx = getPaddingLeft()-rangeLeft;
            else if(rangeRight<getPaddingLeft()+circleRadius*2+circlePadding*2)
                backDx = getPaddingLeft()+circleRadius*2+circlePadding*2-rangeRight;

            offsetChildrenHorizontal(backDx);

            for(int i=getChildCount()-1;i>=0;i--){
                childView = getChildAt(i);
                if(childView.getLeft()-circlePadding>=edgeRight)
                    removeAndRecycleViewAt(i, recycler);
                else if(childView.getRight()+circlePadding<=edgeLeft)
                    removeAndRecycleViewAt(i,recycler);
            }
            Log.e("count!!!!!!!!",getChildCount()+"");
            return dx-backDx;
        }

        @Override
        public boolean canScrollHorizontally() {
            return true;
        }

        private void recycleChildren(Recycler recycler, int startIndex, int endIndex) {
            if (startIndex == endIndex) {
                return;
            }
            if (endIndex > startIndex) {
                for (int i = endIndex - 1; i >= startIndex; i--) {
                    removeAndRecycleViewAt(i, recycler);
                }
            } else {
                for (int i = startIndex; i > endIndex; i--) {
                    removeAndRecycleViewAt(i, recycler);
                }
            }
        }

        @Override
        public void scrollToPosition(int position) {
            scrollPosition = position;
            scrollOffset = getPaddingLeft();
            requestLayout();
        }

        @Override
        public LayoutParams generateDefaultLayoutParams() {
            return new LayoutParams(circleRadius,circleRadius);
        }

        @Override
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }
    }

    private class CircleAdapter extends Adapter<CircleHolder>{

        @Override
        public CircleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RadioButton circleView = new ShadeRadioButton(parent.getContext());
            circleView.setButtonDrawable(android.R.color.transparent);
            circleView.setTextSize(TypedValue.COMPLEX_UNIT_PX,circleTextSize);
            circleView.setTextColor(circleTextColor);

            circleView.setEllipsize(TextUtils.TruncateAt.END);
            circleView.setMaxLines(1);
            circleView.setGravity(Gravity.CENTER);

            ViewCompat.setBackground(circleView,circleBackgroundDrawable);
            return new CircleHolder(circleView);
        }

        @Override
        public void onBindViewHolder(CircleHolder holder, int position) {
            RadioButton tvName = (RadioButton) holder.itemView;
            tvName.setText(String.format(Locale.getDefault(),"D%d",position+1));
            tvName.setChecked(checkedPosition==position);
        }

        @Override
        public int getItemCount() {
            return circleCount;
        }
    }

    class CircleHolder extends ViewHolder{
        CircleHolder(View itemView) {
            super(itemView);
        }
    }

    static class ShadeRadioButton extends android.support.v7.widget.AppCompatRadioButton{
        private int shadeColor;
        private Paint paint;
        private float shadeRadius;

        public ShadeRadioButton(Context context) {
            this(context,null);
        }

        public ShadeRadioButton(Context context, AttributeSet attrs) {
            super(context, attrs);
            shadeColor = 0x5FFFFFFF;
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(shadeColor);
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            shadeRadius = Math.min(getWidth(),getHeight());
            shadeRadius /= 2;
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if(!isChecked()) {
                canvas.drawCircle(getWidth()/2, getHeight()/2, shadeRadius, paint);
            }
        }
    }

}
