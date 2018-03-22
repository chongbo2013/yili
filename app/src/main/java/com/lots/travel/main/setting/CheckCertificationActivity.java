package com.lots.travel.main.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;

/**
 * Created by nalanzi on 2018/1/11.
 */

public class CheckCertificationActivity extends RxBaseActivity implements View.OnClickListener {
    public static final String COMPLETED = "Completed";

    public static void toCheck(Context context,boolean completed,boolean clear){
        Intent intent = new Intent(context,CheckCertificationActivity.class);
        intent.addFlags(clear ?
                Intent.FLAG_ACTIVITY_SINGLE_TOP
                        |Intent.FLAG_ACTIVITY_CLEAR_TOP
                :Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(COMPLETED,completed);
        context.startActivity(intent);
    }

    private boolean mCompleted;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_certification);
        initArgs(getIntent(),savedInstanceState);
        initViews();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(COMPLETED,mCompleted);
    }

    private void initArgs(Intent data, Bundle savedInstanceState){
        mCompleted = data.getBooleanExtra(COMPLETED,false);
        if(savedInstanceState!=null)
            mCompleted = savedInstanceState.getBoolean(COMPLETED);
    }

    private void initViews(){
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        TextView tvTipAppellation = (TextView) findViewById(R.id.tv_tip_appellation);
        TextView tvOperate = (TextView) findViewById(R.id.tv_operate);
        tvOperate.setOnClickListener(this);
        tvTipAppellation.setText(mCompleted ? R.string.certification_tip_completed:R.string.certification_tip_uncompleted);
        tvOperate.setText(mCompleted ? R.string.completed:R.string.certification_onekey);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
            case R.id.tv_cancel:
                finish();
                break;

            case R.id.tv_operate:
                if (mCompleted) {
                    finish();
                }else{
                    Intent intent = new Intent(this,CommitCertificationInfoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
                break;
        }
    }



}
