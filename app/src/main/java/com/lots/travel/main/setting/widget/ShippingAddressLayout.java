package com.lots.travel.main.setting.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lots.travel.R;

/**
 * Created by nalanzi on 2018/1/11.
 */

public class ShippingAddressLayout extends FrameLayout {
    private TextView tvName;
    private EditText etValue;

    public ShippingAddressLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_shipping_address,this,true);
        tvName = (TextView) findViewById(R.id.tv_name);
        etValue = (EditText) findViewById(R.id.et_value);
    }

    public void setEditable(boolean editable){
        if(editable)
            return;
        etValue.setKeyListener(null);
        etValue.setFocusable(false);
        etValue.setFocusableInTouchMode(false);
    }

    public void setName(int id){
        tvName.setText(id);
    }

    public void setValueHint(int id){
        etValue.setHint(id);
    }

    public void setValue(String str){
        etValue.setText(str);
    }

    public String getValue(){
        return etValue.getText().toString();
    }

    public void addTextWatcher(TextWatcher watcher){
        etValue.addTextChangedListener(watcher);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        return isClickable()
                && (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_UP)
                || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        int l = etValue.getLeft();
        int t = etValue.getTop();
        int r = etValue.getRight();
        int b = etValue.getBottom();
        return x>=l && x<=r && y>=t && y<=b && super.onTouchEvent(e);
    }

}
