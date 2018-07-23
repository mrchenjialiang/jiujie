package com.jiujie.jiujie.ui.activity.mediacodec;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;

import com.jiujie.base.util.UIHelper;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by ChenJiaLiang on 2018/6/27.
 * Email:576507648@qq.com
 */

public class VideoAudioThread extends Thread {

    private String TAG = "VideoAudioThread";
    private final String path;
    private final int trackIndex;
    private final String mime;
    private MediaExtractor extractor;
    private MediaCodec audioDecoder;
    private AudioTrack audioTrack;

    VideoAudioThread(String path, int trackIndex, String mime) {
        this.path = path;
        this.trackIndex = trackIndex;
        this.mime = mime;
    }

    @Override
    public void run() {
        try {
            play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void play() throws IOException {
        extractor = new MediaExtractor();
        extractor.setDataSource(path);
        MediaFormat format = extractor.getTrackFormat(trackIndex);

        extractor.selectTrack(trackIndex);

//            ByteBuffer csd = format.getByteBuffer("csd-0");
//            for (int k = 0; k < csd.capacity(); ++k) {
//                Log.e("TAG", "csd : " + csd.array()[k]);
//            }

        int mSampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
//            int channel = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
//            MediaFormat format = makeAACCodecSpecificData(MediaCodecInfo.CodecProfileLevel.AACObjectLC, mSampleRate, channel);

        audioDecoder = MediaCodec.createDecoderByType(mime);
        audioDecoder.configure(format, null, null, 0);
        audioDecoder.start();

        ByteBuffer[] inputBuffers = audioDecoder.getInputBuffers();
        ByteBuffer[] outputBuffers = audioDecoder.getOutputBuffers();

        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();

        int bufferSize = AudioTrack.getMinBufferSize(mSampleRate, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, mSampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize,
                AudioTrack.MODE_STREAM);
        audioTrack.play();

        boolean eosReceived = false;
        long TIMEOUT_US = 1000;
        while (!eosReceived) {
            int inIndex = audioDecoder.dequeueInputBuffer(TIMEOUT_US);
            if (inIndex >= 0) {
                ByteBuffer buffer = inputBuffers[inIndex];
                int sampleSize = extractor.readSampleData(buffer, 0);
                if (sampleSize < 0) {
                    // We shouldn't stop the playback at this point, just pass the EOS
                    // flag to mDecoder, we will get it again from the
                    // dequeueOutputBuffer
                    UIHelper.showLog(TAG, "读取结束");
                    audioDecoder.queueInputBuffer(inIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);

                } else {
                    audioDecoder.queueInputBuffer(inIndex, 0, sampleSize, extractor.getSampleTime(), 0);
                    extractor.advance();
                }

                int outIndex = audioDecoder.dequeueOutputBuffer(info, TIMEOUT_US);
                switch (outIndex) {
                    case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                        UIHelper.showLog(TAG, "播放开始");
                        outputBuffers = audioDecoder.getOutputBuffers();
                        break;

                    case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                        MediaFormat formatNew = audioDecoder.getOutputFormat();
                        UIHelper.showLog(TAG, "New format " + formatNew);
                        audioTrack.setPlaybackRate(formatNew.getInteger(MediaFormat.KEY_SAMPLE_RATE));
                        break;
                    case MediaCodec.INFO_TRY_AGAIN_LATER:
                        UIHelper.showLog(TAG, "dequeueOutputBuffer timed out!");
                        break;

                    default:
                        ByteBuffer outBuffer = outputBuffers[outIndex];
//                        UIHelper.showLog(TAG, "We can't use this buffer but render it due to the API limit, " + outBuffer);

                        final byte[] chunk = new byte[info.size];
                        outBuffer.get(chunk); // Read the buffer all at once
                        outBuffer.clear(); // ** MUST DO!!! OTHERWISE THE NEXT TIME YOU GET THIS SAME BUFFER BAD THINGS WILL HAPPEN

                        audioTrack.write(chunk, info.offset, info.offset + info.size); // AudioTrack write data
                        audioDecoder.releaseOutputBuffer(outIndex, false);
                        break;
                }

                // All decoded frames have been rendered, we can stop playing now
                if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    UIHelper.showLog(TAG, "播放结束");
                    break;
                }
            }
        }
    }

    public void doRelease() {
        interrupt();
//        if (audioDecoder != null) {
//            audioTrack.stop();
//            audioTrack.release();
//            audioTrack = null;
//        }
//        if (audioDecoder != null) {
//            audioDecoder.stop();
//            audioDecoder.release();
//        }
//        if (extractor != null) {
//            extractor.release();
//        }
    }
}
