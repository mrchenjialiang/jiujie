package com.jiujie.jiujie.ui.activity.mediacodec.decoder;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * MediaCodec SurfaceHolder Example
 * @author taehwan
 *
 */
public class VideoDecoderActivity extends Activity implements SurfaceHolder.Callback {
	private VideoDecoderThread mVideoDecoder;
	
//	private String FILE_PATH = Environment.getExternalStorageDirectory()+"/question_huoying.mp4";//问题火影
    private String FILE_PATH = Environment.getExternalStorageDirectory()+"/zipai1.mp4";//问题网红
//    private String FILE_PATH = Environment.getExternalStorageDirectory()+"/video_heng.mp4";//横屏视频
//    private String FILE_PATH = Environment.getExternalStorageDirectory()+"/normal.mp4";//普通视频
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SurfaceView surfaceView = new SurfaceView(this);
		surfaceView.getHolder().addCallback(this);
		setContentView(surfaceView);
		
		mVideoDecoder = new VideoDecoderThread();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {
		if (mVideoDecoder != null) {
			if (mVideoDecoder.init(holder.getSurface(), FILE_PATH)) {
				mVideoDecoder.start();
			} else {
				mVideoDecoder = null;
			}
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mVideoDecoder != null) {
			mVideoDecoder.close();
		}
	}

}
