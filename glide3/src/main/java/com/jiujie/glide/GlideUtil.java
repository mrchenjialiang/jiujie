package com.jiujie.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

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
                   .load(t)
                   .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
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
     * Bitmap to Drawable
     */
    public Drawable bitmap2Drawable(Bitmap bitmap, Context mContext) {
        if(bitmap==null) return null;
        return new BitmapDrawable(mContext.getResources(), bitmap);
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
                                        int width, int height,
                                        OnLoadImageListener<Drawable> listener ){
        try {
            final GlideRequestM glideRequestM = new GlideRequestM(contextObject, null).init();
            if (glideRequestM.isShouldReturn()) return null;
            RequestManager requestManager = glideRequestM.getRequestManager();

            DrawableRequestBuilder<T> builder = getDrawableRequestBuilder(
                    t, 0, isCenterCrop,
                    false, isCircle, isUseCache,
                    conner, blurRadio, blurSimpling,
                    width, height, listener, glideRequestM, requestManager,
            true);

            if(width>0&&height>0){
                return builder.into(width, height).get();
            }else{
                return builder.into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
            }
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
            final GlideRequestM glideRequestM = new GlideRequestM(contextObject, null).init();
            if (glideRequestM.isShouldReturn()) return null;
            DrawableTypeRequest<T> load = glideRequestM.getRequestManager().load(t);
            Bitmap bitmap;
            if(width>0&&height>0){
                bitmap = load.asBitmap().into(width, height).get();
            }else{
                bitmap = load.asBitmap().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clearCache(ImageView imageView){
        Glide.clear(imageView);
    }

    public <T>void setImage(Object contextObject, T t, ImageView imageView,
                            int defaultId,
                            boolean isCenterCrop,
                            boolean isShowAnim,
                            boolean isCircle,
                            boolean isUseCache,
                            int conner,
                            int blurRadio, int blurSimpling,
                            int width, int height,
                            final OnLoadImageListener<Drawable> listener) {
        if(imageView==null||t==null){
            return;
        }
        try {
            final GlideRequestM glideRequestM = new GlideRequestM(contextObject, imageView).init();
            if (glideRequestM.isShouldReturn()) return;
            final RequestManager requestManager = glideRequestM.getRequestManager();
            Glide.clear(imageView);
            if (defaultId <= 0) {
                defaultId = normalDefaultId;
            }
            DrawableTypeRequest<T> builder = getDrawableRequestBuilder(
                    t, defaultId, isCenterCrop,
                    isShowAnim, isCircle, isUseCache,
                    conner, blurRadio, blurSimpling,
                    width, height, listener, glideRequestM, requestManager, false);
            builder.into(imageView);

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

    private <T> DrawableTypeRequest<T> getDrawableRequestBuilder(T t, int defaultId, boolean isCenterCrop,
                                                                 boolean isShowAnim, boolean isCircle, boolean isUseCache,
                                                                 int conner, int blurRadio, int blurSimpling, int width, int height,
                                                                 final OnLoadImageListener<Drawable> listener, GlideRequestM glideRequestM, RequestManager requestManager,
                                                                 boolean isForDownload) {
        DrawableTypeRequest<T> load = requestManager.load(t);
        if(isForDownload){
            load.asBitmap();
        }
        if(defaultId>0){
            load.placeholder(defaultId);
        }
        if (isCenterCrop) {
            load.centerCrop();
        }
        if (isShowAnim) {
            load.crossFade();
        } else {
            load.dontAnimate();
        }
        Context context = glideRequestM.getContext();
        if(blurRadio<25||blurSimpling>1){
            if(blurRadio>25){
                blurRadio = 25;
            }
            if(blurSimpling<1){
                blurSimpling = 1;
            }
            if(isCenterCrop){
                load.bitmapTransform(new CenterCrop(glideRequestM.getContext()), new BlurTransformation(glideRequestM.getContext(),blurRadio,blurSimpling));
            }else{
                load.bitmapTransform(new BlurTransformation(glideRequestM.getContext(),blurRadio,blurSimpling));
            }
        }
        if(conner>0){
            if (isCenterCrop) {
                load.transform(new CenterCrop(glideRequestM.getContext()), new GlideRoundTransform(glideRequestM.getContext(), conner));
            } else {
                load.transform(new GlideRoundTransform(glideRequestM.getContext(), conner));
            }
        }
        if(isCircle){
            if (isCenterCrop) {
                load.transform(new CenterCrop(context), new GlideCircleTransform(context));
            } else {
                load.transform(new GlideCircleTransform(context));
            }
        }
        if(!isUseCache){
            load.diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true);//跳过内存缓存
        }
        if (listener != null) {
            load.listener(new RequestListener<T, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, T model, Target<GlideDrawable> target, boolean isFirstResource) {
                    listener.onFail(e);
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, T model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    listener.onSuccess(resource);
                    return false;
                }
            });
        }
        if(width>0&&height>0){
            load.override(width,height);
        }
        return load;
    }
}
