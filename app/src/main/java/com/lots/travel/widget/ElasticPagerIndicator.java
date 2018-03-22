package com.lots.travel.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

import com.lots.travel.R;

/**
 *  等分宽度+line动画
 *
 *  获取line的位置分布，根据当前偏移、选中位置进行绘制
 *  获取长度，与part进行比较，
 */
public class ElasticPagerIndicator extends View implements PagerIndicator {
    //line绘制
    private float lineWidth;
    private int lineColor;
    private Paint linePaint;

    private float dividerWidth;
    private int dividerColor;
    private Paint dividerPaint;

    //位置 [-1,1]
    private float scrollOffset;
    private int currentItem;

    //viewpager
    private ViewPager viewPager;
    private int count;

    private DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            onDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            onDataSetChanged();
        }
    };

    public ElasticPagerIndicator(Context context) {
        this(context,null);
    }

    public ElasticPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ElasticPagerIndicator);
        lineWidth = a.getDimension(R.styleable.ElasticPagerIndicator_elasticLineWidth,8);
        lineColor = a.getColor(R.styleable.ElasticPagerIndicator_elasticLineColor, Color.RED);
        dividerWidth = a.getDimension(R.styleable.ElasticPagerIndicator_elasticDividerWidth,3);
        dividerColor = a.getColor(R.styleable.ElasticPagerIndicator_elasticDividerColor, Color.GRAY);
        a.recycle();

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(lineWidth);

        dividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dividerPaint.setStyle(Paint.Style.STROKE);
        dividerPaint.setColor(dividerColor);
        dividerPaint.setStrokeWidth(dividerWidth);
    }

    @Override
    public void setup(ViewPager pager) {
        setup(pager,0);
    }

    @Override
    public void setup(ViewPager pager, int initPos) {
        PagerAdapter pagerAdapter;

        if(viewPager==pager)
            return;

        //取消对上一个ViewPager设置的listener
        if(viewPager!=null) {
            viewPager.removeOnPageChangeListener(this);

            pagerAdapter = viewPager.getAdapter();
            if(pagerAdapter!=null)
                pagerAdapter.unregisterDataSetObserver(dataSetObserver);
        }

        if (pager.getAdapter() == null)
            throw new IllegalStateException("ViewPager does not have adapter instance.");

        viewPager = pager;
        viewPager.addOnPageChangeListener(this);

        //对adapter数据进行监听
        pagerAdapter = viewPager.getAdapter();
        pagerAdapter.registerDataSetObserver(dataSetObserver);

        viewPager.setCurrentItem(initPos);

        count = viewPager.getAdapter().getCount();
        currentItem = initPos;
        scrollOffset = 0f;

        invalidate();
    }

    @Override
    public void setCurrentItem(int pos) {
        if (viewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        viewPager.setCurrentItem(pos);
        currentItem = pos;
        invalidate();
    }

    @Override
    public void onDataSetChanged() {
        PagerAdapter pagerAdapter = viewPager.getAdapter();

        currentItem = viewPager.getCurrentItem();
        scrollOffset = 0f;
        count = pagerAdapter.getCount();

        invalidate();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        currentItem = position;
        scrollOffset = positionOffset;
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        currentItem = position;
        scrollOffset = 0f;
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = (int) (lineWidth+.5f)+getPaddingTop()+getPaddingBottom();
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(viewPager==null || viewPager.getAdapter()==null || count==0)
            return;

        float partLength = (getWidth()-getPaddingLeft()-getPaddingRight())/count;
        float start = (currentItem+scrollOffset)*partLength;
        canvas.drawLine(start,getPaddingTop()+lineWidth/2,start+partLength,getPaddingTop()+lineWidth/2,linePaint);
        canvas.drawLine(0,getHeight()-dividerWidth/2,getWidth(),getHeight()-dividerWidth/2,dividerPaint);
    }

}
