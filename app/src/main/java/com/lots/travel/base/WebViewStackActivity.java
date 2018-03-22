package com.lots.travel.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.lots.travel.R;

/**
 * Created by GSXL on 2018-01-12.
 */

public class WebViewStackActivity extends BaseActivity
{
	private String pageTitle, pageUrl;
	private boolean sharable;

	private WebViewStackFragment oldFragment;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview_stack);

		pageTitle=getIntent().getStringExtra(EXTRA_TITLE);
		pageUrl=getIntent().getStringExtra(EXTRA_URL);
		sharable=getIntent().getBooleanExtra(EXTRA_SHARABLE,false);

		open(pageTitle,pageUrl,sharable);
	}

	public void open(String pageTitle,String pageUrl,boolean sharable)
	{
		//Log.e("xulei","open->"+pageUrl);
		FragmentManager fm=getSupportFragmentManager();
		FragmentTransaction transaction=fm.beginTransaction();
		transaction.setCustomAnimations(R.anim.fragment_left_in,R.anim.fragment_left_out,R.anim.fragment_right_in,R
				.anim.fragment_right_out);

		WebViewStackFragment fragment=new WebViewStackFragment();
		fragment.setInfo(pageTitle,pageUrl,sharable,this);

		if(oldFragment!=null)
		{
			transaction.hide(oldFragment);
		}
		transaction.add(R.id.fl_page,fragment);
		transaction.addToBackStack("");

		transaction.show(fragment);
		transaction.commit();
		oldFragment=fragment;
	}

	public void back()
	{
		FragmentManager fm=getSupportFragmentManager();
		int count=fm.getBackStackEntryCount();
		if(count==1)
		{
			finish();
		}
		else
		{
			fm.popBackStack();
		}
	}

	@Override
	public void onBackPressed()
	{
		oldFragment.back();
	}

	public static final String EXTRA_TITLE="title";
	public static final String EXTRA_URL="url";
	public static final String EXTRA_SHARABLE="sharable";

	public static void toWeb(Context context,String title,String url,boolean sharable)
	{
		Intent intent=new Intent(context,WebViewStackActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra(EXTRA_TITLE,title);
		intent.putExtra(EXTRA_URL,url);
		intent.putExtra(EXTRA_SHARABLE,sharable);
		context.startActivity(intent);
	}
}