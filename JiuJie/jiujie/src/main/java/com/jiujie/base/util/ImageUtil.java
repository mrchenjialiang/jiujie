package com.jiujie.base.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.jiujie.base.jk.ICallbackSimple;
import com.jiujie.base.model.Image;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
                    saveImageToLocalAsJpg(imageUrl, getImageLocalDic(activity), name, bitmap, sp);
                } else {
                    saveImageToLocalAsJpg(imageUrl, getImageLocalDic(activity), getImagePngName(), bitmap, sp);
                }
            } else {
                saveImageToLocalAsJpg(imageUrl, getImageLocalDic(activity), getImagePngName(), bitmap, sp);
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
    public String getImagePngName() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        return sdf.format(new Date(currentTimeMillis)) + ".png";
    }

    /**
     * 当前日期 年_月_日_时_分_秒.png
     */
    public String getImageJpgName() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        return sdf.format(new Date(currentTimeMillis)) + ".jpg";
    }

    /**
     * @param fileName 要保存的文件文件名,包括格式
     * @param bitmap   要保存的图片
     */
    public void saveImageToLocalAsJpg(String imageUrl, String fileDic, String fileName,
                                      Bitmap bitmap, SharedPreferences sp) {
        saveImageToLocalAsJpg(fileDic, fileName, bitmap);
        if (sp != null) sp.edit().putString(imageUrl, fileDic + fileName).apply();
    }

    /**
     * @param fileName 要保存的文件文件名,包括格式
     * @param bitmap   要保存的图片
     */
    public void saveImageToLocalAsJpg(String fileDic, String fileName, Bitmap bitmap) {
        if (!UIHelper.isSdCardExist()) {
            return;
        }
        try {
            File file = FileUtil.createFile(fileDic, fileName);
            if(file!=null){
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(CompressFormat.JPEG, 100, fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param fileName 要保存的文件文件名,包括格式
     * @param bitmap   要保存的图片
     */
    public void saveImageToLocalAsPng(String fileDic, String fileName, Bitmap bitmap) {
        if (!UIHelper.isSdCardExist()) {
            return;
        }
        try {
            File file = FileUtil.createFile(fileDic, fileName);
            if(file!=null){
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(CompressFormat.PNG, 100, fos);
            }
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

    public Bitmap cropImage(Bitmap bm,int resultWidth,int resultHeight,int fromLeft,int fromTop,int fromRight,int fromBottom){
        Rect r = new Rect(fromLeft,fromTop,fromRight,fromBottom);
        if(resultWidth<=0||resultHeight<=0){
            return bm;
        }
        Bitmap croppedImage = Bitmap.createBitmap(resultWidth, resultHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(croppedImage);
        Rect dstRect = new Rect(0, 0, resultWidth, resultHeight);
        canvas.drawBitmap(bm, r, dstRect, null);
        return croppedImage;
    }

    public Bitmap scaleBitmap(Bitmap bm,int newWidth,int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        if(width==newWidth&&height==newHeight){
            return bm;
        }
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    public void getAllImageFromLocal(final FragmentActivity activity, final ICallbackSimple<Map<String,Image>> callback){
        final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media._ID };

        activity.getSupportLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(activity,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[4]+">0 AND "+IMAGE_PROJECTION[3]+"=? OR "+IMAGE_PROJECTION[3]+"=? ",
                        new String[]{"image/jpeg", "image/png"}, IMAGE_PROJECTION[2] + " DESC");
            }
            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                Map<String,Image> imageMap = new LinkedHashMap<>();
                if (data != null) {
                    if (data.getCount() > 0) {
                        data.moveToFirst();

                        Image allImageImage = new Image();
                        String allImagePath = Environment.getExternalStorageDirectory()+"";
                        allImageImage.setPath(allImagePath);
                        allImageImage.setName("全部照片");
                        allImageImage.setDir(true);
                        List<Image> allImageImageList = new ArrayList<>();
                        allImageImage.setImageList(allImageImageList);
                        imageMap.put(allImagePath,allImageImage);

                        do{
                            String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                            String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                            if(!fileExist(path)){continue;}
                            File dirFile = new File(path).getParentFile();
                            if(dirFile==null||!dirFile.exists())continue;
                            String dirPath = dirFile.getAbsolutePath();
                            if(!imageMap.containsKey(dirPath)){
                                Image image = new Image();
                                image.setPath(dirPath);
                                image.setName(dirFile.getName());
                                image.setDir(true);
                                imageMap.put(dirPath,image);
                            }
                            Image imageDir = imageMap.get(dirPath);
                            List<Image> imageList = imageDir.getImageList();
                            if(imageList==null){
                                imageList = new ArrayList<>();
                            }
                            Image image = new Image();
                            image.setPath(path);
                            image.setName(name);
                            imageList.add(image);
                            allImageImageList.add(image);
                            imageDir.setImageList(imageList);
                        }while(data.moveToNext());
                    }
                }
                callback.onSucceed(imageMap);
            }

            private boolean fileExist(String path) {
                return !TextUtils.isEmpty(path) && new File(path).exists();
            }
            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
            }
        });
    }

//    /**
//     * min sdk = 17
//     * 模糊图片的具体方法
//     * @param context 上下文对象
//     * @param image   需要模糊的图片
//     * @return 模糊处理后的图片
//     */
//    public static Bitmap blurBitmap(Context context, Bitmap image,float blurRadius) {
//        //图片缩放比例
//        float BITMAP_SCALE = 0.4f;
//
//        // 计算图片缩小后的长宽
//        int width = Math.round(image.getWidth() * BITMAP_SCALE);
//        int height = Math.round(image.getHeight() * BITMAP_SCALE);
//
//        // 将缩小后的图片做为预渲染的图片
//        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
//        // 创建一张渲染后的输出图片
//        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
//
//        // 创建RenderScript内核对象
//        RenderScript rs = RenderScript.create(context);
//        // 创建一个模糊效果的RenderScript的工具对象
//        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
//
//        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
//        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
//        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
//        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
//
//        // 设置渲染的模糊程度, 25f是最大模糊度
//        blurScript.setRadius(blurRadius);
//        // 设置blurScript对象的输入内存
//        blurScript.setInput(tmpIn);
//        // 将输出数据保存到输出内存中
//        blurScript.forEach(tmpOut);
//
//        // 将数据填充到Allocation中
//        tmpOut.copyTo(outputBitmap);
//
//        return outputBitmap;
//    }

}
