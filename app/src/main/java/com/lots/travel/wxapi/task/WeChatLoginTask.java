package com.lots.travel.wxapi.task;

import android.content.Context;
import android.content.Intent;

import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.network.model.result.WeChatUserInfo;
import com.lots.travel.wxapi.task.presenter.WeChatLoginPresenter;

/**
 * Created by nalanzi on 2017/10/14.
 */

public class WeChatLoginTask {

    public static Presenter create(RxBaseActivity context,View view){
        return new WeChatLoginPresenter(context,view);
    }

    public interface Presenter{
        void initWXAPI(Context context);

        void executeWeChatAuth();
        boolean handleWeChatAuthResult(Intent data);

        void obtainWeChatToken(String code);
        void obtainWeChatUserInfo(String accessToken,String openId);

        void login(WeChatUserInfo userInfo);
    }

    public interface View{
        void showLoadingDialog(String msg);
        void dismissLoadingDialog();
        void finishActivity();
        void toMainActivity();
        void sharingSuccess();
    }

}
