package com.lots.travel.main.message;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;

import com.lots.travel.R;

/**
 * Created by nalanzi on 2017/9/26.
 */
public class MessageTabActivity extends AppCompatActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{
    private Fragment[] mFragments;
    private int mCurrentIndex = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_tab);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mFragments = new Fragment[]{
                new MessageListFragment(),
                new MessageGroupFragment(),
                new MessageNotificationFragment()};

        RadioGroup llTabs = (RadioGroup) findViewById(R.id.ll_tabs);
        llTabs.setOnCheckedChangeListener(this);

        int newIndex = 0;
        if(savedInstanceState!=null){
            newIndex = savedInstanceState.getInt("currentIndex",0);
        }

        llTabs.check(R.id.tab_list);
    }

    private void switchFragment(int newIndex){
        if(newIndex==mCurrentIndex)
            return;

        final int oldIndex = mCurrentIndex;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //隐藏旧的fragment
        if(oldIndex!=-1) {
            transaction.hide(mFragments[oldIndex]);
        }

        //显示新的fragment
        if(!mFragments[newIndex].isAdded())
            transaction.add(R.id.fl_container,mFragments[newIndex]);

        transaction.show(mFragments[newIndex]);
        transaction.commit();

        mCurrentIndex = newIndex;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        outState.putInt("currentIndex",mCurrentIndex);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                ;
                break;

            case R.id.tv_blacklist:
                ;
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId){
            case R.id.tab_list:
                ;
                break;

            case R.id.tab_group:
                ;
                break;

            case R.id.tab_notification:
                ;
                break;
        }
    }
}
