package com.jiujie.jiujie;

import android.os.Bundle;

public class FragmentActivity extends MyBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
    }

    @Override
    public boolean isShowTitle() {
        return false;
    }

    @Override
    public void initUI() {
        getSupportFragmentManager().beginTransaction().add(R.id.f_frame,new SimpleListFragment()).commit();
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fragment;
    }
}
