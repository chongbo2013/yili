package com.lots.travel.main.destination;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.lots.travel.R;
import com.lots.travel.base.NativeInterface;
import com.lots.travel.base.RxBaseFragment;
import com.lots.travel.main.message.MessageTabActivity;
import com.lots.travel.network.SingleAdapter;
import com.lots.travel.util.Constant;
import com.lots.travel.util.JsUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class DestinationFragment extends RxBaseFragment implements View.OnClickListener
{
    private WebView webView;

    private FrameLayout fl_toolbar;

    public DestinationFragment()
    {
    }

    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_destination;
    }

    private LocalBroadcastManager lbm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView(View root)
    {
        webView=(WebView)root.findViewById(R.id.web_view);
        webView.setWebChromeClient(new WebChromeClient());
        WebSettings settings=webView.getSettings();
        settings.setJavaScriptEnabled(true);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
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
        webView.loadUrl(Constant.PATH_HOME_CITY);

        fl_toolbar=(FrameLayout)root.findViewById(R.id.fl_toolbar);
        root.findViewById(R.id.iv_message).setOnClickListener(this);
        root.findViewById(R.id.llSearch).setOnClickListener(this);
    }

    @Override
    protected void initData()
    {
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //lbm.unregisterReceiver(receiver);
    }

    private void register()
    {
        lbm=LocalBroadcastManager.getInstance(getActivity());

        IntentFilter filter=new IntentFilter();
        filter.addAction("hiddenNavBar");

        lbm.registerReceiver(receiver,filter);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        register();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        lbm.unregisterReceiver(receiver);
    }


    private BroadcastReceiver receiver=new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context,Intent intent)
        {
            String action=intent.getAction();
            switch(action)
            {
                case "hiddenNavBar":
                {
                    int data=intent.getIntExtra("data",0);
                    if(data==1)
                    {
                        fl_toolbar.setVisibility(View.GONE);
                    }
                    else
                    {
                        Single.timer(200,TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn
                                (AndroidSchedulers.mainThread())
                                //.compose(DestinationFragment.this.<Long>bindUntilEvent(FragmentEvent.DESTROY))
                                .subscribe(new SingleAdapter<Long>()
                                {
                                    @Override
                                    public void onSuccess(@NonNull Long aVoid)
                                    {
                                        fl_toolbar.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onError(Throwable e)
                                    {
                                    }
                                });

                    }
                }
                break;
            }
        }
    };

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {

            case R.id.iv_message:
            {
                Intent intent=new Intent(getActivity(),MessageTabActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.llSearch:
            {
                Intent intent=new Intent(getActivity(),CityPageSearchPlaceActivity.class);
                startActivity(intent);
            }
            break;
        }
    }
}
