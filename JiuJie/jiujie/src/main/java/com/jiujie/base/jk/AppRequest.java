package com.jiujie.base.jk;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by ChenJiaLiang on 2018/6/20.
 * Email:576507648@qq.com
 */

public interface AppRequest {
    void onActivityCreateBeforeSuper(Activity activity);
    void onActivityResume(Activity mActivity, String pageName);
    void onActivityPause(Activity mActivity, String pageName);
    void onActivityResult(int requestCode, int resultCode, Intent data);
    void onFragmentResume(String pageName);
    void onFragmentPause(String pageName);
}
