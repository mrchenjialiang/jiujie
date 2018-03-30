package com.jiujie.jiujie.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;

import com.jiujie.base.jk.MyHandlerInterface;
import com.jiujie.base.util.MyHandler;
import com.jiujie.base.util.SharePHelper;
import com.jiujie.base.util.UIHelper;

import java.util.Timer;
import java.util.TimerTask;

public class JiuJieKeepService extends Service implements MyHandlerInterface {

    private boolean isStart;
    private TimerTask task;
    private Timer timer;
    /**
     * 计数间隔时间-----------秒
     */
    private static final int intervalTime = 1000;
    /**
     * 计数多少次执行一次任务
     */
    private int doWorkOnceCount = 30;
    private MyHandler handler;
    private final int Task_Time_Second_Add = 0;
    private int TIME_PASS_COUNT = 0;
    private ScreenObserver screenObserver;

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Task_Time_Second_Add:
                TIME_PASS_COUNT++;
                if (TIME_PASS_COUNT % doWorkOnceCount==0) {
                    TIME_PASS_COUNT = 0;
                    doWork();
                }
                break;
        }
    }

    private void startAlarm(){
        Long lastStartAlarmTime = SharePHelper.instance(this).readObject("lastStartAlarmTime");
        long timeMillis = System.currentTimeMillis();
        int alarmTime = 5 * 60 * 1000;
        if(lastStartAlarmTime!=null){
            UIHelper.showLog(this,"lastStartAlarmTime:"+UIHelper.timeLongHaoMiaoToString(lastStartAlarmTime,"yyyy-MM-dd HH:mm:ss"));
            if(timeMillis - lastStartAlarmTime < alarmTime /2){
                return;
            }
        }
        SharePHelper.instance(this).saveObject("lastStartAlarmTime",timeMillis);

        Intent intent = new Intent(this, JiuJieKeepService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        if(alarmManager==null)return;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime,alarmTime,pendingIntent);
    }

    private void doWork(){
        UIHelper.showLog(this,"doWork");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        UIHelper.showLog(this,"onStartCommand isStart:"+isStart);
        if (!isStart) {
            isStart = true;
            UIHelper.showLog(this,"服务启动");
            startTime();
//            startAlarm();
            screenObserver = new ScreenObserver(this);
            screenObserver.requestScreenStateUpdate(new ScreenObserver.ScreenStateListener() {
                @Override
                public void onScreenOn() {

                }

                @Override
                public void onScreenOff() {

                }
            });
        }
//        return super.onStartCommand(intent, flags, startId);
		return START_STICKY;
    }

    private void stopTime() {
        UIHelper.showLog(this,"stopTime");
        if(task!=null){
            task.cancel();
            task = null;
        }
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
    }

    private void startTime() {
        UIHelper.showLog(this,"startTime");
        if (handler == null) {
            handler = new MyHandler(this);
        }
        stopTime();
        UIHelper.showLog(this,"startTime real");
        if (task == null || timer == null) {
            task = new TimerTask() {
                public void run() {
                    handler.sendEmptyMessage(Task_Time_Second_Add);
                }
            };
            timer = new Timer(true);
            timer.schedule(task, intervalTime, intervalTime);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTime();
        //非正常销毁，不会调用该生命周期
        UIHelper.showLog(this,"服务销毁");
        if(screenObserver!=null){
            screenObserver.stopScreenStateUpdate();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        UIHelper.showLog(this,"onLowMemory");
    }

}