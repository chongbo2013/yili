package com.lots.travel.widget;

import android.support.v4.view.ViewPager;

/**
 *  ViewPager.OnPageChangeListener：
 *      onPageScrolled
 *      onPageSelected
 *      onPageScrollStateChanged
 *
 *  Indicator会随着ViewPager的滑动而变化，OnPageChangeListener是必须的
 */
public interface PagerIndicator extends ViewPager.OnPageChangeListener{

    /*
        在ViewPager设置adpter以后，调用改方法。并保证每次重新设置adapter时，都调用它
        必须在该方法中设置adapter监听器，这样adapter数据变化时才能随之变化
     */
    void setup(ViewPager pager);

    /*
        pager.setCurrentItem(initPos)
     */
    void setup(ViewPager pager, int initPos);

    void setCurrentItem(int pos);

    /*
        adapter数据变换时的处理
     */
    void onDataSetChanged();

}
