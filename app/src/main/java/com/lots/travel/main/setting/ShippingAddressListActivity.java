package com.lots.travel.main.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.main.setting.adapter.ShippingAddressListAdapter;
import com.lots.travel.main.setting.model.ShippingAddress;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.SingleAdapter;
import com.lots.travel.network.model.request.Empty;
import com.lots.travel.network.model.result.GetShippingResult;
import com.lots.travel.network.service.UserService;
import com.lots.travel.util.DensityUtil;
import com.lots.travel.widget.VerticalLinearSpaceItemDecoration;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nalanzi on 2018/1/10.
 */

public class ShippingAddressListActivity extends RxBaseActivity implements View.OnClickListener,ShippingAddressListAdapter.OnItemClickListener {
    private SwipeRefreshLayout swipeRefresh;
    private ShippingAddressListAdapter mAddressListAdapter;
    private TextView tvAdd;

    private static final int REQ_EDIT = 0;
    private static final int REQ_ADD = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address_list);
        initArgs(getIntent(),savedInstanceState);
        initViews();
    }

    private void initArgs(Intent data,Bundle savedInstanceState){}

    private void initViews(){
        findViewById(R.id.iv_back).setOnClickListener(this);
        tvAdd = (TextView) findViewById(R.id.tv_add);
        tvAdd.setOnClickListener(this);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.color_main);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestLoadAddressList();
            }
        });

        RecyclerView rvContent = (RecyclerView) findViewById(R.id.rv_content);
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        rvContent.addItemDecoration(new VerticalLinearSpaceItemDecoration(DensityUtil.dp2px(this,10)));
        mAddressListAdapter = new ShippingAddressListAdapter(rvContent);
        mAddressListAdapter.setOnItemClickListener(this);
        rvContent.setAdapter(mAddressListAdapter);
    }

    private void loadData(){
        swipeRefresh.setRefreshing(true);
        requestLoadAddressList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_add:
                AddShippingAddressActivity.toAdd(this,REQ_ADD);
                break;
        }
    }

    @Override
    public void onMakeDefault(int position, boolean checked) {
        mAddressListAdapter.makeDefault(position,checked);
    }

    @Override
    public void onEdit(int position, ShippingAddress item) {
        EditShippingAddressActivity.toEdit(this,REQ_EDIT,item);
    }

    @Override
    public void onDelete(int position) {
        mAddressListAdapter.removeItem(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode!= Activity.RESULT_OK)
            return;

        switch (requestCode){
            case REQ_EDIT:
            case REQ_ADD:
                swipeRefresh.setRefreshing(true);
                requestLoadAddressList();
                break;
        }
    }

    private void requestLoadAddressList(){
        ServiceGenerator.createService(this, UserService.class)
                .getShippingAddress(new Empty())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<HttpResult<GetShippingResult>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new SingleAdapter<HttpResult<GetShippingResult>>() {
                    @Override
                    public void onSuccess(@NonNull HttpResult<GetShippingResult> result) {
                        List<ShippingAddress> src = new ArrayList<>();
                        if(result!=null && result.getData()!=null) {
                            GetShippingResult.ShippingBean bean = result.getData().getUserbyaddress();
                            if(bean!=null){
                                ShippingAddress item = new ShippingAddress();
                                item.setName(bean.getConsignee());
                                item.setPhone(bean.getPhone());
                                item.setProvince(bean.getProvince());
                                item.setCity(bean.getCity());
                                item.setDistrict(bean.getDistrict());
                                item.setDetailAddress(bean.getAddress());
                                item.setAsDefault(true);
                                src.add(item);
                            }
                        }
                        tvAdd.setVisibility(src.size()==0 ? View.VISIBLE:View.INVISIBLE);
                        mAddressListAdapter.setData(src);
                        swipeRefresh.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mAddressListAdapter.setData(null);
                        swipeRefresh.setRefreshing(false);
                    }
                });
    }

}
