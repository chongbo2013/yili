package com.lots.travel.place;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.model.request.GetPlaces;
import com.lots.travel.network.service.SpotService;
import com.lots.travel.place.adapter.ChoosePlaceAdapter;
import com.lots.travel.place.model.Condition;
import com.lots.travel.place.model.Place;
import com.lots.travel.place.adapter.ChooseFilterConditionsAdapter;
import com.lots.travel.place.widget.ChooseFilterConditionsPopup;
import com.lots.travel.util.DensityUtil;
import com.lots.travel.widget.SearchBar;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;
import com.lots.travel.widget.refresh.PageIterator;
import com.lots.travel.widget.refresh.PagedItemFragment;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by nalanzi on 2017/9/3.
 */

public abstract class ChoosePlaceActivity extends RxBaseActivity implements View.OnClickListener,ChoosePlaceAdapter.OnCheckChangedListener {
    private ImageView ivMenu;
    private Button btnConfirm;
    private ChoosePlaceFragment choosePlaceFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_place);

        findViewById(R.id.iv_back).setOnClickListener(this);
        ivMenu = (ImageView) findViewById(R.id.iv_menu);
        ivMenu.setOnClickListener(this);

        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);

        SearchBar searchBar = (SearchBar) findViewById(R.id.search_bar);
        searchBar.setOnSearchListener(new SearchBar.OnSearchListener() {
            @Override
            public void onSearch(CharSequence text) {
                choosePlaceFragment.setKeyword(text!=null ? text.toString():null);
                choosePlaceFragment.forceRefresh();
            }
        });

        choosePlaceFragment = new ChoosePlaceFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container,choosePlaceFragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        choosePlaceFragment.setUserVisibleHint(true);
    }

    protected void setConfirmButton(boolean enabled){
        btnConfirm.setEnabled(enabled);
    }

    protected void setConfirmButtonText(int text){
        btnConfirm.setText(text);
    }

    public abstract void onChooseChanged(boolean checked);

    public abstract void onConfirm(Place place);

    @Override
    public void onChecked(boolean checked,Place place) {
        onChooseChanged(checked);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_menu:
                showMenu();
                break;

            case R.id.btn_confirm:
                onConfirm(choosePlaceFragment.getChooseItem());
                break;
        }

    }

    private void showMenu(){
        final ChooseFilterConditionsPopup popup = new ChooseFilterConditionsPopup(this);
        popup.setOnItemClickListener(new ChooseFilterConditionsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Condition condition) {
                popup.dismiss();
                choosePlaceFragment.setCondition(condition);
                choosePlaceFragment.forceRefresh();
            }
        });
        popup.show(ivMenu);
    }

    public static class ChoosePlaceFragment extends PagedItemFragment<Place> {
        private ChoosePlaceActivity host;
        private String keyword;
        private Condition condition;
        private ChoosePlaceAdapter choosePlaceAdapter;

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public void setCondition(Condition condition){
            this.condition = condition;
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            host = (ChoosePlaceActivity) context;
        }

        @Override
        public void onConfigureDisplay(ConfigureAdapter configureAdapter) {
            super.onConfigureDisplay(configureAdapter);
            configureAdapter.addItemDecoration(new SpaceItemDecoration(getContext()));
        }

        @Override
        public PageIterator.PageRequest<Place> createPageRequest() {
            return new PageIterator.PageRequest<Place>() {
                @Override
                public Single<HttpResult<List<Place>>> execute(int page) {
                    GetPlaces params = new GetPlaces();
                    params.setPageNo(page);
                    params.setPageSize(8);
                    params.setViewType("2");
                    if(condition!=null)
                        params.setTagType(condition.getName());
                    if(keyword!=null)
                        params.setKeyword(keyword);
                    return ServiceGenerator.createService(getContext(), SpotService.class)
                            .getPlaceList(params);
                }
            };
        }

        @Override
        public AbstractLoadAdapter<Place> createAdapter(RecyclerView rv) {
            choosePlaceAdapter = new ChoosePlaceAdapter(host,rv);
            choosePlaceAdapter.setOnCheckChangedListener(host);
            return choosePlaceAdapter;
        }

        public Place getChooseItem(){
            ChoosePlaceAdapter adapter = (ChoosePlaceAdapter) getAdapter();
            return adapter.getChooseItem();
        }

    }

    private static class SpaceItemDecoration extends RecyclerView.ItemDecoration{
        int dp10;

        SpaceItemDecoration(Context context){
            dp10 = DensityUtil.dp2px(context,10);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView rv, RecyclerView.State state) {
            int pos = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
            outRect.top = pos==0 ? dp10:0;
            outRect.left = dp10;
            outRect.right = dp10;
            outRect.bottom = dp10;
        }

    }

}
