package com.jiujie.base.jk;

/**
 * @author : Created by ChenJiaLiang on 2017/1/19.
 *         Email : 576507648@qq.com
 */
public interface LoadingCallBack<T> {
//    void onLoadStart();
    void onLoadSuccess(T data);
    void onLoadError(String error);
//    void onLoadCompleted();
}
