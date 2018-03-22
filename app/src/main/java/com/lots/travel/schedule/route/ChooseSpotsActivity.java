package com.lots.travel.schedule.route;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.base.WebViewActivity;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.model.request.GetCityItems;
import com.lots.travel.network.service.SpotService;
import com.lots.travel.place.model.Place;
import com.lots.travel.store.db.Spot;
import com.lots.travel.util.DensityUtil;
import com.lots.travel.util.SystemBarCompat;
import com.lots.travel.widget.EditTextActivity;

import com.lots.travel.widget.WindowInsertsToolbarLayout;
import com.lots.travel.widget.refresh.AbstractLoadAdapter;
import com.lots.travel.widget.refresh.PageIterator;
import com.lots.travel.widget.refresh.PagedItemFragment;

import java.util.List;
import java.util.Locale;

import io.reactivex.Single;

/**
 * Created by nalanzi on 2017/12/28.
 */
//可以将其放在place目录中
public class ChooseSpotsActivity extends RxBaseActivity implements View.OnClickListener,ChooseSpotsAdapter.OnItemCallback{
    private static final int REQ_KEYWORD = 1;

    private WindowInsertsToolbarLayout toolbarContainer;
    private TextView tvTitle;
    private View layoutSearchBar;
    private TextView tvKeyword;

    private Button btnConfirm;

    private ChooseSpotsFragment mChooseSpotsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏
        SystemBarCompat.fullscreen(this);

        setContentView(R.layout.activity_choose_spots);

