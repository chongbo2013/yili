package com.lots.travel.network;

import com.bumptech.glide.RequestBuilder;
import com.lots.travel.AccountManager;
import com.lots.travel.store.SpStore;
import com.lots.travel.store.db.Account;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.ByteString;

/**
 * Created by nalanzi on 2017/9/11.
 */
//sosona使用的是json提交
public class POSTJsonParamsInterceptor implements Interceptor {
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request orgRequest = chain.request();

        if(!"POST".equalsIgnoreCase(orgRequest.method()))
            return chain.proceed(orgRequest);

        JsonBody body = null;
        if(orgRequest.body() instanceof  JsonBody)
            body = (JsonBody) orgRequest.body();

        if (body != null) {
            RequestBody newBody = null;
            try {
                newBody = addParamsToJsonBody(body.getContent());
            } catch (JSONException e) {
                e.printStackTrace();
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

    private JsonBody addParamsToJsonBody(String content) throws JSONException {
        JSONObject jsonObj = new JSONObject(content);

        long timestamp = Calendar.getInstance().getTimeInMillis();
        int nonce = (int)((Math.random()*9+1)*100000);
        int fixIndex = (int) (Math.random()* Util.FIXS.length);

        jsonObj.put("from", "android");
        jsonObj.put("userId", Long.toString(AccountManager.getInstance().getUserId()));
        jsonObj.put("timestamp", Long.toString(timestamp));
        jsonObj.put("nonceStr", Integer.toString(nonce));
        jsonObj.put("fix", Integer.toString(fixIndex));
        jsonObj.put("signature", Util.generateSignature(timestamp,nonce,fixIndex));

        return new JsonBody(ByteString.encodeUtf8(jsonObj.toString()));
    }

}
