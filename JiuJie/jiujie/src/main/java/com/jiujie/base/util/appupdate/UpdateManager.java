package com.jiujie.base.util.appupdate;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.jiujie.base.dialog.BaseDialog;
import com.jiujie.base.dialog.EnsureDialog;
import com.jiujie.base.jk.DownloadFileListen;
import com.jiujie.base.jk.MyHandlerInterface;
import com.jiujie.base.jk.OnBaseDialogClickListener;
import com.jiujie.base.jk.UpdateListen;
import com.jiujie.base.util.ImageUtil;
import com.jiujie.base.util.MyHandler;
import com.jiujie.base.util.UIHelper;
import com.jiujie.base.util.UriUtil;

import java.io.File;

public class UpdateManager implements MyHandlerInterface {
	/* 准备下载 */
	private final int DOWNLOAD_PREPARE = 0;
	/* 开始下载 */
	private final int DOWNLOAD_START = 1;
	/* 下载中 */
	private final int DOWNLOAD_ING = 2;
	/* 下载结束 */
	private final int DOWNLOAD_FINISH = 3;
	/* 下载结束 */
	private final int DOWNLOAD_FAIL = 4;
	private String downLoadPageUrl;

	private String content;
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数量 */
	private long loadedLength;
	private long total;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;
	
	private Activity mActivity;
	private String appName;
	private String appUrl;
	private NotificationUtil notificationUtil;
	private long lastUpdateTime;
	private static final long IntervalTime = 500;
	private boolean isUpdating;
	private MyHandler mHandler;
	private UpdateListen updateListen;
	public static int UpdateAppRequestCode = 125;
	private int progress;

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
			case DOWNLOAD_PREPARE:
				isUpdating = true;
				notificationUtil = new NotificationUtil(mActivity, 110);
				notificationUtil.createAndSetPrepare();
				break;
			case DOWNLOAD_START:
				isUpdating = true;
				notificationUtil.setStart(UIHelper.getFormatSize(total));
				break;
			case DOWNLOAD_ING:
				//频率太快了会死的
				long currentTime = System.currentTimeMillis();
				if(currentTime - lastUpdateTime > IntervalTime){
					lastUpdateTime = currentTime;
					String progressStr = UIHelper.getFormatSize(loadedLength);
					notificationUtil.setLoading(progressStr, progress);
				}
				break;
			case DOWNLOAD_FAIL:
				notificationUtil.setFail();
				EnsureDialog ensureDialog = new EnsureDialog(mActivity);
				ensureDialog.create()
						.setText("更新下载失败，是否使用浏览器进行下载？")
						.setBtnLeft("取消", new OnBaseDialogClickListener() {
							@Override
							public void onClick(BaseDialog dialog, View v) {
								dialog.dismiss();
							}
						})
						.setBtnRight("确定", new OnBaseDialogClickListener() {
							@Override
							public void onClick(BaseDialog dialog, View v) {
								dialog.dismiss();
								Intent intent = new Intent();
								intent.setAction("android.intent.action.VIEW");
								Uri content_url = Uri.parse(TextUtils.isEmpty(downLoadPageUrl)?appUrl:downLoadPageUrl);

								intent.setData(content_url);
								mActivity.startActivity(intent);
							}
						})
						.show();
				break;
			case DOWNLOAD_FINISH:
				isUpdating = false;
				notificationUtil.clearNotification();
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
		this.content = content;
		mHandler = new MyHandler(this);

		mSavePath = ImageUtil.instance().getCacheSDDic(activity)+"res/apk/";
	}

	public UpdateManager(Activity activity, String appName, String appUrl, String content,String downLoadPageUrl) {
		this.mActivity = activity;
		this.appName = appName+".apk";
		this.appUrl = appUrl;
		this.content = content;
		this.downLoadPageUrl = downLoadPageUrl;
		mHandler = new MyHandler(this);

		mSavePath = ImageUtil.instance().getCacheSDDic(activity)+"res/apk/";
	}

	/**
	 * 显示更新对话框
	 */
	public void showUpdateDialog() {
		// 构造对话框
		EnsureDialog ensureDialog = new EnsureDialog(mActivity);
		if(TextUtils.isEmpty(content))content = "检测到有更新版本，建议马上进行更新";
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
	}

	/**
	 * 显示必须更新对话框
	 */
	public void showMustUpdateDialog(UpdateListen updateListen1) {
		this.updateListen = updateListen1;
		// 构造对话框
		EnsureDialog ensureDialog = new EnsureDialog(mActivity);
		if(TextUtils.isEmpty(content))content = "检测到：目前APP必须立即升级，请立即升级，谢谢~";
		ensureDialog.create()
				.setText(content)
				.setBtnLeft("退出APP", new OnBaseDialogClickListener() {
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
		if(requestCode==UpdateAppRequestCode){
			if(resultCode!=Activity.RESULT_OK){
				//下载了，却取消安装或者安装失败
				if(mActivity!=null){
					UIHelper.showToastLong("安装失败，请先卸载然后再安装");
					if(updateListen!=null)updateListen.cancel();
				}
			}
		}
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk(){
		new DownloadFileUtil(appUrl, mSavePath, appName, new DownloadFileListen() {
			@Override
			public void onStart(long total) {
				UpdateManager.this.total = total;
				mHandler.sendEmptyMessage(DOWNLOAD_START);
			}

			@Override
			public void onFinish(String filePath) {
				mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
			}

			@Override
			public void onPrepare() {
				mHandler.sendEmptyMessage(DOWNLOAD_PREPARE);
			}

			@Override
			public void onFail(String error) {
				UIHelper.showLog("downloadApk onFail:" + error);
				mHandler.sendEmptyMessage(DOWNLOAD_FAIL);
			}

			@Override
			public void onLoading(long loadedLength,int progress) {
				UpdateManager.this.loadedLength = loadedLength;
				UpdateManager.this.progress = progress;
				mHandler.sendEmptyMessage(DOWNLOAD_ING);
			}
		}).start();
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
//		Uri uri = Uri.parse("file://" + apkFile.toString());
		Uri uri = UriUtil.getUri(mActivity,i,apkFile);
		i.setDataAndType(uri,"application/vnd.android.package-archive");
//		mActivity.startActivity(i);
		mActivity.startActivityForResult(i, UpdateAppRequestCode);
		notificationUtil.clearNotification();
	}
}
