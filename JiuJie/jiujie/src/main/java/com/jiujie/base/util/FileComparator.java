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
        if (file1.lastModified() < file2.lastModified()) {
            return isDownByTime?1:-1;
        } else {
            return isDownByTime?-1:1;
        }
    }
}