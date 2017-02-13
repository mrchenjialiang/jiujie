package com.jiujie.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;

public class WaitingDialog extends Dialog{

	public WaitingDialog(Context context, int theme) {
		super(context, theme);
	}

	public WaitingDialog(Context context) {
		super(context);
	}
	
	public static class Builder {
		private Context context;
		
		public Builder(Context context) {
			this.context = context;
		}
		/**
		 * Create the custom dialog
		 */
		@SuppressWarnings("deprecation")
		public Dialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final WaitingDialog dialog = new WaitingDialog(context,R.style.Dialog);
			
			View layout = inflater.inflate(R.layout.dialog_waitting, null);
			ImageView image = (ImageView) layout.findViewById(R.id.dialog_waitting_image);
			image.setImageResource(R.drawable.loading);
			AnimationDrawable mLoadingAnimation = (AnimationDrawable) image.getDrawable();
			mLoadingAnimation.start();
			
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			WindowManager m = dialog.getWindow().getWindowManager();
			Display d = m.getDefaultDisplay();
			WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
			p.width = (int) (d.getWidth() * 0.4);
			p.height = (int) (d.getWidth() * 0.4);
			dialog.getWindow().setAttributes(p);
			dialog.setCanceledOnTouchOutside(true);
			dialog.setContentView(layout);
			
			return dialog;
		}
	}
}
