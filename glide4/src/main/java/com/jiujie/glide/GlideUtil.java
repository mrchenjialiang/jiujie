package com.jiujie.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Created by ChenJiaLiang on 2017/1/12.
 *         Email : 576507648@qq.com
 */
public class GlideUtil implements ImageLoader{

    private static GlideUtil glideUtil;

    private int normalDefaultId = R.drawable.glide4_default;
    private int circleDefaultId = R.drawable.glide4_circle;
    private int connerDefaultId = R.drawable.glide4_conner;
    private List<Object> recentUrlList = new ArrayList<>();
    private OnGlideStartListener onGlideStartListener;

    private GlideUtil() {
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

    public List<Object> getRecentUrlList() {
        return recentUrlList;
    }

    public void setOnGlideStartListener(OnGlideStartListener onGlideStartListener) {
        this.onGlideStartListener = onGlideStartListener;
    }

    //    取消加载，一般都不需要，因为会随页面关闭而停止
//    Glide.with(fragment).clear(imageView);

    public void setSimpleImage(Object contextObject,String url,ImageView imageView){
        setImage(contextObject,url,imageView,0,isCenterCropDefault(),isShowAnimDefault(),isCircleDefault(),isUseCacheDefault(),getConnerDefault(),getRadioDefault(),getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public void setSimpleImage(Object contextObject,String url,ImageView imageView,int defaultId){
        setImage(contextObject,url,imageView,defaultId,isCenterCropDefault(),isShowAnimDefault(),isCircleDefault(),isUseCacheDefault(),getConnerDefault(),getRadioDefault(),getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public void setDefaultImage(Object contextObject,String url,ImageView imageView){
        setImage(contextObject,url,imageView,normalDefaultId,isCenterCropDefault(),isShowAnimDefault(),isCircleDefault(),isUseCacheDefault(),getConnerDefault(),getRadioDefault(),getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public void setDefaultImage(Object contextObject,String url,ImageView imageView,boolean isCenterCrop){
        setImage(contextObject,url,imageView,normalDefaultId,isCenterCrop,isShowAnimDefault(),isCircleDefault(),isUseCacheDefault(),getConnerDefault(),getRadioDefault(),getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public void setDefaultImage(Object contextObject,String url,ImageView imageView,int width,int height){
        setImage(contextObject,url,imageView,normalDefaultId,isCenterCropDefault(),isShowAnimDefault(),isCircleDefault(),isUseCacheDefault(),getConnerDefault(),getRadioDefault(),getSimplingDefault(), width, height, null);
    }

    public void setDefaultImage(Object contextObject,String url,ImageView imageView,int defaultId){
        setImage(contextObject,url,imageView,defaultId,isCenterCropDefault(),isShowAnimDefault(),isCircleDefault(),isUseCacheDefault(),getConnerDefault(),getRadioDefault(),getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public void setDefaultImage(Object contextObject,String url,ImageView imageView,int defaultId,boolean isCenterCrop){
        setImage(contextObject,url,imageView,defaultId,isCenterCrop,isShowAnimDefault(),isCircleDefault(),isUseCacheDefault(),getConnerDefault(),getRadioDefault(),getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public void setDefaultImage(Object contextObject,String url,ImageView imageView,int defaultId,int width,int height){
        setImage(contextObject,url,imageView,defaultId,isCenterCropDefault(),isShowAnimDefault(),isCircleDefault(),isUseCacheDefault(),getConnerDefault(),getRadioDefault(),getSimplingDefault(), width, height, null);
    }

    public void setDefaultImage(Object contextObject,String url,ImageView imageView,int defaultId,OnLoadImageListener<Drawable> listener){
        setImage(contextObject,url,imageView,defaultId,isCenterCropDefault(),isShowAnimDefault(),isCircleDefault(),isUseCacheDefault(),getConnerDefault(),getRadioDefault(),getSimplingDefault(), getWidthDefault(), getHeightDefault(), listener);
    }

    public void setDefaultImage(Object contextObject,String url,ImageView imageView,int defaultId,boolean isCenterCrop,OnLoadImageListener<Drawable> listener){
        setImage(contextObject,url,imageView,defaultId,isCenterCrop,isShowAnimDefault(),isCircleDefault(),isUseCacheDefault(),getConnerDefault(),getRadioDefault(),getSimplingDefault(), getWidthDefault(), getHeightDefault(), listener);
    }

    public void setDefaultImage(Object contextObject,String url,ImageView imageView,int defaultId,boolean isCenterCrop,boolean isShowAnim,OnLoadImageListener<Drawable> listener){
        setImage(contextObject,url,imageView,defaultId,isCenterCrop,isShowAnim,isCircleDefault(),isUseCacheDefault(),getConnerDefault(),getRadioDefault(),getSimplingDefault(), getWidthDefault(), getHeightDefault(), listener);
    }

    public void setDefaultNoCacheImage(Object contextObject,String url,ImageView imageView){
        setImage(contextObject,url,imageView,normalDefaultId,isCenterCropDefault(),isShowAnimDefault(),isCircleDefault(),false,getConnerDefault(),getRadioDefault(),getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public void setDefaultNoCacheImage(Object contextObject,String url,ImageView imageView,int defaultId){
        setImage(contextObject,url,imageView,defaultId,isCenterCropDefault(),isShowAnimDefault(),isCircleDefault(),false,getConnerDefault(),getRadioDefault(),getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public void setCircleImage(Object contextObject,String url,ImageView imageView){
        setImage(contextObject,url,imageView,circleDefaultId,isCenterCropDefault(),isShowAnimDefault(),true,isUseCacheDefault(),getConnerDefault(),getRadioDefault(),getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public void setCircleImage(Object contextObject,String url,ImageView imageView,int width,int height){
        setImage(contextObject,url,imageView,circleDefaultId,isCenterCropDefault(),isShowAnimDefault(),true,isUseCacheDefault(),getConnerDefault(),getRadioDefault(),getSimplingDefault(), width, height, null);
    }

    public void setCircleImage(Object contextObject,String url,ImageView imageView,int defaultId){
        setImage(contextObject,url,imageView,defaultId,isCenterCropDefault(),isShowAnimDefault(),true,isUseCacheDefault(),getConnerDefault(),getRadioDefault(),getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public void setCircleImage(Object contextObject,String url,ImageView imageView,int defaultId,int width,int height){
        setImage(contextObject,url,imageView,defaultId,isCenterCropDefault(),isShowAnimDefault(),true,isUseCacheDefault(),getConnerDefault(),getRadioDefault(),getSimplingDefault(), width, height, null);
    }

    public void setCircleNoCacheImage(Object contextObject,String url,ImageView imageView){
        setImage(contextObject,url,imageView,circleDefaultId,isCenterCropDefault(),isShowAnimDefault(),true,false,getConnerDefault(),getRadioDefault(),getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public void setCircleNoCacheImage(Object contextObject,String url,ImageView imageView,int defaultId){
        setImage(contextObject,url,imageView,defaultId,isCenterCropDefault(),isShowAnimDefault(),true,false,getConnerDefault(),getRadioDefault(),getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public void setConnerImage(Object contextObject,String url,ImageView imageView,int conner){
        setImage(contextObject,url,imageView,connerDefaultId,isCenterCropDefault(),isShowAnimDefault(),isCircleDefault(),isUseCacheDefault(),conner,getRadioDefault(),getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public void setConnerImage(Object contextObject,String url,ImageView imageView,int conner,int width,int height){
        setImage(contextObject,url,imageView,connerDefaultId,isCenterCropDefault(),isShowAnimDefault(),isCircleDefault(),isUseCacheDefault(),conner,getRadioDefault(),getSimplingDefault(), width, height, null);
    }

    public void setConnerImage(Object contextObject,String url,ImageView imageView,int conner,int defaultId){
        setImage(contextObject,url,imageView,defaultId,isCenterCropDefault(),isShowAnimDefault(),isCircleDefault(),isUseCacheDefault(),conner,getRadioDefault(),getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public void setConnerImage(Object contextObject,String url,ImageView imageView,int conner,int defaultId,int width,int height){
        setImage(contextObject,url,imageView,defaultId,isCenterCropDefault(),isShowAnimDefault(),isCircleDefault(),isUseCacheDefault(),conner,getRadioDefault(),getSimplingDefault(), width, height, null);
    }

    public void setConnerNoCacheImage(Object contextObject,String url,ImageView imageView,int conner){
        setImage(contextObject,url,imageView,connerDefaultId,isCenterCropDefault(),isShowAnimDefault(),isCircleDefault(),false,conner,getRadioDefault(),getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public void setConnerNoCacheImage(Object contextObject,String url,ImageView imageView,int conner,int defaultId){
        setImage(contextObject,url,imageView,defaultId,isCenterCropDefault(),isShowAnimDefault(),isCircleDefault(),false,conner,getRadioDefault(),getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public void setSimpleBlur(Object contextObject,String url,ImageView imageView){
        setImage(contextObject,url,imageView,normalDefaultId,isCenterCropDefault(),isShowAnimDefault(),isCircleDefault(),isUseCacheDefault(),getConnerDefault(),15,1, getWidthDefault(), getHeightDefault(), null);
    }

    public void setSimpleBlur(Object contextObject,String url,ImageView imageView,int defaultId){
        setImage(contextObject,url,imageView,defaultId,isCenterCropDefault(),isShowAnimDefault(),isCircleDefault(),isUseCacheDefault(),getConnerDefault(),15,1, getWidthDefault(), getHeightDefault(), null);
    }

    public void setSimpleBlur(Object contextObject,String url,ImageView imageView,int defaultId,int blurRadio,int blurSimpling){
        setImage(contextObject,url,imageView,defaultId,isCenterCropDefault(),isShowAnimDefault(),isCircleDefault(),isUseCacheDefault(),getConnerDefault(),blurRadio,blurSimpling, getWidthDefault(), getHeightDefault(), null);
    }

    public void setSimpleBlur(Object contextObject,String url,ImageView imageView,int defaultId,int blurRadio,int blurSimpling,boolean isShowAnim){
        setImage(contextObject,url,imageView,defaultId,isCenterCropDefault(),isShowAnim,isCircleDefault(),isUseCacheDefault(),getConnerDefault(),blurRadio,blurSimpling, getWidthDefault(), getHeightDefault(), null);
    }

    public void setSimpleBlur(Object contextObject,String url,ImageView imageView,int defaultId,boolean isShowAnim){
        setImage(contextObject,url,imageView,defaultId,isCenterCropDefault(),isShowAnim,isCircleDefault(),isUseCacheDefault(),getConnerDefault(),15,1, getWidthDefault(), getHeightDefault(), null);
    }

    public void setSimpleBlurNoCache(Object contextObject,String url,ImageView imageView){
        setImage(contextObject,url,imageView,normalDefaultId,isCenterCropDefault(),isShowAnimDefault(),isCircleDefault(),false,getConnerDefault(),15,1, getWidthDefault(), getHeightDefault(), null);
    }

    public void setSimpleBlurNoCache(Object contextObject,String url,ImageView imageView,int defaultId){
        setImage(contextObject,url,imageView,defaultId,isCenterCropDefault(),isShowAnimDefault(),isCircleDefault(),false,getConnerDefault(),15,1,getWidthDefault(), getHeightDefault(),null);
    }

    private int getHeightDefault(){
        return -1;
    }

    private int getWidthDefault(){
        return -1;
    }

    private boolean isCenterCropDefault(){
        return true;
    }

    private boolean isShowAnimDefault(){
        return true;
    }

    private boolean isCircleDefault(){
        return false;
    }

    private boolean isUseCacheDefault(){
        return true;
    }

    private int getConnerDefault(){
        return 0;
    }

    private int getRadioDefault(){
        return 25;
    }

    private int getSimplingDefault(){
        return 1;
    }

    /**
     * 获取图片原图 缓存文件 应运行于非主线程中
     */
    public <T>File getImageFile(Object contextObject, T t){
        try {
            //这个是缓存的文件路径
            GlideRequestM glideRequestM = new GlideRequestM(contextObject, null).init();
            if (glideRequestM.isShouldReturn()) return null;
            return glideRequestM.getRequestManager()
                    .asFile()
                    .load(t)
                    .apply(new RequestOptions().override(Target.SIZE_ORIGINAL))//指定图片大小(原图)
                    .submit()
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    public <T>Drawable getImageDrawable(Object contextObject, T t){
        return getImageDrawable(contextObject,t,isCenterCropDefault(),isCircleDefault(),isUseCacheDefault(),getConnerDefault(),getRadioDefault(),getSimplingDefault(),getWidthDefault(),getHeightDefault(),null);
    }

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    public <T>Drawable getImageDrawable(Object contextObject, T t,int width,int height){
        return getImageDrawable(contextObject,t,isCenterCropDefault(),isCircleDefault(),isUseCacheDefault(),getConnerDefault(),getRadioDefault(),getSimplingDefault(),width,height,null);
    }

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    public <T>Drawable getImageCircleDrawable(Object contextObject, T t){
        return getImageDrawable(contextObject,t,isCenterCropDefault(),true,isUseCacheDefault(),getConnerDefault(),getRadioDefault(),getSimplingDefault(),getWidthDefault(),getHeightDefault(),null);
    }

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    public <T>Drawable getImageConnerDrawable(Object contextObject, T t,int conner){
        return getImageDrawable(contextObject,t,isCenterCropDefault(),isCircleDefault(),isUseCacheDefault(),conner,getRadioDefault(),getSimplingDefault(),getWidthDefault(),getHeightDefault(),null);
    }

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    public <T>Drawable getImageConnerDrawable(Object contextObject, T t,int conner,boolean isCenterCrop){
        return getImageDrawable(contextObject,t,isCenterCropDefault(),isCircleDefault(),isUseCacheDefault(),conner,getRadioDefault(),getSimplingDefault(),getWidthDefault(),getHeightDefault(),null);
    }

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    public <T>Drawable getImageDrawable(Object contextObject, T t,
                                        boolean isCenterCrop,
                                        boolean isCircle,
                                        boolean isUseCache,
                                        int conner,
                                        int blurRadio, int blurSimpling,
                                        int overrideWidth, int overrideHeight,
                                        final OnLoadImageListener<Drawable> listener){
        try {
            GlideRequestM glideRequestM = new GlideRequestM(contextObject, null).init();
            if (glideRequestM.isShouldReturn()) return null;
            RequestBuilder<Drawable> requestBuilder = glideRequestM.getRequestManager().asDrawable().load(t);
            if (listener != null) {
                requestBuilder = requestBuilder.listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                        listener.onFail(e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                        listener.onSuccess(drawable);
                        return false;
                    }
                });
            }
            RequestOptions options = getOptions(0, isCenterCrop, false, isCircle,
                    isUseCache, conner, blurRadio, blurSimpling, overrideWidth,
                    overrideHeight, glideRequestM.getContext());
            return requestBuilder
                    .apply(options)
                    .submit()
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    public <T>Bitmap getImageBitmap(Object contextObject, T t){
        return getImageBitmap(contextObject,t,0,0);
    }

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    public <T>Bitmap getImageBitmap(Object contextObject, T t,int width,int height){
        try {
            GlideRequestM glideRequestM = new GlideRequestM(contextObject, null).init();
            if (glideRequestM.isShouldReturn()) return null;
            RequestBuilder<Bitmap> requestBuilder = glideRequestM.getRequestManager()
                    .asBitmap()
                    .load(t);
            if(width>0&&height>0){
                requestBuilder = requestBuilder.apply(new RequestOptions().override(width, height));
            }else{
                requestBuilder = requestBuilder.apply(new RequestOptions().override(Target.SIZE_ORIGINAL));//指定图片大小(原图)
            }
            return requestBuilder
                    .submit()
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clearCache(Context context,ImageView imageView){
        Glide.with(context).clear(imageView);
    }

    public <T>void setImage(final Object contextObject, final T t, final ImageView imageView,
                            final int defaultId,
                            final boolean isCenterCrop,
                            final boolean isShowAnim,
                            final boolean isCircle,
                            final boolean isUseCache,
                            final int conner,
                            final int blurRadio, final int blurSimpling,
                            final int overrideWidth, final int overrideHeight,
                            final OnLoadImageListener<Drawable> listener) {
        if(imageView==null||t==null){
            return;
        }
        try {
            final GlideRequestM glideRequestM = new GlideRequestM(contextObject, imageView).init();
            if (glideRequestM.isShouldReturn()) return;
            final RequestManager requestManager = glideRequestM.getRequestManager();
            requestManager.clear(imageView);
            RequestBuilder<Drawable> requestBuilder = requestManager.load(t);
            if (isShowAnim) {
                requestBuilder = requestBuilder.transition(DrawableTransitionOptions.withCrossFade());
            }
            if (listener != null) {
                requestBuilder = requestBuilder.listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                        listener.onFail(e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                        listener.onSuccess(drawable);
                        return false;
                    }
                });
            }
            RequestOptions options = getOptions(defaultId, isCenterCrop, isShowAnim, isCircle, isUseCache, conner, blurRadio, blurSimpling, overrideWidth, overrideHeight, glideRequestM.getContext());
            requestBuilder = requestBuilder.apply(options);
            requestBuilder.into(imageView);

            if(recentUrlList!=null){
                recentUrlList.add(t);
                if(recentUrlList.size()>100){
                    recentUrlList.remove(0);
                }
                if(onGlideStartListener!=null){
                    onGlideStartListener.onListen();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RequestOptions getOptions(int defaultId, boolean isCenterCrop, boolean isShowAnim,
                                      boolean isCircle, boolean isUseCache, int conner, int blurRadio,
                                      int blurSimpling, int overrideWidth, int overrideHeight, Context context) {
        RequestOptions options = new RequestOptions();
        if(defaultId>0){
            options = options.placeholder(defaultId);
        }
        if(overrideWidth>0&&overrideHeight>0){
            options = options.override(overrideWidth,overrideHeight);
        }
        if(isCenterCrop){
            options = options.centerCrop();
        }
        if(isCircle){
            options = options.transform(new CircleCrop());
        }else if(conner>0){
            if(isCenterCrop){
                options = options.transform(new GlideRoundTransform(context,conner));
            }else{
                options = options.transform(new RoundedCorners(conner));
            }
        }else if(blurRadio<25||blurSimpling>1) {
            if (blurRadio > 25) {
                blurRadio = 25;
            }
            if (blurSimpling < 1) {
                blurSimpling = 1;
            }
            options = options.transform(new GlideBlurTransform(context,blurRadio,blurSimpling));
        }
//        options = options.skipMemoryCache(true);
        if(!isUseCache){
            options = options.skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        if(!isShowAnim){
            options = options.dontAnimate();
        }
        options = options.priority(Priority.HIGH);
        return options;
    }

//    @NonNull
//    private RequestOptions getRequestOptionsForDownload(int conner, boolean isCenterCrop, boolean isCircle, int width, int height, Context context) {
//        RequestOptions options = new RequestOptions();
//        if(width>0&&height>0){
//            options = options.override(width, height);
//        }else{
//            options = options.override(Target.SIZE_ORIGINAL);
//        }
//        if(isCircle){
//            options = options.transform(new CircleCrop());
//        }else if(conner>0){
//            if(isCenterCrop){
//                options = options.transform(new GlideRoundTransform(context,conner));
//            }else{
//                options = options.transform(new RoundedCorners(conner));
//            }
//        }
//        return options;
//    }
}
