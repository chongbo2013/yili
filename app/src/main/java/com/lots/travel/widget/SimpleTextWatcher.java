package com.lots.travel.widget;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Author： liyi
 * Date：    2017/2/22.
 */

public class SimpleTextWatcher implements TextWatcher{
    private int id;
    private OnTextListener listener;

    public SimpleTextWatcher(){
        this(-1,null);
    }

    public SimpleTextWatcher(int id, OnTextListener listener){
        this.id = id;
        this.listener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if(listener!=null)
            listener.onTextChanged(id,s.toString());
    }

    public interface OnTextListener{
        void onTextChanged(int id,String text);
    }

}
