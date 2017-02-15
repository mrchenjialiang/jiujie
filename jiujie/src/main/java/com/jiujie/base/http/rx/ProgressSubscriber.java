package com.jiujie.base.http.rx;

import android.app.Activity;
import android.app.Dialog;

import com.google.gson.Gson;
import com.jiujie.base.APP;
import com.jiujie.base.jk.LoadingCallBack;
import com.jiujie.base.util.UIHelper;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 */
public class ProgressSubscriber<T> extends Subscriber<T>{

    public LoadingCallBack mLoadingCallBack;
    private String message;
    private Activity context;
    private Dialog waitingDialog;

    public ProgressSubscriber(Activity context,LoadingCallBack<T> mLoadingCallBack) {
        if(mLoadingCallBack==null) throw new NullPointerException("LoadingCallBack should not be null");
        this.mLoadingCallBack = mLoadingCallBack;
        this.context = context;
        waitingDialog = UIHelper.getWaitingDialog(context);
        waitingDialog.setCanceledOnTouchOutside(false);
    }

    public ProgressSubscriber( Activity context, String message,LoadingCallBack<T> mLoadingCallBack) {
        if(mLoadingCallBack==null) throw new NullPointerException("LoadingCallBack should not be null");
        this.mLoadingCallBack = mLoadingCallBack;
        this.context = context;
        this.message = message;
        waitingDialog = UIHelper.getWaitingDialog(context);
        waitingDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onStart() {
        waitingDialog.show();
//        mLoadingCallBack.onLoadStart();
    }

    @Override
    public void onCompleted() {
        waitingDialog.dismiss();
//        mLoadingCallBack.onLoadCompleted();
    }

    @Override
    public void onError(Throwable e) {
        waitingDialog.dismiss();
        String error;
        if (e instanceof SocketTimeoutException) {
            error = "连接超时，请检查您的网络状态";
        } else if (e instanceof ConnectException) {
            error = "网络中断，请检查您的网络状态";
        } else {
            error = e.getMessage();
        }
        mLoadingCallBack.onLoadError(error);
        UIHelper.showLog("httpResult error:" + error);
    }

    @Override
    public void onNext(T t) {
        if(APP.isDeBug){
            String toJson = new Gson().toJson(t);
            UIHelper.showLog("httpResult data:" + toJson);
            UIHelper.showLogInFile(toJson);
        }
        mLoadingCallBack.onLoadSuccess(t);
    }
}