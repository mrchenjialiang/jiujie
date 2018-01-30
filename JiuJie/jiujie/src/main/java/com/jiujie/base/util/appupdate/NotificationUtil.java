package com.jiujie.base.util.appupdate;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.jiujie.base.R;


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

    @SuppressWarnings("deprecation")

    public static void showNotification(Context context, int drawableId,
                                        String firstContent, Intent actionIntent, String showTitle,
                                        String showContent, int notifyId) {
        //获取NotificationManager实例
        NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //实例化NotificationCompat.Builde并设置相关属性
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                //设置小图标
                .setSmallIcon(drawableId)
                //设置通知标题
                .setContentTitle(showTitle)
                //设置通知内容
                .setContentText(showContent)
                .setTicker(firstContent)
                //设置事件
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(context, 0,
                        actionIntent, PendingIntent.FLAG_CANCEL_CURRENT));
        //设置通知时间，默认为系统发出通知的时间，通常不用设置
        //.setWhen(System.currentTimeMillis());
        //通过builder.build()方法生成Notification对象,并发送通知,id=1
        notifyManager.notify(notifyId, builder.build());


//        showNotification16(context, drawableId, firstContent, actionIntent, showTitle, showContent, notifyId);

//		if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN){
//		}else{
//			showNotification11(activity,drawableId,firstContent,actionIntent,showTitle,showContent,notifyId);
//		}
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

//	@TargetApi(11)
//	private static void showNotification11(Context activity, int drawableId,
//										String firstContent, Intent actionIntent, String showTitle,
//										String showContent,int notifyId) {
//		NotificationManager mNotificationManager = (NotificationManager) activity
//				.getSystemService(Context.NOTIFICATION_SERVICE);
//		Notification notification = new Notification(drawableId, // 通知图片
//				firstContent, System.currentTimeMillis());
//
//		notification.flags = Notification.FLAG_AUTO_CANCEL;
//
//		actionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		PendingIntent contentIntent = PendingIntent.getActivity(activity, 0,
//				actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//		//过时
//		notification.setLatestEventInfo(activity, showTitle, showContent,contentIntent);
//		mNotificationManager.notify(notifyId, notification);
//	}
}
