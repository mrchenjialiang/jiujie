package com.jiujie.base.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.jiujie.base.R;
import com.jiujie.base.jk.LoadStatus;

public abstract class BaseMostFragment extends Fragment implements LoadStatus {

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        FragmentActivity activity = getActivity();
        if(activity==null)return;
        activity.overridePendingTransition(R.anim.slide_right_in, R.anim.no_anim);
    }

    public String getClassName(){
        return this.getClass().getName();
    }

    public String getPageName(){
        return this.getClass().getSimpleName();
    }

}
