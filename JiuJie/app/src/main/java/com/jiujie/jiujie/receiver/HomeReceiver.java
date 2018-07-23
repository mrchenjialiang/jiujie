package com.jiujie.jiujie.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.jiujie.base.util.UIHelper;
import com.jiujie.jiujie.ui.activity.MainActivity;

/**
 * Created by ChenJiaLiang on 2018/7/9.
 * Email:576507648@qq.com
 */

public class HomeReceiver extends BroadcastReceiver {
    static public final String SYSTEM_DIALOG_REASON_KEY = "reason";
    static public final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    static public final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        UIHelper.showLog(this, "action:" + action);
        //按下Home键会发送ACTION_CLOSE_SYSTEM_DIALOGS的广播
        if (!TextUtils.isEmpty(action) && action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            if (reason != null) {
                if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                    UIHelper.showLog(this, "按了home");

                    try {
                        for (int j = 0; j < 10; j++) {
                            Intent keepIntent = new Intent(context, MainActivity.class);
                            keepIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            PendingIntent pendingIntent =
                                    PendingIntent.getActivity(context, 0, keepIntent, 0);
                            pendingIntent.send();
                        }
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }
}
