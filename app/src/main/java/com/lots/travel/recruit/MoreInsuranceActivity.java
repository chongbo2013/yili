package com.lots.travel.recruit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lots.travel.R;
import com.lots.travel.base.BaseActivity;
import com.lots.travel.recruit.adapter.InsuranceItemsAdapter;
import com.lots.travel.recruit.model.Insurance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lWX479187 on 2017/10/30.
 */
public class MoreInsuranceActivity extends BaseActivity {
    private InsuranceItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_insurance);

        itemsAdapter = new InsuranceItemsAdapter(this);

        RecyclerView rvContent = (RecyclerView) findViewById(R.id.rv_content);
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        rvContent.setAdapter(itemsAdapter);

        List<Insurance> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            Insurance item = new Insurance();
            item.setId(1);
            item.setName("新华携程境外旅行保险");
            item.setPrice(50);
            item.setTarget("国内中长途旅行人员");
            item.setActiveTimeStart(System.currentTimeMillis());
            item.setActiveTimeEnd(System.currentTimeMillis()+24*60*60*1000*7L);
            item.setChecked(false);
            list.add(item);
        }
        itemsAdapter.setInsuranceList(list);
    }
}
