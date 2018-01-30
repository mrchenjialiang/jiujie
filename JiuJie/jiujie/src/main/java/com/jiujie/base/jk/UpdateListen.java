package com.jiujie.base.jk;

/**
 * @author : Created by ChenJiaLiang on 2016/12/15.
 *         Email : 576507648@qq.com
 */

public interface UpdateListen{
    void cancel();//在开始安装时调用，不用关闭对话框，因为更新了之后就是新应用打开了，原本的已经杀掉了
}