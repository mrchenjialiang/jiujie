package com.jiujie.base.http.okhttp;

import java.io.Serializable;
import java.util.List;

/**
 * author : Created by ChenJiaLiang on 2016/6/24.
 * Email : 576507648@qq.com
 */
public class MyCookieList implements Serializable{
    List<MyCookie> list;
    MyCookieList( List<MyCookie> list){
        this.list = list;
    }

    public List<MyCookie> getList() {
        return list;
    }
}
