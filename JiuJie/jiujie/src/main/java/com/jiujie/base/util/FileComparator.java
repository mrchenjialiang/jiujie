package com.jiujie.base.util;

import java.io.File;
import java.util.Comparator;

/**
 * Created by ChenJiaLiang on 2017/9/13.
 * Email:576507648@qq.com
 */

public class FileComparator implements Comparator<File> {
    private final boolean isDownByTime;

    /**
     * 最后修改的文件在前
     * @param isDownByTime 是否按时间降序排，
     */
    public FileComparator(boolean isDownByTime) {
        this.isDownByTime = isDownByTime;
    }

    @Override
    public int compare(File file1, File file2) {
        if(file1==null||file2==null||!file1.exists()||!file2.exists()){
            return 0;
        }
        Long key1 = file1.lastModified();
        Long key2 = file2.lastModified();
        if(isDownByTime){
            return key2.compareTo(key1);
        }else{
            return key1.compareTo(key2);
        }
    }
}