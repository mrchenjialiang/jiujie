package com.jiujie.jiujie.ui.activity.mediacodec;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.view.Surface;

import com.jiujie.base.util.UIHelper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenJiaLiang on 2018/7/17.
 * Email:576507648@qq.com
 */

public class VideoDecoderHelper {
    private String TAG = "VideoDecoderHelper";
    private MediaExtractor mExtractor;
    private MediaCodec mDecoder;
    private final String path;
    private final Surface surface;
    private boolean isEnable;
    private MediaCodec.BufferInfo bufferInfo;
    private ByteBuffer[] inputBuffers;
    private MediaFormat videoFormat;
    private ByteBuffer[] outputBuffers;

    public VideoDecoderHelper(Surface surface, String path) {
        this.path = path;
        this.surface = surface;
        init();
    }

    private void init() {
        try {
            mExtractor = new MediaExtractor();
            mExtractor.setDataSource(path);

            for (int i = 0; i < mExtractor.getTrackCount(); i++) {
                MediaFormat format = mExtractor.getTrackFormat(i);

                String mime = format.getString(MediaFormat.KEY_MIME);
                UIHelper.showLog(TAG, "mime : " + mime);
                if (mime.startsWith("video/")) {
                    mExtractor.selectTrack(i);
                    mDecoder = MediaCodec.createDecoderByType(mime);
                    try {
                        UIHelper.showLog(TAG, "format : " + format);
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
                        if (rotation == 90 || rotation == 270) {
                            format.setInteger(MediaFormat.KEY_WIDTH, height);
                            format.setInteger(MediaFormat.KEY_HEIGHT, width);
                        }
                        videoFormat = format;
                        mDecoder.configure(videoFormat, surface, null, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        UIHelper.showLog(TAG, "codec '" + mime + "' failed configuration. " + e);
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            UIHelper.showLog(TAG, "start " + (UIHelper.timeLongHaoMiaoToString(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss")));
            isEnable = true;
            mDecoder.start();
            doWork();
        } catch (Exception e) {
            e.printStackTrace();
            UIHelper.showLog(TAG, "Exception " + e);
            if (Build.VERSION.SDK_INT >= 21) {
                mDecoder.reset();
            } else {
                mDecoder.stop();
            }
            mDecoder.configure(videoFormat, surface, null, 0);
            mDecoder.start();
            doWork();
        }
    }

    public void stop() {
        UIHelper.showLog(TAG, "stop " + (UIHelper.timeLongHaoMiaoToString(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss")));
        isEnable = false;
//        mDecoder.stop();
        release();
    }

    public void release() {
        mDecoder.stop();
        mDecoder.release();
        mExtractor.release();
    }

    private void doWork() {
//        if (bufferInfo == null) {
//            bufferInfo = new MediaCodec.BufferInfo();
//            inputBuffers = mDecoder.getInputBuffers();
//        }
        bufferInfo = new MediaCodec.BufferInfo();
        inputBuffers = mDecoder.getInputBuffers();
        outputBuffers = mDecoder.getOutputBuffers();

        boolean isInput = true;
        boolean first = false;
        long startWhen = 0;


//        List<Integer> inputIndexList = new ArrayList<>();
//        while (isInput) {
//            int inputIndex = mDecoder.dequeueInputBuffer(10000);
//            UIHelper.showLog(TAG, "input inputIndex:" + inputIndex);
//            if (inputIndex >= 0) {
//                ByteBuffer inputBuffer = inputBuffers[inputIndex];
//                int sampleSize = mExtractor.readSampleData(inputBuffer, 0);
//                if (mExtractor.advance() && sampleSize > 0) {
//                    inputIndexList.add(inputIndex);
//                } else {
//                    UIHelper.showLog(TAG, "InputBuffer BUFFER_FLAG_END_OF_STREAM");
//                    isInput = false;
//                }
//            }
//        }
//
//        for (int i = 0; i < inputIndexList.size(); i++) {
//            Integer inputIndex = inputIndexList.get(i);
//            ByteBuffer inputBuffer = inputBuffers[inputIndex];
//            int sampleSize = mExtractor.readSampleData(inputBuffer, 0);
//            mDecoder.queueInputBuffer(inputIndex, 0, sampleSize, mExtractor.getSampleTime(), 0);
//
//            int outIndex = mDecoder.dequeueOutputBuffer(bufferInfo, 10000);
//            UIHelper.showLog(TAG, "output outIndex:" + outIndex);
//            if (outIndex > 0) {
//                if (!first) {
//                    startWhen = System.currentTimeMillis();
//                    first = true;
//                }
//                try {
//                    long sleepTime = (bufferInfo.presentationTimeUs / 1000) - (System.currentTimeMillis() - startWhen);
//                    if (sleepTime > 0)
//                        Thread.sleep(sleepTime);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                mDecoder.releaseOutputBuffer(outIndex, true);
//            }
//        }

        //flush
//        mDecoder.queueInputBuffer(inputIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_CODEC_CONFIG);

        while (isEnable) {
            UIHelper.showLog(TAG, "doWork ing inputBuffers.length=" + inputBuffers.length + ",outputBuffers.length=" + outputBuffers.length + ",bufferInfo.size=" + bufferInfo.size);
            if (isInput) {
                int inputIndex = mDecoder.dequeueInputBuffer(10000);
                UIHelper.showLog(TAG, "input inputIndex:" + inputIndex);
                if (inputIndex >= 0) {
                    ByteBuffer inputBuffer = inputBuffers[inputIndex];

                    int sampleSize = mExtractor.readSampleData(inputBuffer, 0);

                    if (mExtractor.advance() && sampleSize > 0) {
                        mDecoder.queueInputBuffer(inputIndex, 0, sampleSize, mExtractor.getSampleTime(), 0);
                    } else {
                        UIHelper.showLog(TAG, "InputBuffer BUFFER_FLAG_END_OF_STREAM");
                        mDecoder.queueInputBuffer(inputIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                        isInput = false;
                    }
                }
            }

            int outIndex = mDecoder.dequeueOutputBuffer(bufferInfo, 10000);
            UIHelper.showLog(TAG, "output outIndex:" + outIndex);
            switch (outIndex) {
                case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                    UIHelper.showLog(TAG, "INFO_OUTPUT_BUFFERS_CHANGED");
                    outputBuffers = mDecoder.getOutputBuffers();
                    break;

                case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                    mDecoder.setVideoScalingMode(MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                    UIHelper.showLog(TAG, "INFO_OUTPUT_FORMAT_CHANGED format : " + mDecoder.getOutputFormat());
                    break;

                case MediaCodec.INFO_TRY_AGAIN_LATER:
//				UIHelper.showLog(TAG, "INFO_TRY_AGAIN_LATER");
                    break;

                default:
                    if (!first) {
                        startWhen = System.currentTimeMillis();
                        first = true;
                    }
                    try {
                        long sleepTime = (bufferInfo.presentationTimeUs / 1000) - (System.currentTimeMillis() - startWhen);
//                        UIHelper.showLog(TAG, "bufferInfo.presentationTimeUs : " + (bufferInfo.presentationTimeUs / 1000) + " playTime: " + (System.currentTimeMillis() - startWhen) + " sleepTime : " + sleepTime);

                        if (sleepTime > 0)
                            Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    mDecoder.releaseOutputBuffer(outIndex, true /* Surface init */);
                    break;
            }

            // All decoded frames have been rendered, we can stop playing now
            if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                UIHelper.showLog(TAG, "OutputBuffer BUFFER_FLAG_END_OF_STREAM");

                mDecoder.flush();
                break;
            }
        }


        UIHelper.showLog(TAG, "doWork end inputBuffers.length=" + inputBuffers.length + ",bufferInfo.size=" + bufferInfo.size);

//        if(isEnable){
//            mDecoder.flush();
//            doWork();
//        }else{
//            mDecoder.release();
//            mExtractor.release();
//        }


//        if(isEnable){
//            mDecoder.flush();
//            doWork();
//            start();
//        }else{
//            mDecoder.release();
//            mExtractor.release();
//        }
    }

//    private void read(){
//        int inputIndex = mDecoder.dequeueInputBuffer(10000);
//        UIHelper.showLog(TAG, "input inputIndex:" + inputIndex);
//        if (inputIndex >= 0) {
//            ByteBuffer inputBuffer = inputBuffers[inputIndex];
//
//            int sampleSize = mExtractor.readSampleData(inputBuffer, 0);
//
//            if (mExtractor.advance() && sampleSize > 0) {
//                mDecoder.queueInputBuffer(inputIndex, 0, sampleSize, mExtractor.getSampleTime(), 0);
//            } else {
//                UIHelper.showLog(TAG, "InputBuffer BUFFER_FLAG_END_OF_STREAM");
//                mDecoder.queueInputBuffer(inputIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
//                isInput = false;
//            }
//        }
//    }
}
