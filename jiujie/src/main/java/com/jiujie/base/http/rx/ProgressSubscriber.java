package com.jiujie.base.http.rx;

import android.app.Activity;
import android.app.Dialog;

import com.google.gson.Gson;
import com.jiujie.base.APP;
import com.jiujie.base.jk.ICallback;
import com.jiujie.base.util.UIHelper;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 */
public abstract class ProgressSubscriber<T> extends Subscriber<T> implements ICallback<T> {

    private boolean isShowDialog = true;
    private String message;
    private Activity activity;
    private Dialog waitingDialog;

    public ProgressSubscriber(Activity activity) {
        this.activity = activity;
    }

    public ProgressSubscriber(Activity activity,boolean isShowDialog) {
        this.activity = activity;
        this.isShowDialog = isShowDialog;
    }

    public ProgressSubscriber( Activity activity, String message) {
        this.activity = activity;
        this.message = message;
    }

    @Override
    public void onStart() {
        if(isShowDialog)showDialog();
    }

    protected void showDialog() {
        if(activity!=null&&!activity.isFinishing()){
            if(waitingDialog==null){
                waitingDialog = UIHelper.getWaitingDialog(activity);
                waitingDialog.setCanceledOnTouchOutside(false);
            }
            waitingDialog.show();
        }
    }

    protected void hideDialog() {
        if(activity!=null&&!activity.isFinishing()&&waitingDialog!=null&&waitingDialog.isShowing())waitingDialog.dismiss();
    }

    @Override
    public void onCompleted() {
        if(isShowDialog)hideDialog();
    }

    @Override
    public void onError(Throwable e) {
        if(isShowDialog)hideDialog();
        String error;
        if (e instanceof SocketTimeoutException) {
            error = "连接超时，请检查您的网络状态";
        } else if (e instanceof ConnectException) {
            error = "网络中断，请检查您的网络状态";
        } else {
            error = "服务器异常，请稍候再试";
        }
        UIHelper.showLog("httpResult error:" + error);
        if(activity==null||activity.isFinishing()){
            return;
        }
        onFail(error);
    }

    @Override
    public void onNext(T t) {
        if(APP.isDeBug){
            String toJson = new Gson().toJson(t);
            UIHelper.showLog("httpResult data:" + toJson);
//            UIHelper.showLogInFile(toJson);
        }
        if(activity==null||activity.isFinishing()){
            return;
        }
        onSucceed(t);
    }
}