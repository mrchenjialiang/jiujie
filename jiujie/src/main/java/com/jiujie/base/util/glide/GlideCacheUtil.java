package com.jiujie.base.util.glide;

import android.content.Context;
import android.os.Looper;

import com.bumptech.glide.Glide;
import com.jiujie.base.util.UIHelper;

import java.io.File;
import java.math.BigDecimal;

/**
 * @author : Created by ChenJiaLiang on 2017/1/16.
 *         Email : 576507648@qq.com
 */
public class GlideCacheUtil {
    private static GlideCacheUtil instance;
    private final Context context;
    // 图片缓存最大容量，100M，根据自己的需求进行修改
    public static int GLIDE_CACHE_SIZE = 100 * 1024 * 1024;
    // 图片缓存子目录
    public static final String GLIDE_CACHE_DIR = "glide_cache";

    private GlideCacheUtil(Context context){
        this.context = context.getApplicationContext();
        GLIDE_CACHE_SIZE = (int) (UIHelper.getSDAvailableSize()/5);
        if(GLIDE_CACHE_SIZE>100 * 1024 * 1024){
            GLIDE_CACHE_SIZE = 100 * 1024 * 1024;
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
        try {
            return getFormatSize(getFolderSize(new File(context.getCacheDir() + "/" + GLIDE_CACHE_DIR)));
        } catch (Exception e) {
            e.printStackTrace();
            return "0M";
        }
    }

    // 获取Glide磁盘缓存大小
    public String getSimpleCacheSize() {
        try {
            return getSimpleFormatSize(getFolderSize(new File(context.getCacheDir() + "/" + GLIDE_CACHE_DIR)));
        } catch (Exception e) {
            e.printStackTrace();
            return "0M";
        }
    }

    // 获取Glide磁盘缓存大小
    public long getCacheLongSize() {
        try {
            return getFolderSize(new File(context.getCacheDir() + "/" + GLIDE_CACHE_DIR));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 清除Glide磁盘缓存，自己获取缓存文件夹并删除方法
    public boolean cleanCatchDisk() {
        return deleteFolderFile(context.getCacheDir() + "/" + GLIDE_CACHE_DIR, true);
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

    // 获取指定文件夹内所有文件大小的和
    private long getFolderSize(File file) throws Exception {
        long size = 0;
        if(file==null||!file.exists()) return size;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    // 格式化单位
    private static String getSimpleFormatSize(double size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size == 0) {
            return "0M";
        }

        if (size >= gb) {
            return String.format("%.1fG", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0fM" : "%.1fM", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0fK" : "%.1fK", f);
        } else
            return String.format("%dB", size);
    }

    // 格式化单位
    private static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "M";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "G";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "T";
    }

    // 按目录删除文件夹文件方法
    private boolean deleteFolderFile(String filePath, boolean deleteThisPath) {
        try {
            File file = new File(filePath);
            if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (File file1 : files) {
                    deleteFolderFile(file1.getAbsolutePath(), true);
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {
                    file.delete();
                } else {
                    if (file.listFiles().length == 0) {
                        file.delete();
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
