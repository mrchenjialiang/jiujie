package com.jiujie.base.jk;

/**
 * @author : Created by ChenJiaLiang on 2017/2/15.
 *         Email : 576507648@qq.com
 */
public interface ListLoadingCallBack<T>{

    void onLoadSuccess(T t, int type);

    void onLoadError(String error, int type);
}
