package com.lots.travel.place;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.store.db.Food;
import com.lots.travel.store.db.Hotel;
import com.lots.travel.store.db.Spot;
import com.lots.travel.widget.SearchBar;
import com.lots.travel.widget.refresh.SelectItemsAdapter;

import java.util.ArrayList;

/**
 * Created by nalanzi on 2017/10/15.
 */

public class ChooseComponentsActivity extends RxBaseActivity implements SelectItemsAdapter.OnSelectChangedListener,View.OnClickListener {
    private long cityId;
    private String[] titles;
    private ChooseComponentsFragment[] fragments;

    private SearchBar searchBar;
    private Button btnChoose;

    private ViewPager viewPager;
    private CharSequence[] searchKeys = new CharSequence[3];
    private int[] searchHints = new int[]{R.string.place_search_key_spot,R.string.place_search_key_hotel,R.string.place_search_key_food};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cityId = getIntent().getLongExtra(EXTRA_CITY_ID,-1);
        if(cityId==-1){
            Toast.makeText(this,"城市id无效",Toast.LENGTH_SHORT).show();
            finish();
        }

        Resources res = getResources();
        titles = new String[]{
                res.getString(R.string.place_tab_spot),
                res.getString(R.string.place_tab_hotel),
                res.getString(R.string.place_tab_restaurant)
        };

        fragments = new ChooseComponentsFragment[]{new ChooseSpotsFragment(), new ChooseHotelsFragment(),new ChooseRestaurantsFragment()};
        for (ChooseComponentsFragment fragment : fragments) {
            Bundle bundle = new Bundle();
            bundle.putLong(EXTRA_CITY_ID,cityId);
            fragment.setArguments(bundle);
            fragment.setOnSelectChangedListener(this);
        }

        setContentView(R.layout.activity_choose_components);

        searchBar = (SearchBar) findViewById(R.id.search_bar);
        searchBar.setOnSearchListener(new SearchBar.OnSearchListener() {
            @Override
            public void onSearch(CharSequence text) {
                int currentPos = viewPager.getCurrentItem();
                searchKeys[currentPos] = text;
                fragments[currentPos].setSearchKey(text);
                fragments[currentPos].forceRefresh();
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new HereViewPagerAdapter(this,getSupportFragmentManager()));

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                searchBar.setSearchHint(getString(searchHints[position]));
                searchBar.setSearchText(searchKeys[position],false);
            }

        });
        tabLayout.setupWithViewPager(viewPager,true);

        findViewById(R.id.iv_back).setOnClickListener(this);
        btnChoose = (Button) findViewById(R.id.btn_choose);
        btnChoose.setOnClickListener(this);
    }

    @Override
    public void onSelectChanged() {
        boolean hasSelectedComponents = false;

        for (ChooseComponentsFragment fragment : fragments){
            if(fragment.hasSelectedItems()) {
                hasSelectedComponents = true;
                break;
            }
        }

        btnChoose.setEnabled(hasSelectedComponents);
        btnChoose.setText(hasSelectedComponents ?
                R.string.place_add:R.string.place_choose);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                back(false);
                break;

            case R.id.btn_choose:
                back(true);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        back(false);
    }

    public static final String EXTRA_SPOTS = "spots";
    public static final String EXTRA_HOTELS = "hotels";
    public static final String EXTRA_RESTAURANTS = "restaurants";

    private void back(boolean success){
        if(success){
            Intent data = new Intent();
            data.putParcelableArrayListExtra(EXTRA_SPOTS, (ArrayList<? extends Parcelable>) ((ChooseSpotsFragment)fragments[0]).getSelectedItems());
            data.putParcelableArrayListExtra(EXTRA_HOTELS,(ArrayList<? extends Parcelable>) ((ChooseHotelsFragment)fragments[1]).getSelectedItems());
            data.putParcelableArrayListExtra(EXTRA_RESTAURANTS,(ArrayList<? extends Parcelable>) ((ChooseRestaurantsFragment)fragments[2]).getSelectedItems());
            setResult(Activity.RESULT_OK,data);
        }else
            setResult(Activity.RESULT_CANCELED);

        finish();
    }

    private class HereViewPagerAdapter extends FragmentPagerAdapter {

        HereViewPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    public static final String EXTRA_CITY_ID = "cityId";
    public static void toChoose(Activity context,int requestCode,long cityId){
        Intent intent = new Intent(context,ChooseComponentsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(EXTRA_CITY_ID,cityId);
        context.startActivityForResult(intent,requestCode);
    }

}
