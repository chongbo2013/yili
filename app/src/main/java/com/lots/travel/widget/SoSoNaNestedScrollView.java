package com.lots.travel.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by GSXL on 2017-11-20.
 */

public class SoSoNaNestedScrollView extends NestedScrollView
{
	private OnScrollChangeListener listener;

	public SoSoNaNestedScrollView(Context context)
	{
		super(context);
	}

	public SoSoNaNestedScrollView(Context context,AttributeSet attrs)
	{
		super(context,attrs);
	}

	public SoSoNaNestedScrollView(Context context,AttributeSet attrs,int defStyleAttr)
	{
		super(context,attrs,defStyleAttr);
	}

	public void setOnScrollListener(OnScrollChangeListener listener)
	{
		this.listener=listener;
	}


	@Override
	protected void onScrollChanged(int l,int t,int oldl,int oldt)
	{
		super.onScrollChanged(l,t,oldl,oldt);
		listener.onScrollChange(this,l,t,oldl,oldt);
	}
}
