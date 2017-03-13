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

    private boolean isShowDialog = true;
    public LoadingCallBack<T> mLoadingCallBack;
    private String message;
    private Activity activity;
    private Dialog waitingDialog;

    public ProgressSubscriber(Activity activity,LoadingCallBack<T> mLoadingCallBack) {
        if(mLoadingCallBack==null) throw new NullPointerException("LoadingCallBack should not be null");
        this.mLoadingCallBack = mLoadingCallBack;
        this.activity = activity;
        waitingDialog = UIHelper.getWaitingDialog(activity);
        waitingDialog.setCanceledOnTouchOutside(false);
    }

    public ProgressSubscriber(Activity activity,boolean isShowDialog,LoadingCallBack<T> mLoadingCallBack) {
        if(mLoadingCallBack==null) throw new NullPointerException("LoadingCallBack should not be null");
        this.mLoadingCallBack = mLoadingCallBack;
        this.activity = activity;
        this.isShowDialog = isShowDialog;
        if(isShowDialog){
            waitingDialog = UIHelper.getWaitingDialog(activity);
            waitingDialog.setCanceledOnTouchOutside(false);
        }
    }

    public ProgressSubscriber( Activity activity, String message,LoadingCallBack<T> mLoadingCallBack) {
        if(mLoadingCallBack==null) throw new NullPointerException("LoadingCallBack should not be null");
        this.mLoadingCallBack = mLoadingCallBack;
        this.activity = activity;
        this.message = message;
        waitingDialog = UIHelper.getWaitingDialog(activity);
        waitingDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onStart() {
        if(isShowDialog&&activity!=null&&!activity.isFinishing())waitingDialog.show();
    }

    @Override
    public void onCompleted() {
        if(isShowDialog&&activity!=null&&!activity.isFinishing()&&waitingDialog.isShowing())waitingDialog.dismiss();
    }

    @Override
    public void onError(Throwable e) {
        if(isShowDialog&&activity!=null&&!activity.isFinishing()&&waitingDialog.isShowing())waitingDialog.dismiss();
        String error;
        if (e instanceof SocketTimeoutException) {
            error = "连接超时，请检查您的网络状态";
        } else if (e instanceof ConnectException) {
            error = "网络中断，请检查您的网络状态";
        } else {
            error = "服务器异常，请稍候再试";
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