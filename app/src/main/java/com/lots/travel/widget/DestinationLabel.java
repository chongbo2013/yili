package com.lots.travel.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lots.travel.R;

/**
 * Created by lWX479187 on 2017/11/10.
 */
public class DestinationLabel extends RelativeLayout {
    private final int[] ATTRS = new int[]{android.R.attr.text};

    private TextView tvDestination;

    public DestinationLabel(Context context) {
        this(context,null);
    }

    public DestinationLabel(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.layout_destination_label,this,true);
        tvDestination = (TextView) findViewById(R.id.tv_destination);

        TypedArray a = context.obtainStyledAttributes(attrs,ATTRS);
        tvDestination.setText(a.getText(0));
        a.recycle();
    }

    public void setDestination(CharSequence text){
        tvDestination.setText(text);
    }

}
