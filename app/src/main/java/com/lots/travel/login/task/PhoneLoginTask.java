package com.lots.travel.login.task;

import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.login.task.presenter.PhoneLoginPresenter;

/**
 * Author： liyi
 * Date：    2017/3/7.
 */
//验证码登陆 - 默认登陆方式
public class PhoneLoginTask {

    public static PhoneLoginPresenter create(View view){
        return new PhoneLoginPresenter(view);
    }

    public interface Presenter{
        void obtainPin(RxBaseActivity context, String phone);
        void login(RxBaseActivity context, String phone, String pin);
    }

    public interface View{
        void showLoadingDialog(String msg);
        void dismissLoadingDialog();
        void toMainActivity();
        void startCountDown();
    }

}
