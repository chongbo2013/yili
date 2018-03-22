package com.lots.travel.network;

import android.widget.Toast;

import com.lots.travel.base.BaseActivity;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

/**
 * Created by nalanzi on 2017/10/14.
 */

public class DefaultExceptionHandler implements HttpExceptionHandler {

    @Override
    public void handleException(BaseActivity context, Throwable ex) {
        if(ex instanceof HttpException){
            Toast.makeText(context,"网络错误",Toast.LENGTH_SHORT).show();
        }else if(ex instanceof UnknownHostException){
            Toast.makeText(context,"无可用网络",Toast.LENGTH_SHORT).show();
        }else if(ex instanceof SocketTimeoutException){
            Toast.makeText(context,"网络请求超时",Toast.LENGTH_SHORT).show();
        }else if(ex instanceof ApiException){
            Toast.makeText(context,ex.getMessage(),Toast.LENGTH_SHORT).show();

            //在登录过期时，提示重新登录
//            ApiException exception = (ApiException) ex;
//            if(exception.getCode()==-1){
//                context.showExitDialog();
//            }
        }else
            Toast.makeText(context,ex.getMessage(),Toast.LENGTH_SHORT).show();
    }

}
