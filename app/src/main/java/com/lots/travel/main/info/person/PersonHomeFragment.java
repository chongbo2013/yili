package com.lots.travel.main.info.person;

import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lots.travel.R;
import com.lots.travel.base.RxBaseFragment;
import com.lots.travel.network.model.result.UserInformation;
import com.lots.travel.util.TypeUtil;

/**
 * Created by nalanzi on 2017/11/23.
 */

public class PersonHomeFragment extends RxBaseFragment {
    private boolean expanded = false;

    private RelativeLayout rlEmotion,rlLocation,rlJoinTime;
    private TextView tvControl;

    private TextView tvNickname,tvSex,tvProfession;
    private TextView tvEmotion,tvLocation,tvJoinTime;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_person_home;
    }

    @Override
    protected void initView(View v) {
        rlEmotion = (RelativeLayout) v.findViewById(R.id.rl_emotion);
        rlLocation = (RelativeLayout) v.findViewById(R.id.rl_location);
        rlJoinTime = (RelativeLayout) v.findViewById(R.id.rl_join_time);
        tvControl = (TextView) v.findViewById(R.id.tv_control);

        tvNickname = (TextView) v.findViewById(R.id.tv_nickname);
        tvSex = (TextView) v.findViewById(R.id.tv_sex);
        tvProfession = (TextView) v.findViewById(R.id.tv_profession);

        tvEmotion = (TextView) v.findViewById(R.id.tv_emotion);
        tvLocation = (TextView) v.findViewById(R.id.tv_location);
        tvJoinTime = (TextView) v.findViewById(R.id.tv_join_time);

        tvControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanded = !expanded;
                rlEmotion.setVisibility(expanded ? View.VISIBLE:View.GONE);
                rlLocation.setVisibility(expanded ? View.VISIBLE:View.GONE);
                rlJoinTime.setVisibility(expanded ? View.VISIBLE:View.GONE);
                tvControl.setText(expanded ?
                        R.string.person_home_collapse:R.string.person_home_expand);
            }
        });

        final SwipeRefreshLayout swipeRefresh = (SwipeRefreshLayout) v;
        swipeRefresh.setColorSchemeResources(R.color.color_main);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    protected void initData() {}

    public void update(UserInformation.User user){
        tvNickname.setText(user.getUserName());
        String str = user.getSex();
        int sexId = -1;
        if(!TextUtils.isEmpty(str)){
            if (str.equals("1")){
                sexId = R.string.male;
            } else if(str.equals("2")) {
                sexId = R.string.female;
            }
        }
        tvSex.setText(sexId==-1 ? R.string.secret:sexId);
        tvProfession.setText(user.getTitle());
        str = user.getMaritalStatus();
        if(!TextUtils.isEmpty(str)){
            switch (str){
                case "0":
                    tvEmotion.setText(R.string.emotion_single);
                    break;
                case "1":
                    tvEmotion.setText(R.string.emotion_married);
                    break;
                case "2":
                    tvEmotion.setText(R.string.emotion_divorce);
                    break;
            }
        }
        tvLocation.setText(TypeUtil.mergeStrings(user.getProvince(),user.getCity(),user.getDistrict()));
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//        tvJoinTime.setText();
    }
}
