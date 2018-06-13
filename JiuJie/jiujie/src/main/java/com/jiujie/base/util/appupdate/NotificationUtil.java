package com.jiujie.base.util.appupdate;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.jiujie.base.R;
import com.jiujie.base.util.UIHelper;


public class NotificationUtil {
    private NotificationManager notificationManager = null;
    private Notification notification = null;
    private RemoteViews remoteViews = null;
    private Activity activity;
    private int id;

    public NotificationUtil(Activity activity, int id) {
        this.activity = activity;
        this.id = id;
    }

    /**
     * 创建通知
     */
    public void createAndSetPrepare() {
        notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

        remoteViews = new RemoteViews(activity.getPackageName(), R.layout.notification_update);
        remoteViews.setViewVisibility(R.id.notify_message_line, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.notify_loading_line, View.GONE);
        remoteViews.setTextViewText(R.id.notify_message_text, "准备下载中...");

        notification = new Notification.Builder(activity)
                .setSmallIcon(R.drawable.logo)
                .setTicker(activity.getResources().getString(R.string.app_name) + "下载更新")   // 状态栏上显示
                .setAutoCancel(true)
//                .setOngoing(true)
                .setContent(remoteViews)
                .build();

        notificationManager.notify(id, notification);
    }

    /**
     * 刚开始下载
     */
    public void setFail() {
        if (notificationManager != null) {
            remoteViews.setViewVisibility(R.id.notify_message_line, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.notify_loading_line, View.GONE);
            remoteViews.setTextViewText(R.id.notify_message_text, "下载失败");

            notification.contentView = remoteViews;
            notificationManager.notify(id, notification);
        }
    }

    /**
     * 刚开始下载
     */
    public void setStart(String totalStr) {
        if (notificationManager != null) {
            remoteViews.setViewVisibility(R.id.notify_message_line, View.GONE);
            remoteViews.setViewVisibility(R.id.notify_loading_line, View.VISIBLE);
            remoteViews.setImageViewResource(R.id.notify_image, R.drawable.logo);
            remoteViews.setTextViewText(R.id.notify_progress_current, "0KB");
            remoteViews.setTextViewText(R.id.notify_progress_total, totalStr);
            remoteViews.setProgressBar(R.id.notify_progressBar, 100, 0, false);

            notification.contentView = remoteViews;
            notificationManager.notify(id, notification);
        }
    }

    /**
     * 下载中
     *
     * @param progressStr 已下载量 5M
     * @param progressNum 进度百分比数 50
     */
    public void setLoading(String progressStr, int progressNum) {
        if (notificationManager != null) {
            remoteViews.setViewVisibility(R.id.notify_message_line, View.GONE);
            remoteViews.setViewVisibility(R.id.notify_loading_line, View.VISIBLE);
            remoteViews.setTextViewText(R.id.notify_progress_current, progressStr);
            remoteViews.setProgressBar(R.id.notify_progressBar, 100, progressNum, false);

            notification.contentView = remoteViews;
            notificationManager.notify(id, notification);
        }
    }

    /**
     * 清除通知
     */
    public void clearNotification() {
        if (notificationManager != null) {
            notificationManager.cancel(id);
            notificationManager = null;
        }
    }

    public static void showNotification(Context context, int drawableId,
                                        String firstContent, Intent actionIntent, String showTitle,
                                        String showContent, int notifyId) {
        if (TextUtils.isEmpty(firstContent)) {
            firstContent = showTitle;
        }
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        if (mNotificationManager == null) {
            UIHelper.showLog("showNotification16 mNotificationManager==null");
            return;
        }
        actionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(
                context,
                0,
                actionIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(showTitle)
                .setContentText(showContent)
                .setSmallIcon(drawableId)
                .setContentIntent(contentIntent)
//                .setVibrate(new long[]{0, 1000, 1000, 1000})
                .setTicker(firstContent)   // 状态栏上显示
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= 26) {
            String channelID = context.getPackageName() + notifyId;
            String channelName = "消息通知";
            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            //创建通知时指定channelID
            builder.setChannelId(channelID);
        }

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
        mNotificationManager.notify(notifyId, notification);
    }

    @TargetApi(16)
    private static void showNotification16(Context context, int drawableId,
                                           String firstContent, Intent actionIntent, String showTitle,
                                           String showContent, int notifyId) {
        if (TextUtils.isEmpty(firstContent)) {
            firstContent = showTitle;
        }
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        if (mNotificationManager == null) return;
        actionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                actionIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//		FLAG_ONE_SHOT 表示返回的PendingIntent仅能执行一次，执行完后自动取消
//		FLAG_NO_CREATE 表示如果描述的PendingIntent不存在，并不创建相应的PendingIntent，而是返回NULL
//		FLAG_CANCEL_CURRENT 表示相应的PendingIntent已经存在，则取消前者，然后创建新的PendingIntent
//		FLAG_UPDATE_CURRENT 表示更新的PendingIntent


//		Notification.BigTextStyle textStyle = new Notification.BigTextStyle();
//		textStyle.setBigContentTitle("大标题")
//				// 标题
//				.setSummaryText("SummaryText")
//				.bigText(
//						"Big Text!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
//								+ "!!!!!!!!!!!"
//								+ "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");// 内容


        Notification notification = new Notification.Builder(context)
                .setContentTitle(showTitle)
                .setContentText(showContent)
                .setSmallIcon(drawableId)
                .setContentIntent(contentIntent)
                .setTicker(firstContent)   // 状态栏上显示
                .setAutoCancel(true)
//				.setStyle(textStyle) //4.1以后才有效 ，5.0测试一下，没啥效果。。。
                .build();

//		Notification notification = new Notification.Builder(activity)
//				.setContentTitle(showTitle)
//				.setContentText(showContent)
//				.setSmallIcon(drawableId)
//				.setContentIntent(contentIntent)
//				.setProgress(100,10,false)//indeterminate:表示进度是否不确定
//				.setVibrate(new long[]{0, 300, 500, 700})//震动 50, 300, 50, 200
//				.setLights(0xff0000ff, 300, 0)//呼吸灯
//				.setDefaults(Notification.DEFAULT_SOUND)//设置默认铃声
//				.setDefaults(Notification.DEFAULT_LIGHTS)//设置默认呼吸灯
//				.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))//自定义铃声
//				.setTicker(firstContent)   // 状态栏上显示
//				.setAutoCancel(true)
////				.setStyle(textStyle) //4.1以后才有效 ，5.0测试一下，没啥效果。。。
//				.build();

//		.setOngoing( true) true表示正在进行，不可被侧滑删除

//		notification.flags = Notification.FLAG_AUTO_CANCEL;

        mNotificationManager.notify(notifyId, notification);
    }
}
