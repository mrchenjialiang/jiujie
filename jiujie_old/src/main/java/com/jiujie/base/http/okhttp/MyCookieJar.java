package com.jiujie.base.http.okhttp;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.jiujie.base.util.SharePHelper;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * author : Created by ChenJiaLiang on 2016/6/24.
 * Email : 576507648@qq.com
 */
public class MyCookieJar implements CookieJar {

    private final Context context;
    private final boolean isSaveCookie;
    private final boolean isUseCookie;
    private final String saveCookieKey;
    private final String[] useCookieKeys;
    private final String shareFileName = "cookie";
    private SharePHelper sp;
    private boolean isShowLog = false;

    public MyCookieJar(Context context,boolean isSaveCookie,boolean isUseCookie){
        sp = SharePHelper.instance(context,shareFileName);
        this.context = context;
        this.isSaveCookie = isSaveCookie;
        this.isUseCookie = isUseCookie;
        this.saveCookieKey = "cookie";
        this.useCookieKeys = new String[]{"cookie"};
    }

    public MyCookieJar(Context context,boolean isSaveCookie,boolean isUseCookie,String saveCookieKey,String[] useCookieKeys){
        sp = SharePHelper.instance(context,shareFileName);
        this.context = context;
        this.isSaveCookie = isSaveCookie;
        this.isUseCookie = isUseCookie;
        if(TextUtils.isEmpty(saveCookieKey)){
            this.saveCookieKey = "cookie";
        }else{
            this.saveCookieKey = saveCookieKey;
        }
        if(useCookieKeys==null||useCookieKeys.length==0){
            this.useCookieKeys = new String[]{"cookie"};
        }else{
            this.useCookieKeys = useCookieKeys;
        }
    }
    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (isSaveCookie) {
            List<MyCookie> list = new ArrayList<>();
            for (int i = 0;i<cookies.size();i++){
                Cookie cookie = cookies.get(i);
                MyCookie myCookie = new MyCookie(cookie.name(), cookie.value(), cookie.expiresAt(), cookie.domain(), cookie.path(), cookie.secure(), cookie.httpOnly(), cookie.hostOnly(), cookie.persistent());
                list.add(myCookie);
                if(isShowLog)Log.e("isSaveCookie Cookie",myCookie.toString());
            }
            MyCookieList myCookieList = new MyCookieList(list);
            sp.saveObject(saveCookieKey,myCookieList);
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = new ArrayList<>();
        if (isUseCookie) {
            for(String key:useCookieKeys){
                MyCookieList myCookieList = sp.readObject(key);
                if(myCookieList!=null){
                    for (MyCookie c:myCookieList.getList()){
                        if(isShowLog) Log.e("isUseCookie Cookie",c.toString());
                        Cookie.Builder b = new Cookie.Builder();
                        b.name(c.name())
                                .value(c.value())
                                .path(c.path());
                        if(c.secure()){
                            b.secure();
                        }
                        if(c.httpOnly){
                            b.httpOnly();
                        }
                        if(c.hostOnly()){
                            b.hostOnlyDomain(c.domain());
                        }else{
                            b.domain(c.domain());
                        }
                        if(c.persistent()){
                            b.expiresAt(c.expiresAt());
                        }
                        cookies.add(b.build());
                    }
                }
            }
        }
        return cookies;
    }

    public void clearCookie() {
        sp.clear();
//        sp.getSp().edit().remove("cookie").apply();
    }
}
