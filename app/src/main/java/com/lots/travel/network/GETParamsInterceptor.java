package com.lots.travel.network;

import com.lots.travel.AccountManager;
import com.lots.travel.TravelApplication;
import com.lots.travel.store.SpStore;
import com.lots.travel.store.db.Account;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by nalanzi on 2017/9/11.
 */
public class GETParamsInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();

        if(!"GET".equalsIgnoreCase(oldRequest.method()))
            return chain.proceed(oldRequest);

        Request newRequest = addParam(oldRequest);
        return chain.proceed(newRequest);
    }

    private Request addParam(Request oldRequest) {
        long timestamp = System.currentTimeMillis();
        int nonce = (int)((Math.random()*9+1)*100000);
        int fixIndex = (int) (Math.random()* Util.FIXS.length);

        HttpUrl.Builder builder = oldRequest.url()
                .newBuilder()
                .setEncodedQueryParameter("from", "android")
                .setEncodedQueryParameter("userId", Integer.toString(Account.long2int(AccountManager.getInstance().getUserId())))
                .setEncodedQueryParameter("timestamp",Long.toString(timestamp))
                .setEncodedQueryParameter("nonceStr",Integer.toString(nonce))
                .setEncodedQueryParameter("fix",Integer.toString(fixIndex))
                .setEncodedQueryParameter("signature", Util.generateSignature(timestamp,nonce,fixIndex));

        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(builder.build())
                .build();

        return newRequest;
    }

}
