package com.lots.travel.schedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.schedule.note.adapter.SearchPlaceAdapter;
import com.lots.travel.store.db.GreenDaoManager;
import com.lots.travel.store.db.ViewCity;
import com.lots.travel.store.db.ViewCityDao;
import com.lots.travel.widget.SearchBar;

import java.util.List;

/**
 * Created by nalanzi on 2017/9/23.
 */

public class SearchPlaceActivity extends RxBaseActivity implements View.OnClickListener,SearchBar.OnSearchListener{
    private SearchBar searchBar;
    private TextView tvTip;
    private SearchPlaceAdapter placeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_place);

        findViewById(R.id.iv_back).setOnClickListener(this);

        tvTip = (TextView) findViewById(R.id.tv_tip);

        searchBar = (SearchBar) findViewById(R.id.search_bar);
        searchBar.requestFocusAndKeyboard();
        searchBar.setOnSearchListener(this);

        placeAdapter = new SearchPlaceAdapter();
        RecyclerView rvContent = (RecyclerView) findViewById(R.id.rv_content);
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        rvContent.setAdapter(placeAdapter);

        placeAdapter.setOnItemClickListener(new SearchPlaceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewCity spot) {
                back(spot);
            }
        });

        loadHotCities();
    }

    @Override
    public void onBackPressed() {
        back(null);
    }

    public static final String EXTRA_PLACE = "searchPlace";

    public static void toSearch(Activity context,int requestCode){
        Intent intent = new Intent(context,SearchPlaceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivityForResult(intent,requestCode);
    }

    public ViewCity parseData(Intent data){
        return data.getParcelableExtra(EXTRA_PLACE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                back(null);
                break;
        }
    }

    public void back(ViewCity spot){
        if(spot==null){
            setResult(RESULT_CANCELED);
        }else{
            Intent data = new Intent();
            data.putExtra(EXTRA_PLACE,spot);
            setResult(RESULT_OK,data);
        }
        finish();
    }

    private void loadHotCities(){
        List<ViewCity> viewCities = GreenDaoManager.getInstance()
                .getViewCityDao()
                .queryBuilder()
                .limit(60)
                .list();

        if(viewCities!=null){
            placeAdapter.setSpotList(viewCities);
        }
        tvTip.setText(R.string.place_search_tip_hots);
    }

    @Override
    public void onSearch(CharSequence text) {
        List<ViewCity> viewCities;
        int tip;

        if(TextUtils.isEmpty(text)){
            tip = R.string.place_search_tip_hots;

            viewCities = GreenDaoManager.getInstance()
                    .getViewCityDao()
                    .queryBuilder()
                    .limit(60)
                    .list();
        }else{
            tip = R.string.place_search_tip_result;

            viewCities = GreenDaoManager.getInstance()
                    .getViewCityDao()
                    .queryBuilder()
                    .where(ViewCityDao.Properties.Name.like("%"+text+"%"))
                    .list();
        }

        if(viewCities!=null)
            placeAdapter.setSpotList(viewCities);
        tvTip.setText(tip);
    }

    public void setPlace(String place)
    {
        searchBar.setSearchText(place,false);
    }
}
