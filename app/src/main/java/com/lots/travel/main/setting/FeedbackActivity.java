package com.lots.travel.main.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.lots.travel.R;

/**
 * Created by nalanzi on 2018/1/11.
 */

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etFeedback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_completed).setOnClickListener(this);
        etFeedback = (EditText) findViewById(R.id.et_feedback);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_completed:
                String text = etFeedback.getText().toString();
                break;
        }
    }
}
