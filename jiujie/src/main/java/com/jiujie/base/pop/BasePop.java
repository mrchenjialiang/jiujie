package com.jiujie.base.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.PopupWindow;

import com.jiujie.base.jk.OnPopItemClickListen;
import com.jiujie.base.jk.onShowHideListen;

public abstract class BasePop extends PopupWindow {
	
	private boolean isInit = false;
	public View layout;
	private int x = -1;
	private int y = -1;
	private onShowHideListen listen;
	public Activity context;
	protected OnPopItemClickListen onItemClickListen;

	public void setOnShowHideListen(onShowHideListen listen) {
		this.listen = listen;
	}

	public void setOnItemClickListen(OnPopItemClickListen onItemClickListen){
		this.onItemClickListen = onItemClickListen;
	}
	
	public BasePop(Activity context){
		super(context);
		this.context = context;
		init(context);
	}
	public BasePop(Activity context,View view, int width, int height){
		super(view,width,height);
		this.context = context;
		init(view.getContext());
	}
	public BasePop(Activity context, AttributeSet attributeSet){
		super(context, attributeSet);
		this.context = context;
		init(context);
	}

	private void init(Context context) {
		layout = getLayout(context);
		if(layout==null){
			layout = LayoutInflater.from(context).inflate(getLayoutId(), null);
		}
		if(layout==null){
			return;
		}
		initUI();

		this.setContentView(layout);
		this.setBackgroundDrawable(new BitmapDrawable());
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		if (android.os.Build.VERSION.SDK_INT > 10) {
			this.setSplitTouchEnabled(true);
		}
		isInit = true;
	}

	protected abstract void initUI();

	public abstract View getLayout(Context context);

	public abstract int getLayoutId();

	@Override
	public final View getContentView() {
		return super.getContentView();
	}

	/**
	 * 设置显示在某个view之上
	 */
	public void showOnAboutView(View view,int width,int height){
		if(!isInit){
			throw new RuntimeException(this+"尚未初始化");
		}else{
			if(x<0&&y<0){
				layout.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
				int popupHeight = layout.getMeasuredHeight();
				int[] location = new int[2];
				view.getLocationOnScreen(location);
				this.setWidth(width);
				this.setHeight(height);
				x = location[0];
				y = location[1]-popupHeight;
				this.showAtLocation(view, Gravity.NO_GRAVITY, x,y);
			}else{
				this.showAtLocation(view, Gravity.NO_GRAVITY, x,y);
			}
		}
	}
	
	@Override
	public void showAsDropDown(View anchor) {
		super.showAsDropDown(anchor);
		if(listen!=null)listen.show();
	}
	
	@Override
	public void showAsDropDown(View anchor, int x, int y) {
		super.showAsDropDown(anchor, x, y);
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
}
