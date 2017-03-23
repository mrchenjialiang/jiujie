package com.jiujie.base;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiujie.base.widget.SelectorImage;

public class Title {

	private View rootView;
	private Activity activity;
	private View mTitleView;
	private View title_left_btn;
	private View title_right_btn;
	private View title_right_btn2;
	private SelectorImage title_left_btn_image ,title_right_btn_image,title_right_btn_image2;
	private TextView title_text;
	private View title_right_text_btn;
	private TextView title_right_text;
	private LinearLayout title_custom_line;
	private View mDefaultTitle;

	/**
	 * use in fragment
	 */
	public Title(Activity activity,View rootView) {
		this.activity = activity;
		this.rootView = rootView;
		initUI();
	}
	/**
	 * use in activity,the activity should be extends BaseActivity
	 */
	public Title(Activity activity) {
		this.activity = activity;
		initUI();
	}

	private void initUI() {
		if(rootView!=null){
			mTitleView = rootView.findViewById(R.id.base_title);
			mDefaultTitle = rootView.findViewById(R.id.base_default_title);
			title_text = (TextView) rootView.findViewById(R.id.title_text);
			title_left_btn = rootView.findViewById(R.id.title_left_btn);
			title_right_btn = rootView.findViewById(R.id.title_right_btn);
			title_left_btn_image = (SelectorImage) rootView.findViewById(R.id.title_left_btn_image);
			title_right_btn_image = (SelectorImage) rootView.findViewById(R.id.title_right_btn_image);

			title_right_btn2 = rootView.findViewById(R.id.title_right_btn2);
			title_right_btn_image2 = (SelectorImage) rootView.findViewById(R.id.title_right_btn_image2);
			
			title_right_text_btn = rootView.findViewById(R.id.title_right_text_btn);
			title_right_text = (TextView) rootView.findViewById(R.id.title_right_text);
			title_custom_line = (LinearLayout) rootView.findViewById(R.id.title_custom_line);
		}else if(activity!=null){
			mTitleView = activity.findViewById(R.id.base_title);
			mDefaultTitle = activity.findViewById(R.id.base_default_title);
			title_text = (TextView) activity.findViewById(R.id.title_text);
			title_left_btn = activity.findViewById(R.id.title_left_btn);
			title_right_btn = activity.findViewById(R.id.title_right_btn);
			title_left_btn_image = (SelectorImage) activity.findViewById(R.id.title_left_btn_image);
			title_right_btn_image = (SelectorImage) activity.findViewById(R.id.title_right_btn_image);

			title_right_btn2 = activity.findViewById(R.id.title_right_btn2);
			title_right_btn_image2 = (SelectorImage) activity.findViewById(R.id.title_right_btn_image2);
			
			title_right_text_btn = activity.findViewById(R.id.title_right_text_btn);
			title_right_text = (TextView) activity.findViewById(R.id.title_right_text);
			title_custom_line = (LinearLayout) activity.findViewById(R.id.title_custom_line);
		}else{
			try {
				throw new Exception();
			} catch (Exception e) {
				Log.e("Title Exception", "Title Exception when initUI,because rootView==null and activity==null,there muse be one not null");
			}
		}
	}
	public void setTitleHide(){
		mTitleView.setVisibility(View.GONE);
	}
	public void setTitleShow(){
		mTitleView.setVisibility(View.VISIBLE);
	}
	
	public View setCustomTitle(int id){
		mDefaultTitle.setVisibility(View.GONE);
		View customTitle = activity.getLayoutInflater().inflate(id, null);
		title_custom_line.addView(customTitle);
		return customTitle;
	}
	
