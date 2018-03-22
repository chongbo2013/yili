package com.lots.travel.main.destination;

import android.view.View;

import com.lots.travel.R;
import com.lots.travel.base.WebViewActivity;
import com.lots.travel.base.WebViewStackActivity;
import com.lots.travel.schedule.SearchPlaceActivity;
import com.lots.travel.store.db.ViewCity;
import com.lots.travel.util.Constant;
import com.lots.travel.util.JsUtil;

/**
 * 城市页面的搜索目的地<br/>
 * Created by GSXL on 2018-01-09.
 *
 */

public class CityPageSearchPlaceActivity extends SearchPlaceActivity
{
	@Override
	public void back(ViewCity spot)
	{
		//setPlace(spot.getName());
		String id=spot.getId()+"";
		WebViewStackActivity.toWeb(this,null,Constant.PATH_CITY_DETAIL+JsUtil.addSignature(id),false);
	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.iv_back:
			{
				finish();
			}
			break;
		}
	}

	@Override
	public void onBackPressed()
	{
		finish();
	}
}