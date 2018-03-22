package com.lots.travel.main.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lots.travel.AccountManager;
import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.main.info.mine.ChooseAreaDialog;
import com.lots.travel.main.setting.adapter.ShippingAddressAdapter;
import com.lots.travel.main.setting.model.ShippingAddress;
import com.lots.travel.network.DefaultExceptionHandler;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.SingleAdapter;
import com.lots.travel.network.model.request.SaveShipping;
import com.lots.travel.network.service.CommonService;
import com.lots.travel.widget.LinearItemsLayout;
import com.lots.travel.widget.SimpleTextWatcher;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nalanzi on 2018/1/10.
 */

public class AddShippingAddressActivity extends RxBaseActivity implements View.OnClickListener {
    public static final String SHIPPING_ADDRESS = "ShippingAddress";

    public static void toAdd(Activity context,int requestCode){
        Intent intent = new Intent(context,AddShippingAddressActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivityForResult(intent,requestCode);
    }

    private ShippingAddress mShippingAddress;
    private ShippingAddressAdapter mShippingAddressAdapter;
    private ChooseAreaDialog dlgChooseArea;
    private TextView tvSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);
        initArgs(getIntent(),savedInstanceState);
        initViews();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mShippingAddressAdapter.getShippingAddress(mShippingAddress);
        outState.putParcelable(SHIPPING_ADDRESS,mShippingAddress);
    }

    private void initArgs(Intent data, Bundle savedInstanceState){
        if(savedInstanceState!=null)
            mShippingAddress = savedInstanceState.getParcelable(SHIPPING_ADDRESS);
        if(mShippingAddress==null)
            mShippingAddress = new ShippingAddress();
    }

    private void initViews(){
        dlgChooseArea = new ChooseAreaDialog(this, new ChooseAreaDialog.OnChooseListener() {
            @Override
            public void onChoose(String province, String city, String district) {
                mShippingAddress.setProvince(province);
                mShippingAddress.setCity(city);
                mShippingAddress.setDistrict(district);
                mShippingAddressAdapter.setRegion(province,city,district);
            }
        });

        findViewById(R.id.iv_back).setOnClickListener(this);
        tvSave = (TextView) findViewById(R.id.tv_save);
        tvSave.setOnClickListener(this);

        LinearItemsLayout llItems = (LinearItemsLayout) findViewById(R.id.ll_items);
        llItems.setOnItemClickListener(new LinearItemsLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(position==ShippingAddressAdapter.REGION){
                    dlgChooseArea.show(mShippingAddress.getProvince(),mShippingAddress.getCity(),mShippingAddress.getDistrict());
                }
            }
        });
        mShippingAddressAdapter = new ShippingAddressAdapter(new SimpleTextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvSave.setEnabled(mShippingAddressAdapter.isCompleted());
            }
        });
        llItems.setAdapter(mShippingAddressAdapter);
        mShippingAddressAdapter.setShippingAddress(mShippingAddress);
    }

    @Override
    protected void onDestroy() {
        if(dlgChooseArea!=null)
            dlgChooseArea.dismiss();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        back(false);
    }

    private void back(boolean success){
        setResult(success ? Activity.RESULT_OK:Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                back(false);
                break;

            case R.id.tv_save:
                mShippingAddressAdapter.getShippingAddress(mShippingAddress);

                SaveShipping params = new SaveShipping();
                params.setUserId(""+AccountManager.getInstance().getUserId());
                params.setConsignee(mShippingAddress.getName());
                params.setPhone(mShippingAddress.getPhone());
                params.setProvince(mShippingAddress.getProvince());
                params.setCity(mShippingAddress.getCity());
                params.setDistrict(mShippingAddress.getDistrict());
                params.setAddress(mShippingAddress.getDetailAddress());
                params.setDescript(mShippingAddress.getRemark());
                ServiceGenerator.createService(this, CommonService.class)
                        .saveShippingAddress(params)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleAdapter<HttpResult<SaveShipping>>() {
                            @Override
                            public void onSuccess(@NonNull HttpResult<SaveShipping> result) {
                                Toast.makeText(AddShippingAddressActivity.this,R.string.shipping_add_success,Toast.LENGTH_SHORT).show();
                                back(true);
                            }

                            @Override
                            public void onError(Throwable e) {
                                new DefaultExceptionHandler().handleException(AddShippingAddressActivity.this,e);
                            }
                        });
                break;
        }
    }

}