	public void setTitleText(String title){
		if(title_text!=null)title_text.setText(title);
	}
	public void setLeftButtonImage(int drawId){
		if(title_left_btn_image!=null&&drawId>0){
			title_left_btn_image.setImageResource(drawId);
			title_left_btn.setVisibility(View.VISIBLE);
		}
	}
	public void setLeftButtonImage(int normalId,int pressId){
		if(title_left_btn_image!=null){
			title_left_btn_image.setImageSelector(normalId, pressId);
			title_left_btn.setVisibility(View.VISIBLE);
		}
	}
	public void setLeftButtonClick(OnClickListener clickListener){
		if(title_left_btn!=null){
			title_left_btn.setOnClickListener(clickListener);
		}
	}
	public void setRightButtonImage(int drawId){
		if(title_right_btn_image!=null&&drawId>0){
			title_right_btn_image.setImageResource(drawId);
			title_right_btn.setVisibility(View.VISIBLE);
		}
	}
	public void setRightButtonImage(int normalId,int pressId){
		if(title_right_btn_image!=null){
			title_right_btn_image.setImageSelector(normalId, pressId);
			title_right_btn.setVisibility(View.VISIBLE);
		}
	}
	public void setRightButtonClick(OnClickListener clickListener){
		if(title_right_btn!=null){
			title_right_btn.setOnClickListener(clickListener);
		}
	}
	
	public void setRightTextButtonTextSize(float textSizeSp){
		if(title_right_text!=null){
			title_right_text.setTextSize(textSizeSp);
		}
	}

	public void setRightTextButtonText(String text){
		if(title_right_text!=null&&!TextUtils.isEmpty(text)){
			title_right_text.setText(text);
			title_right_text_btn.setVisibility(View.VISIBLE);
		}
	}
	public void setRightTextButtonClick(OnClickListener clickListener){
		if(title_right_text_btn!=null){
			title_right_text_btn.setOnClickListener(clickListener);
		}
	}
	/**
	 * only use in activity
	 */
	public void setLeftButtonBack(){
		title_left_btn.setVisibility(View.VISIBLE);
		setLeftButtonImage(R.drawable.title_jt_left);
		if(title_left_btn!=null){
			if(activity!=null){
				title_left_btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						activity.finish();
					}
				});
			}
		}
	}
	/**
	 * only use in activity
	 */
	public void setLeftButtonBack(int drawId){
		title_left_btn.setVisibility(View.VISIBLE);
		title_left_btn_image.setImageResource(drawId);
		if(title_left_btn!=null){
			if(activity!=null){
				title_left_btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						activity.finish();
					}
				});
			}
		}
	}
	/**
	 * only use in activity
	 */
	public void setLeftButtonBack(int normalId,int pressId){
		title_left_btn.setVisibility(View.VISIBLE);
		setLeftButtonImage(normalId,pressId);
		if(title_left_btn!=null){
			if(activity!=null){
				title_left_btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						activity.finish();
					}
				});
			}
		}
	}

	public void setRight2ButtonImage(int drawId){
		if(title_right_btn_image2!=null&&drawId>0){
			title_right_btn_image2.setImageResource(drawId);
			title_right_btn2.setVisibility(View.VISIBLE);
		}
	}
	public void setRight2ButtonImage(int normalId,int pressId){
		if(title_right_btn_image2!=null){
			title_right_btn_image2.setImageSelector(normalId, pressId);
			title_right_btn2.setVisibility(View.VISIBLE);
		}
	}
	public void setRight2ButtonClick(OnClickListener clickListener){
		if(title_right_btn2!=null){
			title_right_btn2.setOnClickListener(clickListener);
		}
	}

	public void showRightTextButton() {
		if(title_right_text!=null){
			title_right_text_btn.setVisibility(View.VISIBLE);
		}
	}
	public void hideRightTextButton() {
		if(title_right_text!=null){
			title_right_text_btn.setVisibility(View.GONE);
		}
	}

	public void showRightImageButton() {
		if(title_right_btn!=null){
			title_right_btn.setVisibility(View.VISIBLE);
		}
	}
	public void hideRightImageButton() {
		if(title_right_btn!=null){
			title_right_btn.setVisibility(View.GONE);
		}
	}
	
	public View getRightButton() {
		return title_right_btn;
	}

	public void setTextColor(int titleTextColor) {
		if(title_text!=null)title_text.setTextColor(titleTextColor);
		if(title_right_text!=null)title_right_text.setTextColor(titleTextColor);
	}

	public void setBackGroundColor(int titleBackGroundColor) {
		if(mDefaultTitle!=null)mDefaultTitle.setBackgroundColor(titleBackGroundColor);
		if(title_custom_line!=null)title_custom_line.setBackgroundColor(titleBackGroundColor);
	}

}
