package com.jiujie.base.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.jiujie.base.APP;

import java.io.File;

/**
 * Created by ChenJiaLiang on 2017/6/2.
 * Email:576507648@qq.com
 */
public class UriUtil {

    /**
     * 要将Uri提供给外部使用时，Android7.0必须使用FileProvider来转换，注意FLAG_GRANT_READ_URI_PERMISSION
     * 比如使用系统裁剪，比如安装APP
     * 注：拍照不要用这个，不然会回调失败
     */
    public static Uri getUri(Context context, Intent intent, File file) {
        return getUri(context, intent, file, false);
    }

    public static Uri getUri(Context context, Intent intent, File file, boolean isUseForCamera) {
        if(isUseForCamera){
//            targetSdkVersion<=23 or 手机Android版本< 7.0
            if(context.getApplicationInfo().targetSdkVersion <= 23 || Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
                return Uri.fromFile(file);
            }
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (intent != null) intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile(context, APP.getProviderAuthorities(), file);
        } else {
            if (intent != null) intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            uri = Uri.fromFile(file);
        }
        return uri;
    }
}
