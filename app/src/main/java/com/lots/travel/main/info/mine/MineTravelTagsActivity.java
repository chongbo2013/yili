package com.lots.travel.main.info.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lots.travel.AccountManager;
import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.main.info.mine.model.MineTag;
import com.lots.travel.network.DefaultExceptionHandler;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.SingleAdapter;
import com.lots.travel.network.model.request.SaveTags;
import com.lots.travel.network.model.result.MineTags;
import com.lots.travel.network.model.result.TagUserInfo;
import com.lots.travel.network.service.CommonService;
import com.lots.travel.store.db.Account;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nalanzi on 2018/1/11.
 */

public class MineTravelTagsActivity extends RxBaseActivity implements View.OnClickListener{
    private TagFlowLayout vTravelTags,vInterestTags;
    private TravelTagsAdapter mTravelAdapter,mInterestAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_travel_tags);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_save).setOnClickListener(this);
        vTravelTags = (TagFlowLayout) findViewById(R.id.v_travel_tags);
        vInterestTags = (TagFlowLayout) findViewById(R.id.v_interest_tags);

        TextView tvTravelTagsDesc = (TextView) findViewById(R.id.tv_travel_tags_desc);
        TextView tvInterestTagsDesc = (TextView) findViewById(R.id.tv_interest_tags_desc);
        tvTravelTagsDesc.setText(showDesc(getString(R.string.travel_tags_travel),getString(R.string.travel_tags_most)));
        tvInterestTagsDesc.setText(showDesc(getString(R.string.travel_tags_interest),getString(R.string.travel_tags_most)));
        requestTravelTags();
    }

    private SpannableStringBuilder showDesc(String str1, String str2){
        SpannableStringBuilder strBuilder = new SpannableStringBuilder();

        int textColor;
        int end;
        strBuilder.append(str1);
        end = strBuilder.length();
        textColor = ContextCompat.getColor(this,R.color.color_title_big);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.head_small)),0,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        strBuilder.append(str2);
        textColor = ContextCompat.getColor(this,R.color.color_text);
        strBuilder.setSpan(new ForegroundColorSpan(textColor),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.fonts_tip)),end,strBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return strBuilder;
    }

    private void requestTravelTags(){
        ServiceGenerator.createService(this, CommonService.class)
                .getMineTags()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<HttpResult<List<MineTags>>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new SingleAdapter<HttpResult<List<MineTags>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        showProgressDialog();
                    }

                    @Override
                    public void onSuccess(@NonNull HttpResult<List<MineTags>> result) {
                        dismissProgressDialog();
                        List<MineTags> src = result.getData();
                        if(src==null || src.size()!=2)
                            return;

                        String mine = AccountManager.getInstance().getTags();
                        String[] mineTags = null;
                        if(!TextUtils.isEmpty(mine)){
                            mineTags = mine.split(",");
                        }

                        for(MineTags eTags:src){
                            String type = eTags.getTag();
                            List<MineTag> tags;
                            if(MineTag.TAG_TRAVEL.equals(type)){
                                tags = eTags.getData();
                                if(tags!=null){
                                    mTravelAdapter = new TravelTagsAdapter(mineTags,tags);
                                    vTravelTags.setAdapter(mTravelAdapter);
                                }
                            }else if(MineTag.TAG_INTEREST.equals(type)){
                                tags = eTags.getData();
                                if(tags!=null){
                                    mInterestAdapter = new TravelTagsAdapter(mineTags,tags);
                                    vInterestTags.setAdapter(mInterestAdapter);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                    }
                });
    }

    private void requestSaveMineTags(){
        String travelTags = mTravelAdapter.getSelectedTags(vTravelTags.getSelectedList());
        String interestTags = mInterestAdapter.getSelectedTags(vInterestTags.getSelectedList());
        StringBuilder totalTags = new StringBuilder();
        boolean eTravel = TextUtils.isEmpty(travelTags);
        boolean eInterest = TextUtils.isEmpty(interestTags);
        if(!eTravel && !eInterest){
            totalTags.append(travelTags).append(',');
            totalTags.append(interestTags);
        }else if(!eTravel){
            totalTags.append(travelTags);
        }else if(!eInterest){
            totalTags.append(interestTags);
        }

        SaveTags params = new SaveTags();
        params.setUserId(AccountManager.getInstance().getUserId());
        params.setTags(totalTags.toString());
        ServiceGenerator.createService(this,CommonService.class)
                .saveTags(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<HttpResult<TagUserInfo>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new SingleAdapter<HttpResult<TagUserInfo>>() {
                    @Override
                    public void onSuccess(@NonNull HttpResult<TagUserInfo> result) {
                        TagUserInfo data = result.getData();
                        if(data==null)
                            return;
                        AccountManager.getInstance().setTags(data.getTags());
                        Toast.makeText(MineTravelTagsActivity.this,R.string.travel_tags_save_success,Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        new DefaultExceptionHandler().handleException(MineTravelTagsActivity.this,e);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_save:
                requestSaveMineTags();
                break;
        }
    }

    private class TravelTagsAdapter extends TagAdapter<MineTag>{
        private List<MineTag> mTags;

        TravelTagsAdapter(String[] mineTags, List<MineTag> data) {
            super(data);
            mTags = data;
            if(mineTags==null || mineTags.length==0)
                return;
            Set<Integer> selectedTags = new HashSet<>();
            for (String mineTag : mineTags) {
                for (int j = 0;j<data.size();j++) {
                    MineTag tag = data.get(j);
                    if(mineTag.equals(tag.getValue())) {
                        selectedTags.add(j);
                        break;
                    }
                }
            }
            setSelectedList(selectedTags);
        }

        String getSelectedTags(Set<Integer> src){
            if(src.size()==0)
                return null;
            StringBuilder strBuilder = new StringBuilder();
            Iterator<Integer> it = src.iterator();
            while (it.hasNext()){
                MineTag tag = mTags.get(it.next());
                strBuilder.append(tag.getValue());
                if(it.hasNext())
                    strBuilder.append(',');
            }
            return strBuilder.toString();
        }

        @Override
        public View getView(FlowLayout parent, int position, MineTag s) {
            TextView tvTag = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_travel_tag,parent,false);
            tvTag.setText(s.getValue());
            return tvTag;
        }

    }

}
