package com.lots.travel.recruit.personal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.SingleAdapter;
import com.lots.travel.network.model.request.GetTripInfo;
import com.lots.travel.network.model.request.PublishTrip;
import com.lots.travel.network.model.result.ExpenseTag;
import com.lots.travel.network.model.result.TripInfo;
import com.lots.travel.network.model.result.TripServiceTag;
import com.lots.travel.network.service.CommonService;
import com.lots.travel.network.service.TripService;
import com.lots.travel.recruit.ExpenseInstructionActivity;
import com.lots.travel.recruit.model.ExpenseAdditional;
import com.lots.travel.recruit.widget.ChooseRecruitPeopleView;
import com.lots.travel.recruit.widget.ExpenseItemsView;
import com.lots.travel.schedule.ChooseTripDateActivity;
import com.lots.travel.schedule.SearchPlaceActivity;
import com.lots.travel.store.db.ViewCity;
import com.lots.travel.util.SosonaOssClient;
import com.lots.travel.util.TypeUtil;
import com.lots.travel.widget.ChooseSrcDstLayout;
import com.lots.travel.widget.EditTextActivity;
import com.lots.travel.widget.TripServiceView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nalanzi on 2017/10/31.
 */
public class RecruitCompanionActivity extends RxBaseActivity implements View.OnClickListener {
    private static final int REQ_CODE_DATE = 0x10;

    private static final int REQ_CODE_PLACE_SRC = 0x11;

    private static final int REQ_EDIT_PRICE = 0x12;

    private static final int REQ_EXPENSE_ADDITIONAL = 0x13;

    private ChooseSrcDstLayout vChooseSrcDst;
    private ChooseRecruitPeopleView vChooseRecruitPeople;
    private TripServiceView vTripService;
    private TextView tvExpense;

    private String mRouteId;
    private TripInfo mTripInfo;

    private Date mChoseRangeStart, mChoseRangeEnd;
    private ViewCity mSrcPlace;
    private int mPrice;
    private ExpenseAdditional mExpenseAdditional;

