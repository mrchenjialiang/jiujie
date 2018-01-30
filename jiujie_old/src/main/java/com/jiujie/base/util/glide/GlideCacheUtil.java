package com.jiujie.base.util.glide;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.jiujie.base.util.FileUtil;
import com.jiujie.base.util.UIHelper;

import java.io.File;

/**
 * @author : Created by ChenJiaLiang on 2017/1/16.
 *         Email : 576507648@qq.com
 */
public class GlideCacheUtil {
    private static GlideCacheUtil instance;
    private final Context context;
    // 图片缓存最大容量，100M，根据自己的需求进行修改
    public static int GLIDE_CACHE_SIZE = 200 * 1024 * 1024;
    // 图片缓存子目录
    public static final String GLIDE_CACHE_DIR = "glide_cache";

    private GlideCacheUtil(Context context){
        this.context = context.getApplicationContext();
        int size = (int) (UIHelper.getSDAvailableSize()/5);
        if(size>GLIDE_CACHE_SIZE){
            GLIDE_CACHE_SIZE = size;
        }
    }

    public static GlideCacheUtil getInstance(Context context) {
        if (null == instance) {
            instance = new GlideCacheUtil(context);
        }
        return instance;
    }

    // 获取Glide磁盘缓存大小
    public String getCacheSize() {
        return FileUtil.getFormatSize(getCacheFileSize());
    }

    // 获取Glide磁盘缓存大小
    public String getSimpleCacheSize() {
        return FileUtil.getFormatSize(getCacheFileSize());
    }

    public long getCacheFileSize(){
        return FileUtil.getFileSize(getCacheFile());
    }

    @NonNull
    public File getCacheFile() {
        return new File(context.getCacheDir() + "/" + GLIDE_CACHE_DIR);
    }

    // 获取Glide磁盘缓存大小
    public long getCacheLongSize() {
        return getCacheFileSize();
    }

    // 清除Glide磁盘缓存，自己获取缓存文件夹并删除方法
    public boolean cleanCatchDisk() {
        return FileUtil.deleteFile(getCacheFile());
    }

    // 清除图片磁盘缓存，调用Glide自带方法
    public boolean clearCacheDiskSelf() {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 清除Glide内存缓存
    public boolean clearCacheMemory() {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context).clearMemory();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
