package com.lots.travel.main.info.mine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lots.travel.AccountManager;
import com.lots.travel.R;
import com.lots.travel.base.RxBaseFragment;
import com.lots.travel.footprint.mine.MineFootprintFragment;
import com.lots.travel.main.info.person.PersonMainActivity;
import com.lots.travel.main.message.MessageTabActivity;
import com.lots.travel.main.setting.SettingActivity;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.SingleAdapter;
import com.lots.travel.network.model.result.UserInformation;
import com.lots.travel.network.service.UserService;
import com.lots.travel.store.db.Account;
import com.lots.travel.util.TimeUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.blurry.Blurry;

/**
 * Created by nalanzi on 2017/9/7.
 */
//save restore
public class MineFragment extends RxBaseFragment implements View.OnClickListener{
    public static final int REQ_EDIT_INFO = 1;

    public static final int REQ_SETTING = 2;

    private RadioGroup tabs;
    private ViewPager viewPager;

    private RelativeLayout rlUserInfo;
    //头像
    private ImageView ivPortrait;
    //背景图
    private ImageView ivBackground;
    private TextView tvName;
    private TextView tvProfile;
    private TextView tvFollow;
    private TextView tvFans;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View root) {
        root.findViewById(R.id.iv_setting).setOnClickListener(this);
        root.findViewById(R.id.iv_message).setOnClickListener(this);

        AppBarLayout appBarLayout = (AppBarLayout) root.findViewById(R.id.app_bar_layout);
        rlUserInfo = (RelativeLayout) root.findViewById(R.id.rl_user_info);
        ivPortrait = (ImageView) root.findViewById(R.id.iv_portrait);
        ivPortrait.setOnClickListener(this);
        ivBackground= (ImageView) root.findViewById(R.id.iv_background);
        tvName =(TextView) root.findViewById(R.id.tv_name);
        tvProfile = (TextView) root.findViewById(R.id.tv_profile);
        tvFollow = (TextView) root.findViewById(R.id.tv_follow);
        tvFans = (TextView) root.findViewById(R.id.tv_fans);
        //使用Account进行初始化
        updateInfo(AccountManager.getInstance().getAccount());

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                int totalRange = appBarLayout.getTotalScrollRange();
                if(totalRange==0)
                    return;

                rlUserInfo.setAlpha(1-Math.abs(verticalOffset*1f/totalRange));
            }
        });

        tabs = (RadioGroup) root.findViewById(R.id.tabs);
        tabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId==R.id.tab_center){
                    viewPager.setCurrentItem(0);
                }else if(checkedId==R.id.tab_footprint){
                    viewPager.setCurrentItem(1);
                }
            }
        });

        viewPager = (ViewPager) root.findViewById(R.id.content);
        viewPager.setAdapter(new MineAdapter(getChildFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                int[] ids = new int[]{R.id.tab_center,R.id.tab_footprint};
                tabs.check(ids[position]);
            }
        });
    }

    @Override
    protected void initData() {
        final String userId = Long.toString(AccountManager.getInstance().getUserId());
        ServiceGenerator.createService(getActivity(),UserService.class)
                .getAllInformation(userId,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<HttpResult<UserInformation>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new SingleAdapter<HttpResult<UserInformation>>() {
                    @Override
                    public void onSuccess(@NonNull HttpResult<UserInformation> result) {
                        //更新Account
                        Account account = AccountManager.getInstance().getAccount();
                        account.update(result.getData());
                        AccountManager.getInstance().update(account);
                        //更新界面
                        updateInfo(account);
                    }

                    @Override
                    public void onError(Throwable e) {}
                });
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()){
            case R.id.iv_setting:
                SettingActivity.toSetting(this,REQ_SETTING);
                break;

            case R.id.iv_message:
                intent = new Intent(getActivity(),MessageTabActivity.class);
                startActivity(intent);
                break;

            case R.id.iv_portrait:
                EditUserInfoActivity.toEdit(this,REQ_EDIT_INFO);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode==REQ_EDIT_INFO
                || requestCode==REQ_SETTING)
                && resultCode==Activity.RESULT_OK){
            updateInfo(AccountManager.getInstance().getAccount());
        }
    }

    private  void  loadImage(String url){
        RequestOptions backgroundOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);

        if (TextUtils.isEmpty(url))
            return;

        Glide.with(getContext())
                .asBitmap()
                .load(url)
                .apply(backgroundOptions)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        ivPortrait.setImageBitmap(resource);
                        Blurry.with(getContext())
                                .color(0x5F23B6B7)
                                .from(resource)
                                .into(ivBackground);
                    }
                });
    }

    private void updateInfo(Account account){
        loadImage(account.getPortraitUrl());
        tvName.setText(account.getUsername());
        String str = account.getSex();

        int sexDrawableId = -1;
        if(!TextUtils.isEmpty(str)){
            if (str.equals("1")){
                sexDrawableId = R.drawable.ico_male;
            } else if(str.equals("2")) {
                sexDrawableId = R.drawable.ico_female;
            }
        }
        if(sexDrawableId!=-1) {
            Drawable sexDrawable = ContextCompat.getDrawable(getContext(),sexDrawableId);
            sexDrawable.setBounds(0,0,sexDrawable.getIntrinsicWidth(),sexDrawable.getIntrinsicHeight());
            tvName.setCompoundDrawables(null,null,sexDrawable,null);
        }
        tvProfile.setText(showProfile(account.getBirthday(),account.getWeight(),account.getHeight(),account.getEmotion()));
        tvFollow.setText(getString(R.string.mine_follow)+" "+account.getFollow());
        tvFans.setText(getString(R.string.mine_fans)+" "+account.getFans());
    }

    private String showProfile(String birthday,int weight,int height,String emotion){
        //年龄 、星座
        int age = -1;
        String constellation = null;
        if(!TextUtils.isEmpty(birthday)){
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date date = sdf.parse(birthday);
                age = TimeUtil.getBetweenYears(date,new Date());
                constellation = TimeUtil.getConstellation(date);
            }catch (ParseException e){
                Log.e(PersonMainActivity.class.getName(),"showProfile出生年月日格式错误");
            }
        }
        //感情状况
        String emotionDesc = null;
        if(emotion!=null){
            switch (emotion){
                case "0":
                    emotionDesc = getString(R.string.emotion_single);
                    break;

                case "1":
                    emotionDesc = getString(R.string.emotion_married);
                    break;

                case "2":
                    emotionDesc = getString(R.string.emotion_divorce);
                    break;
            }
        }

        StringBuilder strBuilder = new StringBuilder();
        if(age!=-1 && constellation!=null){
            strBuilder.append(age).append("岁").append('、')
                    .append(constellation).append('、');
        }
        if(weight>0){
            strBuilder.append(weight).append("kg").append('、');
        }
        if(height>0){
            strBuilder.append(height).append("cm").append('、');
        }
        if(emotionDesc!=null){
            strBuilder.append(emotionDesc);
        }
        int length = strBuilder.length();
        if(length>0){
            char endChar = strBuilder.charAt(length-1);
            if(endChar=='、')
                strBuilder.deleteCharAt(length-1);
        }
        return strBuilder.toString();
    }

    private class MineAdapter extends FragmentPagerAdapter{
        private Fragment[] fragments = new Fragment[]{new MineCenterFragment(),new MineFootprintFragment()};

        MineAdapter(FragmentManager fm) {
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
    }

}
