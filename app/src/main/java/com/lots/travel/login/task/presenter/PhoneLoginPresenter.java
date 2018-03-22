package com.lots.travel.login.task.presenter;

import android.widget.Toast;

import com.lots.travel.AccountManager;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.login.task.PhoneLoginTask;
import com.lots.travel.network.DefaultExceptionHandler;
import com.lots.travel.network.HttpExceptionHandler;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.SingleAdapter;
import com.lots.travel.network.model.result.LoginByMobileCode;
import com.lots.travel.network.service.LoginService;
import com.lots.travel.store.db.Account;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Author： liyi
 * Date：    2017/3/7.
 */

public class PhoneLoginPresenter implements PhoneLoginTask.Presenter{
    private PhoneLoginTask.View mView;

    public PhoneLoginPresenter(PhoneLoginTask.View view){
        mView = view;
    }

    @Override
    public void obtainPin(final RxBaseActivity context, String phone) {
        mView.showLoadingDialog(null);

        ServiceGenerator.createAuthService(context,LoginService.class)
                .getMobileCode(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(context.<HttpResult<String>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new SingleAdapter<HttpResult<String>>() {
                    @Override
                    public void onSuccess(@NonNull HttpResult<String> stringHttpResult) {
                        mView.dismissLoadingDialog();
                        mView.startCountDown();
                        Toast.makeText(context,"已发送验证码到您的手机",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.dismissLoadingDialog();
                        new DefaultExceptionHandler().handleException(context,e);
                    }

                });
    }

    @Override
    public void login(final RxBaseActivity context, final String phone, String pin){
        mView.showLoadingDialog(null);

        ServiceGenerator.createAuthService(context,LoginService.class)
                .loginByMobileCode(phone,pin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(context.<HttpResult<LoginByMobileCode>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new SingleAdapter<HttpResult<LoginByMobileCode>>() {

                    @Override
                    public void onSuccess(@NonNull HttpResult<LoginByMobileCode> result) {
                        LoginByMobileCode data = result.getData();
                        LoginByMobileCode.User user = data.getUser();

                        Account account = new Account();
                        account.setUserId(Account.int2long(user.getUserId()));
                        account.setSecret(user.getScr());
                        AccountManager.getInstance().login(account);

                        mView.dismissLoadingDialog();
                        mView.toMainActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.dismissLoadingDialog();
                        new DefaultExceptionHandler().handleException(context,e);
                    }

                });
    }

}
