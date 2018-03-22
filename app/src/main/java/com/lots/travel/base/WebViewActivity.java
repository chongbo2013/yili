package com.lots.travel.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.lots.travel.main.destination.DestinationFragment;
import com.lots.travel.util.Constant;
import com.lots.travel.util.JsUtil;

import org.json.JSONObject;

/**
 * Created by nalanzi on 2017/11/5.
 */

public class WebViewActivity extends BaseActivity implements View.OnClickListener {
    private FrameLayout toolbar;
    private TextView tvTitle;
    private ImageView ivShare;
    private WebView webView;
    private ProgressBar progressBar;
    protected Runnable invisibleRunnable = new Runnable() {
        @Override
        public void run() {
            progressBar.setVisibility(View.INVISIBLE);
        }
    };

    private String pageTitle,pageUrl;
    private boolean sharable;

    private boolean backPressed = false;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseData(getIntent(),savedInstanceState);
        setContentView(R.layout.activityt_webview);

        toolbar = (FrameLayout) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        webView = (WebView) findViewById(R.id.web_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        if(!TextUtils.isEmpty(pageTitle)){
            tvTitle.setText(pageTitle);
        }
        findViewById(R.id.tv_back).setOnClickListener(this);
        ivShare = (ImageView) findViewById(R.id.iv_share);
        ivShare.setVisibility(sharable ? View.VISIBLE:View.INVISIBLE);
        ivShare.setOnClickListener(this);

        WebSettings ws = webView.getSettings();
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);// 设置WebView可触摸放大缩小
        ws.setJavaScriptEnabled(true);
        ws.setUseWideViewPort(true); // 适应屏幕
        ws.setLoadWithOverviewMode(true);
        ws.setAppCacheEnabled(false);
        ws.setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
        ws.setDatabaseEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setAllowFileAccess(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            WebView.enableSlowWholeDocumentDraw();
        }

        // 点击链接继续在当前browser中响应，而不是新开Android的系统browser中响应该链接，必须覆盖
        // webview的WebViewClient对象
        webView.setWebViewClient(new WvClient());
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webView.addJavascriptInterface(new WebViewInterface(this),"NativeInterface");

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.postDelayed(invisibleRunnable, 200);
                }
            }

        });

//        webView.loadUrl(JsUtil.loadJs(WebViewActivity.this, "app.js"));
        webView.loadUrl(pageUrl);
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back(){
        if(webView.canGoBack()) {
            backPressed = true;
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
            webView.goBack();
        }else
            finish();
    }

    private void parseData(Intent data,Bundle saved){
        pageTitle = data.getStringExtra(EXTRA_TITLE);
        pageUrl = data.getStringExtra(EXTRA_URL);
        sharable = data.getBooleanExtra(EXTRA_SHARABLE,false);
        if(saved!=null){
            pageTitle = saved.getString(EXTRA_TITLE);
            pageUrl = saved.getString(EXTRA_URL);
            sharable = saved.getBoolean(EXTRA_SHARABLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_TITLE,pageTitle);
        outState.putString(EXTRA_URL,pageUrl);
        outState.putBoolean(EXTRA_SHARABLE,sharable);
    }

    @Override
    protected void onDestroy() {
        progressBar.removeCallbacks(invisibleRunnable);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_back:
                back();
                break;

            case R.id.iv_share:
                ;
                break;
        }
    }

    /**
     * 	在Android中，WebView可以用来加载http和https网页到本地应用的控件。但是在默认情况下，通过loadUrl(String url)方法，
     *可以顺利loadUrl(“http://www.baidu.com”)之类的页面。但是，当load通过ssl加密的https页面，但是如果这个网站的安全证书在Android无法得到认证，
     *WebView就会变成一个空白页，而并不会像自带的浏览器一样弹出提示。因此，我们必须针对这种情况进行处理。(onReceivedSslError)
     *
     **/
    private class WvClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            backPressed = false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.loadUrl(JsUtil.loadJs(WebViewActivity.this, "app.js"));
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
            // Ignore SSL certificate errors
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            return super.shouldInterceptRequest(view, url);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

    }

    private class WebViewInterface extends NativeInterface{

        WebViewInterface(Activity context) {
            super(context);
        }

        @JavascriptInterface
        public void changeWebTitle(final String title){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvTitle.setText(title);
                }
            });
        }

        @JavascriptInterface
        public void pushNativeVC(String data){
            if(data==null)
                return;

            String url = null;
            String title = null;
            try{
                JSONObject root = new JSONObject(data);
                if(root.has("title"))
                    title = root.getString("title");
                if(root.has("url"))
                    url = root.getString("url");
            }catch (Exception e){
                Log.e(DestinationFragment.class.getName(),e.toString());
            }

            if(url==null)
                return;

            url = !url.contains("http") && !url.contains("https") ? Constant.HOST_H5 +url:url;
            Log.d("pushNativeVC",url);

            final String finalUrl = url;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(finalUrl);
                }
            });
        }

        @JavascriptInterface
        public void hiddenNavBar(final String data){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toolbar.setVisibility("1".equals(data) ? View.GONE: View.VISIBLE);
                }
            });
        }

        @Override
        public void hasLoadWebView(final int data){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(data==1)
                        dismissProgressDialog();
                    else
                        showProgressDialog();
                }
            });
        }

        @JavascriptInterface
        public void showShareButton(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ivShare.setVisibility(View.VISIBLE);
                }
            });
        }

        @JavascriptInterface
        public void hideShareButton(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ivShare.setVisibility(View.INVISIBLE);
                }
            });
        }

    }

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_SHARABLE = "sharable";

    public static void toWeb(Context context,String title,String url,boolean sharable){
        Intent intent = new Intent(context,WebViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(EXTRA_TITLE,title);
        intent.putExtra(EXTRA_URL,url);
        intent.putExtra(EXTRA_SHARABLE,sharable);
        context.startActivity(intent);
    }

}
