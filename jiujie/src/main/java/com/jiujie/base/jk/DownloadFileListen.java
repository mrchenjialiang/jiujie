package com.jiujie.base.jk;

/**
 * call on background thread
 * @author : Created by ChenJiaLiang on 2017/3/31.
 *         Email : 576507648@qq.com
 */
public interface DownloadFileListen {
    void onPrepare();
    void onStart(long total);
    void onFinish(String filePath);
    void onFail(String error);
    void onLoading(long loadedLength, int progress);
}
