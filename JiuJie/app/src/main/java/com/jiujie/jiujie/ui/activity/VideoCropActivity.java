package com.jiujie.jiujie.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;

import com.jiujie.jiujie.R;
import com.jiujie.jiujie.ui.weight.VideoThumbView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VideoCropActivity extends MyBaseActivity {

    @Bind(R.id.vt_videoThumbView)
    VideoThumbView videoThumbView;


        private String videoPath = Environment.getExternalStorageDirectory()+"/Download/wallpaperfairy/liveWallpaper/111.mp4";
//    private String videoPath = Environment.getExternalStorageDirectory()+"/Download/wallpaperfairy/liveWallpaper/222.mp4";//问题火影
//    private String videoPath = "http://s.3987.com/uploadfile/userload/vedio/2018/0507/20180507121253269.mp4";//竖屏视频
//    private String videoPath = "http://s.3987.com/uploadfile/video/2018/0428/20180428093338723.mp4";//横屏视频

    public static void launch(Activity activity) {
        activity.startActivity(new Intent(activity,VideoCropActivity.class));
    }

    @Override
    public void initUI() {
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        videoThumbView.setVideoPath(videoPath);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_thumb;
    }

}
