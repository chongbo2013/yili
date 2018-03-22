package com.lots.travel.schedule.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.util.CommonUtil;
import com.lots.travel.widget.SimpleTextWatcher;

/**
 * Created by nalanzi on 2018/1/15.
 */

public class SearchSpotsView extends FrameLayout {
    private TextView tvHint;
    private EditText etInput;
    private ImageView ivClear;

    private OnSearchListener mOnSearchListener;

    public SearchSpotsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_search_spots,this,true);
        tvHint = (TextView) findViewById(R.id.tv_hint);
        etInput = (EditText) findViewById(R.id.et_input);
        ivClear = (ImageView) findViewById(R.id.iv_clear);

        etInput.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showVisibility(etInput.isFocused(),TextUtils.isEmpty(s.toString()));
                tvHint.setText(s);
            }
        });

        etInput.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showVisibility(hasFocus, TextUtils.isEmpty(etInput.getText().toString()));
            }
        });

        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    if(mOnSearchListener!=null)
                        mOnSearchListener.onSearch(etInput.getText().toString());

                    etInput.setVisibility(View.INVISIBLE);
                    CommonUtil.hideSoftInput(etInput);
                }
                return false;
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                etInput.setVisibility(View.VISIBLE);
                etInput.requestFocus();
                CommonUtil.showSoftInput(etInput);
            }
        });

        ivClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                etInput.setText("");
            }
        });

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.SearchSpotsView);
        String hint = a.getString(R.styleable.SearchSpotsView_android_hint);
        tvHint.setHint(hint);
        showVisibility(false,true);
        a.recycle();
    }

    public void setText(String text){
        etInput.setText(text);
    }

    public void setOnSearchListener(OnSearchListener listener){
        mOnSearchListener = listener;
    }

    private void showVisibility(boolean focused, boolean empty){
        tvHint.setVisibility(!focused ? View.VISIBLE:View.INVISIBLE);
        ivClear.setVisibility(focused&&!empty ? View.VISIBLE:View.INVISIBLE);
        etInput.setVisibility(focused ? View.VISIBLE:View.INVISIBLE);
    }

    public interface OnSearchListener{
        void onSearch(String text);
    }

}
