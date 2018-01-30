package com.jiujie.base;

import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.jiujie.base.jk.InputAction;
import com.jiujie.base.util.UIHelper;

public class SearchTitle {

	private Activity activity;
	private EditText mSearchEd;
	private View mTitleView;
	private View mBtnBack;
	private View mBtnSearch;

	public SearchTitle(Activity activity) {
		this.activity = activity;
		initUI();
	}

	private void initUI() {
		mTitleView = activity.findViewById(R.id.ts_base_title);
		mBtnBack = activity.findViewById(R.id.ts_btn_back);
		mBtnSearch = activity.findViewById(R.id.ts_btn_search);
		mSearchEd = (EditText) activity.findViewById(R.id.ts_ed_text);
	}

	public EditText getSearchText() {
		return mSearchEd;
	}

	public void setTitleHide(){
		mTitleView.setVisibility(View.GONE);
	}
	public void setTitleShow(){
		mTitleView.setVisibility(View.VISIBLE);
	}
	
	public void setLeftButtonBack(){
		if(mBtnBack !=null) mBtnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(activity!=null) activity.finish();
			}
		});
	}
	
	public void setSearchAction(final InputAction inputAction){
		if(mBtnSearch !=null) mBtnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(activity!=null) UIHelper.hidePan(activity);
				if(inputAction!=null) {
					Editable text = mSearchEd.getText();
					if(TextUtils.isEmpty(text.toString().trim())){
						inputAction.send("");
					}else{
						inputAction.send(mSearchEd.getText().toString().trim());
					}
				}
			}
		});
		
		mSearchEd.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH||actionId == EditorInfo.IME_ACTION_DONE||actionId == EditorInfo.IME_ACTION_UNSPECIFIED||actionId == EditorInfo.IME_ACTION_GO) {
    				if(activity!=null) UIHelper.hidePan(activity);
                	if(inputAction!=null) {
    					Editable text = mSearchEd.getText();
    					if(TextUtils.isEmpty(text.toString().trim())){
    						inputAction.send("");
    					}else{
    						inputAction.send(mSearchEd.getText().toString().trim());
    					}
    				}
                }
                return false;
            }

        });
	}

	public void setHintText(String hint) {
		mSearchEd.setHint(hint);
	}

}
