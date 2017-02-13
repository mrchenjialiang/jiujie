package com.jiujie.base.http.okhttp;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.jiujie.base.APP;
import com.jiujie.base.jk.ICallback;
import com.jiujie.base.util.UIHelper;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * author : Created by ChenJiaLiang on 2016/6/24.
 * Email : 576507648@qq.com
 */
public class MyOkHttpUtil {


    private static OkHttpClient.Builder builder;
//    private static OkHttpClient okHttpClient;

    public static OkHttpClient getOkHttpClient(Context context,boolean isSaveCookie,boolean isUseCookie,String saveCookieKey,String[] useCookieKeys) {
        if (builder == null) {
            builder = new OkHttpClient.Builder();
            builder .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                    .readTimeout(10000L, TimeUnit.MILLISECONDS)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });
            if (APP.isDeBug) {
                builder.addInterceptor(new LoggerInterceptor("CJL"));
            }
        }
        builder.cookieJar(new MyCookieJar(context,isSaveCookie,isUseCookie,saveCookieKey,useCookieKeys));
        return builder.build();
    }

    public static void clearCookie(){
        if(builder!=null){
            CookieJar cookieJar = builder.build().cookieJar();
            if(cookieJar!=null&&cookieJar instanceof MyCookieJar){
                MyCookieJar myCookieJar = (MyCookieJar) cookieJar;
                myCookieJar.clearCookie();
            }
        }
    }

    private static String getGetUrl(String url, Map<String, Object> paramMap) {
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

    public static void httpGet(final Activity activity, String url, Map<String, Object> paramMap, String tag, final boolean isSaveCookie, final boolean isUseCookie,String saveCookieKey,String[] useCookieKeys, final ICallback<String> callback) {
        Request.Builder builder = new Request.Builder();
        builder.get().url(getGetUrl(url, paramMap)).tag(tag);

        Request request = builder.build();
        getOkHttpClient(activity,isSaveCookie,isUseCookie,saveCookieKey,useCookieKeys).newCall(request).enqueue(new Callback() {
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
            public void onResponse(Call call, final Response response) throws IOException {
                if (activity != null && callback != null) {
                    try {
                        if (response.isSuccessful()) {
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

    public static void httpGet(final Activity context, String url, Map<String, Object> paramMap, String tag, final boolean isSaveCookie, final boolean isUseCookie, final ICallback<String> callback) {
        httpGet(context, url, paramMap, tag, isSaveCookie, isUseCookie, null, null, callback);
    }

    /**
     * callBack in thread no UI thread
     */
    public static void httpGet1(final Context context, String url, Map<String, Object> paramMap, String tag, final boolean isSaveCookie, final boolean isUseCookie,String saveCookieKey,String[] useCookieKeys, final ICallback<String> callback) {
        Request.Builder builder = new Request.Builder();
        builder.get().url(getGetUrl(url, paramMap)).tag(tag);

        Request request = builder.build();
        getOkHttpClient(context, isSaveCookie, isUseCookie,saveCookieKey,useCookieKeys).newCall(request).enqueue(new Callback() {
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
                        ResponseBody body = response.body();
                        final String decode = UIHelper.decode(body.string());
                        callback.onSucceed(decode);
                        body.close();
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
     * callBack in thread no UI thread
     */
    public static void httpGet1(final Context context, String url, Map<String, Object> paramMap, String tag, final boolean isSaveCookie, final boolean isUseCookie, final ICallback<String> callback) {
        httpGet1(context, url, paramMap, tag, isSaveCookie, isUseCookie, null, null, callback);
    }

    public static void httpPost(final Activity activity, String url, Map<String, String> postParamMap, String tag, final boolean isSaveCookie, final boolean isUseCookie,String saveCookieKey,String[] useCookieKeys, final ICallback<String> callback) {
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
        builder.url(url).post(formBody).tag(tag);

        Request request = builder.build();
        getOkHttpClient(activity,isSaveCookie,isUseCookie,saveCookieKey,useCookieKeys).newCall(request).enqueue(new Callback() {
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
            public void onResponse(Call call, final Response response) throws IOException {
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

    /**
     * 用于表单上传图片
     */
    public static void httpPostFile(Activity activity,String url,Map<String, String> postParamMap, File file,String keyName,String tag,ICallback<String> callback){
        httpPostFile(activity, url, postParamMap, file, keyName, tag, false, false, null, null, callback);
    }

    /**
     * 用于表单上传图片
     */
    public static void httpPostFile(final Activity activity, String url, Map<String, String> postParamMap, File file,String keyName,String tag, final boolean isSaveCookie, final boolean isUseCookie,String saveCookieKey,String[] useCookieKeys, final ICallback<String> callback) {
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if(file != null){
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            if(TextUtils.isEmpty(keyName)){
                keyName = "uploadedfile";
            }
            requestBody.addFormDataPart(keyName, file.getName(), body);
        }
        if (postParamMap != null&&postParamMap.size()>0) {
            // map 里面是请求中所需要的 key 和 value
            for (String key : postParamMap.keySet()) {
                requestBody.addFormDataPart(key, postParamMap.get(key));
            }
        }
        Request request = new Request.Builder().url(url).post(requestBody.build()).tag(tag).build();

        getOkHttpClient(activity,isSaveCookie,isUseCookie,saveCookieKey,useCookieKeys).newCall(request).enqueue(new Callback() {
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
            public void onResponse(Call call, final Response response) throws IOException {
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


    public static void httpPost(final Activity context, String url, Map<String, String> postParamMap, String tag, final boolean isSaveCookie, final boolean isUseCookie, final ICallback<String> callback) {
        httpPost(context, url, postParamMap, tag, isSaveCookie, isUseCookie, null, null, callback);
    }

    public static void httpPostGBK(final Activity activity, String url, Map<String, String> postParamMap, String tag, final boolean isSaveCookie, final boolean isUseCookie, String saveCookieKey,String[] useCookieKeys,final ICallback<String> callback) {
        MyFormBody formBody;
        if (postParamMap != null && postParamMap.size() > 0) {
            MyFormBody.Builder builder = new MyFormBody.Builder();
            for (String key : postParamMap.keySet()) {
                builder.add(key, postParamMap.get(key));
            }
            formBody = builder.build();
        } else {
            UIHelper.showLog("httpPost should have post params");
            return;
        }

        Request.Builder builder = new Request.Builder();
        builder.url(url).post(formBody).tag(tag);

        Request request = builder.build();
        getOkHttpClient(activity,isSaveCookie,isUseCookie,saveCookieKey,useCookieKeys).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if(activity!=null&&callback!=null){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String message = e.getMessage();
                            callback.onFail(TextUtils.isEmpty(message)?"服务器异常，请稍后再试":message);
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
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
                        }else{
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFail("服务器异常，请稍后再试");
                                }
                            });
                        }
                    }catch (Exception ex){
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

    public static void httpPostGBK(final Activity context, String url, Map<String, String> postParamMap, String tag, final boolean isSaveCookie, final boolean isUseCookie, final ICallback<String> callback) {
        httpPostGBK(context, url, postParamMap, tag, isSaveCookie, isUseCookie, null, null, callback);
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
