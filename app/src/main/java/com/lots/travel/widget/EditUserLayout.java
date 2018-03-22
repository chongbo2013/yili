package com.lots.travel.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lots.travel.R;

/**
 * Created by lWX479187 on 2017/11/29.
 */
public class EditUserLayout extends FrameLayout {
    private EditText etValue;

    public EditUserLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_edit_user,this,true);

        TextView tvLabel = (TextView) findViewById(R.id.tv_label);
        etValue = (EditText) findViewById(R.id.et_value);

        final float scale = context.getResources().getDisplayMetrics().density;

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.EditUserLayout);
        if(a.getBoolean(R.styleable.EditUserLayout_showStar,false)){
            Drawable star = new StarDrawable(Color.RED,(int)(14*scale + 0.5f));
            star.setBounds(0,0,star.getIntrinsicWidth(),star.getIntrinsicHeight());
            etValue.setCompoundDrawables(star,null,null,null);

            int starPadding = a.getDimensionPixelSize(R.styleable.EditUserLayout_starPadding,(int)(4*scale + 0.5f));
            etValue.setCompoundDrawablePadding(starPadding);

            FrameLayout.LayoutParams lp = (LayoutParams) etValue.getLayoutParams();
            lp.leftMargin -= starPadding+star.getIntrinsicWidth();
        }

        String text = a.getString(R.styleable.EditUserLayout_editLabel);
        int textSize = a.getDimensionPixelSize(R.styleable.EditUserLayout_editLabelSize,(int)(14*scale + 0.5f));
        int textColor = a.getColor(R.styleable.EditUserLayout_editLabelColor,0xFF696969);
        tvLabel.setText(text);
        tvLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
        tvLabel.setTextColor(textColor);

        if(!a.getBoolean(R.styleable.EditUserLayout_canEdit,true)){
            etValue.setFocusable(false);
            etValue.setFocusableInTouchMode(false);
            etValue.setKeyListener(null);
        }
        text = a.getString(R.styleable.EditUserLayout_editValue);
        textSize = a.getDimensionPixelSize(R.styleable.EditUserLayout_editValueSize,(int)(14*scale + 0.5f));
        textColor = a.getColor(R.styleable.EditUserLayout_editValueColor,0xFF696969);
        etValue.setText(text);
        etValue.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
        etValue.setTextColor(textColor);
        text = a.getString(R.styleable.EditUserLayout_editHint);
        textColor = a.getColor(R.styleable.EditUserLayout_editHintColor,0xFFADADAD);
        etValue.setHint(text);
        etValue.setHintTextColor(textColor);
        if(a.getBoolean(R.styleable.EditUserLayout_isDigit,false)){
            etValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        a.recycle();
    }

    public void setText(int text){
        etValue.setText(text);
    }

    public void setText(CharSequence text){
        etValue.setText(text);
    }

    public String getText(){
        return etValue.getText().toString();
    }

    public boolean isEmpty(){
        return TextUtils.isEmpty(etValue.getText());
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
