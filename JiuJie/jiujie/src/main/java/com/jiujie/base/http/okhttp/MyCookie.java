package com.jiujie.base.http.okhttp;

import java.io.Serializable;

import okhttp3.Cookie;

/**
 * author : Created by ChenJiaLiang on 2016/6/24.
 * Email : 576507648@qq.com
 */
public class MyCookie implements Serializable{
    private static final long serialVersionUID = -6977481843253829729L;
    String name;
    String value;
    long expiresAt;
    String domain;
    String path;
    boolean secure;
    boolean httpOnly;
    boolean hostOnly;
    boolean persistent;
    public MyCookie(String name, String value, long expiresAt, String domain, String path,
            boolean secure, boolean httpOnly, boolean hostOnly, boolean persistent){
        this.name = name;
        this.value = value;
        this.expiresAt = expiresAt;
        this.domain = domain;
        this.path = path;
        this.secure = secure;
        this.httpOnly = httpOnly;
        this.hostOnly = hostOnly;
        this.persistent = persistent;
    }

    @Override
    public String toString() {
        return "["+"name:"+name+",value:"+value+",expiresAt:"+expiresAt+
                ",domain:"+domain+",path:"+path+",secure:"+secure
                +",httpOnly:"+httpOnly+",hostOnly:"+hostOnly
                +",persistent:"+persistent+"]";
    }

    public String name() {
        return name;
    }

    public String value() {
        return value;
    }

    public boolean persistent() {
        return persistent;
    }

    public long expiresAt() {
        return expiresAt;
    }

    public boolean hostOnly() {
        return hostOnly;
    }

    public String domain() {
        return domain;
    }

    public String path() {
        return path;
    }

    public boolean httpOnly() {
        return httpOnly;
    }

    public boolean secure() {
        return secure;
    }
    public Cookie toCookie(){
        Cookie.Builder b = new Cookie.Builder();
        b.name(name())
                .value(value())
                .path(path());
        if(secure()){
            b.secure();
        }
        if(httpOnly){
            b.httpOnly();
        }
        if(hostOnly()){
            b.hostOnlyDomain(domain());
        }else{
            b.domain(domain());
        }
        if(persistent()){
            b.expiresAt(expiresAt());
        }
        return b.build();
    }
}
