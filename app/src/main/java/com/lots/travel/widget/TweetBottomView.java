package com.lots.travel.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.util.CommonUtil;

/**
 * Created by nalanzi on 2018/1/24.
 */

public class TweetBottomView extends RelativeLayout implements View.OnClickListener{
    private EditText etInput;
    private ImageView ivFace;
    private TextView tvSend;
    private FrameLayout flFaces;

    private OnTweetListener mOnTweetListener;

    public TweetBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_tweet_bottom,this,true);

        etInput = (EditText) findViewById(R.id.et_input);
        ivFace = (ImageView) findViewById(R.id.iv_face);
        tvSend = (TextView) findViewById(R.id.tv_send);
        flFaces = (FrameLayout) findViewById(R.id.fl_faces);

        etInput.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvSend.setEnabled(s.length()>0);
            }
        });
        tvSend.setOnClickListener(this);
    }

    public void setOnTweetListener(OnTweetListener listener){
        mOnTweetListener = listener;
    }

    public void setHint(String hint){
        etInput.setHint(hint);
    }

    public void clear(){
        etInput.getText().clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_send:
                if(mOnTweetListener!=null)
                    mOnTweetListener.onSend(etInput.getText().toString());
                CommonUtil.hideSoftInput(etInput);
                break;
        }
    }

    public interface OnTweetListener{
        void onSend(String text);
    }

}
