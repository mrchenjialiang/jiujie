package com.jiujie.jiujie.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.jiujie.base.util.UIHelper;

public class JiuJieKeepService extends Service{

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stopService(new Intent(this,JiuJieKeepService.class));
		return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //非正常销毁，不会调用该生命周期
        UIHelper.showLog(this,"服务销毁");
    }

}