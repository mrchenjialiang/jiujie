package com.jiujie.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;


public class GlideModuleMine implements GlideModule {

    // 需要在AndroidManifest.xml中声明
    // <meta-data
    //    android:name="com.jiujie.base_swipe_back.util.glide.GlideModuleMine"
    //    android:value="GlideModule" />

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

//        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
        //自定义缓存目录
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context,
                GlideCacheUtil.GLIDE_CACHE_DIR,
                GlideCacheUtil.getMaxCacheSize()));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        //nil
    }
}
