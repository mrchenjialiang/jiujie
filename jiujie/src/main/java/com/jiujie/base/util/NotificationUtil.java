package com.jiujie.base.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.jiujie.base.R;

public class NotificationUtil {
	private NotificationManager notificationManager = null;
	private Notification notification = null;
	private RemoteViews contentView = null;
	private Activity activity;
	private int id;

	public NotificationUtil(Activity activity,int id) {
		this.activity = activity;
		this.id = id;
	}

	@SuppressWarnings("deprecation")
	/**
	 * 创建通知
	 */
	public void createRecordingNotification(String totalStr) {
		notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
		Resources res = activity.getResources();
		long when = System.currentTimeMillis();
		notification =new Notification(R.drawable.logo, res.getString(R.string.app_name)+"开始下载更新", when);
		notification.flags = Notification.FLAG_ONGOING_EVENT;

		contentView = new RemoteViews(activity.getPackageName(),R.layout.notification_update);
		contentView.setImageViewResource(R.id.notify_image, R.drawable.logo);
		contentView.setTextViewText(R.id.notify_progress_current,"0KB");
		contentView.setTextViewText(R.id.notify_progress_total, totalStr);
		contentView.setProgressBar(R.id.notify_progressBar, 100, 0, false);

		notification.contentView = contentView;

//		Intent actionIntent = new Intent(activity, SoundRecorder.class);
//		actionIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		
//		PendingIntent contentItent = PendingIntent.getActivity(activity, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//		
//		notification.contentIntent = contentItent;
		notificationManager.notify(id, notification);
	}

	/**
	 * 更新通知
	 * @param progress 左边显示的当前量
	 * @param progressNum  进度百分比数
	 */
	public void updateRecordingNotification(String progress,int progressNum) {
		if (notificationManager != null) {
			notification.flags = Notification.FLAG_NO_CLEAR;
			contentView.setTextViewText(R.id.notify_progress_current, progress);
			contentView.setProgressBar(R.id.notify_progressBar, 100, progressNum, false);
			notificationManager.notify(id, notification);
		}
	}

	/**
	 * 清除通知
	 */
	public void clearRecordingNotification() {
		if (notificationManager != null) {
			notificationManager.cancel(id);
			notificationManager = null;
		}
	}

	@SuppressWarnings("deprecation")

	public static void showNotification(Context context, int drawableId,
										String firstContent, Intent actionIntent, String showTitle,
										String showContent,int notifyId) {
		showNotification16(context,drawableId,firstContent,actionIntent,showTitle,showContent,notifyId);
//		if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN){
//		}else{
//			showNotification11(context,drawableId,firstContent,actionIntent,showTitle,showContent,notifyId);
//		}
	}

	@TargetApi(16)
	private static void showNotification16(Context context, int drawableId,
										   String firstContent, Intent actionIntent, String showTitle,
										   String showContent,int notifyId){
		if(TextUtils.isEmpty(firstContent)){
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

//		.setOngoing( true) true表示正在进行，不可被侧滑删除

//		notification.flags = Notification.FLAG_AUTO_CANCEL;


		mNotificationManager.notify(notifyId, notification);




	}

//	@TargetApi(11)
//	private static void showNotification11(Context context, int drawableId,
//										String firstContent, Intent actionIntent, String showTitle,
//										String showContent,int notifyId) {
//		NotificationManager mNotificationManager = (NotificationManager) context
//				.getSystemService(Context.NOTIFICATION_SERVICE);
//		Notification notification = new Notification(drawableId, // 通知图片
//				firstContent, System.currentTimeMillis());
//
//		notification.flags = Notification.FLAG_AUTO_CANCEL;
//
//		actionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
//				actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//		//过时
//		notification.setLatestEventInfo(context, showTitle, showContent,contentIntent);
//		mNotificationManager.notify(notifyId, notification);
//	}
}
