package com.lots.travel.main.info.person;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lots.travel.R;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.footprint.model.Footprint;
import com.lots.travel.footprint.person.PersonFootprintFragment;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.SingleAdapter;
import com.lots.travel.network.model.result.UserInformation;
import com.lots.travel.network.service.UserService;
import com.lots.travel.util.SystemBarCompat;
import com.lots.travel.util.TimeUtil;
import com.lots.travel.widget.OnChooseListener;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.blurry.Blurry;

/**
 * Created by nalanzi on 2017/11/23.
 */

public class PersonMainActivity extends RxBaseActivity implements View.OnClickListener {
    private TextView tvTitle;

    private RelativeLayout rlUserInfo;
    //头像
    private ImageView ivPortrait;
    //背景图
    private ImageView ivBackground;
    private TextView tvName;
    private TextView tvProfile;
    //关注数
    private TextView tvFollow;
    //粉丝数
    private TextView tvFans;
    //按钮
    private TextView btnFollow,btnContact,btnHots;

    private ChooseFollowPopup popupChooseFollow;
    private ChooseContactPopup popupChooseContact;
    private ChooseHotsPopup popupChooseHots;

    private long mUserId;
    private String[] mTitles;
    private Fragment[] mFragments;
    private boolean mGzed;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseData(getIntent(),savedInstanceState);
        setContentView(R.layout.activity_person_main);

        mFragments = new Fragment[3];
        mFragments[0] = new PersonHomeFragment();
        mFragments[1] = new PersonFootprintFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(PersonFootprintFragment.EXTRA_USER_ID, mUserId);
        mFragments[1].setArguments(bundle);
        mFragments[2] = PersonAlbumFragment.create(mUserId);

        int[] strIds = new int[]{R.string.person_tab_home,R.string.person_tab_footprint,R.string.person_tab_photograph};
        mTitles = new String[strIds.length];
        for (int i=0;i<strIds.length;i++){
            mTitles[i] = getResources().getString(strIds[i]);
        }

