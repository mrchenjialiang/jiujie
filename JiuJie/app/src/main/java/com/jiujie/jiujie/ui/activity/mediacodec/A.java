//package com.jiujie.jiujie.ui.activity.mediacodec;
//
//import android.media.MediaCodec;
//import android.media.MediaFormat;
//import android.os.Build;
//import android.support.annotation.NonNull;
//
//import com.jiujie.base.util.UIHelper;
//
//import java.io.IOException;
//import java.nio.ByteBuffer;
//
///**
// * Created by ChenJiaLiang on 2018/7/18.
// * Email:576507648@qq.com
// */
//
//public class A {
//
//    public void show(String name) {
//        try {
//
//
//            if(Build.VERSION.SDK_INT>=21){
//                final MediaCodec codec = MediaCodec.createByCodecName(name);
//                MediaFormat mOutputFormat; // member variable
//                codec.setCallback(new MediaCodec.Callback() {
////                    @Override
////                    void onInputBufferAvailable(MediaCodec mc, int inputBufferId) {
////                        ByteBuffer inputBuffer = codec.getInputBuffer(inputBufferId);
////// fill inputBuffer with valid data
////                        codec.queueInputBuffer(inputBufferId, …);
////                    }
//
//                    @Override
//                    public void onInputBufferAvailable(@NonNull MediaCodec codec, int index) {
//                        if(Build.VERSION.SDK_INT>=21){
//                            ByteBuffer inputBuffer = codec.getInputBuffer(index);
//                            int sampleSize = mExtractor.readSampleData(inputBuffer, 0);
//                            if (mExtractor.advance() && sampleSize > 0) {
//                                codec.queueInputBuffer(index, 0, sampleSize, mExtractor.getSampleTime(), 0);
//                            } else {
//                                codec.queueInputBuffer(index, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onOutputBufferAvailable(@NonNull MediaCodec codec, int index, @NonNull MediaCodec.BufferInfo info) {
//                        if(Build.VERSION.SDK_INT>=21){
//                            ByteBuffer outputBuffer = codec.getOutputBuffer(index);
//                            MediaFormat bufferFormat = codec.getOutputFormat(index); // option A
//                            codec.releaseOutputBuffer(index,true);
//                        }
//                    }
//
//                    @Override
//                    public void onError(@NonNull MediaCodec codec, @NonNull MediaCodec.CodecException e) {
//
//                    }
//
//                    @Override
//                    public void onOutputFormatChanged(@NonNull MediaCodec codec, @NonNull MediaFormat format) {
//
//                    }
//
////                    @Override
////                    void onOutputFormatChanged(MediaCodec mc, MediaFormat format) {
////// Subsequent data will conform to new format.
////// Can ignore if using getOutputFormat(outputBufferId)
////                        mOutputFormat = format; // option B
////                    }
//                });
//                codec.configure(format, …);
//                mOutputFormat = codec.getOutputFormat(); // option B
//                codec.start();
//                // wait for processing to complete
//                codec.stop();
//                codec.release();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
