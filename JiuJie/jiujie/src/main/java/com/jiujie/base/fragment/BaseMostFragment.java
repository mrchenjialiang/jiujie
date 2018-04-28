package com.jiujie.base.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.jiujie.base.APP;
import com.jiujie.base.R;
import com.jiujie.base.jk.LoadStatus;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseMostFragment extends Fragment implements LoadStatus {

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        FragmentActivity activity = getActivity();
        if(activity==null)return;
        activity.overridePendingTransition(R.anim.slide_right_in, R.anim.no_anim);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    @Override
    public void onResume() {
        super.onResume();
        if(APP.isUseUMeng){
            MobclickAgent.onPageStart(getPageName());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(APP.isUseUMeng){
            MobclickAgent.onPageEnd(getPageName());
        }
    }

    public String getClassName(){
        return this.getClass().getName();
    }

    public String getPageName(){
        return this.getClass().getSimpleName();
    }

}
