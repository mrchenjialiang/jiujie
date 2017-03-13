package com.jiujie.base.http.rx;


import android.app.Activity;
import android.text.TextUtils;

import com.jiujie.base.APP;
import com.jiujie.base.http.okhttp.LoggerInterceptor;
import com.jiujie.base.jk.ICallback;
import com.jiujie.base.util.UIHelper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public abstract class BaseHttpMethods<T> {

    public T httpService;
    public final OkHttpClient okHttpClient;

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
                BaseHttpMethods.this.saveFromResponse(httpUrl, list);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                return BaseHttpMethods.this.loadForRequest(httpUrl);
            }
        });
        if (APP.isDeBug) {
            builder.addInterceptor(new LoggerInterceptor("LOG"));
        }

        okHttpClient = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
//                .addConverterFactory(ScalarsConverterFactory.create())//只能用于请求String等基本类型数据
//                .addConverterFactory(GsonConverterFactory.create())//只能请求json
                .addConverterFactory(getConverterFactory())//只能请求json
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(getBaseUrl())
                .build();

        httpService = retrofit.create(getServiceClass());
    }

    public Converter.Factory getConverterFactory(){
//        return ResponseConverterFactory.create(buildGson());
//        return ScalarsConverterFactory.create();//只能用于请求String等基本类型数据
        return GsonConverterFactory.create();//只能请求json，而且，0.3类型写成int会报错等
//        return GsonConverterFactory.create(buildGson());//只能请求json，而且，0.3类型写成int会报错等
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

    protected abstract void saveFromResponse(HttpUrl httpUrl, List<Cookie> list);
    protected abstract List<Cookie> loadForRequest(HttpUrl httpUrl);

    /**
     * must do in background thread
     */
    public void httpPost(final Activity activity, String url, Map<String, String> postParamMap,final ICallback<String> callback){
        RequestBody formBody;
        if (postParamMap != null && postParamMap.size() > 0) {
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : postParamMap.keySet()) {
                builder.add(key, postParamMap.get(key));
            }
            formBody = builder.build();
        } else {
            UIHelper.showLog("httpPost should have post params");
            return;
        }

        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).post(formBody).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (activity != null && callback != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String message = e.getMessage();
                            callback.onFail(TextUtils.isEmpty(message) ? "服务器异常，请稍后再试" : message);
                        }
                    });
                }
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(activity!=null&&callback!=null){
                    try {
                        if (response.isSuccessful()) {
                            //这一步也是网络操作。。。
                            ResponseBody body = response.body();
                            final String decode = UIHelper.decode(body.string());
                            body.close();
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onSucceed(decode);
                                }
                            });
                        } else {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFail("服务器异常，请稍后再试");
                                }
                            });
                        }
                    } catch (Exception ex) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFail("服务器异常，请稍后再试");
                            }
                        });
                    }
                }
            }
        });
    }

}
