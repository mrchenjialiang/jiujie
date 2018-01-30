//package com.jiujie.base.http.rx;
//
//import android.content.Context;
//
//import com.jiujie.base.http.okhttp.MyCookie;
//import com.jiujie.base.util.SharePHelper;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import okhttp3.Cookie;
//import okhttp3.HttpUrl;
//
///**
// * 使用rx的Http工具类示例，
// * @author : Created by ChenJiaLiang on 2017/3/14.
// *         Email : 576507648@qq.com
// */
//public class HttpMethods extends BaseHttpMethods<HttpService>{
//
//    private final Context activity;
//    private Cookie SESSION_COOKIE;
//    private static HttpMethods httpMethods;
//
//    protected HttpMethods(Context activity) {
//        this.activity = activity.getApplicationContext();
//    }
//
//    public static HttpMethods instance(Context activity){
//        if(httpMethods ==null){
//            httpMethods = new HttpMethods(activity);
//        }
//        return httpMethods;
//    }
//
//    public void logoff() {
//        SESSION_COOKIE = null;
//        SharePHelper.instance(activity).remove("LoginCookie");
//    }
//
//    @Override
//    protected Class<HttpService> getServiceClass() {
//        return HttpService.class;
//    }
//
//    @Override
//    protected String getBaseUrl() {
//        return "http://10086.php";
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
//}
