package com.lots.travel.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;

import org.w3c.dom.Text;

/**
 * Created by nalanzi on 2017/9/12.
 */

public class CreateSpotActivity extends RxBaseActivity implements View.OnClickListener{
    private EditText etName;

    private ImageView ivCover;
    private TextView tvCoverChange;
    private TextView tvCoverUpload;

    private EditText etDesc;

    private TextView tvLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_spot);

        findViewById(R.id.iv_back).setOnClickListener(this);

        etName = (EditText) findViewById(R.id.et_name);

        ivCover = (ImageView) findViewById(R.id.iv_cover);
        tvCoverChange = (TextView) findViewById(R.id.tv_cover_change);
        tvCoverUpload = (TextView) findViewById(R.id.tv_cover_upload);

        etDesc = (EditText) findViewById(R.id.et_desc);

        tvLocation = (TextView) findViewById(R.id.tv_location);

        findViewById(R.id.btn_save).setOnClickListener(this);

        showCoverEmpty();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_cover_change:
                ;
                break;

            case R.id.tv_cover_upload:
                ;
                break;

            case R.id.tv_location:
                ;
                break;

            case R.id.btn_save:
                ;
                break;
        }
    }

    private void showCoverEmpty(){
        tvCoverChange.setVisibility(View.INVISIBLE);
        tvCoverUpload.setVisibility(View.VISIBLE);
        tvCoverChange.setEnabled(false);
        tvCoverUpload.setEnabled(true);
    }

    private void showCoverNotEmpty(){
        tvCoverChange.setVisibility(View.VISIBLE);
        tvCoverUpload.setVisibility(View.INVISIBLE);
        tvCoverChange.setEnabled(true);
        tvCoverUpload.setEnabled(false);
    }

}
