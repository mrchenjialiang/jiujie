package com.jiujie.base.http.okhttp;

import android.app.Activity;
import android.text.TextUtils;

import com.jiujie.base.APP;
import com.jiujie.base.jk.ICallback;
import com.jiujie.base.util.ImageUtil;
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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * author : Created by ChenJiaLiang on 2016/6/24.
 * Email : 576507648@qq.com
 */
public abstract class MyOkHttpUtil {

    private final OkHttpClient okHttpClient;
    private final boolean isNeedDecode;

    protected MyOkHttpUtil(){
        this(false);
    }

    protected MyOkHttpUtil(boolean isNeedDecode){
        this.isNeedDecode = isNeedDecode;
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(getConnectTimeOutSecond(), TimeUnit.SECONDS);
        builder.readTimeout(getReadTimeOutSecond(), TimeUnit.SECONDS);
        builder.cookieJar(new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                MyOkHttpUtil.this.saveFromResponse(httpUrl, list);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                return MyOkHttpUtil.this.loadForRequest(httpUrl);
            }
        });
        if (APP.isDeBug) {
            builder.addInterceptor(new LoggerInterceptor(true));
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
        File cacheFile = new File(ImageUtil.instance().getCacheSDDic(APP.getContext())+"httpCache/");
        builder .cache(new Cache(cacheFile, 30 * 1024 * 1024));//最大 30m

        okHttpClient = builder.build();
    }

    protected abstract int getConnectTimeOutSecond();
    protected abstract int getReadTimeOutSecond();

    protected abstract void saveFromResponse(HttpUrl httpUrl, List<Cookie> list);
    protected abstract List<Cookie> loadForRequest(HttpUrl httpUrl);

    private String getGetUrl(String url, Map<String, Object> paramMap) {
        if (paramMap != null && paramMap.size() > 0) {
            StringBuilder sb = new StringBuilder(url);
            sb.append("?");
            for (String key : paramMap.keySet()) {
                if(paramMap.get(key)==null){
                    continue;
                }
                if (sb.length() != (url.length() + 1)) {
                    sb.append("&");
                }
                try {
                    sb.append(key)
                            .append("=")
                            .append(URLEncoder.encode(paramMap.get(key).toString(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }
        return url;
    }

    public void httpGet(final Activity activity, String url, Map<String, Object> paramMap, final ICallback<String> callback) {
        httpGet(activity, url, paramMap, null, callback);
    }

    public void httpGet(final Activity activity, String url, Map<String, Object> paramMap, String tag, final ICallback<String> callback) {
        Request.Builder builder = new Request.Builder();
        builder.get().url(getGetUrl(url, paramMap));
        if(!TextUtils.isEmpty(tag)){
            builder.tag(tag);
        }

        Request request = builder.build();
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
            public void onResponse(Call call, final Response response) throws IOException {
                if (activity != null && !activity.isFinishing() && callback != null) {
                    try {
                        if (response.isSuccessful()) {
                            final String string = response.newBuilder().build().body().string();
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(isNeedDecode){
                                        callback.onSucceed(UIHelper.decode(string));
                                    }else{
                                        callback.onSucceed(string);
                                    }
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
     * callBack in background thread
     */
    public void httpGet1(String url, Map<String, Object> paramMap,final ICallback<String> callback) {
        httpGet1(url,paramMap,null,callback);
    }

    /**
     * callBack in background thread
     */
    public void httpGet1(String url, Map<String, Object> paramMap, String tag, final ICallback<String> callback) {
        Request.Builder builder = new Request.Builder();
        builder.get().url(getGetUrl(url, paramMap));
        if(!TextUtils.isEmpty(tag)){
            builder.tag(tag);
        }

        Request request = builder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                try {
                    callback.onFail(e.getMessage());
                } catch (Exception ex) {
                    callback.onFail("服务器异常，请稍后再试");
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String string = response.newBuilder().build().body().string();
                        if(isNeedDecode){
                            callback.onSucceed(UIHelper.decode(string));
                        }else{
                            callback.onSucceed(string);
                        }
                    } else {
                        callback.onFail("服务器异常，请稍后再试");
                    }
                } catch (Exception ex) {
                    callback.onFail("服务器异常，请稍后再试");
                }
            }
        });
    }

    public void httpPost(String url, Map<String, String> postParamMap, String tag, Callback callback) {
        RequestBody formBody;
        if (postParamMap != null && postParamMap.size() > 0) {
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : postParamMap.keySet()) {
                builder.add(key, postParamMap.get(key));
            }
            formBody = builder.build();
        } else {
            formBody = new FormBody.Builder().build();
        }

        Request.Builder builder = new Request.Builder();
        builder.url(url).post(formBody);
        if(!TextUtils.isEmpty(tag)){
            builder.tag(tag);
        }

        Request request = builder.build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public void httpPost1(String url, Map<String, String> postParamMap,final ICallback<String> callback) {
        httpPost(url, postParamMap, null, new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                String message = e.getMessage();
                callback.onFail(TextUtils.isEmpty(message) ? "服务器异常，请稍后再试" : message);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String string = response.newBuilder().build().body().string();
                        if(isNeedDecode){
                            callback.onSucceed(UIHelper.decode(string));
                        }else{
                            callback.onSucceed(string);
                        }
                    } else {
                        callback.onFail("服务器异常，请稍后再试");
                    }
                } catch (Exception ex) {
                    callback.onFail("服务器异常，请稍后再试");
                }
            }
        });
    }

    /**
     * 参数使用表单方式上传
     * callback in background thread
     */
    public void httpPostForm(String url, Map<String, Object> postParamMap, String tag, Callback callback) {
        RequestBody requestBody;
        String result = "";
        if (postParamMap != null) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Object> entry : postParamMap.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            if (postParamMap.size() > 0) {
                result = sb.substring(0, sb.length() - 1);
            }
            MediaType FORM_CONTENT_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
            requestBody = RequestBody.create(FORM_CONTENT_TYPE, result);
        }else{
            requestBody = new FormBody.Builder().build();
        }

        Request.Builder builder = new Request.Builder();
        builder.url(url).post(requestBody);
        if(!TextUtils.isEmpty(tag)){
            builder.tag(tag);
        }

        Request request = builder.build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public void httpPost(final Activity activity, String url, Map<String, String> postParamMap,final ICallback<String> callback) {
        httpPost(activity,url,postParamMap,null,callback);
    }

    public void httpPost(final Activity activity, String url, Map<String, String> postParamMap, String tag, final ICallback<String> callback) {
        httpPost(url, postParamMap, tag, new Callback() {
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
            public void onResponse(Call call, final Response response) throws IOException {
                if (activity != null && !activity.isFinishing() && callback != null) {
                    try {
                        if (response.isSuccessful()) {
                            //这一步也是网络操作。。。
                            final String string = response.newBuilder().build().body().string();
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(isNeedDecode){
                                        callback.onSucceed(UIHelper.decode(string));
                                    }else{
                                        callback.onSucceed(string);
                                    }
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
     * 用于表单上传图片
     */
    protected void httpPostFile(final Activity activity, String url, Map<String, Object> postParamMap, File file, String keyName, final ICallback<String> callback) {
        httpPostFile(activity,url,postParamMap,file,null,keyName,null,callback);
    }

    /**
     * 用于表单上传图片
     */
    private void httpPostFile(final Activity activity, String url, Map<String, Object> postParamMap, File file, String fileType, String keyName, String tag, final ICallback<String> callback) {
        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);
        if (file != null) {
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/"+(TextUtils.isEmpty(fileType)?"*":fileType)), file);
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            if (TextUtils.isEmpty(keyName)) {
                keyName = "uploadedfile";
            }
            requestBody.addFormDataPart(keyName, file.getName(), body);
        }
        if (postParamMap != null && postParamMap.size() > 0) {
            // map 里面是请求中所需要的 key 和 value
            for (String key : postParamMap.keySet()) {
//                requestBody.addFormDataPart(key, postParamMap.get(key));
                requestBody.addFormDataPart(key, postParamMap.get(key).toString());
            }
        }
        Request.Builder builder = new Request.Builder();
        builder.addHeader("Connection", "close");
        builder.addHeader("Content-Type","multipart/form-data");
        builder.url(url).post(requestBody.build());
        if(!TextUtils.isEmpty(tag)){
            builder.tag(tag);
        }
        Request request = builder.build();

        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        OkHttpClient mOkHttpClient = okHttpBuilder.connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).writeTimeout(10,TimeUnit.SECONDS).build();
        if (APP.isDeBug) {
            okHttpBuilder.addInterceptor(new LoggerInterceptor(true));
        }
        mOkHttpClient.newCall(request).enqueue(new Callback() {
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
            public void onResponse(Call call, final Response response) throws IOException {
                if (activity != null && !activity.isFinishing() && callback != null) {
                    try {
                        if (response.isSuccessful()) {
                            //这一步也是网络操作。。。
                            final String string = response.newBuilder().build().body().string();
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(isNeedDecode){
                                        callback.onSucceed(UIHelper.decode(string));
                                    }else{
                                        callback.onSucceed(string);
                                    }
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

    public void httpPostGBK(final Activity activity, String url, Map<String, String> postParamMap, String tag, final ICallback<String> callback) {
        MyFormBody formBody;
        if (postParamMap != null && postParamMap.size() > 0) {
            MyFormBody.Builder builder = new MyFormBody.Builder();
            for (String key : postParamMap.keySet()) {
                builder.add(key, postParamMap.get(key));
            }
            formBody = builder.build();
        } else {
            formBody = new MyFormBody.Builder().build();
        }

        Request.Builder builder = new Request.Builder();
        builder.url(url).post(formBody);
        if(!TextUtils.isEmpty(tag)){
            builder.tag(tag);
        }

        Request request = builder.build();
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
            public void onResponse(Call call, final Response response) throws IOException {
                if (activity != null && !activity.isFinishing() && callback != null) {
                    try {
                        if (response.isSuccessful()) {
                            //这一步也是网络操作。。。
                            final String string = response.newBuilder().build().body().string();
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(isNeedDecode){
                                        callback.onSucceed(UIHelper.decode(string));
                                    }else{
                                        callback.onSucceed(string);
                                    }
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

//    @Multipart
//    @POST("uploadProfile")
//    Call<User> uploadProfile(@PartMap Map<String, RequestBody> params);
//
//    这是参数：
//    RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data"), user.username);
//    RequestBody password = RequestBody.create(MediaType.parse("multipart/form-data"), user.password);
//    File imgFile = new File(croppedImagePath);
//    RequestBody file = RequestBody.create(MediaType.parse("image/*"), imgFile);
//
//    Map<String, RequestBody> params = new HashMap<>();
//    params.put("username", username);
//    params.put("password", password);
//    params.put("file[]\"; filename=\"" + imgFile.getName(), file);

}
