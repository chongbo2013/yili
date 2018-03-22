package com.lots.travel.network;

import com.lots.travel.AccountManager;
import com.lots.travel.TravelApplication;
import com.lots.travel.store.SpStore;
import com.lots.travel.store.db.Account;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by nalanzi on 2017/9/11.
 */
//sosona使用的是json提交，这里不需要
@Deprecated
public class POSTFormParamsInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request orgRequest = chain.request();

        if(!"POST".equalsIgnoreCase(orgRequest.method()))
            return chain.proceed(orgRequest);

        RequestBody body = orgRequest.body();

        if (body != null) {

            RequestBody newBody = null;

            if (body instanceof FormBody) {
                newBody = addParamsToFormBody((FormBody) body);
            } else if (body instanceof MultipartBody) {
                newBody = addParamsToMultipartBody((MultipartBody) body);
            }

            if (null != newBody) {
                Request newRequest = orgRequest.newBuilder()
                        .url(orgRequest.url())
                        .method(orgRequest.method(), newBody)
                        .build();

                return chain.proceed(newRequest);
            }
        }

        return chain.proceed(orgRequest);
    }

    private MultipartBody addParamsToMultipartBody(MultipartBody body) {
        long timestamp = System.currentTimeMillis();
        int nonce = (int)((Math.random()*9+1)*100000);
        int fixIndex = (int) (Math.random()* Util.FIXS.length);

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("from", "android");
        builder.addFormDataPart("userId", Integer.toString(Account.long2int(AccountManager.getInstance().getUserId())));
        builder.addFormDataPart("timestamp", Long.toString(timestamp));
        builder.addFormDataPart("nonceStr", Integer.toString(nonce));
        builder.addFormDataPart("fix", Integer.toString(fixIndex));
        builder.addFormDataPart("signature", Util.generateSignature(timestamp,nonce,fixIndex));

        for (int i = 0; i < body.size(); i++) {
            builder.addPart(body.part(i));
        }

        return builder.build();
    }

    private FormBody addParamsToFormBody(FormBody body) {
        FormBody.Builder builder = new FormBody.Builder();

        long timestamp = System.currentTimeMillis();
        int nonce = (int)((Math.random()*9+1)*100000);
        int fixIndex = (int) (Math.random()* Util.FIXS.length);

        builder.add("from", "android");
        builder.add("userId", Integer.toString(Account.long2int(AccountManager.getInstance().getUserId())));
        builder.add("timestamp", Long.toString(timestamp));
        builder.add("nonceStr", Integer.toString(nonce));
        builder.add("fix", Integer.toString(fixIndex));
        builder.add("signature", Util.generateSignature(timestamp,nonce,fixIndex));

        for (int i = 0; i < body.size(); i++) {
            builder.addEncoded(body.encodedName(i), body.encodedValue(i));
        }

        return builder.build();
    }

}
