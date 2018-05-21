package com.jiujie.jiujie.video;

import android.media.MediaPlayer;

/**
 * Created by ChenJiaLiang on 2018/5/8.
 * Email:576507648@qq.com
 */

public interface OnVideoStatusListener {

    void onPrepare(int videoWidth, int videoHeight);

    void onStart();

    void onPause();

    void onCompleted(MediaPlayer mp);

    void onBufferListen(boolean isBuffering);

    void onError(MediaPlayer mp, String error);
}
