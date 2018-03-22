package com.lots.travel.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.base.BaseActivity;

/**
 * Created by lWX479187 on 2017/10/17.
 */
public class EditTextActivity extends BaseActivity implements View.OnClickListener{
    public static final String CONFIG = "Config";
    public static final String TEXT = "Text";

    private Config mConfig;
    private EditText etInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);

        initData(getIntent(),savedInstanceState);
        initViews();
    }

    private void initData(Intent intent,Bundle saved){
        mConfig = intent.getParcelableExtra(CONFIG);
        if(saved!=null) {
            mConfig = saved.getParcelable(CONFIG);
        }
    }

    private void initViews(){
        etInput = (EditText) findViewById(R.id.et_input);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_save).setOnClickListener(this);

        if(mConfig.title!=null){
            TextView tvTitle = (TextView) findViewById(R.id.tv_title);
            tvTitle.setText(mConfig.title);
        }
        if(mConfig.text!=null) {
            etInput.setText(mConfig.text);
            etInput.setSelection(etInput.getText().length());
        }
        if(mConfig.hint!=null){
            etInput.setHint(mConfig.hint);
        }
        if(mConfig.inputType!=InputType.TYPE_NULL){
            etInput.setInputType(mConfig.inputType);
        }
        if(mConfig.maxLength>0){
            etInput.setMaxEms(mConfig.maxLength);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CONFIG, mConfig);
    }

    @Override
    public void onBackPressed() {
        back(false,null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                back(false,null);
                break;

            case R.id.tv_save:
                String text = etInput.getText().toString();
                back(true,text);
                break;
        }
    }

    private void back(boolean success,String text){
        if(!success)
            setResult(Activity.RESULT_CANCELED);
        else{
            Intent data = new Intent();
            data.putExtra(TEXT,text);
            setResult(Activity.RESULT_OK,data);
        }
        finish();
    }

    public static class Config implements Parcelable{
        private String title,text,hint;
        private int inputType;
        private int maxLength;

        public Config(){
            inputType = InputType.TYPE_NULL;
            maxLength = -1;
        }

        protected Config(Parcel in) {
            title = in.readString();
            text = in.readString();
            hint = in.readString();
            inputType = in.readInt();
            maxLength = in.readInt();
        }

        public static final Creator<Config> CREATOR = new Creator<Config>() {
            @Override
            public Config createFromParcel(Parcel in) {
                return new Config(in);
            }

            @Override
            public Config[] newArray(int size) {
                return new Config[size];
            }
        };

        public Config title(String title){
            this.title = title;
            return this;
        }

        public Config text(String text){
            this.text = text;
            return this;
        }

        public Config hint(String hint){
            this.hint = hint;
            return this;
        }

        public Config inputType(int inputType){
            this.inputType = inputType;
            return this;
        }

        public Config maxLength(int maxLength){
            this.maxLength = maxLength;
            return this;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeString(text);
            dest.writeString(hint);
            dest.writeInt(inputType);
            dest.writeInt(maxLength);
        }
    }

    public static void toEdit(Activity activity,int requestCode,Config config){
        Intent intent = new Intent(activity,EditTextActivity.class);
        intent.putExtra(CONFIG,config);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivityForResult(intent,requestCode);
    }

}
