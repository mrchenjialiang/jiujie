package com.jiujie.base.util.glide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jiujie.base.R;
import com.jiujie.base.util.UIHelper;

import java.io.File;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * @author : Created by ChenJiaLiang on 2017/1/12.
 *         Email : 576507648@qq.com
 */
public class GlideUtil {

    private static GlideUtil glideUtil;
    private boolean isKeepInMemory;
    private int normalDefaultId = R.drawable.logo_gray;
    private int circleDefaultId = R.drawable.circle_bg_gray;
    private int connerDefaultId = R.drawable.logo_gray_conner;

    private GlideUtil() {
        long romAvailableSize = UIHelper.getRomAvailableSize();
        isKeepInMemory = romAvailableSize > 100 * 1024 * 1024;
    }

    public static GlideUtil instance() {
        if (glideUtil == null) {
            glideUtil = new GlideUtil();
        }
        return glideUtil;
    }

    public void setNormalDefaultId(int normalDefaultId){
        this.normalDefaultId = normalDefaultId;
    }

    public void setCircleDefaultId(int circleDefaultId) {
        this.circleDefaultId = circleDefaultId;
    }

    public void setConnerDefaultId(int connerDefaultId) {
        this.connerDefaultId = connerDefaultId;
    }

    public boolean isKeepInMemory() {
        return isKeepInMemory;
    }

    public void setSimpleImage(Context activity, String url, ImageView imageView) {
        Glide.clear(imageView);
        DrawableRequestBuilder<String> builder = Glide.with(activity)
                .load(url)
                .crossFade();
        builder.skipMemoryCache(true);//跳过内存缓存
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder.into(imageView);
    }

    public void setDefaultNoCenterCropImage(Context context, String url, ImageView imageView) {
        setDefaultNoCenterCropImage(context, url, imageView, null);
    }

    public void setDefaultNoCenterCropImage(Context context, String url, ImageView imageView, RequestListener<String, GlideDrawable> listener) {
        Glide.clear(imageView);
        DrawableRequestBuilder<String> builder = Glide.with(context)
                .load(url)
                .placeholder(normalDefaultId)
                .crossFade();
        builder.skipMemoryCache(true);//跳过内存缓存
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        if (listener != null) {
            builder.listener(listener);
        }
        builder.into(imageView);
    }

    public void setDefaultImage(Context context, int drawId, ImageView imageView,int defaultDrawId) {
        if(defaultDrawId<=0){
            defaultDrawId = normalDefaultId;
        }
        DrawableRequestBuilder<Integer> builder = Glide.with(context)
                .load(drawId)
                .placeholder(defaultDrawId)
                .crossFade()
                .centerCrop();
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder.into(imageView);
    }

    public void setVideoImage(Context context, Uri uri, ImageView imageView, int defaultDrawId) {
        if(defaultDrawId<=0){
            defaultDrawId = normalDefaultId;
        }
        Glide.with(context)
                .load(uri)
                .placeholder(defaultDrawId)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(imageView);
    }

    public void setDefaultImage(Context context, String url, ImageView imageView, int width, int height) {
        Glide.clear(imageView);
        DrawableRequestBuilder<String> builder = Glide.with(context)
                .load(url)
                .placeholder(normalDefaultId)
                .crossFade()
                .centerCrop()
                .override(width, height);
        builder.skipMemoryCache(true);//跳过内存缓存
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder.into(imageView);
    }

    public void setDefaultImage(Context context, String url, ImageView imageView) {
        setDefaultImage(context, url, imageView, 0, true, true, null);
    }

    public void setDefaultImage(Context context, String url, ImageView imageView, RequestListener<String, GlideDrawable> listener) {
        setDefaultImage(context, url, imageView, 0, true, true, listener);
    }

    public void setDefaultImage(Context context, String url, ImageView imageView, int defaultId) {
        setDefaultImage(context, url, imageView, defaultId, true, true, null);
    }

    public void setDefaultImage(Context context, String url, ImageView imageView, boolean isCenterCrop, boolean isShowAnim) {
        setDefaultImage(context, url, imageView, 0, isCenterCrop, isShowAnim, null);
    }

    public void setDefaultImage(Context context, String url, ImageView imageView, int defaultId, boolean isCenterCrop, boolean isShowAnim, RequestListener<String, GlideDrawable> listener) {
        setDefaultImage(context, url, imageView, defaultId, isCenterCrop, isShowAnim, 25,1,listener);
    }

    /**
     * 简单高斯模糊
     */
    public void setSimpleBlur(Context context, String url, ImageView imageView, int defaultId) {
        setSimpleBlur(context, url, imageView, defaultId,15,1);
    }

    /**
     * 简单高斯模糊
     */
    public void setSimpleBlur(Context context, String url, ImageView imageView, int defaultId,int radio,int simpling) {
        setDefaultImage(context, url, imageView, defaultId,true,true,radio,simpling,null);
    }

