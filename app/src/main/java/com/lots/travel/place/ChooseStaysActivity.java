package com.lots.travel.place;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.model.request.GetCityItems;
import com.lots.travel.network.service.SpotService;
import com.lots.travel.place.adapter.StayAdapter;
import com.lots.travel.store.db.Hotel;

import com.lots.travel.util.DensityUtil;
import com.lots.travel.widget.SearchBar;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;
import com.lots.travel.widget.refresh.PageIterator;
import com.lots.travel.widget.refresh.PagedItemFragment;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by nalanzi on 2017/11/8.
 */

public class ChooseStaysActivity extends RxBaseActivity implements View.OnClickListener {
    private long cityId;
    private CharSequence searchKey;
    private StayAdapter stayAdapter;

    private Button btnAdd;

    private ChooseStaysFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initArgs(getIntent(),savedInstanceState);
        setContentView(R.layout.activity_choose_stays);
        initViews();
    }

    private void initViews(){
        findViewById(R.id.iv_back).setOnClickListener(this);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);

        SearchBar searchBar = (SearchBar) findViewById(R.id.search_bar);
        searchBar.setOnSearchListener(new SearchBar.OnSearchListener() {
            @Override
            public void onSearch(CharSequence text) {
                searchKey = text;
                fragment.forceRefresh();
            }
        });

        fragment = new ChooseStaysFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container,fragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fragment.setUserVisibleHint(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                back(false);
                break;

            case R.id.btn_add:
                back(true);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        back(false);
    }

    private void back(boolean success){
        if(success && stayAdapter.getSelected()!=null){
            Intent data = new Intent();
            data.putExtra(EXTRA_STAYS, stayAdapter.getSelected());
            setResult(Activity.RESULT_OK,data);
        }else{
            setResult(Activity.RESULT_CANCELED);
        }
        finish();
    }

    private void initArgs(Intent data,Bundle saved){
        cityId = data.getLongExtra(EXTRA_CITY_ID,-1);
        if(saved!=null)
            cityId = saved.getLong(EXTRA_CITY_ID);
        if(cityId==-1) {
            Toast.makeText(this,"城市id无效",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(EXTRA_CITY_ID,cityId);
    }

    public static final String EXTRA_STAYS = "stays";
    public static final String EXTRA_CITY_ID = "cityId";

    public static void toChoose(Activity context, int requestCode, Long cityId){
        Intent intent = new Intent(context,ChooseStaysActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(EXTRA_CITY_ID,cityId);
        context.startActivityForResult(intent,requestCode);
    }

    public static class ChooseStaysFragment extends PagedItemFragment<Hotel>{
        ChooseStaysActivity activity;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            activity = (ChooseStaysActivity) context;
        }

        @Override
        public PageIterator.PageRequest<Hotel> createPageRequest() {
            return new PageIterator.PageRequest<Hotel>() {
                @Override
                public Single<HttpResult<List<Hotel>>> execute(int page) {
                    GetCityItems params = new GetCityItems();
                    params.setCityId(activity.cityId);
                    params.setPageNo(page);
                    params.setPageSize(10);
                    params.setKeyword(activity.searchKey==null ?
                            "":activity.searchKey.toString());
                    return ServiceGenerator.createService(getContext(), SpotService.class)
                            .getHotelListByCity(params);
                }
            };
        }

        @Override
        public AbstractLoadAdapter<Hotel> createAdapter(RecyclerView rv) {
            activity.stayAdapter = new StayAdapter(activity,rv);
            activity.stayAdapter.setOnSelectChangedListener(new StayAdapter.OnSelectChangedListener() {
                @Override
                public void onSelectChanged(boolean hasSelected) {
                    activity.btnAdd.setEnabled(hasSelected);
                }
            });
            return activity.stayAdapter;
        }

        @Override
        public void onConfigureDisplay(ConfigureAdapter configureAdapter) {
            super.onConfigureDisplay(configureAdapter);
            configureAdapter.addItemDecoration(new SpaceItemDecoration(getContext()));
        }
    }

    private static class SpaceItemDecoration extends RecyclerView.ItemDecoration{
        private int DP10;

        SpaceItemDecoration(Context context){
            DP10 = DensityUtil.dp2px(context,10);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int pos = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
            outRect.left = DP10;
            outRect.right = DP10;
            outRect.top = pos==0 ? DP10:0;
            outRect.bottom = DP10;
        }
    }


}
