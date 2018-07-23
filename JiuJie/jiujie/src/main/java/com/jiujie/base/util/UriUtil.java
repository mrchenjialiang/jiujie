package com.jiujie.base.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
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
        if (isUseForCamera) {
//            targetSdkVersion<=23 or 手机Android版本< 7.0
            if (context.getApplicationInfo().targetSdkVersion <= 23 || Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
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

    private String getPath(Context context, Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

//    public static File uriToFile(Uri uri, Context context) {
//        String path = null;
//        if ("file".equals(uri.getScheme())) {
//            path = uri.getEncodedPath();
//            if (path != null) {
//                path = Uri.decode(path);
//                ContentResolver cr = context.getContentResolver();
//                String selection = "(" + MediaStore.Images.ImageColumns.DATA + "=" + "'" + path + "'" + ")";
//                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA}, selection, null, null);
//                if (cur == null) return null;
//                int index = 0;
//                int dataIdx;
//                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
//                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
//                    index = cur.getInt(index);
//                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//                    path = cur.getString(dataIdx);
//                }
//                cur.close();
//                if (index != 0) {
//                    Uri u = Uri.parse("content://media/external/images/media/" + index);
//                    System.out.println("temp uri is :" + u);
//                }
//            }
//            if (path != null) {
//                return new File(path);
//            }
//        } else if ("content".equals(uri.getScheme())) {
//            // 4.2.2以后
//            String[] projection = {MediaStore.Images.Media.DATA};
//            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
//            if (cursor == null) return null;
//            if (cursor.moveToFirst()) {
//                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                path = cursor.getString(columnIndex);
//            }
//            cursor.close();
//            if (path != null) {
//                return new File(path);
//            }
//        }
//        return null;
//    }
//
//    public static Uri getImageContentUri(Context context, File imageFile) {
//        String filePath = imageFile.getAbsolutePath();
//        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
//                new String[]{filePath}, null);
//        if (cursor != null && cursor.moveToFirst()) {
//            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
//            Uri baseUri = Uri.parse("content://media/external/images/media");
//            cursor.close();
//            return Uri.withAppendedPath(baseUri, "" + id);
//        } else {
//            if (imageFile.exists()) {
//                ContentValues values = new ContentValues();
//                values.put(MediaStore.Images.Media.DATA, filePath);
//                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            } else {
//                return null;
//            }
//        }
//    }
}
