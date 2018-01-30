package com.jiujie.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.jiujie.base.R;

public abstract class BaseDialog {

	private Dialog dialog;
	private Context context;
	private View layout;

	public BaseDialog(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	public Dialog create(int width,int height,int gravity,int animStyleId){
		layout = getLayout();
		if(layout==null){
			if(getLayoutId()>0){
				layout = LayoutInflater.from(context).inflate(getLayoutId(),null);
			}
		}
		if(layout==null){
			throw new NullPointerException("BaseDialog getLayoutId or getLayout should one not be null or 0");
		}
		initUI(layout);

		dialog = new Dialog(context, R.style.Dialog);
//		dialog = new Dialog(activity, R.style.bottom_dialog);
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();

		if(gravity>0){
			dialogWindow.setGravity(gravity);
		}
		if(animStyleId>0){
			dialogWindow.setWindowAnimations(animStyleId);
		}

		if(width== ViewGroup.LayoutParams.MATCH_PARENT){
			WindowManager wm = dialogWindow.getWindowManager();
			Display d = wm.getDefaultDisplay();
			WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
			width = d.getWidth();
		}
		lp.width = width; // 宽度
		lp.height = height; // 高度
		dialogWindow.setBackgroundDrawableResource(R.color.trans);
		dialogWindow.setAttributes(lp);
		dialogWindow.setContentView(layout, new ViewGroup.LayoutParams(width, height));
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}

	public Dialog getDialog() {
		return dialog;
	}

	public void dismiss(){
		if(dialog!=null)dialog.dismiss();
	}

	public void dismissForTime(long time){
		layout.postDelayed(new Runnable() {
			@Override
			public void run() {
				if(dialog!=null)dialog.dismiss();
			}
		},time);
	}

	public void show(){
		if(dialog!=null)dialog.show();
	}

	public void showForTime(long time){
		show();
		layout.postDelayed(new Runnable() {
			@Override
			public void run() {
				dismiss();
			}
		},time);
	}

	protected abstract void initUI(View layout);

	public int getLayoutId(){
		return 0;
	}

	public View getLayout(){
		return null;
	}
}
