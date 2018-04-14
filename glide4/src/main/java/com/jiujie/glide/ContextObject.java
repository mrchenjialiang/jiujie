package com.jiujie.glide;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * Created by ChenJiaLiang on 2018/4/10.
 * Email:576507648@qq.com
 */
public class ContextObject {
    private View view;
    private FragmentActivity fragmentActivity;
    private Activity activity;
    private Context context;
    private Fragment fragment;
    public ContextObject(Context context) {
        this.context = context;
    }
    public ContextObject(Activity activity) {
        this.activity = activity;
        context = activity;
    }
    public ContextObject(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
        context = activity;
    }
    public ContextObject(Fragment fragment) {
        this.fragment = fragment;
        context = fragment.getContext();
    }
    public ContextObject(View view) {
        this.view = view;
        context = view.getContext();
    }
    public Context getContext() {
        return context;
    }

    public Object getRealContextObject() {
        if(fragment!=null){
            return fragment;
        }else if(fragmentActivity!=null){
            return fragmentActivity;
        }else if(activity!=null){
            return activity;
        }else if(view!=null){
            return view;
        }
        return context;
    }
}
