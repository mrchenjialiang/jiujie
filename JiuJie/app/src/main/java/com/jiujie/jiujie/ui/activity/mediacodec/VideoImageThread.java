package com.jiujie.jiujie.ui.activity.mediacodec;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.view.Surface;

import com.jiujie.base.util.UIHelper;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by ChenJiaLiang on 2018/6/27.
 * Email:576507648@qq.com
 */

public class VideoImageThread extends Thread {

    private String TAG = "VideoImageThread";
    private final Surface surface;
    private final String path;
    private final int trackIndex;
    private final String mime;
    private MediaCodec videoDecoder;
    private MediaCodec.BufferInfo bufferInfo;
    private final long TIMEOUT_US = 10000;
    private MediaExtractor extractor;

//    public VideoImageThread(Surface surface, String path) {
//        this.surface = surface;
//        this.path = path;
//    }

    VideoImageThread(Surface surface, String path, int trackIndex, String mime) {
        this.surface = surface;
        this.path = path;
        this.trackIndex = trackIndex;
        this.mime = mime;
    }

    @Override
    public void run() {
        try {
            playVideo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doPrepare() {
        try {
            extractor = new MediaExtractor();
            extractor.setDataSource(path);
            MediaFormat format = extractor.getTrackFormat(trackIndex);
//            logFormat(format);
            extractor.selectTrack(trackIndex);
            videoDecoder = MediaCodec.createDecoderByType(mime);
            videoDecoder.configure(format, surface, null, 0);

            videoDecoder.start();//启动MediaCodec ，等待传入数据

            bufferInfo = new MediaCodec.BufferInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logFormat(MediaFormat format){
        int rotation = 0;
        int width = 0;
        int height = 0;
        if (format.containsKey("rotation-degrees")) {
            rotation = format.getInteger("rotation-degrees");
        }
        if (format.containsKey("height")) {
            height = format.getInteger("height");
        }
        if (format.containsKey("width")) {
            width = format.getInteger("width");
        }

        if(rotation==90||rotation==270){
            format.setInteger("rotation-degrees",90);
            format.setInteger("height",width);
            format.setInteger("width",height);
        }

        UIHelper.showLog("getVideoInfo width:" + width + "，height:" + height + "，rotation:" + rotation);
    }

    private void playVideo() throws IOException {
        if(extractor==null){
            doPrepare();
        }

        doWork();
    }

    private void doWork() {
        boolean isReadEnd = false;
        long startTime = System.currentTimeMillis();
        while (isEnable) {
            UIHelper.showLog(TAG, "doWork isEnable："+isEnable);
            if (!isReadEnd) {
                int inIndex = videoDecoder.dequeueInputBuffer(TIMEOUT_US);
                if (inIndex >= 0) {
                    ByteBuffer buffer = videoDecoder.getInputBuffers()[inIndex];
                    int sampleSize = extractor.readSampleData(buffer, 0);
                    if (sampleSize < 0) {
                        UIHelper.showLog(TAG, "读取结束");
                        videoDecoder.queueInputBuffer(inIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                        isReadEnd = true;
                    } else {
                        UIHelper.showLog(TAG, "读取中 inIndex:"+inIndex);
                        videoDecoder.queueInputBuffer(inIndex, 0, sampleSize, extractor.getSampleTime(), 0);
                        extractor.advance();//将进度调整到正确的帧位置
                    }
                }
            }

            int outIndex = videoDecoder.dequeueOutputBuffer(bufferInfo, TIMEOUT_US);
            if (outIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                UIHelper.showLog(TAG, "播放开始");
            } else if (outIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                //每次都要设置
                videoDecoder.setVideoScalingMode(MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            } else if (outIndex > 0) {
                UIHelper.showLog(TAG, "播放中 outIndex:"+outIndex);
                videoDecoder.releaseOutputBuffer(outIndex, true);
            }

            // All decoded frames have been rendered, we can stop playing now
            if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {//即 bufferInfo.flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM
                UIHelper.showLog(TAG, "播放结束");
                videoDecoder.stop();
                videoDecoder.release();
                extractor.release();
                break;
            }
        }
        long endTime = System.currentTimeMillis();
        UIHelper.showLog("doWord 花费时间:"+(endTime-startTime)+",endTime:"+UIHelper.timeLongHaoMiaoToString(endTime,"HH:ss"));
    }

    private boolean isEnable = true;
    public void doStart(){
        isEnable = true;
        if(extractor==null){
            doPrepare();
        }
        doWork();
    }

    public void doPause(){
        isEnable = false;
        UIHelper.showLog(TAG, "doPause isEnable："+isEnable);
        UIHelper.showLog("doPause 时间"+UIHelper.timeLongHaoMiaoToString(System.currentTimeMillis(),"HH:ss"));
    }

    public void doRelease() {
        interrupt();
//        try {
//            sleep(10);
//            if (videoDecoder != null) {
//                videoDecoder.stop();
//                videoDecoder.release();
//            }
//            if (extractor != null) {
//                extractor.release();
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
