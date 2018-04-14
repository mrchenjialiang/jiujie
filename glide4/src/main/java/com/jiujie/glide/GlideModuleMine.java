package com.jiujie.glide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;

@GlideModule
public class GlideModuleMine extends AppGlideModule {
    @Override
    public boolean isManifestParsingEnabled() {
//    return super.isManifestParsingEnabled();
        return false;
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        //设置内存
        //方法1：设置缓存数，默认2，指缓存两个屏幕的像素量
//        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
//                .setMemoryCacheScreens(2)
//                .build();
//        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));

        //方法2，直接设置最大缓存内存数
        int memoryCacheSizeBytes = 1024 * 1024 * 5; // 20mb
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));


        //设置存储
//        builder.setDiskCache(new DiskCache.Factory() {
//            @Override
//            public DiskCache build() {
//                return new MyCustomDiskCache();
//            }
//        });
        //自定义缓存目录
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context,
                GlideCacheUtil.GLIDE_CACHE_DIR,
                GlideCacheUtil.getMaxCacheSize()));
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
    }


//    public static class MyCustomDiskCache implements DiskCache {
//
//        @Nullable
//        @Override
//        public File get(Key key) {
//            return null;
//        }
//
//        @Override
//        public void put(Key key, Writer writer) {
//        }
//
//        @Override
//        public void delete(Key key) {
//
//        }
//
//        @Override
//        public void clear() {
//
//        }
//    }
}