    private SosonaOssClient mOssClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit_companion);
        initData(getIntent(),savedInstanceState);
        initViews();
    }

    private void initData(Intent data,Bundle savedInstanceState){
        mRouteId = data.getStringExtra(ROUTE_ID);
        if(savedInstanceState!=null)
            mRouteId = savedInstanceState.getString(ROUTE_ID);
        mExpenseAdditional = new ExpenseAdditional();
        mOssClient = new SosonaOssClient(this);
    }

    private void initViews(){
        vChooseSrcDst = (ChooseSrcDstLayout) findViewById(R.id.v_choose_src_dst);
        vChooseRecruitPeople = (ChooseRecruitPeopleView) findViewById(R.id.v_choose_recruit_people);
        vChooseRecruitPeople.setRecruitPeople(4);
        vTripService = (TripServiceView) findViewById(R.id.v_trip_service);

        vChooseSrcDst.applyClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_expenses_additional).setOnClickListener(this);
        findViewById(R.id.tv_more).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
        tvExpense = (TextView) findViewById(R.id.tv_expenses);
        tvExpense.setOnClickListener(this);

        requestTripService();
        requestTripInfo();
        requestExpenseTags();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;

            case ChooseSrcDstLayout.ID_TIME_BACK:
            case ChooseSrcDstLayout.ID_TIME_GO:
                Date rangeStart,rangeEnd;
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.set(Calendar.HOUR_OF_DAY,0);
                cal.set(Calendar.MINUTE,0);
                cal.set(Calendar.SECOND,0);
                cal.set(Calendar.MILLISECOND,0);
                rangeStart = cal.getTime();

                cal.add(Calendar.YEAR, 100);
                rangeEnd = cal.getTime();

                ChooseTripDateActivity.toChoose(this,REQ_CODE_DATE, mChoseRangeStart, mChoseRangeEnd,rangeStart,rangeEnd);
                break;

            case ChooseSrcDstLayout.ID_PLACE_SRC:
                SearchPlaceActivity.toSearch(this,REQ_CODE_PLACE_SRC);
                break;

            case R.id.tv_expenses_additional:
                ExpenseInstructionActivity.toInstruction(this,REQ_EXPENSE_ADDITIONAL,mExpenseAdditional);
                break;

            case R.id.tv_more:
                vTripService.turnExpandState();
                break;

            case R.id.tv_expenses:
                EditTextActivity.toEdit(this,REQ_EDIT_PRICE,
                        new EditTextActivity.Config()
                                .title(getString(R.string.companion_expenses))
                                .hint(getString(R.string.companion_expenses_hint))
                                .inputType(InputType.TYPE_CLASS_NUMBER));
                break;

            case R.id.btn_cancel:
                finish();
                break;

            case R.id.btn_confirm:
                commit();
                break;
        }
    }

    private SpannableStringBuilder showPrice(String value, String extra){
        SpannableStringBuilder strBuilder = new SpannableStringBuilder();

        int textColor,textSize;
        int end = 0;
        if(!TextUtils.isEmpty(value)){
            strBuilder.append(value);
            end = strBuilder.length();
            textColor = ContextCompat.getColor(this,R.color.color_main);
            textSize = getResources().getDimensionPixelSize(R.dimen.head_small);
            strBuilder.setSpan(new AbsoluteSizeSpan(textSize),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if(!TextUtils.isEmpty(extra)){
            strBuilder.append(extra);
            textColor = ContextCompat.getColor(this,R.color.color_text);
            textSize = getResources().getDimensionPixelSize(R.dimen.fonts_tip);
            strBuilder.setSpan(new AbsoluteSizeSpan(textSize),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return strBuilder;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ROUTE_ID,mRouteId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode!= Activity.RESULT_OK)
            return;

        switch (requestCode){
            case REQ_CODE_DATE:{
                mChoseRangeStart = (Date) data.getSerializableExtra(ChooseTripDateActivity.CHOSE_START_DATE);
                mChoseRangeEnd = (Date) data.getSerializableExtra(ChooseTripDateActivity.CHOSE_END_DATE);
                vChooseSrcDst.setTime(mChoseRangeStart, mChoseRangeEnd);
                break;
            }

            case REQ_CODE_PLACE_SRC:{
                mSrcPlace = data.getParcelableExtra(SearchPlaceActivity.EXTRA_PLACE);
                vChooseSrcDst.setPlace(mSrcPlace !=null ? mSrcPlace.getName():null,true);
                break;
            }

            case REQ_EDIT_PRICE: {
                String number = data.getStringExtra(EditTextActivity.TEXT);
                if (TextUtils.isEmpty(number))
                    return;
                mPrice = Integer.parseInt(number);
                tvExpense.setText(showPrice(number, getString(R.string.unit_rmb)));
                break;
            }

            case REQ_EXPENSE_ADDITIONAL:{
                mExpenseAdditional = data.getParcelableExtra(ExpenseInstructionActivity.EXPENSE_ADDITIONAL);
                break;
            }
        }
    }

    private void requestTripInfo(){
        GetTripInfo params = new GetTripInfo();
        params.setBaseId(mRouteId);
        ServiceGenerator.createService(this, TripService.class)
                .getTripInfo(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleAdapter<HttpResult<TripInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        showProgressDialog();
                    }

                    @Override
                    public void onSuccess(@NonNull HttpResult<TripInfo> result) {
                        dismissProgressDialog();
                        mTripInfo = result.getData();
                        mSrcPlace = new ViewCity();
                        mSrcPlace.setName(mTripInfo.getCityFromName());
                        vChooseSrcDst.setPlace(mTripInfo.getCityFromName(),true);
                        vChooseSrcDst.setPlace(mTripInfo.getCityToName(),false);

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date());
                        cal.set(Calendar.HOUR_OF_DAY,0);
                        cal.set(Calendar.MINUTE,0);
                        cal.set(Calendar.SECOND,0);
                        mChoseRangeStart = cal.getTime();

                        cal.add(Calendar.DAY_OF_MONTH, mTripInfo.getDays());
                        mChoseRangeEnd = cal.getTime();
                        vChooseSrcDst.setTime(mChoseRangeStart, mChoseRangeEnd);
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        Toast.makeText(RecruitCompanionActivity.this,R.string.companion_obtain_trip_failed,Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void requestTripService(){
        ServiceGenerator.createService(this, CommonService.class)
                .getTripServiceTags()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleAdapter<HttpResult<List<TripServiceTag>>>() {
                    @Override
                    public void onSuccess(@NonNull HttpResult<List<TripServiceTag>> result) {
                        List<TripServiceTag> src = result.getData();
                        List<TripServiceView.Item> items = new ArrayList<>();
                        for (int i=0;i<src.size();i++){
                            TripServiceView.Item item = new TripServiceView.Item(src.get(i));
                            items.add(item);
                        }
                        vTripService.setTags(items);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RecruitCompanionActivity.this,"获取旅行服务失败",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void requestExpenseTags(){
        ServiceGenerator.createService(this,CommonService.class)
                .getExpenseTags()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleAdapter<HttpResult<List<ExpenseTag>>>() {
                    @Override
                    public void onSuccess(@NonNull HttpResult<List<ExpenseTag>> result) {
                        List<ExpenseItemsView.ExpenseItem> includeItems = new ArrayList<>();
                        List<ExpenseItemsView.ExpenseItem> excludeItems = new ArrayList<>();
                        List<ExpenseTag> src = result.getData();
                        for (int i=0;i<src.size();i++){
                            ExpenseItemsView.ExpenseItem includeItem = new ExpenseItemsView.ExpenseItem(src.get(i));
                            includeItems.add(includeItem);
                            ExpenseItemsView.ExpenseItem excludeItem = new ExpenseItemsView.ExpenseItem(src.get(i));
                            excludeItems.add(excludeItem);
                        }
                        mExpenseAdditional.setIncludeExpenseItems(includeItems);
                        mExpenseAdditional.setExcludeExpenseItems(excludeItems);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(RecruitCompanionActivity.class.getName(),e.toString());
                    }
                });
    }

    private void commit(){
        Single.create(
                new SingleOnSubscribe<Boolean>() {

                    @Override
                    public void subscribe(@NonNull SingleEmitter<Boolean> e) throws Exception {
                        List<SosonaOssClient.Item> uploadItems = new ArrayList<>();
                        String path,url;
                        String[] images = mExpenseAdditional.getImages();
                        if(images!=null && images.length>0){
                            for (String image : images)
                                uploadItems.add(new SosonaOssClient.Item(SosonaOssClient.ITEM_TYPE_RECRUIT, image));
                        }
                        path = mExpenseAdditional.getVideoCover();
                        if(path!=null)
                            uploadItems.add(new SosonaOssClient.Item(SosonaOssClient.ITEM_TYPE_RECRUIT, path));
                        path = mExpenseAdditional.getVideoFilepath();
                        if(path!=null)
                            uploadItems.add(new SosonaOssClient.Item(SosonaOssClient.ITEM_TYPE_RECRUIT, path));
                        path = mExpenseAdditional.getSoundFilepath();
                        if(mExpenseAdditional.getSoundLength()>0 && path!=null)
                            uploadItems.add(new SosonaOssClient.Item(SosonaOssClient.ITEM_TYPE_RECRUIT, path));

                        SparseArray<String> urlMap = new SparseArray<>();
                        if(uploadItems.size()>0)
                            mOssClient.upload(RecruitCompanionActivity.this,uploadItems,urlMap);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                        PublishTrip params = new PublishTrip();
                        List<PublishTrip.Recruit> recruits = new ArrayList<>();
                        PublishTrip.Recruit recruit = new PublishTrip.Recruit();
                        Long fromId = mSrcPlace.getId();
                        recruit.setCityFrom(fromId==null||fromId<=0 ? mSrcPlace.getName():fromId+"");
                        recruit.setCityTo(mTripInfo.getCityTo());
                        recruit.setDateFrom(sdf.format(mChoseRangeStart));
                        recruit.setDateTo(sdf.format(mChoseRangeEnd));
                        recruit.setStyle("1");
                        recruit.setProvideService(vTripService.getSelectedTags());
                        recruit.setPeople(vChooseRecruitPeople.getRecruitPeople()+"");
                        recruit.setMoney(mPrice+"");
                        recruit.setMoneyContain(mExpenseAdditional.getIncludeExpense());
                        recruit.setMoneyNotContain(mExpenseAdditional.getExcludeExpense());
                        recruit.setMoneyDesc(mExpenseAdditional.getText());

                        path = mExpenseAdditional.getVideoCover();
                        url = !TextUtils.isEmpty(path) ? urlMap.get(path.hashCode()):null;
                        if(!TextUtils.isEmpty(url))
                            recruit.setMoneyVideoImg(url);

                        path = mExpenseAdditional.getVideoFilepath();
                        url = !TextUtils.isEmpty(path) ? urlMap.get(path.hashCode()):null;
                        if(!TextUtils.isEmpty(url))
                            recruit.setMoneyVideo(url);

                        images = mExpenseAdditional.getImages();
                        if(images!=null && images.length>0){
                            for (int i=0;i<images.length;i++){
                                path = images[i];
                                url = !TextUtils.isEmpty(path) ? urlMap.get(path.hashCode()):null;
                                if(!TextUtils.isEmpty(url))
                                    images[i] = url;
                            }
                            recruit.setMoneyImgs(TypeUtil.array2str(images));
                        }

                        if(mExpenseAdditional.getSoundLength()>0 && mExpenseAdditional.getSoundFilepath()!=null){
                            path = mExpenseAdditional.getSoundFilepath();
                            url = !TextUtils.isEmpty(path) ? urlMap.get(path.hashCode()):null;
                            if(!TextUtils.isEmpty(url))
                                recruit.setMoneySound(url);
                            recruit.setMoneySoundLen(mExpenseAdditional.getSoundLength()+"");
                        }

                        recruits.add(recruit);

                        params.setBaseId(mRouteId);
                        params.setRecruit(recruits);
                        ServiceGenerator.createService(RecruitCompanionActivity.this,TripService.class)
                                .publishTrip(params)
                                .blockingGet();
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
                        Toast.makeText(RecruitCompanionActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        Toast.makeText(RecruitCompanionActivity.this,"保存失败",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static final String ROUTE_ID = "RouteId";

    public static void toRecruit(Context context,String routeId){
        Intent intent = new Intent(context,RecruitCompanionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(ROUTE_ID,routeId);
        context.startActivity(intent);
    }

}
