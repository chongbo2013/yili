package com.lots.travel.main.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.lots.travel.R;
import com.lots.travel.base.WebViewActivity;
import com.lots.travel.store.db.GreenDaoManager;
import com.lots.travel.store.db.HttpInterface;
import com.lots.travel.store.db.HttpInterfaceDao;

import java.util.List;

/**
 * Created by nalanzi on 2018/1/11.
 */

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {
    private String mCrowFundingServiceUrl;
    private String mFeedbackUrl;
    private String mOursUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initViews();
        loadData();
    }

    private void initViews(){
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_crow_funding_service).setOnClickListener(this);
        findViewById(R.id.tv_feedback).setOnClickListener(this);
        findViewById(R.id.tv_ours).setOnClickListener(this);
    }

    private void loadData(){
        mCrowFundingServiceUrl = getUrl("zhong_xieyi");
        mFeedbackUrl = getUrl("zhong_complain");
        mOursUrl = getUrl("zhong_aboutUs");
    }

    private String getUrl(String name){
        List<HttpInterface> result;
        HttpInterface httpInterface;

        result = GreenDaoManager.getInstance().getHttpInterfaceDao()
                .queryBuilder()
                .where(HttpInterfaceDao.Properties.Name.eq(name))
                .list();

        if(result!=null && result.size()>0) {
            httpInterface = result.get(0);
            return httpInterface.getControl()+httpInterface.getAction();
        }

        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_crow_funding_service:
                if(!TextUtils.isEmpty(mCrowFundingServiceUrl))
                    WebViewActivity.toWeb(this,getString(R.string.about_crow_funding_service),mCrowFundingServiceUrl,false);
                break;

            case R.id.tv_feedback:
                if(!TextUtils.isEmpty(mFeedbackUrl))
                    WebViewActivity.toWeb(this,getString(R.string.about_feedback),mFeedbackUrl,false);
                break;

            case R.id.tv_ours:
                if(!TextUtils.isEmpty(mOursUrl))
                    WebViewActivity.toWeb(this,getString(R.string.about_ours),mOursUrl,false);
                break;
        }
    }

}
