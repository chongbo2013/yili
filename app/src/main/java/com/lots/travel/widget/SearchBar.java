package com.lots.travel.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.TintTypedArray;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lots.travel.R;

/**
 * Created by nalanzi on 2017/9/23.
 */

public class SearchBar extends FrameLayout {
    private boolean importable;
    private boolean immediate;

    private EditText etInput;
    private TextView tvInput;
    private ImageView ivClear;

    private OnSearchListener mOnSearchListener;

    public SearchBar(@NonNull Context context){
        this(context,null);
    }

    public SearchBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.layout_searchbar,this,true);
        etInput = (EditText) findViewById(R.id.et_input);
        tvInput = (TextView) findViewById(R.id.tv_input);
        ivClear = (ImageView) findViewById(R.id.iv_clear);

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.SearchBar);
        importable = a.getBoolean(R.styleable.SearchBar_importable,false);
        immediate = a.getBoolean(R.styleable.SearchBar_immediate,false);
        CharSequence hint = a.getText(R.styleable.SearchBar_android_hint);
        tvInput.setHint(hint);
        etInput.setHint(hint);
        a.recycle();

        if(!importable){
            tvInput.setVisibility(View.VISIBLE);
            etInput.setVisibility(View.GONE);
        }else{
            tvInput.setVisibility(View.VISIBLE);
            etInput.setVisibility(View.INVISIBLE);
        }

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!importable){
                    if(mOnSearchListener!=null)
                        mOnSearchListener.onSearch(null);
                }else{
                    tvInput.setVisibility(View.INVISIBLE);
                    etInput.setVisibility(View.VISIBLE);

                    requestFocusAndKeyboard();
                }
            }
        });
        etInput.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CharSequence text = etInput.getText();
                if(importable && text.length()==0) {
                    tvInput.setVisibility(hasFocus ? View.INVISIBLE : View.VISIBLE);
                    etInput.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                }

                showClearIcon(text,etInput.isFocused());
            }
        });
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    if(mOnSearchListener!=null)
                        mOnSearchListener.onSearch(etInput.getText());

                    hideSoftInput(etInput);
                    //etInput.clearFocus();
                }
                return false;
            }
        });

        ivClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                etInput.getText().clear();
                if(importable && mOnSearchListener!=null)
                    mOnSearchListener.onSearch(null);
            }
        });

        etInput.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(immediate && mOnSearchListener!=null)
                    mOnSearchListener.onSearch(s);

                showClearIcon(s,etInput.isFocused());
            }
        });

        setBackgroundResource(R.drawable.bg_searchbar);
    }

    public void requestFocusAndKeyboard(){
        etInput.requestFocus();
        showSoftInput(etInput);
    }

    public void setSearchHint(CharSequence hint){
        etInput.setHint(hint);
        tvInput.setHint(hint);
    }

    public void setSearchText(CharSequence text,boolean search){
        etInput.setText(text);
        etInput.setSelection(etInput.getText().length());

        if (!TextUtils.isEmpty(text))
            etInput.requestFocus();

        if(importable) {
            tvInput.setVisibility(View.INVISIBLE);
            etInput.setVisibility(View.VISIBLE);
        }

        if(search && mOnSearchListener!=null)
            mOnSearchListener.onSearch(text);
    }

    private void showClearIcon(CharSequence text,boolean hasFocused){
        ivClear.setVisibility(
                importable && !TextUtils.isEmpty(text) && hasFocused ? View.VISIBLE:View.INVISIBLE);
    }

    public static void showSoftInput(View view){
        Context context = view.getContext();
        InputMethodManager im = (InputMethodManager)context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if(view.getWindowToken()!=null)
            im.showSoftInput(view, 0);
    }

    public static void hideSoftInput(View view){
        Context context = view.getContext();
        InputMethodManager im = (InputMethodManager)context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if(view.getWindowToken()!=null)
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void setOnSearchListener(OnSearchListener listener){
        mOnSearchListener = listener;
    }

    public interface OnSearchListener{
        void onSearch(CharSequence text);
    }

}
