package com.jiujie.jiujie.ui.activity;

import com.jiujie.base.util.UIHelper;
import com.jiujie.jiujie.R;

import butterknife.ButterKnife;

public class CsActivity extends MyBaseActivity {

    @Override
    public void initUI() {
        ButterKnife.bind(this);

        UIHelper.clearClipBoard();
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.cs;
    }

}