        SystemBarCompat.fullscreen(this);

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_share).setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvProfile = (TextView) findViewById(R.id.tv_profile);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        rlUserInfo = (RelativeLayout) findViewById(R.id.rl_user_info);
        tvName =(TextView) findViewById(R.id.tv_name);
        tvFollow = (TextView) findViewById(R.id.tv_follow);
        tvFans = (TextView) findViewById(R.id.tv_fanse);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                int totalRange = appBarLayout.getTotalScrollRange();
                if(totalRange==0)
                    return;

                float ratio = Math.abs(verticalOffset*1f/totalRange);
                tvTitle.setAlpha(ratio);
                rlUserInfo.setAlpha(1-ratio);
            }
        });

        ivPortrait = (ImageView) findViewById(R.id.iv_portrait);
        ivPortrait.setOnClickListener(this);
        ivBackground= (ImageView) findViewById(R.id.iv_background);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.content);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new PersonAdapter(getSupportFragmentManager()));

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setTabMode(TabLayout.MODE_FIXED);
        tabs.setupWithViewPager(viewPager);

        btnFollow = (TextView) findViewById(R.id.btn_follow);
        btnFollow.setOnClickListener(this);
        btnContact = (TextView) findViewById(R.id.btn_contact);
        btnContact.setOnClickListener(this);
        btnHots = (TextView) findViewById(R.id.btn_hots);
        btnHots.setOnClickListener(this);

        popupChooseFollow = new ChooseFollowPopup(this, new OnChooseListener() {
            @Override
            public void onChoose(int type) {
                switch (type){
                    case ChooseFollowPopup.TYPE_CANCEL:
                        ;
                        break;

                    case ChooseFollowPopup.TYPE_GROUP:
                        ;
                        break;
                }
            }
        });
        popupChooseContact = new ChooseContactPopup(this, new OnChooseListener() {
            @Override
            public void onChoose(int type) {
                switch (type){
                    case ChooseContactPopup.TYPE_MESSAGE:
                        ;
                        break;

                    case ChooseContactPopup.TYPE_VOICE:
                        ;
                        break;
                }
            }
        });
        popupChooseHots = new ChooseHotsPopup(this, new OnChooseListener() {
            @Override
            public void onChoose(int type) {
                String str = null;
                switch (type){
                    case ChooseHotsPopup.TYPE_NOTE:
                        str = Footprint.NOTE;
                        break;

                    case ChooseHotsPopup.TYPE_TRIP:
                        str = Footprint.TRIP;
                        break;

                    case ChooseHotsPopup.TYPE_ACTIVE:
                        str = Footprint.ACTIVE;
                        break;

                    case ChooseHotsPopup.TYPE_FOOTPRINT:
                        str = Footprint.FOOTPRINT;
                        break;

                    case ChooseHotsPopup.TYPE_LIVE:
                        str = Footprint.LIVE;
                        break;

                    case ChooseHotsPopup.TYPE_DESTINATION:
                        str = Footprint.DESTINATION;
                        break;
                }
                ((PersonFootprintFragment)mFragments[1]).setSearchType(str);
                viewPager.setCurrentItem(1);
                ((PersonFootprintFragment)mFragments[1]).startSearch(str);
            }
        });

        requestInfo(Long.toString(mUserId));
    }

    private void requestInfo(String userId){
        ServiceGenerator.createService(this,UserService.class).getAllInformation(userId,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<HttpResult<UserInformation>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new SingleAdapter<HttpResult<UserInformation>>() {
                    @Override
                    public void onSuccess(@NonNull HttpResult<UserInformation> result) {
                        UserInformation info = result.getData();
                        UserInformation.User user = info.getUser();
                        if(user==null)
                            return;

                        tvTitle.setText(user.getUserName());
                        tvName.setText(user.getUserName());

                        //0 - 保密、1 - 男、2 - 女
                        String sex = user.getSex();
                        int sexDrawableId = -1;
                        if(!TextUtils.isEmpty(sex)){
                            if (sex.equals("1")){
                                sexDrawableId = R.drawable.ico_male;
                            } else if(sex.equals("2")) {
                                sexDrawableId = R.drawable.ico_female;
                            }
                        }
                        if(sexDrawableId!=-1) {
                            Drawable sexDrawable = ContextCompat.getDrawable(PersonMainActivity.this,sexDrawableId);
                            sexDrawable.setBounds(0,0,sexDrawable.getIntrinsicWidth(),sexDrawable.getIntrinsicHeight());
                            tvName.setCompoundDrawables(null, null, sexDrawable, null);
                        }

                        loadImage(user.getFaceImg());
                        tvProfile.setText(showProfile(user.getBirthday(),user.getWeight(),user.getHeight(),user.getMaritalStatus()));
                        tvFollow.setText(getString(R.string.mine_follow)+" "+ info.getMeGzAll());
                        tvFans.setText(getString(R.string.mine_fans)+" "+ info.getGzMeAll());

                        ((PersonHomeFragment)mFragments[0]).update(user);
                    }

                    @Override
                    public void onError(Throwable e) {}
                });
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

    private  void  loadImage(String url){
        RequestOptions backgroundOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);

        if (TextUtils.isEmpty(url)){
            return;
        }

        Glide.with(this)
                .asBitmap()
                .load(url)
                .apply(backgroundOptions)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        ivPortrait.setImageBitmap(resource);
                        Blurry.with(PersonMainActivity.this)
                                .color(0x5F23B6B7)
                                .from(resource)
                                .into(ivBackground);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_share:
                ;
                break;

            case R.id.btn_follow:
                popupChooseFollow.show(btnFollow);
                break;

            case R.id.btn_contact:
                popupChooseContact.show(btnContact);
                break;

            case R.id.btn_hots:
                popupChooseHots.show(btnHots);
                break;
        }
    }

    private class PersonAdapter extends FragmentPagerAdapter {
        PersonAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
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

    private void parseData(Intent data,Bundle saved){
        mUserId = data.getLongExtra(EXTRA_USER_ID,-1);
        if(saved!=null)
            mUserId = saved.getLong(EXTRA_USER_ID,-1);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(EXTRA_USER_ID, mUserId);
    }

    private static final String EXTRA_USER_ID = "UserId";
    public static void toPerson(Context context,long userId){
        Intent intent = new Intent(context,PersonMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(EXTRA_USER_ID,userId);
        context.startActivity(intent);
    }

}
