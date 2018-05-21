package com.jiujie.jiujie.video;

import android.media.MediaPlayer;
import android.view.ViewGroup;

/**
 * Created by ChenJiaLiang on 2018/5/8.
 * Email:576507648@qq.com
 */

public interface VideoController {
    void setOnVideoStatusListener(OnVideoStatusListener onVideoStatusListener);
    void setOnVideoPrepareListener(OnVideoPrepareListener onVideoPrepareListener);
    void doPrepare(String url);
    void doStart();
    void doPause();
    void doSeekTo(int position);
    void doReStart();
    void doRelease();
    boolean isPlaying();
    void setVoice(float volume);
    MediaPlayer getMediaPlayer();
}
