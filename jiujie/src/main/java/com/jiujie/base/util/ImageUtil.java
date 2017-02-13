package com.jiujie.base.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@SuppressLint("SimpleDateFormat")
public class ImageUtil {
    private static ImageUtil imageUtil;

    private ImageUtil() {
    }

    public static ImageUtil instance() {
        if (imageUtil == null)
            imageUtil = new ImageUtil();
        return imageUtil;
    }

    public Drawable getImageFromNet(Activity activity, String imageUrl) {
        return getImageDrawableFromNet(activity, imageUrl);
    }

    public Bitmap getImageFromNetUseOkHttp(String imageUrl){
        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder().url(imageUrl).build();
            Response response = client.newCall(request).execute();
            InputStream is = response.body().byteStream();
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Drawable getImageFromNetUseOldHttp(String imageUrl){
        URL url;
        Drawable drawable = null;
        try {
            url = new URL(imageUrl);
            drawable = Drawable.createFromStream(url.openStream(), ""); // 获取网路图片
        }catch (Exception e){
            e.printStackTrace();
        }
        return drawable;
    }
    public Drawable getImageDrawableFromNet(Activity activity, String imageUrl){
        Bitmap bitmap = getImageBitmapFromNet(activity, imageUrl);
        return bitmap2Drawable(bitmap,activity);
    }

    public Bitmap getImageBitmapFromNet(Activity activity, String imageUrl){
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(activity)
                    .load(imageUrl)
                    .asBitmap()
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public Drawable getImageFromNetUseOkHttp(Activity activity, String imageUrl,int reqWidth,int reqHeight) {
        if(TextUtils.isEmpty(imageUrl)||!imageUrl.startsWith("http")){
            return null;
        }

        SharedPreferences sp = activity.getSharedPreferences("local_image", Activity.MODE_PRIVATE);
        if (sp.contains(imageUrl)) {
            String filePath = sp.getString(imageUrl, "");
            Bitmap bitmap = getImageBitmapFromLocal(filePath,reqWidth,reqHeight);
            if (bitmap != null) {
                return bitmap2Drawable(bitmap, activity);
            }
        }

        Drawable drawable = null;
        Bitmap bitmap = getImageFromNetUseOkHttp(imageUrl);
        if(bitmap!=null){
            drawable = bitmap2Drawable(bitmap,activity);
            String[] split = imageUrl.split("/");
            if (split.length > 0) {
                String name = split[split.length - 1];
                if (name.contains(".")) {
                    saveImageToLocal(imageUrl, getImageLocalDic(activity), name, bitmap, sp);
                } else {
                    saveImageToLocal(imageUrl, getImageLocalDic(activity), getImageName(), bitmap, sp);
                }
            } else {
                saveImageToLocal(imageUrl, getImageLocalDic(activity), getImageName(), bitmap, sp);
            }
        }

        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
        } else {
            return null;
        }
        return drawable;
    }

    public String getCacheSDDic(Context context) {
        String packageName = context.getPackageName();
        String[] split = packageName.split("\\.");
        String dic = Environment.getExternalStorageDirectory() + "/";
        if (split.length > 1) {
            for (String str : split) {
                if (!str.equals("com")) {
                    dic += (str + "/");
                }
            }
        } else {
            dic += "jiujie/";
        }
        return dic;
    }

    public String getImageLocalDic(Context context) {
        return getCacheSDDic(context) + "res/image/";
    }

    /**
     * 当图片较大时，需设置需求宽高来压缩，避免OOM
     */
    public Bitmap getImageBitmapFromLocal(String filePath,int reqWidth,int reqHeight) {
        Bitmap bitmap = null;
        BufferedInputStream bs;
        FileInputStream fis;
        try {
            fis = new FileInputStream(filePath);
            bs = new BufferedInputStream(fis);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//为了读取图片属性而不将图片读取到内存中
            int height = options.outHeight;
            int width = options.outWidth;
            if((reqWidth>0||reqHeight>0)&&(height>reqHeight||width>reqWidth)){
                bitmap = BitmapFactory.decodeStream(bs, null, options);

                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);//图片缩放比例
            }
            options.inJustDecodeBounds = false;


            bitmap = BitmapFactory.decodeStream(bs, null, options);
            bs.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ImageUtil", "getImageBitmapFromLocal error:" + e.getMessage());
        }
        return bitmap;
    }
    /**
     * 当图片较大时，需设置需求宽高来压缩，避免OOM
     */
    public Bitmap getImageDrawableFromLocal(String filePath,int reqWidth,int reqHeight) {
        Bitmap bitmap = null;
        BufferedInputStream bs;
        FileInputStream fis;
        try {
            fis = new FileInputStream(filePath);
            bs = new BufferedInputStream(fis);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//为了读取图片属性而不将图片读取到内存中
            int height = options.outHeight;
            int width = options.outWidth;
            if((reqWidth>0||reqHeight>0)&&(height>reqHeight||width>reqWidth)){
                bitmap = BitmapFactory.decodeStream(bs, null, options);

                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);//图片缩放比例
            }
            options.inJustDecodeBounds = false;


            bitmap = BitmapFactory.decodeStream(bs, null, options);
            bs.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ImageUtil", "getImageBitmapFromLocal error:" + e.getMessage());
        }
        return bitmap;
    }

    private int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;

        int initSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                initSize = Math.round((float) height / (float) reqHeight);
            } else {
                initSize = Math.round((float) width / (float) reqWidth);
            }
        }

    /*
     * the function rounds up the sample size to a power of 2 or multiple of 8 because
     * BitmapFactory only honors sample size this way. For example, BitmapFactory
     * down samples an image by 2 even though the request is 3.
     */
        int roundedSize;
        if (initSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    /**
     * 当前日期 年_月_日_时_分_秒.png
     */
    public String getImageName() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        return sdf.format(new Date(currentTimeMillis)) + ".png";
    }

    /**
     * @param fileName 要保存的文件文件名,包括格式
     * @param bitmap   要保存的图片
     */
    public void saveImageToLocal(String imageUrl, String fileDic, String fileName,
                                 Bitmap bitmap, SharedPreferences sp) {
        saveImageToLocal(fileDic, fileName, bitmap);
        if (sp != null) sp.edit().putString(imageUrl, fileDic + fileName).apply();
    }

    /**
     * @param fileName 要保存的文件文件名,包括格式
     * @param bitmap   要保存的图片
     */
    public void saveImageToLocal(String fileDic, String fileName, Bitmap bitmap) {
        if (!Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            return;
        }
        File dir = new File(fileDic);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                return;
            }
        }
        File file = new File(fileDic, fileName);
        try {
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    return;
                }
            }
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(CompressFormat.JPEG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    /**
     * Bitmap to Drawable
     */
    public Drawable bitmap2Drawable(Bitmap bitmap, Context mContext) {
        if(bitmap==null) return null;
        return new BitmapDrawable(mContext.getResources(), bitmap);
    }

    public Bitmap stringToBitmap(String string) {
        //将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public String bitmapToString(Bitmap bitmap) {
        //将Bitmap转换成字符串
        String string;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }

    public Bitmap getImageFromRes(Activity activity,int imageResId,int reqWidth,int reqHeight) {
        Bitmap bitmap = null;
        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//为了读取图片属性而不将图片读取到内存中
            int height = options.outHeight;
            int width = options.outWidth;
            if((reqWidth>0||reqHeight>0)&&(height>reqHeight||width>reqWidth)){
                bitmap = BitmapFactory.decodeResource(activity.getResources(),imageResId,options);

                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);//图片缩放比例
            }
            options.inJustDecodeBounds = false;


            bitmap = BitmapFactory.decodeResource(activity.getResources(),imageResId,options);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ImageUtil", "getImageFromRes error:" + e.getMessage());
        }
        return bitmap;
    }
}
