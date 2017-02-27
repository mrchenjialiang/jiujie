package com.jiujie.base.http.rx;

import android.app.Activity;
import android.app.Dialog;

import com.google.gson.Gson;
import com.jiujie.base.APP;
import com.jiujie.base.jk.ListLoadingCallBack;
import com.jiujie.base.util.UIHelper;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

public class ListSubscriber<T> extends Subscriber<T>{

    protected int type;
    public ListLoadingCallBack mLoadingCallBack;
    protected Activity activity;
    private Dialog waitingDialog;

    public ListSubscriber(Activity context, int type, ListLoadingCallBack<T> mLoadingCallBack) {
        if(mLoadingCallBack==null) throw new NullPointerException("LoadingCallBack should not be null");
        this.mLoadingCallBack = mLoadingCallBack;
        this.activity = context;
        this.type = type;
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
        mLoadingCallBack.onLoadError(error,type);
        UIHelper.showLog("httpResult error:" + error);
    }

    @Override
    public void onNext(T t) {
        if(APP.isDeBug){
            String toJson = new Gson().toJson(t);
            UIHelper.showLog("httpResult data:" + toJson);
            UIHelper.showLogInFile(toJson);
        }
        mLoadingCallBack.onLoadSuccess(t,type);
    }
}