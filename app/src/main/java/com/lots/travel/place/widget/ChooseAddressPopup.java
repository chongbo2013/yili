package com.lots.travel.place.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.lots.travel.R;
import com.lots.travel.place.adapter.MenuSpinnerAdapter;
import com.lots.travel.util.DensityUtil;

/**
 * Created by nalanzi on 2017/9/3.
 */

public class ChooseAddressPopup {
    private final int X_OFFSET;
    private final int Y_OFFSET;
    private PopupWindow mPopup;
    private MenuSpinnerAdapter mMenuSpinnerAdapter;

    private OnItemClickListener onItemClickListener;

    public ChooseAddressPopup(Context context,MenuSpinnerAdapter adapter) {
        X_OFFSET = DensityUtil.dp2px(context,80);
        Y_OFFSET = (int) ((context.getResources().getDimension(R.dimen.actionBarSize)-DensityUtil.dp2px(context,32))/2);

        mPopup = new PopupWindow(context);
        ListView listView = (ListView) LayoutInflater.from(context).inflate(R.layout.popup_choose_address,null);
        mPopup.setContentView(listView);

        mMenuSpinnerAdapter = adapter;
        listView.setAdapter(mMenuSpinnerAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(onItemClickListener!=null)
                    onItemClickListener.onItemClick(mMenuSpinnerAdapter.getItem(position));
            }
        });

        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int widthSpec = View.MeasureSpec.makeMeasureSpec(DensityUtil.dp2px(context,200), View.MeasureSpec.EXACTLY);
        listView.measure(widthSpec,heightSpec);

        mPopup.setWidth(listView.getMeasuredWidth());
        mPopup.setHeight(listView.getMeasuredHeight() > DensityUtil.dp2px(context,240) ?
                DensityUtil.dp2px(context,240):WindowManager.LayoutParams.WRAP_CONTENT);

        mPopup.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.popup_choose_filer_conditions));
        mPopup.setOutsideTouchable(true);
        mPopup.setTouchable(true);
        mPopup.setFocusable(true);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    public void show(View anchor){
        mPopup.showAsDropDown(anchor,anchor.getWidth()/2-X_OFFSET,Y_OFFSET+4);
    }

    public void dismiss(){
        if(mPopup.isShowing())
            mPopup.dismiss();
    }

    public interface OnItemClickListener{
        void onItemClick(MenuSpinnerAdapter.Item item);
    }

}
