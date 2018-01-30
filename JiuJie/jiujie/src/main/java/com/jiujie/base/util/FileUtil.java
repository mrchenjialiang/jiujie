package com.jiujie.base.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ChenJiaLiang on 2017/7/19.
 * Email:576507648@qq.com
 */

@SuppressLint("SimpleDateFormat")
public class FileUtil {
    private static final String TAG = "FileUtil  ";
    private static final String DirName_Camera = "camera";
    private static final String DirName_Picture = "picture";
    private static final String DirName_Log = "log";


    public static File createLogFile(Context context,String fileName){
        if(TextUtils.isEmpty(fileName)){
            fileName = getTimeFileName(".txt");
        }
        return createFile(context, DirName_Log,fileName);
    }

    public static File createPictureFile(Context context,String fileName){
        if(TextUtils.isEmpty(fileName)){
            fileName = getTimeFileName(".png");
        }
        return createFile(context, DirName_Picture,fileName);
    }

    public static File createCameraFile(Context context,String fileName){
        if(TextUtils.isEmpty(fileName)){
            fileName = getTimeFileName(".png");
        }
        return createFile(context, DirName_Camera,fileName);
    }

    /**
     * 当前日期 年_月_日_时_分_秒
     * @param fileType .jpg  .png   .txt  ...
     */
    private static String getTimeFileName(String fileType) {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        return sdf.format(new Date(currentTimeMillis)) + fileType;
    }

    private static File createFile(Context context,String dirName,String fileName){
        try {
            File dir = getDirFile(context,dirName);
            if (dir == null||!dir.exists()) return null;
            return createFile(dir.getAbsolutePath(),fileName);
        } catch (Exception e) {
            UIHelper.showLog(TAG+"createFile fail :"+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static File createFile(String dirPath,String fileName){
        try {
            File dirFile = new File(dirPath);
            if(!dirFile.exists()){
                if(!dirFile.mkdirs()){
                    UIHelper.showLog(TAG+"createFile dirFile.mkdirs fail");
                    return null;
                }
            }
            File file = new File(dirPath, fileName);
            if (!file.exists()) {
                if(!file.createNewFile()){
                    UIHelper.showLog(TAG+"createFile createNewFile fail");
                    return null;
                }
            }
            return file;
        }catch (Exception e){
            UIHelper.showLog(TAG+"createFile fail :"+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static File getDirFile(Context context,String dirName) {
        File dir;
        if(UIHelper.isSdCardExist()) {
            String dirPath = ImageUtil.instance().getCacheSDDic(context)+dirName;
            dir = new File(dirPath);
            if (!dir.exists()) {
                if(!dir.mkdirs()){
                    dir = context.getCacheDir();
                }
            }
        }else{
            dir = context.getCacheDir();
        }
        if(!dir.exists()){
            UIHelper.showLog(TAG+"createFile dir mkdirs fail");
            return null;
        }
        return dir;
    }

    public static boolean deleteFile(File file){
        boolean isSuccess = true;
        if(file==null||!file.exists()) return true;
        if(file.isDirectory()){
            File[] fileList = file.listFiles();
            for (File f : fileList) {
                if(!deleteFile(f)){
                    isSuccess = false;
                }
            }
        }else{
            return file.delete();
        }
        return isSuccess;
    }

    // 获取指定文件夹内所有文件大小的和
    public static long getFileSize(File... files){
        long size = 0;
        for (File file : files){
            if(file==null||!file.exists()) return size;
            if(file.isDirectory()){
                File[] fileList = file.listFiles();
                for (File f : fileList) {
                    size = size + getFileSize(f);
                }
            }else{
                size += file.length();
            }
        }
        return size;
    }

    // 格式化单位
    public static String getSimpleFormatSize(long size) {
        if(size==0){
            return "0B";
        }
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

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
    public static String getFormatSize(long size) {
        return getFormatSize(size,2);
    }

    // 格式化单位
    public static String getFormatSize(long size,int dotSize) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(dotSize, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(dotSize, BigDecimal.ROUND_HALF_UP).toPlainString() + "M";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(dotSize, BigDecimal.ROUND_HALF_UP).toPlainString() + "G";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(dotSize, BigDecimal.ROUND_HALF_UP).toPlainString() + "T";
    }
}
