package com.jiujie.base.http.rx;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.jiujie.base.APP;
import com.jiujie.base.http.okhttp.LoggerInterceptor;
import com.jiujie.base.jk.ICallback;
import com.jiujie.base.util.UIHelper;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
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

    protected T httpService;
    protected OkHttpClient okHttpClient;

    protected BaseHttpMethods() {
        if (TextUtils.isEmpty(getBaseUrl())) {
            throw new NullPointerException("getBaseUrl should not return null");
        }
        if (getConnectTimeOutSecond() == 0) {
            throw new NullPointerException("getConnectTimeOutSecond should not return 0");
        }
        if (getReadTimeOutSecond() == 0) {
            throw new NullPointerException("getReadTimeOutSecond should not return 0");
        }
        if (getServiceClass() == null) {
            throw new NullPointerException("getServiceClass should not return null");
        }
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(getConnectTimeOutSecond(), TimeUnit.SECONDS);
        builder.readTimeout(getReadTimeOutSecond(), TimeUnit.SECONDS);
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
            builder.addInterceptor(new LoggerInterceptor());
        }
        builder.addNetworkInterceptor(new Interceptor() {//添加网络拦截器
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                if(!UIHelper.getNetWorkStatus(APP.getContext())){
                    int maxStale = 60 * 60 * 24 * 7;// 没网 就1周可用
                    return response.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }else{
                    return response.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", CacheControl.FORCE_NETWORK.toString())
                            .build();
                }
//                if (isNetworkConnected(activity)) {
////                    int maxAge = 60 * 60;// 有网 就1个小时可用
//                    int maxAge = 2;// 有网 就2秒可用
//                    return response.newBuilder()
//                            .header("Cache-Control", "public, max-age=" + maxAge)
//                            .build();
//                } else {
//                    int maxStale = 60 * 60 * 24 * 7;// 没网 就1周可用
//                    return response.newBuilder()
//                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                            .build();
//                }
            }
        });
        //data下
        Cache cache = new Cache(getHttpCacheFile(), getMaxCacheSize());
        builder.cache(cache);
        //sd卡下
//        File cacheFile = new File(ImageUtil.instance().getCacheSDDic(APP.getContext())+"httpCache/");
//        builder.cache(new Cache(cacheFile, 30 * 1024 * 1024));//最大 30m

        okHttpClient = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
//                .addConverterFactory(ScalarsConverterFactory.create())//只能用于请求String等基本类型数据
//                .addConverterFactory(GsonConverterFactory.create())//只能请求json
                .addConverterFactory(getConverterFactory())//交给外部决定
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(getBaseUrl())
                .build();

        httpService = retrofit.create(getServiceClass());

//        IntentFilter filter = new IntentFilter();
//        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
//        filter.addAction("android.net.wifi.STATE_CHANGE");
//        APP.getContext().registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                reset();
//            }
//        },filter);

    }

    public abstract void reset();

    protected int getMaxCacheSize() {
        return 1024 * 1024 * 30;
    }

    @NonNull
    public File getHttpCacheFile() {
        return new File(APP.getContext().getCacheDir(), "HttpCache");
    }

    public Converter.Factory getConverterFactory() {
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
    protected abstract int getConnectTimeOutSecond();
    protected abstract int getReadTimeOutSecond();

    protected abstract void saveFromResponse(HttpUrl httpUrl, List<Cookie> list);

    protected abstract List<Cookie> loadForRequest(HttpUrl httpUrl);

    /**
     * callback in UI thread
     */
    public void httpPost(final Activity activity, String url, Map<String, String> postParamMap, final ICallback<String> callback) {
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
                if (activity != null && !activity.isFinishing() && callback != null) {
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
                if (activity != null && !activity.isFinishing() && callback != null) {
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

    /**
     * callback in UI thread
     */
    public void httpGet(final Activity activity, String url, Map<String, Object> parmamMap, final ICallback<String> callback) {
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(getGetUrl(url, parmamMap)).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (activity != null && !activity.isFinishing() && callback != null) {
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
                if (activity != null && !activity.isFinishing() && callback != null) {
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

    public String getGetUrl(String url, Map<String, Object> paramMap) {
        if (paramMap != null && paramMap.size() > 0) {
            StringBuilder sb = new StringBuilder(url);
            sb.append("?");
            for (String key : paramMap.keySet()) {
                if (sb.length() != (url.length() + 1)) {
                    sb.append("&");
                }
                try {
                    sb.append(key)
                            .append("=")
                            .append(URLEncoder.encode(paramMap.get(key)
                                    .toString(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }
        return url;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

}
