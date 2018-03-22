package com.lots.travel.network;

import android.net.Uri;

import com.lots.travel.AccountManager;
import com.lots.travel.store.db.GreenDaoManager;
import com.lots.travel.store.db.HttpInterface;
import com.lots.travel.store.db.HttpInterfaceDao;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.lots.travel.network.Util.trimStartSlash;

/**
 * Created by nalanzi on 2017/9/16.
 */

public class HttpPathInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();

        HttpUrl.Builder urlBuilder = oldRequest.url().newBuilder();

        List<String> paths = oldRequest.url().pathSegments();
        if(paths==null || paths.size()>1) {
            throw new RuntimeException("请使用path名称而不是完整的路径");
        }
        urlBuilder.removePathSegment(0);

        List<HttpInterface> httpInterfaces = GreenDaoManager.getInstance().getHttpInterfaceDao()
                .queryBuilder()
                .where(HttpInterfaceDao.Properties.Name.eq(paths.get(0)))
                .list();

        if(httpInterfaces==null || httpInterfaces.size()>1){
            throw new RuntimeException("path路径重复");
        }

        HttpInterface httpInterface = httpInterfaces.get(0);
        Uri uri = Uri.parse(httpInterface.getControl());
        urlBuilder.scheme(uri.getScheme());
        urlBuilder.host(uri.getHost());
        urlBuilder.port(uri.getPort());

        //修改path
        if(httpInterface.getAction()!=null)
            urlBuilder.addPathSegments(trimStartSlash(httpInterface.getAction()));

        if("POST".equalsIgnoreCase(oldRequest.method())){
            urlBuilder.addQueryParameter("from","android");
            if(AccountManager.getInstance().isCurrentValidate())
                urlBuilder.addQueryParameter("userId", AccountManager.getInstance().getUserId()+"");
        }

        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(urlBuilder.build())
                .build();

        return chain.proceed(newRequest);
    }
}
