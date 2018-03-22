package com.lots.travel.network;

import android.content.Context;

import com.lots.travel.util.Constant;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class ServiceGenerator {
    private final static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private final static Retrofit.Builder builder = new Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(SosonaGsonConverterFactory.create());

    private final static OkHttpClient httpClient = new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(loggingInterceptor)
            .build();

    //已登录
    public static <S> S createService(Context context, Class<S> serviceClass) {
        OkHttpClient client = httpClient.newBuilder()
                .addInterceptor(new HttpPathInterceptor())
                .addInterceptor(new GETParamsInterceptor())
                .addInterceptor(new POSTJsonParamsInterceptor())
                .build();

        Retrofit retrofit = builder.baseUrl(Constant.HOST_HTTP)
                .client(client)
                .build();
        return retrofit.create(serviceClass);
    }

    //未登录
    public static <S> S createAuthService(Context context, Class<S> serviceClass) {
        OkHttpClient client = httpClient.newBuilder()
                .addInterceptor(new HttpPathInterceptor())
                .build();

        Retrofit retrofit = builder.baseUrl(Constant.HOST_HTTP)
                .client(client)
                .build();
        return retrofit.create(serviceClass);
    }

    //微信
    public static <S> S createWXService(Context context, Class<S> serviceClass) {
        OkHttpClient client = httpClient.newBuilder()
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        Retrofit retrofit = builder.baseUrl(Constant.HOST_HTTP)
                .client(client)
                .build();
        return retrofit.create(serviceClass);
    }
}
