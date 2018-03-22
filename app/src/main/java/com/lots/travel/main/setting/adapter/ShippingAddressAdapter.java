package com.lots.travel.main.setting.adapter;

import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.lots.travel.R;
import com.lots.travel.main.setting.model.ShippingAddress;
import com.lots.travel.main.setting.widget.ShippingAddressLayout;
import com.lots.travel.widget.LinearItemsLayout;
import com.lots.travel.widget.SimpleTextWatcher;

/**
 * Created by nalanzi on 2018/1/10.
 */

public class ShippingAddressAdapter implements LinearItemsLayout.Adapter {
    public static final int REGION = 2;

    private final int[] names = new int[]{
            R.string.shipping_name,
            R.string.shipping_phone,
            R.string.shipping_region,
            R.string.shipping_detail_address,
            R.string.shipping_remark};

    private final int[] hints = new int[]{
            R.string.shipping_name_hint,
            R.string.shipping_phone_hint,
            R.string.shipping_region_hint,
            R.string.shipping_detail_address_hint,
            R.string.shipping_remark_hint};

    private ShippingAddressLayout[] vShippingLayouts = new ShippingAddressLayout[names.length];

    private TextWatcher mTextWatcher;

    public ShippingAddressAdapter(TextWatcher watcher) {
        mTextWatcher = watcher;
    }

    public void setShippingAddress(ShippingAddress item){
        vShippingLayouts[0].setValue(item.getName());
        vShippingLayouts[1].setValue(item.getPhone());
        setRegion(item.getProvince(),item.getCity(),item.getDistrict());
        vShippingLayouts[3].setValue(item.getDetailAddress());
        vShippingLayouts[4].setValue(item.getRemark());
    }

    public void getShippingAddress(ShippingAddress item){
        item.setName(vShippingLayouts[0].getValue());
        item.setPhone(vShippingLayouts[1].getValue());
        item.setDetailAddress(vShippingLayouts[3].getValue());
        item.setRemark(vShippingLayouts[4].getValue());
    }

    public void setRegion(String province,String city,String district){
        if(province!=null && city!=null && district!=null)
            vShippingLayouts[2].setValue(province+city+district);
    }

    public boolean isCompleted(){
        for (int i=0;i<vShippingLayouts.length-1;i++){
            String text = vShippingLayouts[i].getValue();
            if(TextUtils.isEmpty(text))
                return false;
        }
        return true;
    }

    @Override
    public boolean isClickable(int position) {
        return position==2;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public View getView(LayoutInflater inflater, LinearLayout container, int position) {
        ShippingAddressLayout v = (ShippingAddressLayout) inflater.inflate(R.layout.item_shipping_address,container,false);
        vShippingLayouts[position] = v;
        v.setName(names[position]);
        v.setValueHint(hints[position]);
        v.addTextWatcher(mTextWatcher);
        if(position==2)
            v.setEditable(false);
        return v;
    }

}
