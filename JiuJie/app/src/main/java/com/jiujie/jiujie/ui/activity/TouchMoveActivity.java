package com.jiujie.jiujie.ui.activity;

import android.view.View;

import com.jiujie.base.activity.BaseActivity;
import com.jiujie.base.util.UIHelper;
import com.jiujie.base.widget.TouchMoveListenLayout;
import com.jiujie.jiujie.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TouchMoveActivity extends BaseActivity {

    @Bind(R.id.tm_child)
    View child;

    @Override
    public void initUI() {
        ButterKnife.bind(this);
        TouchMoveListenLayout touchMoveListenLayout = new TouchMoveListenLayout(this);
        touchMoveListenLayout.attachToActivity(mActivity);
        touchMoveListenLayout.setEnable(true, true, true, true);
        touchMoveListenLayout.setListenTouchMoveButNotMoveAndScroll(true);
        touchMoveListenLayout.setOnTouchMoveListener(new TouchMoveListenLayout.OnTouchMoveListener() {
            @Override
            public void onTouchMoveFromLeftToRight(float distance) {
                UIHelper.showLog(this,"onTouchMoveFromLeftToRight distance:"+distance);
            }

            @Override
            public void onTouchMoveFromRightToLeft(float distance) {
                UIHelper.showLog(this,"onTouchMoveFromRightToLeft distance:"+distance);
            }

            @Override
            public void onTouchMoveFromBottomToTop(float distance) {
                UIHelper.showLog(this,"onTouchMoveFromBottomToTop distance:"+distance);
            }

            @Override
            public void onTouchMoveFromTopToBottom(float distance) {
                UIHelper.showLog(this,"onTouchMoveFromTopToBottom distance:"+distance);
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_touch_move;
    }
}
