package com.lots.travel.wxapi.task.presenter;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lots.travel.AccountManager;
import com.lots.travel.base.RxBaseActivity;
import com.lots.travel.network.DefaultExceptionHandler;
import com.lots.travel.network.HttpResult;
import com.lots.travel.network.ServiceGenerator;
import com.lots.travel.network.SingleAdapter;
import com.lots.travel.network.model.request.WeChatLogin;
import com.lots.travel.network.model.result.LoginByWeChat;
import com.lots.travel.network.model.result.WeChatUserInfo;
import com.lots.travel.network.model.result.WeChatToken;
import com.lots.travel.network.service.LoginService;
import com.lots.travel.store.db.Account;
import com.lots.travel.util.Constant;
import com.lots.travel.wxapi.task.WeChatLoginTask;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by nalanzi on 2017/10/14.
 */

public class WeChatLoginPresenter implements WeChatLoginTask.Presenter,IWXAPIEventHandler {
    private RxBaseActivity mContext;
    private WeChatLoginTask.View mView;

    private IWXAPI mWXAPI;

    public WeChatLoginPresenter(RxBaseActivity context,WeChatLoginTask.View view){
        mContext = context;
        mView = view;
    }

    @Override
    public void initWXAPI(Context context) {
        // 通过WXAPIFactory工厂,获得IWXAPI的实例
        mWXAPI = WXAPIFactory.createWXAPI(context, Constant.WX_APP_ID,true);
        // 将应用的appid注册到微信
        mWXAPI.registerApp(Constant.WX_APP_ID);
    }

    @Override
    public void executeWeChatAuth() {
        mView.showLoadingDialog(null);
        SendAuth.Req req=new SendAuth.Req();
        req.scope="snsapi_userinfo";
        req.state="carjob_wx_login";
        mWXAPI.sendReq(req);
    }

    @Override
    public boolean handleWeChatAuthResult(Intent data) {
        mView.showLoadingDialog(null);
        return mWXAPI.handleIntent(data,this);
    }

    @Override
    public void obtainWeChatToken(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+Constant.WX_APP_ID
                +"&secret="+Constant.WX_APP_SECRET+"&code="+code+"&grant_type=authorization_code";

        requestWeChat(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mView.finishActivity();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    try
                    {
                        String json = response.body().string();
                        if(json.contains("errcode")) {
                            mView.finishActivity();
                        } else {
                            WeChatToken token = new Gson().fromJson(json,WeChatToken.class);
                            obtainWeChatUserInfo(token.getAccessToken(),token.getOpenId());
                        }
                    } catch(NullPointerException|JsonSyntaxException e) {
                        mView.finishActivity();
                    }
                }else
                    mView.finishActivity();
            }
        });
    }

    @Override
    public void obtainWeChatUserInfo(String accessToken,String openId) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token="+accessToken+"&openid="+openId;

        requestWeChat(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mView.finishActivity();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try
                {
                    String json = response.body().string();
                    if(json.contains("errcode")) {
                        mView.finishActivity();
                    } else {
                        WeChatUserInfo weChatUserInfo = new Gson().fromJson(json,WeChatUserInfo.class);
                        login(weChatUserInfo);
                    }
                } catch(NullPointerException|JsonSyntaxException e) {
                    mView.finishActivity();
                }
            }
        });
    }

    @Override
    public void login(WeChatUserInfo userInfo) {
        WeChatLogin weChatLogin = new WeChatLogin();
        weChatLogin.setCity(userInfo.getCity());
        weChatLogin.setCountry(userInfo.getCountry());
        weChatLogin.setHeadimgurl(userInfo.getHeadImgUrl());
        weChatLogin.setNickname(userInfo.getNickname());
        weChatLogin.setOpenid(userInfo.getOpenId());
        weChatLogin.setProvince(userInfo.getProvince());
        weChatLogin.setSex(userInfo.getSex());
        weChatLogin.setUnionid(userInfo.getUnionId());

        ServiceGenerator.createAuthService(mContext,LoginService.class)
                .loginByWeChat(weChatLogin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mContext.<HttpResult<LoginByWeChat>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new SingleAdapter<HttpResult<LoginByWeChat>>() {

                    @Override
                    public void onSuccess(@NonNull HttpResult<LoginByWeChat> result) {
                        LoginByWeChat data = result.getData();

                        Account account = new Account();
                        account.setUserId(Account.int2long(data.getUserId()));
                        account.setSecret(data.getScr());
                        account.setSex(data.getSex());
                        account.setPortraitUrl(data.getFaceImg());
                        account.setUsername(data.getUserName());
                        AccountManager.getInstance().login(account);

                        mView.dismissLoadingDialog();
                        mView.toMainActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.dismissLoadingDialog();
                        new DefaultExceptionHandler().handleException(mContext,e);
                        mView.finishActivity();
                    }

                });
    }

    @Override
    public void onReq(BaseReq baseReq) {}

    @Override
    public void onResp(BaseResp resp) {
        switch(resp.errCode) {
            case BaseResp.ErrCode.ERR_OK: {
                if(resp instanceof SendAuth.Resp) {
                    SendAuth.Resp newResp = (SendAuth.Resp)resp;
                    //获取微信传回的code
                    String code = newResp.code;
                    obtainWeChatToken(code);
                }
                else if(resp instanceof SendMessageToWX.Resp)//微信分享
                {
                    mView.sharingSuccess();
                }
                else
                    mView.finishActivity();
            }
            break;

            case BaseResp.ErrCode.ERR_USER_CANCEL:
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            default:
                mView.finishActivity();
            break;
        }
    }

    //http请求，获取
    private void requestWeChat(String url,Callback callback) {
        // 创建请求客户端
        OkHttpClient okClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();
        // 创建请求参数
        Request request = new Request.Builder().url(url).build();
        // 创建请求对象
        Call call = okClient.newCall(request);
        // 发起异步的请求
        call.enqueue(callback);
    }

}
