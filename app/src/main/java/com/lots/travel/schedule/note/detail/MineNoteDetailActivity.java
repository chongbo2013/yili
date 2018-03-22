package com.lots.travel.schedule.note.detail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.SingleAdapter;
import com.lots.travel.network.model.request.FollowElement;
import com.lots.travel.network.model.request.GetTripDaysInfo;
import com.lots.travel.network.service.CommonService;
import com.lots.travel.network.service.TripService;
import com.lots.travel.schedule.base.DataManager;
import com.lots.travel.schedule.base.Schedule;
import com.lots.travel.schedule.model.TripDaysInfo;
import com.lots.travel.schedule.model.UserInfo;
import com.lots.travel.schedule.widget.CircleIndicatorLayout;
import com.lots.travel.util.SystemBarCompat;
import com.lots.travel.widget.OnChooseListener;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nalanzi on 2018/1/15.
 */
//获取游记内容，存储至数据库，fragment中进行展示
public class MineNoteDetailActivity extends RxBaseActivity implements View.OnClickListener,OnScrollChangedListener{
    private static final String SCHEDULE_ID = "ScheduleId";

    public static void toDetail(Fragment fragment, int requestCode, String scheduleId){
        Intent intent = new Intent(fragment.getContext(),MineNoteDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(SCHEDULE_ID,scheduleId);
        fragment.startActivityForResult(intent,requestCode);
    }

    private TextView tvGz;
    private RadioGroup tabs;
    private ViewPager viewPager;
    private RelativeLayout rlHead;
    private CircleIndicatorLayout circleIndicator;
    private TabsAdapter mTabsAdapter;
    private View cardWorth;
    private RequestOptions mRequestOptions;
    private Button btnCollect;

    private ChooseDetailGzPopup popupChooseGz;

    private String mScheduleId;
    private UserInfo mUserInfo;
    private Schedule mSchedule;
    private DataManager mDataManager;

    private String mCoverUrl;
    private String mPlaceName;
    private String mTravelTags;
    private int mPraiseCount;
    private int mKpCount,mUnKpCount;
    private boolean mFollowed,mGzed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_note_detail);
        SystemBarCompat.fullscreen(this);
        initArgs(getIntent(),savedInstanceState);
        initViews();
        loadData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SCHEDULE_ID,mScheduleId);
    }

    private void initArgs(Intent data, Bundle savedInstanceState){
        mScheduleId = data.getStringExtra(SCHEDULE_ID);
        if(savedInstanceState!=null)
            mScheduleId = savedInstanceState.getString(SCHEDULE_ID);
        mRequestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);
    }

    private void initViews(){
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_share).setOnClickListener(this);

        tvGz = (TextView) findViewById(R.id.tv_gz);
        tvGz.setOnClickListener(this);

        rlHead = (RelativeLayout) findViewById(R.id.rl_head);
        cardWorth = findViewById(R.id.card_worth);
        circleIndicator = (CircleIndicatorLayout) findViewById(R.id.circle_indicator);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                int totalRange = appBarLayout.getTotalScrollRange();
                if(totalRange==0) {
                    tabs.setAlpha(0);
                    return;
                }

                float ratio = Math.abs(verticalOffset*1f/totalRange);
                rlHead.setAlpha(1-ratio);
                tabs.setAlpha(ratio);
                circleIndicator.setVisibility(ratio>=.9f ? View.VISIBLE: View.INVISIBLE);
            }
        });
        tabs = (RadioGroup) findViewById(R.id.tabs);
        tabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId==R.id.tab_note){
                    viewPager.setCurrentItem(0);
                }else if(checkedId==R.id.tab_route){
                    viewPager.setCurrentItem(1);
                }
            }
        });

        mTabsAdapter = new TabsAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.content);
        viewPager.setAdapter(mTabsAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                int[] ids = new int[]{R.id.tab_note,R.id.tab_route};
                tabs.check(ids[position]);
                cardWorth.setVisibility(mTabsAdapter.fragments[position].isFooterVisible() ? View.VISIBLE:View.GONE);
                circleIndicator.checkToPosition(mTabsAdapter.fragments[position].getCurrentGroup());
            }
        });

        btnCollect = (Button) findViewById(R.id.btn_collect);
        btnCollect.setOnClickListener(this);

        popupChooseGz = new ChooseDetailGzPopup(this, new OnChooseListener() {
            @Override
            public void onChoose(int type) {
                switch (type){
                    case ChooseDetailGzPopup.TYPE_CANCEL:
                        requestGz(false);
                        break;

                    case ChooseDetailGzPopup.TYPE_GROUP:
                        ;
                        break;
                }
            }
        });
    }

    private void loadData(){
        Single.create(
                new SingleOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(@NonNull SingleEmitter<Boolean> e) throws Exception {
                        //加载数据
                        GetTripDaysInfo params = new GetTripDaysInfo();
                        params.setBaseId(mScheduleId);
                        params.setLoadAllData("1");
                        params.setQueryType("viewInfo");
                        HttpResult<TripDaysInfo> result = ServiceGenerator.createService(MineNoteDetailActivity.this, TripService.class)
                                .getTripDaysInfo(params)
                                .blockingGet();
                        //存储至数据库
                        TripDaysInfo tripInfo = result.getData();
                        TripDaysInfo.BaseInfo baseInfo = tripInfo.getBaseInfo();
                        TripDaysInfo.CommonInfo commonInfo = tripInfo.getCommonInfo();
                        if(baseInfo!=null) {
                            mCoverUrl = baseInfo.getImg();
                            mPlaceName = baseInfo.getCityToName();
                            mTravelTags = baseInfo.getTravelTag();
                        }
                        if(commonInfo!=null){
                            mPraiseCount = commonInfo.getPjCount();
                            mFollowed = commonInfo.getHasFollow()==1;
                            mGzed = commonInfo.getHasFollowCreater()==1;
                            mKpCount = commonInfo.getWorthCount();
                            mUnKpCount = commonInfo.getUnworthCount();
                        }
                        mUserInfo = UserInfo.create(tripInfo.getUserInfo());
                        mSchedule = DataManager.createSchedule(tripInfo);
                        //根据数据库数据建立topology
                        mDataManager = DataManager.create(mSchedule);
                        e.onSuccess(true);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleAdapter<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        showProgressDialog();
                    }

                    @Override
                    public void onSuccess(@NonNull Boolean aBoolean) {
                        dismissProgressDialog();
                        //更新head显示
                        showGz();
                        showPlaceInfo();
                        showUserInfo();
                        //bottom
                        showWorthCount();
                        showCollect();
                        //indicator
                        circleIndicator.setCircleCount(mSchedule.getCount());
                        //更新tab显示
                        mTabsAdapter.setup(mSchedule,mDataManager,mUserInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        Toast.makeText(MineNoteDetailActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void showPlaceInfo(){
        if (mCoverUrl != null) {
            ImageView ivCover = (ImageView) findViewById(R.id.iv_cover);
            Glide.with(MineNoteDetailActivity.this)
                    .load(mCoverUrl)
                    .apply(mRequestOptions)
                    .into(ivCover);
        }
        TextView tvPlaceName = (TextView) findViewById(R.id.tv_place_name);
        tvPlaceName.setText(mPlaceName);

        TextView tvTravelTags = (TextView) findViewById(R.id.tv_travel_tags);
        tvTravelTags.setText(getHandledTags(mTravelTags));

        TextView tvKpPeople = (TextView) findViewById(R.id.tv_praise_count);
        tvKpPeople.setText(String.format(Locale.getDefault(),"%d人",mPraiseCount));
    }

    private void showUserInfo(){
        ImageView ivPortrait = (ImageView) findViewById(R.id.iv_portrait);
        Glide.with(MineNoteDetailActivity.this)
                .load(mUserInfo.getPortrait())
                .apply(mRequestOptions)
                .into(ivPortrait);

        TextView tvUsername = (TextView) findViewById(R.id.tv_username);
        tvUsername.setText(mUserInfo.getUserName());
        int sexDrawableId = -1;
        String sex = mUserInfo.getSex();
        if(!TextUtils.isEmpty(sex)){
            if (sex.equals("1")){
                sexDrawableId = R.drawable.ico_male;
            } else if(sex.equals("2")) {
                sexDrawableId = R.drawable.ico_female;
            }
        }
        if(sexDrawableId!=-1) {
            Drawable sexDrawable = ContextCompat.getDrawable(MineNoteDetailActivity.this,sexDrawableId);
            sexDrawable.setBounds(0,0,sexDrawable.getIntrinsicWidth(),sexDrawable.getIntrinsicHeight());
            tvUsername.setCompoundDrawables(null,null,sexDrawable,null);
        }

        TextView tvProfession = (TextView) findViewById(R.id.tv_profession);
        tvProfession.setText(mUserInfo.getProfession());
    }

    private void showWorthCount(){
        TextView tvNotWorth = (TextView) findViewById(R.id.tv_notworth);
        if(mUnKpCount>0){
            tvNotWorth.setText(getString(R.string.note_detail_notworth)+'('+mUnKpCount+')');
        }
        TextView tvWorth = (TextView) findViewById(R.id.tv_worth);
        if(mKpCount>0){
            tvWorth.setText(getString(R.string.note_detail_worth)+'('+mKpCount+')');
        }
    }

    private void showCollect(){
        Drawable drawable = ContextCompat.getDrawable(this,
                mFollowed ? R.drawable.ico_bottom_collect_en:R.drawable.ico_bottom_collect_dis);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        btnCollect.setCompoundDrawables(null,drawable,null,null);
    }

    private void showGz(){
        tvGz.setText(mGzed ? R.string.schedule_userinfo_gzed:R.string.schedule_userinfo_gz);
        tvGz.setTextColor(ContextCompat.getColor(this,mGzed ? R.color.color_divider:R.color.color_main));
        tvGz.setBackgroundResource(mGzed ? R.drawable.bg_note_detail_gzed :R.drawable.bg_note_detail_gz);
    }

//    private SpannableStringBuilder showCount(String prev, int value){
//        value = TextUtils.isEmpty(value) ? "0":value;
//        int end;
//        int textSize,textColor;
//        Resources res = mContext.getResources();
//
//        SpannableStringBuilder strBuilder = new SpannableStringBuilder();
//
//        strBuilder.append(prev);
//        end = strBuilder.length();
//        textSize = (int) res.getDimension(R.dimen.fonts_tip);
//        textColor = ContextCompat.getColor(mContext,R.color.color_title_small);
//        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        strBuilder.append(value);
//        textSize = (int) res.getDimension(R.dimen.head_medium);
//        textColor = ContextCompat.getColor(mContext,R.color.color_warn);
//        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),end,strBuilder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        strBuilder.setSpan(new StyleSpan(Typeface.BOLD),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        end = strBuilder.length();
//        strBuilder.append(next);
//        textSize = (int) res.getDimension(R.dimen.fonts_tip);
//        textColor = ContextCompat.getColor(mContext,R.color.color_title_small);
//        strBuilder.setSpan(new AbsoluteSizeSpan(textSize),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return strBuilder;
//    }

    private String getHandledTags(String themes){
        if(TextUtils.isEmpty(themes))
            return "旅行游记";

        String[] frags = themes.split(",");
        StringBuilder strBuilder = new StringBuilder();
        for (int i=0;i<frags.length;i++){
            if(i!=0)
                strBuilder.append('/');
            strBuilder.append(frags[i]);
        }
        strBuilder.append(" 旅行游记");
        return strBuilder.toString();
    }

    private void back(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                back();
                break;

            case R.id.iv_share:
                ;
                break;

            case R.id.btn_collect:
                requestFollow(!mFollowed);
                break;

            case R.id.tv_gz:
                if(mGzed){
                    popupChooseGz.showAsDropDown(tvGz);
                }else{
                    requestGz(true);
                }
                break;
        }
    }

    @Override
    public void onScrollChanged(Class<? extends BaseDetailFragment> src,int currentGroup,boolean footerVisible) {
        int currentItem = viewPager.getCurrentItem();

        if((src==NoteDetailFragment.class && currentItem!=0)
                || (src==RouteDetailFragment.class && currentItem!=1))
            return;

        cardWorth.setVisibility(footerVisible ? View.VISIBLE:View.GONE);
        circleIndicator.checkToPosition(currentGroup);
    }

    private class TabsAdapter extends FragmentPagerAdapter {
        private BaseDetailFragment[] fragments = new BaseDetailFragment[]{new NoteDetailFragment(),new RouteDetailFragment()};

        TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        void setup(Schedule schedule,DataManager dataManager,UserInfo userInfo){
            fragments[0].loadData(schedule,dataManager);
            fragments[1].loadData(schedule,dataManager);
            ((NoteDetailFragment)fragments[0]).setPortraitUrl(userInfo.getPortrait());
        }

        @Override
        public BaseDetailFragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }

    private void requestFollow(final boolean followed){
        FollowElement params = new FollowElement();
        params.setDataTable(FollowElement.TYPE_NOTE);
        params.setDataKey(mScheduleId);
        params.setStyle(followed ? FollowElement.ACTION_UNFOLLOW:FollowElement.ACTION_FOLLOW);
        ServiceGenerator.createService(this, CommonService.class)
                .followElement(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<HttpResult<String>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new SingleObserver<HttpResult<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        showProgressDialog();
                    }

                    @Override
                    public void onSuccess(@NonNull HttpResult<String> result) {
                        dismissProgressDialog();
                        mFollowed = followed;
                        showCollect();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dismissProgressDialog();
                    }
                });
    }

    private void requestGz(final boolean gz){
        FollowElement params = new FollowElement();
        params.setDataTable(FollowElement.TYPE_USER);
        params.setDataKey(mUserInfo.getUserId()+"");
        params.setStyle(gz
                ? FollowElement.ACTION_FOLLOW:FollowElement.ACTION_UNFOLLOW);
        ServiceGenerator.createService(this, CommonService.class)
                .followElement(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<HttpResult<String>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new SingleObserver<HttpResult<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        showProgressDialog();
                    }

                    @Override
                    public void onSuccess(@NonNull HttpResult<String> result) {
                        dismissProgressDialog();
                        mGzed = gz;
                        showGz();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dismissProgressDialog();
                    }
                });
    }

}
