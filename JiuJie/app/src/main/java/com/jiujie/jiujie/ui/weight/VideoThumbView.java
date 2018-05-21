package com.jiujie.jiujie.ui.weight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ChenJiaLiang on 2018/5/21.
 * Email:576507648@qq.com
 */

public class VideoThumbView extends View{

    private ArrayList<Bitmap> thumbnailList = new ArrayList<>();
    private Paint mPaint;
    private int currentIndex;
    private long totalLength;
    private boolean isStart;
    private Bitmap bitmap;
    private long lastDrawTime;
    private long lastGetBitmapTime;
    private Timer timer;
    private TimerTask timerTask;

    public VideoThumbView(Context context) {
        super(context);
    }

    public VideoThumbView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoThumbView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setVideoPath(final String videoPath){
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(getContext(), Uri.parse(videoPath));
//                totalLength = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) * 1000;


                thumbnailList.add(mediaMetadataRetriever.getFrameAtTime(0));
                thumbnailList.add(mediaMetadataRetriever.getFrameAtTime(1000*1000));
                thumbnailList.add(mediaMetadataRetriever.getFrameAtTime(2000*1000));
                thumbnailList.add(mediaMetadataRetriever.getFrameAtTime(3000*1000));
                thumbnailList.add(mediaMetadataRetriever.getFrameAtTime(4000*1000));
                thumbnailList.add(mediaMetadataRetriever.getFrameAtTime(5000*1000));
                thumbnailList.add(mediaMetadataRetriever.getFrameAtTime(6000*1000));
                thumbnailList.add(mediaMetadataRetriever.getFrameAtTime(7000*1000));
                thumbnailList.add(mediaMetadataRetriever.getFrameAtTime(8000*1000));
                thumbnailList.add(mediaMetadataRetriever.getFrameAtTime(9000*1000));
                thumbnailList.add(mediaMetadataRetriever.getFrameAtTime(10000*1000));
                thumbnailList.add(mediaMetadataRetriever.getFrameAtTime(11000*1000));
                thumbnailList.add(mediaMetadataRetriever.getFrameAtTime(12000*1000));
                thumbnailList.add(mediaMetadataRetriever.getFrameAtTime(13000*1000));
                thumbnailList.add(mediaMetadataRetriever.getFrameAtTime(14000*1000));
                thumbnailList.add(mediaMetadataRetriever.getFrameAtTime(15000*1000));
                thumbnailList.add(mediaMetadataRetriever.getFrameAtTime(16000*1000));
                thumbnailList.add(mediaMetadataRetriever.getFrameAtTime(17000*1000));
                thumbnailList.add(mediaMetadataRetriever.getFrameAtTime(18000*1000));
                thumbnailList.add(mediaMetadataRetriever.getFrameAtTime(19000*1000));
                thumbnailList.add(mediaMetadataRetriever.getFrameAtTime(20000*1000));
                start();
//                totalLength = 5000;
//                for (int i = thumbnailList.size(); i < totalLength; i++) {
//                    Bitmap bitmap1 = mediaMetadataRetriever.getFrameAtTime(i*1000);
//                    Log.e("LOG","bitmap："+(bitmap!=null));
//                    if(bitmap!=null){
//                        thumbnailList.add(bitmap);
//
//                        long currentTime = System.currentTimeMillis();
//                        if(lastGetBitmapTime !=0){
//                            Log.e("LOG","获取图片间隔时间："+(currentTime - lastGetBitmapTime));
//                        }
//                        lastGetBitmapTime = currentTime;
//                    }
//                    if(!isStart&&thumbnailList.size()>100){
//                        start();
//                        break;
//                    }
//                }
            }
        }).start();
    }

    private void start() {
        timer = new Timer(true);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                isStart = true;
                if(bitmap!=null){
                    currentIndex++;
                    if(currentIndex>thumbnailList.size()-1){
                        currentIndex = 0;
                    }
                }
                bitmap = thumbnailList.get(currentIndex);
                postInvalidate();
            }
        };
        timer.schedule(timerTask,0,1000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mPaint==null){
            mPaint = new Paint();
        }
        if(bitmap==null)return;

        long currentTime = System.currentTimeMillis();
        if(lastDrawTime !=0){
            Log.e("LOG","绘制间隔时间：" + (currentTime - lastDrawTime));
        }
        lastDrawTime = currentTime;

        canvas.drawBitmap(bitmap,0,0, mPaint);
    }
}
