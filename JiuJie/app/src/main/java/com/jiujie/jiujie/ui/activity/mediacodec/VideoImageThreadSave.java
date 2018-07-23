//package com.jiujie.jiujie.ui.activity.mediacodec;
//
//import android.media.MediaCodec;
//import android.media.MediaExtractor;
//import android.media.MediaFormat;
//import android.view.Surface;
//
//import com.jiujie.base.util.UIHelper;
//
//import java.io.IOException;
//import java.nio.ByteBuffer;
//
///**
// * Created by ChenJiaLiang on 2018/6/27.
// * Email:576507648@qq.com
// */
//
//public class VideoImageThreadSave extends Thread {
//
//    private String TAG = "VideoImageThread";
//    private final Surface surface;
//    private final String path;
//    private final int trackIndex;
//    private final String mime;
//    private MediaCodec videoDecoder;
//    private MediaExtractor extractor;
//
////    public VideoImageThread(Surface surface, String path) {
////        this.surface = surface;
////        this.path = path;
////    }
//
//    VideoImageThreadSave(Surface surface, String path, int trackIndex, String mime) {
//        this.surface = surface;
//        this.path = path;
//        this.trackIndex = trackIndex;
//        this.mime = mime;
//    }
//
//    @Override
//    public void run() {
//        try {
//            playVideo();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void playVideo() throws IOException {
//        extractor = new MediaExtractor();
//        extractor.setDataSource(path);
//        MediaFormat format = extractor.getTrackFormat(trackIndex);
//        int rotation = 0;
//        int width = 0;
//        int height = 0;
//        if (format.containsKey("rotation-degrees")) {
//            rotation = format.getInteger("rotation-degrees");
//        }
//        if (format.containsKey("height")) {
//            height = format.getInteger("height");
//        }
//        if (format.containsKey("width")) {
//            width = format.getInteger("width");
//        }
//
////        UIHelper.showLog(TAG, "New format original " + format);
//        UIHelper.showLog(TAG, "width:" + width + "，height:" + height + "，rotation:" + rotation);
//
//        extractor.selectTrack(trackIndex);
//        videoDecoder = MediaCodec.createDecoderByType(mime);
//        videoDecoder.configure(format, surface, null, 0);
//
//        videoDecoder.start();//启动MediaCodec ，等待传入数据
//
//        ByteBuffer[] inputBuffers = videoDecoder.getInputBuffers();
//        ByteBuffer[] outputBuffers = videoDecoder.getOutputBuffers();
//        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
//        long TIMEOUT_US = 10000;
//        boolean isEOS = false;
//        long startMs = System.currentTimeMillis();
//
//        while (!Thread.interrupted()) {
//            if (!isEOS) {
////                int inIndex = videoDecoder.dequeueInputBuffer(10000);
//                int inIndex = videoDecoder.dequeueInputBuffer(TIMEOUT_US);
//                if (inIndex >= 0) {
//                    ByteBuffer buffer = inputBuffers[inIndex];
//                    int sampleSize = extractor.readSampleData(buffer, 0);
//                    if (sampleSize < 0) {
//                        // We shouldn't stop the playback at this point, just pass the EOS
//                        // flag to videoDecoder, we will get it again from the
//                        // dequeueOutputBuffer
//                        UIHelper.showLog(TAG, "读取结束");
//                        videoDecoder.queueInputBuffer(inIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
//                        isEOS = true;
//                    } else {
//                        videoDecoder.queueInputBuffer(inIndex, 0, sampleSize, extractor.getSampleTime(), 0);
//                        extractor.advance();//将进度调整到正确的帧位置
//                    }
//                }
//            }
//
//
//
////            int outIndex = videoDecoder.dequeueOutputBuffer(info, 10000);
//            int outIndex = videoDecoder.dequeueOutputBuffer(info, TIMEOUT_US);
//            UIHelper.showLog(TAG,"outIndex:"+outIndex);
//            switch (outIndex) {
//                case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
//                    UIHelper.showLog(TAG, "播放开始");
//                    break;
//                case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
//                    //每次都要设置
//                    videoDecoder.setVideoScalingMode(MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
//                    break;
//                case MediaCodec.INFO_TRY_AGAIN_LATER:
//                    break;
//                default:
//                    // We use a very simple clock to keep the video FPS, or the video
//                    // playback will be too fast
////                    while (info.presentationTimeUs / 1000 > System.currentTimeMillis() - startMs) {
////                        try {
////                            sleep(10);
////                        } catch (InterruptedException e) {
////                            e.printStackTrace();
////                            break;
////                        }
////                    }
//                    videoDecoder.releaseOutputBuffer(outIndex, true);
//                    break;
//            }
//
//
//
////            int outIndex = videoDecoder.dequeueOutputBuffer(info, 10000);
////            UIHelper.showLog(TAG,"outIndex:"+outIndex);
////            switch (outIndex) {
////                case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
////                    videoDecoder.setVideoScalingMode(MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
////                    UIHelper.showLog(TAG, "播放开始");
////                    outputBuffers = videoDecoder.getOutputBuffers();
////                    break;
////                case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
////                    //每次都要设置
////                    videoDecoder.setVideoScalingMode(MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
//////                    MediaFormat outputFormat = videoDecoder.getOutputFormat();
//////                    UIHelper.showLog(TAG, "New format " + outputFormat);
////                    break;
////                case MediaCodec.INFO_TRY_AGAIN_LATER:
//////                    UIHelper.showLog(TAG, "dequeueOutputBuffer timed out!");
////                    break;
////                default:
//////                    ByteBuffer buffer = outputBuffers[outIndex];
//////                    UIHelper.showLog(TAG, "We can't use this buffer but render it due to the API limit, " + buffer);
////
////                    // We use a very simple clock to keep the video FPS, or the video
////                    // playback will be too fast
////                    while (info.presentationTimeUs / 1000 > System.currentTimeMillis() - startMs) {
////                        try {
////                            sleep(10);
////                        } catch (InterruptedException e) {
////                            e.printStackTrace();
////                            break;
////                        }
////                    }
////                    videoDecoder.releaseOutputBuffer(outIndex, true);
////                    break;
////            }
//
//            // All decoded frames have been rendered, we can stop playing now
//            if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
//                UIHelper.showLog(TAG, "播放结束");
//                if (videoDecoder != null) {
//                    videoDecoder.stop();
//                    videoDecoder.release();
//                }
//                if (extractor != null) {
//                    extractor.release();
//                }
//                break;
//            }
//        }
//    }
//
//    public void doRelease() {
//        interrupt();
////        try {
////            sleep(10);
////            if (videoDecoder != null) {
////                videoDecoder.stop();
////                videoDecoder.release();
////            }
////            if (extractor != null) {
////                extractor.release();
////            }
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
//    }
//}