    public void setDefaultImage(Context context, File file, ImageView imageView){
        setDefaultImage(context, file, imageView, 0, true, true, 25,1,null);
    }

    public <T>void setDefaultImage(Context context, T t, ImageView imageView, int defaultId, boolean isCenterCrop, boolean isShowAnim, int radio,int simpling,RequestListener<T, GlideDrawable> listener) {
        try {
            if(context instanceof Activity){
                Activity activity = (Activity) context;
                if(activity.isFinishing()){
                    return;
                }
            }
            Glide.clear(imageView);
            if (defaultId <= 0) {
                defaultId = normalDefaultId;
            }
            DrawableRequestBuilder<T> builder = Glide.with(context)
                    .load(t)
                    .placeholder(defaultId);
            if (isCenterCrop) {
                builder.centerCrop();
            }
            if (isShowAnim) {
                builder.crossFade();
            } else {
                builder.dontAnimate();
            }
            if(radio<25||simpling>1){
                if(radio>25){
                    radio = 25;
                }
                if(simpling<1){
                    simpling = 1;
                }
                if(isCenterCrop){
                    builder.bitmapTransform(new CenterCrop(context), new BlurTransformation(context,radio,simpling));
                }else{
                    builder.bitmapTransform(new BlurTransformation(context,radio,simpling));
                }
            }
            if (listener != null) {
                builder.listener(listener);
            }
            builder.skipMemoryCache(true);//跳过内存缓存
            if (!isKeepInMemory) {
                builder.diskCacheStrategy(DiskCacheStrategy.NONE);//禁用磁盘缓存
            }
            builder.into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDefaultImage(Context context, String url, ImageView imageView, int defaultId, boolean isCenterCrop, boolean isShowAnim, int radio,int simpling,RequestListener<String, GlideDrawable> listener) {
        try {
            if(context instanceof Activity){
                Activity activity = (Activity) context;
                if(activity.isFinishing()){
                    return;
                }
            }
            Glide.clear(imageView);
            if (defaultId <= 0) {
                defaultId = normalDefaultId;
            }
            DrawableRequestBuilder<String> builder = Glide.with(context)
                    .load(url)
                    .placeholder(defaultId);
            if (isCenterCrop) {
                builder.centerCrop();
            }
            if (isShowAnim) {
                builder.crossFade();
            } else {
                builder.dontAnimate();
            }
            if(radio<25||simpling>1){
                if(radio>25){
                    radio = 25;
                }
                if(simpling<1){
                    simpling = 1;
                }
                if(isCenterCrop){
                    builder.bitmapTransform(new CenterCrop(context), new BlurTransformation(context,radio,simpling));
                }else{
                    builder.bitmapTransform(new BlurTransformation(context,radio,simpling));
                }
            }
            if (listener != null) {
                builder.listener(listener);
            }
            builder.skipMemoryCache(true);//跳过内存缓存
            if (!isKeepInMemory) {
                builder.diskCacheStrategy(DiskCacheStrategy.NONE);//禁用磁盘缓存
            }
            builder.into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDefaultImage(Context context, String url, ImageView imageView, boolean isShowAnim) {
        Glide.clear(imageView);
        DrawableRequestBuilder<String> builder = Glide.with(context)
                .load(url)
                .placeholder(normalDefaultId)
                .crossFade()
                .centerCrop();
        if (isShowAnim) {
            builder.crossFade();
        } else {
            builder.dontAnimate();
        }
        builder.skipMemoryCache(true);//跳过内存缓存
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder.into(imageView);
    }

    public void setStockKLineImage(Context context, String url, ImageView imageView) {
        Glide.clear(imageView);
        DrawableRequestBuilder<String> builder = Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                .skipMemoryCache(true)//跳过内存缓存
                .dontAnimate()
                .centerCrop();
        builder.skipMemoryCache(true);//跳过内存缓存
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder
                .into(imageView);
    }

    public void setDefaultNoCacheImage(Context context, String url, ImageView imageView) {
        Glide.clear(imageView);
        Glide.with(context)
                .load(url)
                .placeholder(normalDefaultId)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                .skipMemoryCache(true)//跳过内存缓存
                .crossFade()
                .centerCrop()
                .into(imageView);
    }

    public void setDefaultNoCacheImage(Context context, int id, ImageView imageView) {
        Glide.clear(imageView);
        DrawableRequestBuilder<Integer> builder = Glide.with(context)
                .load(id)
                .placeholder(normalDefaultId)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                .skipMemoryCache(true)//跳过内存缓存
                .crossFade()
                .centerCrop();
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder
                .into(imageView);
    }

    public void setDefaultNoCacheImage(Context context, File file, ImageView imageView) {
        Glide.clear(imageView);
        DrawableRequestBuilder<File> builder = Glide.with(context)
                .load(file)
                .placeholder(normalDefaultId)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                .skipMemoryCache(true)//跳过内存缓存
                .crossFade()
                .centerCrop();
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder
                .into(imageView);
    }

    public void setCircleNoCacheImage(Context context, String url, ImageView imageView) {
        Glide.clear(imageView);
        Glide.with(context)
                .load(url)
                .placeholder(circleDefaultId)
                .transform(new CenterCrop(context), new GlideCircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)//跳过内存缓存
                .crossFade()
                .into(imageView);
    }

    public void setCircleNoCacheImage(Context context, String url, ImageView imageView, int defaultId) {
        Glide.clear(imageView);
        Glide.with(context)
                .load(url)
                .placeholder(defaultId)
                .transform(new CenterCrop(context), new GlideCircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)//跳过内存缓存
                .crossFade()
                .into(imageView);
    }

    public void setCircleImage(Context context, String url, ImageView imageView) {
        Glide.clear(imageView);
        DrawableRequestBuilder<String> builder = Glide.with(context)
                .load(url)
                .placeholder(circleDefaultId)
                .transform(new CenterCrop(context), new GlideCircleTransform(context))
                .crossFade();
        builder.skipMemoryCache(true);//跳过内存缓存
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder
//                .centerCrop()//不能这样，否则会变成方形
                .into(imageView);
    }

    public void setCircleImage(Context activity, String url, ImageView imageView, int defaultId) {
        Glide.clear(imageView);
        DrawableRequestBuilder<String> builder = Glide.with(activity)
                .load(url)
                .placeholder(defaultId)
                .transform(new CenterCrop(activity.getApplicationContext()), new GlideCircleTransform(activity.getApplicationContext()))
                .crossFade();
        builder.skipMemoryCache(true);//跳过内存缓存
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder
//                .centerCrop()//不能这样，否则会变成方形
                .into(imageView);
    }

    public void setConnerImage(Context context, String url, ImageView imageView, int dp) {
        setConnerImage(context, url, imageView, dp, 0);
    }

    public void setConnerImage(Context context, String url, ImageView imageView, int dp, int drawId) {
        Glide.clear(imageView);
        DrawableRequestBuilder<String> builder = Glide.with(context)
                .load(url)
                .placeholder(drawId == 0 ? connerDefaultId : drawId)
                .transform(new CenterCrop(context), new GlideRoundTransform(context, dp))
                .crossFade();
        builder.skipMemoryCache(true);//跳过内存缓存
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder
//                .centerCrop()//不能直接这样，否则会变成方形
                .into(imageView);
    }

    public void setConnerImage(Context context, String url, ImageView imageView, int dp, boolean isCenterCrop) {
        Glide.clear(imageView);
        DrawableRequestBuilder<String> builder = Glide.with(context)
                .load(url)
                .placeholder(connerDefaultId)
                .crossFade();
        if (isCenterCrop) {
            builder.transform(new CenterCrop(context), new GlideRoundTransform(context, dp));
        } else {
            builder.transform(new GlideRoundTransform(context, dp));
        }
        builder.skipMemoryCache(true);//跳过内存缓存
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder
//                .centerCrop()//不能这样，否则会变成方形
                .into(imageView);
    }

    public void setConnerImage(Context context, String url, ImageView imageView, int dp, float scaleHeight) {
        Glide.clear(imageView);
        DrawableRequestBuilder<String> builder = Glide.with(context)
                .load(url)
                .transform(new CenterCrop(context), new GlideRoundTransform(context, dp, scaleHeight))
                .placeholder(connerDefaultId)
                .crossFade();
        builder.skipMemoryCache(true);//跳过内存缓存
        if (!isKeepInMemory) {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        builder
//                .centerCrop()//不能这样，否则会变成方形
                .into(imageView);
    }

    /**
     * Must do on a background thread
     */
    public Bitmap getImage(Context context, String url, int width, int height) {
        return getImage(context, url, true, true, width, height);
    }

    /**
     * Must do on a background thread
     */
    public Bitmap getImage(Context context, String url) {
        return getImage(context, url, true, true, Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
    }

    /**
     * Must do on a background thread
     */
    public Bitmap getImage(Context context, String url, boolean isUseCache, boolean isShowAnim) {
        return getImage(context, url, isUseCache, isShowAnim, Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
    }

    /**
     * Must do on a background thread
     */
    public Bitmap getImage(Context context, String url, boolean isUseCache, boolean isShowAnim, int width, int height) {
        DrawableTypeRequest<String> builder = Glide.with(context)
                .load(url);
        if (isUseCache) {
            builder.diskCacheStrategy(DiskCacheStrategy.ALL);
        } else {
            builder.diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true);
        }
        if (!isShowAnim) {
            builder.dontAnimate();
        } else {
            builder.crossFade();
        }
        Bitmap bitmap = null;
        try {
            bitmap = builder.asBitmap()
                    .into(width, height)
                    .get();
        } catch (Exception e) {
            UIHelper.showLog("GlideUtil getImage error " + e);
        }
        return bitmap;
    }

}
