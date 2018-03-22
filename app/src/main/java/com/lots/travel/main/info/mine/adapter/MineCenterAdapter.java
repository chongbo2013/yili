package com.lots.travel.main.info.mine.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.widget.LinearItemsLayout;

/**
 * Created by nalanzi on 2017/9/25.
 */

public class MineCenterAdapter implements LinearItemsLayout.Adapter{
    public static final int ITEM_ORDER = 0;

    public static final int ITEM_TRIP = 1;

    public static final int ITEM_ACTIVITY = 2;

    public static final int ITEM_CROW_FUNDING = 3;

    public static final int ITEM_REPLAY = 4;

    public static final int ITEM_WALLET = 5;

    public static final int ITEM_FAVORITE = 6;

    public static final int ITEM_WANT_DST = 7;

    public static final int ITEM_FOLLOW = 8;

    public static final int ITEM_LABEL = 9;

    public static final int ITEM_GROUP = 10;

    private Context context;

    private int[] texts = new int[]{
            R.string.mine_content_order,R.string.mine_content_trip,R.string.mine_content_activity,
            R.string.mine_content_crowd_funding, R.string.mine_content_repay, R.string.mine_content_wallet,
            R.string.mine_content_favorite, R.string.mine_content_want_dst,R.string.mine_content_follow,
            R.string.mine_content_label, R.string.mine_content_group
        };

    private int[] icons = new int[]{
            R.drawable.ico_mine_order,R.drawable.ico_mine_xingcheng,R.drawable.ico_mine_xingcheng,
            R.drawable.ico_mine_zhongchou, R.drawable.ico_mine_return, R.drawable.ico_mine_wallet,
            R.drawable.ico_mine_collect, R.drawable.ico_mine_destination,R.drawable.ico_mine_frends,
            R.drawable.ico_mine_lable, R.drawable.ico_mine_group
    };

    public MineCenterAdapter(Context context){
        this.context = context;
    }

    @Override
    public boolean isClickable(int position) {
        return true;
    }

    @Override
    public int getCount() {
        return texts.length;
    }

    @Override
    public View getView(LayoutInflater inflater, LinearLayout container, int position) {
        TextView root = (TextView) inflater.inflate(R.layout.item_mine_content,container,false);
        root.setText(texts[position]);

        Drawable drawableLeft = ContextCompat.getDrawable(context,icons[position]);
        drawableLeft.setBounds(0,0,drawableLeft.getIntrinsicWidth(),drawableLeft.getIntrinsicHeight());

        Drawable drawableRight = ContextCompat.getDrawable(context,R.drawable.arrow_right_gray);
        drawableRight.setBounds(0,0,drawableRight.getIntrinsicWidth(),drawableRight.getIntrinsicHeight());

        root.setCompoundDrawables(drawableLeft,null,drawableRight,null);
        return root;
    }

}