        toolbarContainer = (WindowInsertsToolbarLayout) findViewById(R.id.toolbar_container);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        findViewById(R.id.iv_back).setOnClickListener(this);
        layoutSearchBar = findViewById(R.id.layout_searchbar);
        tvKeyword = (TextView) findViewById(R.id.tv_keyword);
        tvKeyword.setOnClickListener(this);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);

        mChooseSpotsFragment = new ChooseSpotsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container,mChooseSpotsFragment)
                .commit();

        parseData(getIntent(),savedInstanceState);
    }

    private void parseData(Intent data,Bundle savedInstanceState){
        Place place = data.getParcelableExtra(PLACE);
        String keyword = null;

        if(savedInstanceState!=null){
            place = savedInstanceState.getParcelable(PLACE);
            keyword = savedInstanceState.getString(KEYWORD);
        }

        if(place==null){
            finish();
            return;
        }

        mChooseSpotsFragment.setPlace(place);
        mChooseSpotsFragment.setKeyword(keyword);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PLACE,mChooseSpotsFragment.getPlace());
        outState.putString(KEYWORD,mChooseSpotsFragment.getKeyword());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mChooseSpotsFragment.setUserVisibleHint(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Activity.RESULT_OK && requestCode==REQ_KEYWORD){
            String keyword = data.getStringExtra(EditTextActivity.TEXT);
            tvKeyword.setText(keyword);
            mChooseSpotsFragment.setKeyword(keyword);
            mChooseSpotsFragment.forceRefresh();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_keyword:
                EditTextActivity.toEdit(this,REQ_KEYWORD,
                        new EditTextActivity.Config()
                                .hint(tvKeyword.getHint().toString())
                                .text(tvKeyword.getText().toString()));
                break;

            case R.id.btn_confirm:
                EditRouteLabelsActivity.toEditLabels(this,mChooseSpotsFragment.getPlace(),mChooseSpotsFragment.getSelectedSpots());
                break;
        }
    }

    @Override
    public void onDetail(Spot spot) {
        WebViewActivity.toWeb(this,null,spot.getViewurl(),false);
    }

    @Override
    public void onAdditional(Spot spot) {}

    @Override
    public void onSelectChanged(int n) {
        btnConfirm.setEnabled(n!=0);
        String str = n==0 ?
                getString(R.string.route_choose_spots_confirm_1) :
                String.format(Locale.getDefault(),getString(R.string.route_choose_spots_confirm_2),n);
        btnConfirm.setText(str);
    }

    public static class ChooseSpotsFragment extends PagedItemFragment<Spot>{
        private String mKeyword;
        private Place mPlace;
        private ChooseSpotsAdapter mChooseSpotsAdapter;
        private ChooseSpotsAdapter.OnItemCallback mOnItemCallback;

        private ChooseSpotsHeader mHeaderHolder;
        private TextView tvKeyword;

        private ChooseSpotsActivity mContext;
        private int mHeaderCoverHeight;

        private int[] mTempLocation = new int[2];

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            mContext = (ChooseSpotsActivity) context;
            mOnItemCallback = (ChooseSpotsAdapter.OnItemCallback) context;
            mHeaderCoverHeight = DensityUtil.dp2px(context,220);
        }

        void setKeyword(String keyword){
            mKeyword = keyword;
            if(tvKeyword!=null)
                tvKeyword.setText(mKeyword);
        }

        String getKeyword(){
            return mKeyword;
        }

        void setPlace(Place place){
            mPlace = place;
        }

        Place getPlace(){
            return mPlace;
        }

        public List<Spot> getSelectedSpots(){
            return mChooseSpotsAdapter.getSelectedSpots();
        }

        @Override
        public void onConfigureDisplay(ConfigureAdapter configureAdapter) {
            super.onConfigureDisplay(configureAdapter);
            configureAdapter.addItemDecoration(new SpaceItemDecoration(getContext()));
            configureAdapter.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView rv, int dx, int dy) {
                    super.onScrolled(rv, dx, dy);
                    LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
                    int pos = layoutManager.findFirstVisibleItemPosition();

                    if(pos!=0){
                        int bgColor = ContextCompat.getColor(mContext,R.color.color_main);
                        mContext.toolbarContainer.setBackgroundColor(setAlpha(bgColor));
                        mContext.tvTitle.setAlpha(1);
                        mContext.layoutSearchBar.setVisibility(View.VISIBLE);
                        mHeaderHolder.layoutSearchBar.setVisibility(View.INVISIBLE);
                    }else{
                        mContext.toolbarContainer.getLocationOnScreen(mTempLocation);
                        int toolbarTop = mTempLocation[1];
                        mHeaderHolder.itemView.getLocationInWindow(mTempLocation);
                        int headerTop = mTempLocation[1];

                        float ratio = (toolbarTop-headerTop)*1f/(mHeaderCoverHeight-mContext.toolbarContainer.getHeight());
                        ratio = Math.min(ratio,1);
                        mContext.toolbarContainer.setAlpha(ratio);
                        mContext.tvTitle.setAlpha(ratio);

                        if(ratio==1){
                            mContext.layoutSearchBar.setVisibility(View.VISIBLE);
                            mHeaderHolder.layoutSearchBar.setVisibility(View.INVISIBLE);
                        }else{
                            mContext.layoutSearchBar.setVisibility(View.INVISIBLE);
                            mHeaderHolder.layoutSearchBar.setVisibility(View.VISIBLE);
                        }
                    }

                }
            });
        }

        @Override
        public PageIterator.PageRequest<Spot> createPageRequest() {
            return new PageIterator.PageRequest<Spot>() {
                @Override
                public Single<HttpResult<List<Spot>>> execute(int page) {
                    GetCityItems params = new GetCityItems();
                    params.setCityId(mPlace.getViewId());
                    params.setPageNo(page);
                    params.setPageSize(8);
                    if(!TextUtils.isEmpty(mKeyword))
                        params.setKeyword(mKeyword);
                    return ServiceGenerator.createService(getContext(),SpotService.class)
                            .getSpotListByCity(params);
                }
            };
        }

        @Override
        public AbstractLoadAdapter<Spot> createAdapter(RecyclerView rv) {
            mChooseSpotsAdapter = new ChooseSpotsAdapter(getContext(),rv);
            mChooseSpotsAdapter.setOnItemCallback(mOnItemCallback);
            mHeaderHolder = ChooseSpotsHeader.create(rv);
            mHeaderHolder.setTripName(mPlace.getName());
            tvKeyword = (TextView) mHeaderHolder.itemView.findViewById(R.id.tv_keyword);
            tvKeyword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditTextActivity.toEdit(mContext,REQ_KEYWORD,
                            new EditTextActivity.Config()
                                    .hint(tvKeyword.getHint().toString())
                                    .text(tvKeyword.getText().toString()));
                }
            });
            mChooseSpotsAdapter.addHeader(mChooseSpotsAdapter.genHeaderFooterTypes(),mHeaderHolder);
            return mChooseSpotsAdapter;
        }
    }

    private static int setAlpha(int color){
        int alpha = (color>>24)&0x0FF;
        return color|alpha<<24;
    }

    private static class SpaceItemDecoration extends RecyclerView.ItemDecoration{
        private int DP10;

        SpaceItemDecoration(Context context){
            DP10 = DensityUtil.dp2px(context,10);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int pos = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
            if(pos==0)
                return;
            outRect.left = DP10;
            outRect.right = DP10;
            outRect.bottom = DP10;
        }
    }

    public static final String PLACE = "Place";
    public static final String KEYWORD = "Keyword";
    public static void toChoose(Context context,Place place){
        Intent intent = new Intent(context,ChooseSpotsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(PLACE,place);
        context.startActivity(intent);
    }

}
