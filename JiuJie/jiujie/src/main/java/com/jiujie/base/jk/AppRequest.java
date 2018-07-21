package com.jiujie.base.jk;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by ChenJiaLiang on 2018/6/20.
 * Email:576507648@qq.com
 */

public interface AppRequest {
    void onActivityCreateBeforeSuper(Activity activity);

    void onActivityRestart(Activity activity);

    void onActivityResume(Activity activity, String pageName);

    void onActivityPause(Activity activity, String pageName);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onActivityDestroy(Activity activity);

    void onFragmentResume(String pageName);

    void onFragmentPause(String pageName);

    void onFragmentResult(int requestCode, int resultCode, Intent data);
}
