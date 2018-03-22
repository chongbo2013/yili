package com.lots.travel.main.info.mine;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;

import com.lots.travel.R;
import com.lots.travel.store.db.Area;
import com.lots.travel.util.AreaUtil;
import com.lots.travel.widget.PopupDialog;
import com.lots.travel.widget.picker.core.PickerAdapter;
import com.lots.travel.widget.picker.core.PickerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nalanzi on 2017/12/14.
 */
//assets中的转储至数据库，然后再取出
public class ChooseAreaDialog extends PopupDialog implements View.OnClickListener {
    private OnChooseListener onChooseListener;
    private PickerView vProvince,vCity,vDistrict;
    private AreaAdapter provinceAdapter,cityAdapter,districtAdapter;

    public ChooseAreaDialog(Context context,OnChooseListener listener) {
        super(context, R.layout.dialog_choose_area);
        onChooseListener = listener;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        Dialog dialog = getDialog();
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(this);
        dialog.findViewById(R.id.tv_confirm).setOnClickListener(this);
        vProvince = (PickerView) dialog.findViewById(R.id.v_province);
        provinceAdapter = new AreaAdapter();
        vProvince.setAdapter(provinceAdapter,0);
        provinceAdapter.setData(AreaUtil.query(AreaUtil.PID_PROVINCE));

        vCity = (PickerView) dialog.findViewById(R.id.v_city);
        cityAdapter = new AreaAdapter();
        vCity.setAdapter(cityAdapter,0);

        vDistrict = (PickerView) dialog.findViewById(R.id.v_district);
        districtAdapter = new AreaAdapter();
        vDistrict.setAdapter(districtAdapter,0);

        PickerView.OnPickListener provinceListener = new PickerView.OnPickListener() {
            @Override
            public void onPick(PickerView picker, int oldPosition, int newPosition) {
                Area province = provinceAdapter.areas.get(newPosition);
                cityAdapter.setData(AreaUtil.query(province.getAid()));
                if(cityAdapter.areas.size()==0) {
                    districtAdapter.setData(null);
                    return;
                }else{
                    vCity.setValueIndex(0);
                }
                Area city = cityAdapter.areas.get(0);
                districtAdapter.setData(AreaUtil.query(city.getAid()));
            }
        };
        vProvince.setOnPickListener(provinceListener);
        PickerView.OnPickListener cityListener = new PickerView.OnPickListener() {
            @Override
            public void onPick(PickerView picker, int oldPosition, int newPosition) {
                Area city = cityAdapter.areas.get(newPosition);
                districtAdapter.setData(AreaUtil.query(city.getAid()));
            }
        };
        vCity.setOnPickListener(cityListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_confirm:
                String province = provinceAdapter.getText(vProvince.getValueIndex());
                String city = cityAdapter.getText(vCity.getValueIndex());
                String district = districtAdapter.getText(vDistrict.getValueIndex());
                if(onChooseListener!=null)
                    onChooseListener.onChoose(province,city,district);
                break;
        }

        dismiss();
    }

    public void update(String province,String city,String district){
        Area aProvince = null;
        if(TextUtils.isEmpty(province)){
            aProvince = provinceAdapter.areas.size()>0 ? provinceAdapter.areas.get(0):null;
        }else{
            for (int i=0;i<provinceAdapter.areas.size();i++){
                if(provinceAdapter.areas.get(i).getName().equals(province)) {
                    aProvince = provinceAdapter.areas.get(i);
                    vProvince.setValueIndex(i);
                    break;
                }
            }
            if (aProvince==null && provinceAdapter.areas.size()>0){
                aProvince = provinceAdapter.areas.get(0);
            }
        }

        if(aProvince==null){
            cityAdapter.setData(null);
            districtAdapter.setData(null);
            return;
        }

        cityAdapter.setData(AreaUtil.query(aProvince.getAid()));
        Area aCity = null;
        if(TextUtils.isEmpty(city)){
            aCity = cityAdapter.areas.size()>0 ? cityAdapter.areas.get(0):null;
        }else{
            for (int i=0;i<cityAdapter.areas.size();i++){
                if(cityAdapter.areas.get(i).getName().equals(city)) {
                    aCity = cityAdapter.areas.get(i);
                    vCity.setValueIndex(i);
                    break;
                }
            }
            if (aCity==null && cityAdapter.areas.size()>0){
                aCity = cityAdapter.areas.get(0);
            }
        }

        if(aCity==null){
            districtAdapter.setData(null);
            return;
        }

        districtAdapter.setData(AreaUtil.query(aCity.getAid()));
        if(TextUtils.isEmpty(district)){
            if(districtAdapter.areas.size()>0)
                vDistrict.setValueIndex(0);
        }else{
            for (int i=0;i<districtAdapter.areas.size();i++){
                if(districtAdapter.areas.get(i).getName().equals(district)) {
                    vDistrict.setValueIndex(i);
                    break;
                }
            }
        }
    }

    public void show(String province,String city,String district) {
        update(province, city, district);
        super.show();
    }

    private class AreaAdapter extends PickerAdapter{
        private List<Area> areas;

        AreaAdapter(){
            areas = new ArrayList<>();
        }

        void setData(List<Area> src){
            areas.clear();
            if(src!=null && src.size()>0)
                areas.addAll(src);
            notifyDataSetChanged();
        }

        @Override
        public String getText(int pos) {
            return areas.get(pos).getName();
        }

        @Override
        public Drawable getIcon(int pos) {
            return null;
        }

        @Override
        public int getCount() {
            return areas.size();
        }
    }

    public interface OnChooseListener{
        void onChoose(String province,String city,String district);
    }
    
}
