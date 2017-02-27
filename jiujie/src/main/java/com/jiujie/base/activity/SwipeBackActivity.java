package com.jiujie.base.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.jiujie.base.R;
import com.jiujie.base.util.UIHelper;
import com.jiujie.base.widget.SwipeBackLayout;

/**
 * 想要实现向右滑动删除Activity效果只需要继承SwipeBackActivity即可，如果当前页面含有ViewPager
 * 只需要调用SwipeBackLayout的setViewPager()方法即可
 * @author xiaanming
 *
 */
public class SwipeBackActivity extends BaseMostActivity {
	protected SwipeBackLayout swipeBackLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getIsSwipeBackDo()){
			swipeBackLayout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
					R.layout.base_swipe_back, null);
			swipeBackLayout.attachToActivity(this);
		}
	}

	public boolean getIsSwipeBackDo(){
		return true;
	}

	public void setDrawerScrollEnable(boolean isEnable){
		if(swipeBackLayout!=null)
			swipeBackLayout.setIsCanScroll(isEnable);
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		UIHelper.hidePan(this);
		overridePendingTransition(R.anim.slide_right_in, R.anim.no_anim);
	}

	@Override
	public void finish() {
		super.finish();
		UIHelper.hidePan(this);
		overridePendingTransition(0, R.anim.slide_right_out);
	}
}
