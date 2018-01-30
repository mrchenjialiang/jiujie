//package com.jiujie.base.http.okhttp;
//
//import android.content.Context;
//
//import com.jiujie.base.util.SharePHelper;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import okhttp3.Cookie;
//import okhttp3.HttpUrl;
//
///**
// * 直接使用OkHttp3的示例工具类
// * @author : Created by ChenJiaLiang on 2017/3/14.
// *         Email : 576507648@qq.com
// */
//public class HttpUtil extends MyOkHttpUtil{
//
//    private final Context activity;
//    private Cookie SESSION_COOKIE;
//    private static HttpUtil httpUtil;
//
//    private HttpUtil(Context activity) {
//        this.activity = activity;
//    }
//
//    public static HttpUtil instance(Context activity){
//        if(httpUtil==null){
//            httpUtil = new HttpUtil(activity);
//        }
//        return httpUtil;
//    }
//
//    @Override
//    protected int getConnectTimeOutSecond() {
//        return 20;
//    }
//
//    @Override
//    protected int getReadTimeOutSecond() {
//        return 10;
//    }
//
//    @Override
//    protected void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
//        if (list != null && list.size() > 0) {
//            for (int i = 0; i < list.size(); i++) {
//                Cookie cookie = list.get(i);
//                if (cookie.name().equals("SESSION")) {
//                    String path = httpUrl.url().getPath();
//                    if (path.contains("/api/app-login")) {
//                        SESSION_COOKIE = cookie;
//                        MyCookie myCookie = new MyCookie(cookie.name(), cookie.value(), cookie.expiresAt(), cookie.domain(), cookie.path(), cookie.secure(), cookie.httpOnly(), cookie.hostOnly(), cookie.persistent());
//                        SharePHelper.instance(activity).saveObject("LoginCookie",myCookie);
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    protected List<Cookie> loadForRequest(HttpUrl httpUrl) {
//        String path = httpUrl.url().getPath();
//        if(path.contains("/api/app-login")){
//            return new ArrayList<>();
//        }
//        List<Cookie> cookies = new ArrayList<>();
//        if(SESSION_COOKIE==null){
//            MyCookie myCookie = SharePHelper.instance(activity).readObject("LoginCookie");
//            if(myCookie!=null){
//                SESSION_COOKIE = myCookie.toCookie();
//            }
//        }
//        if (SESSION_COOKIE != null) {
//            cookies.add(SESSION_COOKIE);
//        }
//        return cookies;
//    }
//
//    public void logoff() {
//        SESSION_COOKIE = null;
//        SharePHelper.instance(activity).remove("LoginCookie");
//    }
//}
