package com.lots.travel.base;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.util.Constant;
import com.lots.travel.util.JsUtil;

import org.json.JSONObject;

/**
 * Created by GSXL on 2018-01-12.
 */

public class WebViewStackFragment extends BaseFragment implements View.OnClickListener
{
	private FrameLayout toolbar;
	private WebView webView;
	private String pageTitle;
	private String pageUrl;
	private boolean sharable;

	private TextView tvTitle;
	private ImageView ivShare;
	private ProgressBar progressBar;
	protected Runnable invisibleRunnable=new Runnable()
	{
		@Override
		public void run()
		{
			progressBar.setVisibility(View.INVISIBLE);
		}
	};

	private WebViewStackActivity activity;

	public void setInfo(String pageTitle,String pageUrl,boolean sharable,WebViewStackActivity activity)
	{
		this.pageTitle=pageTitle;
		this.pageUrl=pageUrl;
		this.sharable=sharable;
		this.activity=activity;
	}

	@Override
	protected int getLayoutId()
	{
		return R.layout.activityt_webview;
	}

	@Override
	protected void initView(View root)
	{
		toolbar=(FrameLayout)root.findViewById(R.id.toolbar);
		//toolbar.setVisibility(View.GONE);
		tvTitle=(TextView)root.findViewById(R.id.tv_title);
		webView=(WebView)root.findViewById(R.id.web_view);
		progressBar=(ProgressBar)root.findViewById(R.id.progress_bar);

		if(!TextUtils.isEmpty(pageTitle))
		{
			tvTitle.setText(pageTitle);
			//toolbar.setVisibility(View.VISIBLE);
		}
		root.findViewById(R.id.tv_back).setOnClickListener(this);
		ivShare=(ImageView)root.findViewById(R.id.iv_share);
		ivShare.setVisibility(sharable?View.VISIBLE:View.INVISIBLE);
		ivShare.setOnClickListener(this);

		WebSettings ws=webView.getSettings();
		ws.setSupportZoom(true);
		ws.setBuiltInZoomControls(true);// 设置WebView可触摸放大缩小
		ws.setJavaScriptEnabled(true);
		ws.setUseWideViewPort(true); // 适应屏幕
		ws.setLoadWithOverviewMode(true);
		ws.setAppCacheEnabled(true);
		ws.setAppCachePath(getActivity().getApplicationContext().getCacheDir().getAbsolutePath());
		ws.setDatabaseEnabled(true);
		ws.setDomStorageEnabled(true);
		ws.setAllowFileAccess(true);

		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
		{
			CookieManager.getInstance().setAcceptThirdPartyCookies(webView,true);
			ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
			WebView.enableSlowWholeDocumentDraw();
		}

		// 点击链接继续在当前browser中响应，而不是新开Android的系统browser中响应该链接，必须覆盖
		// webview的WebViewClient对象
		webView.setWebViewClient(new WebViewStackFragment.WvClient());
		webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
		webView.addJavascriptInterface(new WebViewStackFragment.WebViewInterface(getActivity()),"NativeInterface");

		progressBar.setVisibility(View.VISIBLE);
		progressBar.setProgress(0);
		webView.setWebChromeClient(new WebChromeClient()
		{
			@Override
			public void onProgressChanged(WebView view,int newProgress)
			{
				super.onProgressChanged(view,newProgress);

				progressBar.setProgress(newProgress);
				if(newProgress==100)
				{
					progressBar.postDelayed(invisibleRunnable,200);
				}
			}

		});

		webView.loadUrl(pageUrl);
	}

	@Override
	protected void initData()
	{
		//webView.loadUrl(pageUrl);
	}

