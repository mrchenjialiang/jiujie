package com.jiujie.base.http.okhttp;

import java.io.Serializable;

/**
 * author : Created by ChenJiaLiang on 2016/6/24.
 * Email : 576507648@qq.com
 */
public class MyCookie implements Serializable{
    String name;
    String value;
    long expiresAt;
    String domain;
    String path;
    boolean secure;
    boolean httpOnly;
    boolean hostOnly;
    boolean persistent;
    MyCookie(String name, String value, long expiresAt, String domain, String path,
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
}
