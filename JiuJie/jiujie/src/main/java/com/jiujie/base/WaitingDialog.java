package com.jiujie.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

import com.jiujie.base.dialog.BaseDialog;
import com.jiujie.base.widget.RotateImageView;

public class WaitingDialog extends BaseDialog {

	private RotateImageView rotateImageView;

	public WaitingDialog(Activity context) {
		super(context);
	}

	public void create(){
		create(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER, 0);
	}

	@Override
	protected void initUI(View layout) {
		rotateImageView = (RotateImageView) layout.findViewById(R.id.dialog_waitting_image);
	}

	@Override
	public int getLayoutId() {
		return R.layout.dialog_waitting;
	}

	public RotateImageView getRotateImageView() {
		return rotateImageView;
	}

	@Override
	public void show() {
		super.show();
		rotateImageView.start();
	}

	@Override
	public void dismiss() {
		super.dismiss();
		rotateImageView.stop();
	}
}
