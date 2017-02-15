package com.jiujie.base.http.rx;


import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public abstract class BaseHttpMethods<T> {

    public T httpService;
    private Cookie SESSION_COOKIE;

    public void logoff(){
        SESSION_COOKIE = null;
    }

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
        builder.cookieJar(new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Cookie cookie = list.get(i);
                        if (cookie.name().equals("SESSION")) {
                            String path = httpUrl.url().getPath();
                            if(path.contains("/api/app-login")){
                                SESSION_COOKIE = cookie;
//                                UIHelper.showLog("saveFromResponse SESSION_COOKIE:"+SESSION_COOKIE);
                            }
                        }
                    }
                }
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                List<Cookie> cookies = new ArrayList<>();
                if(SESSION_COOKIE!=null){
                    cookies.add(SESSION_COOKIE);
                    if (SESSION_COOKIE.name().equals("SESSION")) {
//                        UIHelper.showLog("loadForRequest SESSION_COOKIE:"+SESSION_COOKIE);
                    }
                }
                return cookies;
            }
        });

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
