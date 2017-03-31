package com.jiujie.base.util.appupdate;

import com.jiujie.base.jk.DownloadFileListen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author : Created by ChenJiaLiang on 2017/2/24.
 *         Email : 576507648@qq.com
 */
public class DownloadFileUtil {

    private final String url;
    private final String saveDir;
    private final String saveName;
    private final DownloadFileListen downloadListen;

    public DownloadFileUtil(String url,String saveDir,String saveName,DownloadFileListen downloadListen) {
        this.url = url;
        this.saveDir = saveDir;
        this.saveName = saveName;
        this.downloadListen = downloadListen;
    }

    public void start(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(downloadListen!=null)downloadListen.onPrepare();
                Request request = new Request.Builder().url(url).build();
                // 创建okHttpClient对象
                OkHttpClient mOkHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).readTimeout(10,TimeUnit.MINUTES).build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if(downloadListen!=null)downloadListen.onFail(e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream is = null;
                        byte[] buf = new byte[1024];
                        int len;
                        FileOutputStream fos = null;
                        try {
                            is = response.body().byteStream();
                            long total = response.body().contentLength();
                            File dirFile = new File(saveDir);
                            // 判断文件目录是否存在
                            if (!dirFile.exists()) {
                                boolean isCreateFile = dirFile.mkdirs();
                                if(!isCreateFile){

                                    if(downloadListen!=null)downloadListen.onFail("文件创建失败");
                                    return;
                                }
                            }
                            File file = new File(saveDir, saveName);
                            fos = new FileOutputStream(file);
                            long sum = 0;
                            if(downloadListen!=null)downloadListen.onStart(total);
                            int oldProgress;
                            int newProgress = 0;
                            while ((len = is.read(buf)) != -1) {
                                fos.write(buf, 0, len);
                                sum += len;
                                oldProgress = newProgress;
                                newProgress = (int) (sum * 1.0f / total * 100);
                                if(oldProgress!=newProgress){
                                    if(downloadListen!=null)downloadListen.onLoading(sum,newProgress);
                                }
                            }
                            fos.flush();
                            if(downloadListen!=null)downloadListen.onFinish(saveDir+saveName);
                        } catch (Exception e) {
                            e.printStackTrace();
                            if(downloadListen!=null)downloadListen.onFail(e.getMessage());
                        } finally {
                            try {
                                if (is != null) is.close();
                                if (fos != null) fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }).start();
    }
}
