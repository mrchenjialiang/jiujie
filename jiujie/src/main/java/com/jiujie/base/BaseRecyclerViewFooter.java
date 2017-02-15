package com.jiujie.base;

import android.view.View;
import android.widget.TextView;


/**
 * 列表页的底部加载中、加载完
 */
public class BaseRecyclerViewFooter {

	private View footer,loadingLine;
	private TextView loadingText,loadEndText;
	public BaseRecyclerViewFooter(View footer){
		this.footer = footer;
		loadingLine = footer.findViewById(R.id.lf_loading_line);
		loadingText = (TextView) footer.findViewById(R.id.lf_loading_text);
		loadEndText = (TextView) footer.findViewById(R.id.lf_load_end_text);
	}

	/**
	 * 显示正在加载更多
	 */
	public void setReadMore() {
		if(footer.getVisibility()==View.GONE)show();
		loadingLine.setVisibility(View.VISIBLE);
		loadEndText.setVisibility(View.GONE);
	}

	/**
	 * 显示加载完成
	 */
	public void setReadEnd() {
		if(footer.getVisibility()==View.GONE)show();
		loadingLine.setVisibility(View.GONE);
		loadEndText.setVisibility(View.VISIBLE);
	}


	/**
	 * 设置加载更多的文字说明
	 */
	public void setReadMoreText(int resId) {
		loadingText.setText(resId);
	}

	/**
	 * 设置加载更多的文字说明
	 */
	public void setReadMoreText(String text) {
		loadingText.setText(text);
	}

	/**
	 * 设置加载完成的文字说明
	 */
	public void setReadEndText(int resId) {
		loadEndText.setText(resId);
	}

	/**
	 * 设置加载完成的文字说明
	 */
	public void setReadEndText(String text) {
		loadEndText.setText(text);
	}

	public void hide() {
		footer.setVisibility(View.GONE);
	}

	public void show() {
		footer.setVisibility(View.VISIBLE);
	}

}