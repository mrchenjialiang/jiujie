package com.jiujie.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by ChenJiaLiang on 2018/4/13.
 * Email:576507648@qq.com
 */

public interface ImageLoader {
    void setSimpleImage(Object contextObject, String url, ImageView imageView);

    void setSimpleImage(Object contextObject, String url, ImageView imageView, int defaultId);

    void setDefaultImage(Object contextObject, String url, ImageView imageView);

    void setDefaultImage(Object contextObject, String url, ImageView imageView, boolean isCenterCrop);

    void setDefaultImage(Object contextObject, String url, ImageView imageView, int width, int height);

    void setDefaultImage(Object contextObject, String url, ImageView imageView, int defaultId);

    void setDefaultImage(Object contextObject, String url, ImageView imageView, int defaultId, boolean isCenterCrop);

    void setDefaultImage(Object contextObject, String url, ImageView imageView, int defaultId, int width, int height);

    void setDefaultImage(Object contextObject, String url, ImageView imageView, int defaultId, OnLoadImageListener<Drawable> listener);

    void setDefaultImage(Object contextObject, String url, ImageView imageView, int defaultId, boolean isCenterCrop, OnLoadImageListener<Drawable> listener);

    void setDefaultImage(Object contextObject, String url, ImageView imageView, int defaultId, boolean isCenterCrop, boolean isShowAnim, OnLoadImageListener<Drawable> listener);

    void setDefaultNoCacheImage(Object contextObject, String url, ImageView imageView);

    void setDefaultNoCacheImage(Object contextObject, String url, ImageView imageView, int defaultId);

    void setCircleImage(Object contextObject, String url, ImageView imageView);

    void setCircleImage(Object contextObject, String url, ImageView imageView, int width, int height);

    void setCircleImage(Object contextObject, String url, ImageView imageView, int defaultId);

    void setCircleImage(Object contextObject, String url, ImageView imageView, int defaultId, int width, int height);

    void setCircleNoCacheImage(Object contextObject, String url, ImageView imageView);

    void setCircleNoCacheImage(Object contextObject, String url, ImageView imageView, int defaultId);

    void setConnerImage(Object contextObject, String url, ImageView imageView, int conner);

    void setConnerImage(Object contextObject, String url, ImageView imageView, int conner, int width, int height);

    void setConnerImage(Object contextObject, String url, ImageView imageView, int conner, int defaultId);

    void setConnerImage(Object contextObject, String url, ImageView imageView, int conner, int defaultId, int width, int height);

    void setConnerNoCacheImage(Object contextObject, String url, ImageView imageView, int conner);

    void setConnerNoCacheImage(Object contextObject, String url, ImageView imageView, int conner, int defaultId);

    void setSimpleBlur(Object contextObject, String url, ImageView imageView);

    void setSimpleBlur(Object contextObject, String url, ImageView imageView, int defaultId);

    void setSimpleBlur(Object contextObject, String url, ImageView imageView, int defaultId, int blurRadio, int blurSimpling);

    void setSimpleBlur(Object contextObject, String url, ImageView imageView, int defaultId, int blurRadio, int blurSimpling, boolean isShowAnim);

    void setSimpleBlur(Object contextObject, String url, ImageView imageView, int defaultId, boolean isShowAnim);

    void setSimpleBlurNoCache(Object contextObject, String url, ImageView imageView);

    void setSimpleBlurNoCache(Object contextObject, String url, ImageView imageView, int defaultId);

    /**
     * 获取图片原图 缓存文件 应运行于非主线程中
     */
    <T>File getImageFile(Object contextObject, T t);

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    <T>Drawable getImageDrawable(Object contextObject, T t);

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    <T>Drawable getImageDrawable(Object contextObject, T t, int width, int height);

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    <T>Drawable getImageCircleDrawable(Object contextObject, T t);

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    <T>Drawable getImageConnerDrawable(Object contextObject, T t, int conner);

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    <T>Drawable getImageConnerDrawable(Object contextObject, T t, int conner, boolean isCenterCrop);

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    <T>Drawable getImageDrawable(Object contextObject, T t,
                                 boolean isCenterCrop,
                                 boolean isCircle,
                                 boolean isUseCache,
                                 int conner,
                                 int blurRadio, int blurSimpling,
                                 int overrideWidth, int overrideHeight,
                                 final OnLoadImageListener<Drawable> listener);

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    <T>Bitmap getImageBitmap(Object contextObject, T t);

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    <T>Bitmap getImageBitmap(Object contextObject, T t, int width, int height);
}
