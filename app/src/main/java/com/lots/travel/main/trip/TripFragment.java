package com.lots.travel.main.trip;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lots.travel.R;
import com.lots.travel.base.BaseFragment;
import com.lots.travel.base.NativeInterface;
import com.lots.travel.booking.BookingChoosePlaceActivity;
import com.lots.travel.util.Constant;
import com.lots.travel.util.JsUtil;
import com.lots.travel.widget.SearchBar;

/**
 * Created by nalanzi on 2017/9/7.
 */

public class TripFragment extends BaseFragment implements View.OnClickListener, SearchBar.OnSearchListener
{
	private WebView webView;
	private SearchBar searchBar;

	@Override
	protected int getLayoutId()
	{
		return R.layout.fragment_trip;
	}

	@Override
	protected void initView(View root)
	{
		root.findViewById(R.id.tv_filter).setOnClickListener(this);
		searchBar=(SearchBar)root.findViewById(R.id.search_bar);
		searchBar.setOnSearchListener(this);
		root.findViewById(R.id.tv_reset).setOnClickListener(this);
		webView=(WebView)root.findViewById(R.id.web_view);
		webView.setWebChromeClient(new WebChromeClient());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient()
		{
			@Override
			public void onPageFinished(WebView view,String url)
			{
				super.onPageFinished(view,url);
				webView.loadUrl(JsUtil.loadJs(getContext(),"app.js"));
			}
		});
		webView.addJavascriptInterface(new NativeInterface(getActivity()),"NativeInterface");
	}

	@Override
	protected void initData()
	{
		webView.loadUrl(Constant.PATH_HOME_TRIP);
	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.tv_filter:
			{
				webView.loadUrl("javascript:$('.popup-t').css({'top':'0px'});$('.popup-t').toggle();");
			}
			break;

			case R.id.tv_reset:
			{
				webView.loadUrl("javascript:$('.popup-t').hide();");
				Intent intent=new Intent(getActivity(),BookingChoosePlaceActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
			}
			break;
		}
	}

	@Override
	public void onSearch(CharSequence text)
	{
		webView.loadUrl("javascript:$('.popup-t').hide();");
		if(TextUtils.isEmpty(text))
		{
			text="";
		}
		webView.loadUrl("javascript:$('.ig').val('"+text+"');loadData(true);");
	}
}