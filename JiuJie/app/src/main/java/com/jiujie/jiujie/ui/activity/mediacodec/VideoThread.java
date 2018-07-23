package com.jiujie.jiujie.ui.activity.mediacodec;

import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.view.Surface;

import com.jiujie.base.util.UIHelper;

/**
 * Created by ChenJiaLiang on 2018/6/27.
 * Email:576507648@qq.com
 */

public class VideoThread extends Thread {

    private final Surface surface;
    private final String path;
    private VideoImageThread videoThread;
    private VideoAudioThread audioThread;

    public VideoThread(Surface surface, String path) {
        this.surface = surface;
        this.path = path;
    }

    @Override
    public void run() {
        try {
            MediaExtractor extractor = new MediaExtractor();
            extractor.setDataSource(path);

            //遍历媒体轨道
            for (int i = 0; i < extractor.getTrackCount(); i++) {
                MediaFormat format = extractor.getTrackFormat(i);
                String mime = format.getString(MediaFormat.KEY_MIME);
                UIHelper.showLog("mime:"+mime);
                if (mime.startsWith("video/")) {//视频轨道
                    videoThread = new VideoImageThread(surface, path, i, mime);
                    videoThread.start();
                } else if (mime.startsWith("audio/")) {//获取音频轨道
                    audioThread = new VideoAudioThread(path, i, mime);
                    audioThread.start();
                }
            }
            extractor.release();
            extractor = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doStart(){

    }

    public void doStop() {
        interrupt();
    }

    public void doRelease() {
        interrupt();
        if (videoThread != null) {
            videoThread.doRelease();
        }
        if (audioThread != null) {
            audioThread.doRelease();
        }
    }
}
