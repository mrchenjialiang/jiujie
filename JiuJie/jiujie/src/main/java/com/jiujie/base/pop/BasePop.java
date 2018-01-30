package com.jiujie.base.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.PopupWindow;

import com.jiujie.base.jk.OnPopItemClickListen;
import com.jiujie.base.jk.onShowHideListen;
import com.jiujie.base.util.UIHelper;

public abstract class BasePop extends PopupWindow {

	private final int height;
	private final int width;
	private boolean isInit = false;
	private int x = -1;
	private int y = -1;
	private onShowHideListen listen;
	private Activity activity;
	protected OnPopItemClickListen onItemClickListen;
	private View layout;

	public void setOnShowHideListen(onShowHideListen listen) {
		this.listen = listen;
	}

	public void setOnItemClickListen(OnPopItemClickListen onItemClickListen){
		this.onItemClickListen = onItemClickListen;
	}
	
	public BasePop(Activity activity, int width, int height){
		super(activity);
		this.activity = activity;
		this.width = width;
		this.height = height;
		init(activity);
	}

	public Activity getActivity() {
		return activity;
	}

	private void init(Context context) {
		layout = getLayout(context);
		if(layout ==null){
			layout = LayoutInflater.from(context).inflate(getLayoutId(), null);
		}
		if(layout ==null){
			return;
		}
		initUI(layout);

		this.setContentView(layout);
		this.setBackgroundDrawable(new BitmapDrawable());
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		if (android.os.Build.VERSION.SDK_INT > 10) {
			this.setSplitTouchEnabled(true);
		}
		isInit = true;
		setWidth(width);
		setHeight(height);
	}

	protected abstract void initUI(View layout);

	public abstract View getLayout(Context context);

	public abstract int getLayoutId();

	@Override
	public final View getContentView() {
		return super.getContentView();
	}

	public void showOnAboutView(View view){
		if(!isInit){
			throw new RuntimeException(this+"尚未初始化");
		}

		layout.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		int height = layout.getMeasuredHeight();
		int width = layout.getMeasuredWidth();

		UIHelper.showLog("showOnAboutView layout width:"+width);
		UIHelper.showLog("showOnAboutView layout getWidth():"+getWidth());
		UIHelper.showLog("showOnAboutView layout layout.getWidth():"+layout.getWidth());
		UIHelper.showLog("showOnAboutView layout height:"+height);
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		int x = location[0];
		int y = location[1];
		x = x - (width/2 - view.getWidth()/2);
		y = y - height;
		showAtLocation(view, Gravity.NO_GRAVITY, x,y);
	}

	public void showOnBottomView(View view){
		if(!isInit){
			throw new RuntimeException(this+"尚未初始化");
		}
		layout.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//		int height = layout.getMeasuredHeight();
		float width = layout.getMeasuredWidth();
		showAsDropDown(view, (int) - (width/2 - view.getWidth()/2 + 5),0);//老是会自己偏移了几个像素，我这里写5纠正
	}

	@Override
	public void showAsDropDown(View view) {
		super.showAsDropDown(view);
		if(listen!=null)listen.show();
	}
	
	@Override
	public void showAsDropDown(View view, int x, int y) {
		super.showAsDropDown(view, x, y);
		if(listen!=null)listen.show();
	}
	
	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		if(listen!=null)listen.show();
	}
	
	@Override
	public void dismiss() {
		super.dismiss();
		if(listen!=null) listen.hide();
	}

	public void dismissForTime(int longTime) {
		layout.postDelayed(new Runnable() {
			@Override
			public void run() {
				dismiss();
			}
		},longTime);
	}
}
