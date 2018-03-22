package com.lots.travel.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lots.travel.R;

/**
 * Author： liyi
 * Date：    2017/2/21.
 */

public class ValueEditText extends LinearLayout implements TextWatcher,View.OnClickListener,View.OnFocusChangeListener{
    private ImageView mDeleteIco;
    private EditText mEditText;

    public ValueEditText(Context context) {
        this(context,null);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);

        if(child instanceof EditText){
            int count = getChildCount();
            if(count!=1)
                throw new IllegalArgumentException("layout文件中只需要一个EditText作为child");

            mEditText = (EditText) child;
            mEditText.setBackgroundColor(Color.TRANSPARENT);
            LayoutInflater.from(getContext()).inflate(R.layout.layout_edittext_value,this,true);
            mDeleteIco = (ImageView) findViewById(R.id.delete);
            setAddStatesFromChildren(true);

            mEditText.addTextChangedListener(this);
            mEditText.setOnFocusChangeListener(this);
            mDeleteIco.setOnClickListener(this);

            updateDeleteIcon(mEditText.getText().toString(),mEditText.isFocused());
        }
    }

    public ValueEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updateDeleteIcon(s.toString(),mEditText.isFocused());
    }

    @Override
    public void afterTextChanged(Editable s) {}

    @Override
    public void onClick(View v) {
        mEditText.setText("");
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        updateDeleteIcon(mEditText.getText().toString(),hasFocus);
    }

    private void updateDeleteIcon(String text,boolean focused){
        if(!TextUtils.isEmpty(text) && focused)
            mDeleteIco.setVisibility(View.VISIBLE);
        else
            mDeleteIco.setVisibility(View.INVISIBLE);
    }

}
