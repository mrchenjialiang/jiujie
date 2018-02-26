package com.jiujie.base.util.file;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;

import com.jiujie.base.APP;
import com.jiujie.base.jk.DownloadFileListen;
import com.jiujie.base.jk.OnListener;
import com.jiujie.base.util.PermissionsManager;
import com.jiujie.base.util.UIHelper;

import java.io.File;


public class SystemDownloadUtil {
    //下载器
    private DownloadManager downloadManager;
    //下载的ID
    private long downloadId;

    private final String url;
    private final String saveDir;
    private final String saveName;
    private final DownloadFileListen downloadListen;
    private int totalLength;

    public SystemDownloadUtil(String url, String saveDir, String saveName, DownloadFileListen downloadListen) {
        this.url = url;
        File file = new File(saveDir, saveName);
        if (file.exists()) {
            file.delete();
        }
        String sdRootPath = Environment.getExternalStorageDirectory() + "/";
        if (saveDir.startsWith(sdRootPath)) {
            saveDir = saveDir.substring(sdRootPath.length());
        }
        this.saveDir = saveDir;
        this.saveName = saveName;
        this.downloadListen = downloadListen;
    }

    //下载apk
    public void start() {
        PermissionsManager.getPermissionSimple(new OnListener<Boolean>() {
            @Override
            public void onListen(Boolean isHasPermission) {
                if (isHasPermission) {
                    realStart();
                } else {
                    if (downloadListen != null) downloadListen.onFail("没有读写权限，请在权限管理中给予");
                }
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }


    private void realStart() {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(saveDir) || TextUtils.isEmpty(saveName)) {
            if (downloadListen != null) downloadListen.onFail("无相关下载资源");
            return;
        }
        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //移动网络情况下是否允许漫游
        request.setAllowedOverRoaming(false);

        //在通知栏中显示，默认就是显示的
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
//        request.setTitle("新版本Apk");
//        request.setDescription("Apk Downloading");

      /*
    <!--注：当设置不显示Notification时，必须要以下权限，否则会崩，DownloadManager下载时不显示Notification-->
    <uses-permission android:name="android.permission.DOWNLOAD_WITHOUT_NOTIFICATION" />*/
        /*设置通知栏是否可见  VISIBILITY_HIDDEN  VISIBILITY_VISIBLE*/
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);

        /*如果我们希望下载的文件可以被系统的Downloads应用扫描到并管理，
         我们需要调用Request对象的setVisibleInDownloadsUi方法，传递参数true.*/
        request.setVisibleInDownloadsUi(false);

        //设置下载的路径
        request.setDestinationInExternalPublicDir(saveDir, saveName);

        //获取DownloadManager
        downloadManager = (DownloadManager) APP.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        //将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
        if (downloadManager != null) {
            downloadId = downloadManager.enqueue(request);


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    boolean isEnd = checkLoadingStatus();
                    if (!isEnd) {
                        handler.postDelayed(this, 200);
                    } else {
                        handler.removeCallbacks(this);
                    }
                }
            }, 200);
        } else {
            if (downloadListen != null) downloadListen.onFail("下载失败");
        }
    }


    private boolean isStarted;

    //检查下载状态
    private boolean checkLoadingStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        //通过下载的id查找
        query.setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                //下载暂停
                case DownloadManager.STATUS_PAUSED:
//                    if(downloadListen!=null)downloadListen.onPause("下载暂停");
                    break;
                //下载延迟
                case DownloadManager.STATUS_PENDING:
                    break;
                //正在下载
                case DownloadManager.STATUS_RUNNING:
                    //已经下载文件大小
                    int loadedLength = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    if (!isStarted || totalLength <= 0) {
                        //下载文件的总大小
                        totalLength = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        UIHelper.showLog("totalLength:" + totalLength);
                        isStarted = true;
                        if (downloadListen != null) downloadListen.onStart(totalLength);
                    } else {
                        int progress = (int) (loadedLength * 1.0f / totalLength * 100);
                        if (downloadListen != null)
                            downloadListen.onLoading(loadedLength, progress);
                    }
                    break;
                //下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    String filePath = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                    if (downloadListen != null) downloadListen.onFinish(filePath);
                    UIHelper.showLog("filePath:" + filePath);
                    c.close();
                    return true;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    if (downloadListen != null) downloadListen.onFail("下载失败");
                    c.close();
                    return true;
            }
        }
        c.close();
        return false;
    }
}
