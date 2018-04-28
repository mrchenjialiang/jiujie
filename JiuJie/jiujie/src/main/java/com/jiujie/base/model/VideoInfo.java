package com.jiujie.base.model;

import java.io.Serializable;

/**
 * Created by ChenJiaLiang on 2018/4/28.
 * Email:576507648@qq.com
 */

public class VideoInfo implements Serializable{
    private static final long serialVersionUID = 1176189758484826192L;
    private String path;
    private long duration;//视频时间长度

    public VideoInfo(String path, long duration) {
        this.path = path;
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