	public void reload()
	{
		//webView.reload();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		webView.destroy();
	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.tv_back:
			{
				back();
			}
			break;

			case R.id.iv_share:
			{
				//Java->js->Java
				webView.loadUrl("javascript:getShareData()");
			}
			break;
		}
	}

	public void back()
	{
		if(webView.canGoBack())
		{
			webView.goBack();
		}
		else
		{
			activity.back();
		}
	}



	/**
	 * 在Android中，WebView可以用来加载http和https网页到本地应用的控件。但是在默认情况下，通过loadUrl(String url)方法，
	 * 可以顺利loadUrl(“http://www.baidu.com”)之类的页面。但是，当load通过ssl加密的https页面，但是如果这个网站的安全证书在Android无法得到认证，
	 * WebView就会变成一个空白页，而并不会像自带的浏览器一样弹出提示。因此，我们必须针对这种情况进行处理。(onReceivedSslError)
	 **/
	private class WvClient extends WebViewClient
	{
		@Override
		public void onPageStarted(WebView view,String url,Bitmap favicon)
		{
			super.onPageStarted(view,url,favicon);
		}

		@Override
		public void onPageFinished(WebView view,String url)
		{
			super.onPageFinished(view,url);
			view.loadUrl(JsUtil.loadJs(getActivity(),"app.js"));
			if(!url.contains("sosona"))
			{
				Log.e("xulei","goBack->"+url);
				if(view.canGoBack())
				{
					//view.goBack();
				}
			}
		}

		@Override
		public void onReceivedSslError(WebView view,SslErrorHandler handler,SslError error)
		{
			handler.proceed();
			// Ignore SSL certificate errors
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view,String url)
		{
			if((!TextUtils.isEmpty(url))&&url.startsWith("http")&&url.contains("sosona"))
			{
				activity.open("",url,false);
				return true;
			}
			return super.shouldOverrideUrlLoading(view,url);
		}

		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view,String url)
		{
			return super.shouldInterceptRequest(view,url);
		}

		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view,WebResourceRequest request)
		{
			return super.shouldInterceptRequest(view,request);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view,WebResourceRequest request)
		{
			return super.shouldOverrideUrlLoading(view,request);
		}

	}

	private class WebViewInterface extends NativeInterface
	{

		WebViewInterface(Activity context)
		{
			super(context);
		}

		@JavascriptInterface
		public void changeWebTitle(final String title)
		{
			getActivity().runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					tvTitle.setText(title);
				}
			});
		}

		@JavascriptInterface
		public void pushNativeVC(String data)
		{
			//Log.e("xulei","pushNativeVC->"+data);
			if(data==null)
				return;

			String url=null;
			String title=null;
			try
			{
				JSONObject root=new JSONObject(data);
				if(root.has("title"))
					title=root.getString("title");
				if(root.has("url"))
					url=root.getString("url");
			}
			catch(Exception e)
			{
				Log.e(WebViewStackFragment.class.getName(),e.toString());
			}

			if(url==null)
				return;

			url=!url.contains("http")&&!url.contains("https")?Constant.HOST_H5+url:url;

			final String finalUrl=url;
			final String finalTitle=title;
			getActivity().runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					activity.open(finalTitle,finalUrl,false);
				}
			});
		}

		@JavascriptInterface
		@Override
		public void hiddenNavBar(final int data)
		{
			getActivity().runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					toolbar.setVisibility(data==1?View.GONE:View.VISIBLE);
				}
			});
		}

		@Override
		public void hasLoadWebView(final int data)
		{
			getActivity().runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					//					if(data==1)
					//						//dismissProgressDialog();
					//					else
					//						//showProgressDialog();
				}
			});
		}

		@JavascriptInterface
		public void showShareButton()
		{
			getActivity().runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					ivShare.setVisibility(View.VISIBLE);
				}
			});
		}

		@JavascriptInterface
		public void hideShareButton()
		{
			getActivity().runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					ivShare.setVisibility(View.INVISIBLE);
				}
			});
		}

		@JavascriptInterface
		public void backToHome()
		{
			activity.back();
		}

		@JavascriptInterface
		public void backToRootVC(String num)
		{
			activity.finish();
		}

		@JavascriptInterface
		public void reloadWebView()
		{
			getActivity().runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					reload();
				}
			});
		}
	}
}