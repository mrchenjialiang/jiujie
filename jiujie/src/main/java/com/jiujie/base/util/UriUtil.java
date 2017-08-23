package com.jiujie.base.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

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
    public static Uri getUri(Context context,Intent intent,File file){

        Uri uri;
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            BuildConfig.APPLICATION_ID==context.getPackageName()
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            uri = Uri.fromFile(file);
        }
        return uri;
    }
}
