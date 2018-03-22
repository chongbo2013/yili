package com.lots.travel.main.info.mine;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;

import com.lots.travel.R;
import com.lots.travel.main.info.mine.model.FollowPlace;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.SingleAdapter;
import com.lots.travel.network.model.request.GetFollowPlaces;
import com.lots.travel.network.service.CommonService;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nalanzi on 2018/1/12.
 */

public class MineFollowPlacesActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private Fragment[] mFragments;
    private int[] mTabIds;

    private ViewPager vPager;
    private RadioGroup rgTabs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_follow_places);
        initViews();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {}

    private void initViews(){
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTabIds = new int[]{
                R.id.tab_countries,
                R.id.tab_cities,
                R.id.tab_spots};

        mFragments = new Fragment[]{
                new MineFollowCountriesFragment(),
                new MineFollowCitiesFragment(),
                new MineFollowSpotsFragment()};

        rgTabs = (RadioGroup) findViewById(R.id.rg_tabs);
        rgTabs.setOnCheckedChangeListener(this);

        vPager = (ViewPager) findViewById(R.id.v_pager);
        vPager.setOffscreenPageLimit(3);
        vPager.setAdapter(new PlacesFragmentAdapter(getSupportFragmentManager()));
        vPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                rgTabs.check(mTabIds[position]);
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId){
            case R.id.tab_countries:
                vPager.setCurrentItem(0);
                break;

            case R.id.tab_cities:
                vPager.setCurrentItem(1);
                break;

            case R.id.tab_spots:
                vPager.setCurrentItem(2);
                break;
        }
    }

    private class PlacesFragmentAdapter extends FragmentPagerAdapter{

        PlacesFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }
    }

}
