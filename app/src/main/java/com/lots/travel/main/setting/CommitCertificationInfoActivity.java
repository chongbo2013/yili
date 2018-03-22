package com.lots.travel.main.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;

/**
 * Created by nalanzi on 2018/1/11.
 */

public class CommitCertificationInfoActivity extends RxBaseActivity implements View.OnClickListener {
    private EditText etName,etId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commmit_certification_info);
        initArgs(getIntent(),savedInstanceState);
        initViews();
    }

    private void initArgs(Intent data,Bundle savedInstanceState){}

    private void initViews(){
        etName = (EditText) findViewById(R.id.et_name);
        etId = (EditText) findViewById(R.id.et_id);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_commit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_commit:
                ;
                break;
        }
    }
}
