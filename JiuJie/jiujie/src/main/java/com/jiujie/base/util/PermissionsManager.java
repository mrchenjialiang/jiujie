package com.jiujie.base.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.jiujie.base.jk.ICallBackNoParam;
import com.jiujie.base.jk.PermissionRequest;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * author : Created by ChenJiaLiang on 2016/7/29.
 * Email : 576507648@qq.com
 */
public class PermissionsManager {

//    private static ICallBackNoParam mCallback;
    private static Map<Integer,ICallBackNoParam> callBackMap = new HashMap<>();
    private static final int REQUEST_CODE_CALL_PHONE = 1;
    private static final int REQUEST_CODE_WRITE_SDCARD = 2;
    private static final int REQUEST_CODE_READ_SDCARD = 3;

    public static void doWriteSdCard(Activity mActivity,Fragment fragment,String message,@NonNull ICallBackNoParam callback){
        checkAndGetPermission(mActivity,fragment,message,callback,Manifest.permission.WRITE_EXTERNAL_STORAGE,REQUEST_CODE_WRITE_SDCARD);
    }

    public static void doWriteSdCard(Activity mActivity,Fragment fragment,@NonNull ICallBackNoParam callback){
        doWriteSdCard(mActivity, fragment, "为避免多次网络加载，设置缓存，需要读写SD卡权限", callback);
    }

    public static void doReadSdCard(Activity mActivity,Fragment fragment,String message,@NonNull ICallBackNoParam callback){
        checkAndGetPermission(mActivity, fragment, message, callback, Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_CODE_READ_SDCARD);
    }

    public static void doReadSdCard(Activity mActivity,Fragment fragment,@NonNull ICallBackNoParam callback){
        doReadSdCard(mActivity, fragment, "为避免多次网络加载，设置缓存，需要读写SD卡权限", callback);
    }

    public static void doCallPhone(Activity mActivity,Fragment fragment,String message,@NonNull ICallBackNoParam callback){
        checkAndGetPermission(mActivity, fragment, message, callback, Manifest.permission.CALL_PHONE, REQUEST_CODE_CALL_PHONE);
    }

    public static void doCallPhone(Activity mActivity,Fragment fragment,@NonNull ICallBackNoParam callback){
        doCallPhone(mActivity, fragment, "正在请求拨打电话权限", callback);
    }

    public static void checkAndGetPermission(Activity mActivity,Fragment fragment,String message,@NonNull ICallBackNoParam callback,String permission,int requestCode){
        callBackMap.put(requestCode, callback);
        if (Build.VERSION.SDK_INT >= 23&&mActivity.getApplicationInfo().targetSdkVersion>=23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(mActivity, permission);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                //如果没有被授权，则申请权限
                boolean isShow = ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission);
                if(isShow){
                    showRationaleDialog(mActivity,message,new MyPermissionRequest(mActivity,fragment,permission,requestCode));
                }else{
                    //走这里的时候，不显示“不再提示”选项
                    if(fragment==null){
                        ActivityCompat.requestPermissions(mActivity, new String[]{permission}, requestCode);
                    }else{
                        fragment.getParentFragment().requestPermissions(new String[]{permission}, requestCode);
                    }
                }
            }else{
                callBackMap.get(requestCode).onSucceed();
                callBackMap.remove(requestCode);
            }
        } else {
            callBackMap.get(requestCode).onSucceed();
            callBackMap.remove(requestCode);
        }
    }

    private static final class MyPermissionRequest implements PermissionRequest {
        private final WeakReference<Activity> weakTarget;
        private final String permissions;
        private final int requestCode;
        private final Fragment fragment;

        private MyPermissionRequest(Activity activity, Fragment fragment, @NonNull String permissions, final int requestCode) {
            this.weakTarget = new WeakReference<>(activity);
            this.fragment = fragment;
            this.permissions = permissions;
            this.requestCode = requestCode;
        }

        @Override
        public void proceed() {
            Activity activity = weakTarget.get();
            if (activity == null) return;
            if(fragment==null){
                ActivityCompat.requestPermissions(activity, new String[]{permissions}, requestCode);
            }else{
                fragment.getParentFragment().requestPermissions(new String[]{permissions}, requestCode);
            }
        }

        @Override
        public void cancel() {
            Activity activity = weakTarget.get();
            if (activity == null) return;
            UIHelper.showToastShort(activity, "该权限已被拒绝，某些功能可能无法实现");
        }
    }

    private static void showRationaleDialog(Context context,@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(context)
                .setPositiveButton("下一步", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    private static void showRationaleDialog(Context context,@StringRes String message, final PermissionRequest request) {
        new AlertDialog.Builder(context)
                .setPositiveButton("下一步", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(message)
                .show();
    }

    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(callBackMap!=null&&callBackMap.containsKey(requestCode)){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限被授予
                callBackMap.get(requestCode).onSucceed();
                callBackMap.remove(requestCode);
            }else{
                callBackMap.get(requestCode).onFail();
                callBackMap.remove(requestCode);
            }
        }
    }
}
