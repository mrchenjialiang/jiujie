package com.jiujie.base.http.rx;


import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public abstract class BaseHttpMethods<T> {

    public T httpService;

    protected BaseHttpMethods() {
        if(TextUtils.isEmpty(getBaseUrl())){
            throw new NullPointerException("getBaseUrl should not return null");
        }
        if(getTimeOutSecond()==0){
            throw new NullPointerException("getTimeOutSecond should not return 0");
        }
        if(getServiceClass()==null){
            throw new NullPointerException("getServiceClass should not return null");
        }
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(getTimeOutSecond(), TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(getBaseUrl())
                .build();

        httpService = retrofit.create(getServiceClass());
    }

    /**
     * 必须重写
     */
    protected abstract Class<T> getServiceClass();

    /**
     * 必须重写
     */
    protected abstract String getBaseUrl();

    /**
     * 必须重写
     */
    protected abstract int getTimeOutSecond();

}
