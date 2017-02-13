package com.jiujie.base.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.jiujie.base.dialog.BaseDialog;
import com.jiujie.base.dialog.EnsureDialog;
import com.jiujie.base.jk.MyHandlerInterface;
import com.jiujie.base.jk.OnBaseDialogClickListener;
import com.jiujie.base.jk.UpdateListen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateManager implements MyHandlerInterface {
	/* 开始下载 */
	public static final int START_DOWN = 0;
	/* 下载中 */
	private static final int DOWNLOADING = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	private final String content;
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数量 */
	private int progress;
	private int total;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;
	
	private Activity mActivity;
	/* 更新进度条 */
//	private ProgressBar mProgress;
//	private Dialog mDownloadDialog;
	private String appName;
	private String appUrl;
	private NotificationUtil notificationUtil;
	private long lastUpdateTime;
	private static final long IntervalTime = 200;
	private boolean isUpdating;
	private MyHandler mHandler;
	private UpdateListen updateListen;
	public static int UpdateAppRequestCode = 125;

	public void cancelUpdate(){
		if(isUpdating)
		cancelUpdate = true;
	}

	public boolean isUpdating() {
		return isUpdating;
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case START_DOWN:
				isUpdating = true;
				notificationUtil = new NotificationUtil(mActivity, 110);
				notificationUtil.createRecordingNotification(UIHelper.getFormatSize(total));
				break;
			case DOWNLOADING:
				//频率太快了会死的
				long currentTime = System.currentTimeMillis();
				if(currentTime - lastUpdateTime > IntervalTime){
					lastUpdateTime = currentTime;
					int num = progress*100/total;
					String progressStr = UIHelper.getFormatSize(progress);
					notificationUtil.updateRecordingNotification(progressStr, num);
				}
				break;
			case DOWNLOAD_FINISH:
				isUpdating = false;
				// 安装文件
				installApk();
				break;
			default:
				break;
		}
	}

	public UpdateManager(Activity activity, String appName, String appUrl, String content) {
		this.mActivity = activity;
		this.appName = appName+".apk";
		this.appUrl = appUrl;
		if(TextUtils.isEmpty(content))content = "检测到有更新版本，建议马上进行更新";
		this.content = content;
		mHandler = new MyHandler(this);

		mSavePath = Environment.getExternalStorageDirectory() + "/";
		String packageName = activity.getPackageName();
		String[] split = packageName.split("\\.");
		if(split.length>1){
			for (String str : split){
				if(!str.equals("com"))
					mSavePath+=(str+"/");
			}
		}else{
			mSavePath+="jiujie/";
		}
		mSavePath+="res/apk/";
	}
	
	/**
	 * 显示更新对话框
	 */
	public void showUpdateDialog() {
		// 构造对话框
		EnsureDialog ensureDialog = new EnsureDialog(mActivity);
		ensureDialog.create()
				.setText(content)
				.setBtnLeft("下次再说", new OnBaseDialogClickListener() {
					@Override
					public void onClick(BaseDialog dialog, View v) {
						dialog.dismiss();
					}
				})
				.setBtnRight("立即升级", new OnBaseDialogClickListener() {
					@Override
					public void onClick(BaseDialog dialog, View v) {
						dialog.dismiss();
						// 显示下载对话框
						downloadApk();
					}
				})
				.show();

//		Builder builder = new Builder(mActivity);
//		builder.setTitle("软件更新");
//		builder.setMessage("检测到有更新版本，建议马上进行更新");
//		// 更新
//		builder.setPositiveButton("立即升级",
//				new OnBaseDialogClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				// 显示下载对话框
//				downloadApk();
//			}
//		});
//		// 稍后更新
//		builder.setNegativeButton("下次再说",
//				new OnBaseDialogClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
//		Dialog noticeDialog = builder.create();
//		noticeDialog.show();
	}

	/**
	 * 显示必须更新对话框
	 */
	public void showMustUpdateDialog(UpdateListen updateListen1) {
		this.updateListen = updateListen1;
		// 构造对话框
		EnsureDialog ensureDialog = new EnsureDialog(mActivity);
		ensureDialog.create()
				.setText(content)
				.setBtnLeft("下次再说", new OnBaseDialogClickListener() {
					@Override
					public void onClick(BaseDialog dialog, View v) {
						dialog.dismiss();
						updateListen.cancel();
					}
				})
				.setBtnRight("立即升级", new OnBaseDialogClickListener() {
					@Override
					public void onClick(BaseDialog dialog, View v) {
						dialog.dismiss();
						Dialog waitingDialog = UIHelper.getWaitingDialog(mActivity);
						waitingDialog.setCanceledOnTouchOutside(false);
						waitingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
							@Override
							public void onDismiss(DialogInterface dialog) {
								updateListen.cancel();
							}
						});
						waitingDialog.show();
						// 显示下载对话框
						downloadApk();
					}
				});
		ensureDialog.getDialog().setCanceledOnTouchOutside(false);
		ensureDialog.show();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		UIHelper.showLog("UpdateManager onActivityResult requestCode:"+requestCode);
		UIHelper.showLog("UpdateManager onActivityResult resultCode:"+resultCode);
		UIHelper.showLog("UpdateManager onActivityResult data:" + data);
		if(requestCode==UpdateAppRequestCode){
			if(resultCode!=Activity.RESULT_OK){
				//下载了，却取消安装或者安装失败
				UIHelper.showToastShort(mActivity,"安装失败，请先卸载然后再安装");
				if(updateListen!=null)updateListen.cancel();
			}
		}
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk() {
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 */
	private class downloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					URL url = new URL(appUrl);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					// 获取文件大小
					total = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();
					
					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists()) {
						boolean isCreateFile = file.mkdir();
						if(!isCreateFile){
							return;
						}
					}
					File apkFile = new File(mSavePath, appName);
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					mHandler.sendEmptyMessage(START_DOWN);
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = count;
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOADING);
						if (numread <= 0) {
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 取消下载对话框显示
//			mDownloadDialog.dismiss();
		}
	}
	
	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkFile = new File(mSavePath, appName);
		if (!apkFile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkFile.toString()),
				"application/vnd.android.package-archive");
//		mActivity.startActivity(i);
		mActivity.startActivityForResult(i, UpdateAppRequestCode);
		notificationUtil.clearRecordingNotification();
	}
}
