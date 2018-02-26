package com.jiujie.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.jiujie.base.APP;
import com.jiujie.base.R;
import com.jiujie.base.util.glide.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageViewPagerActivity extends BaseActivity {

	protected ArrayList<String> imageList = new ArrayList<>();
	protected ViewPager mViewPager;
	private int index;
	protected TextView mTvTitle;
	private SamplePagerAdapter adapter;

	public static void launch(Activity activity, int index, List<String> imageList){
		Intent intent = new Intent(activity, ImageViewPagerActivity.class);
		intent.putExtra("index",index);
		intent.putExtra("imageList",new ArrayList<>(imageList));
		activity.startActivity(intent);
		activity.overridePendingTransition(R.anim.center_0_to_max, R.anim.alpha_null);
	}

	protected void getIntentData() {
		Intent intent = getIntent();
		index = intent.getIntExtra("index",0);
		imageList = intent.getStringArrayListExtra("imageList");
		if(index <0){
			index = 0;
		}
		if(index > imageList.size()-1){
			index = imageList.size()-1;
		}
	}

	public void initUI() {
		getIntentData();
		mViewPager = findViewById(R.id.iv_ViewPager);
		View mTitleLayout = findViewById(R.id.iv_title_layout);
		boolean showTitleLayout = isShowTitleLayout();
		if(showTitleLayout){
			mTitleLayout.setVisibility(View.VISIBLE);
			if(APP.isTitleContainStatusBar()){
				mTitleLayout.setPadding(0,APP.getStatusBarHeight(),0,0);
			}
			mTvTitle = findViewById(R.id.iv_title);
			findViewById(R.id.iv_back).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish(true);
				}
			});
			View mBtnDel = findViewById(R.id.iv_delete);
			OnClickListener delAction = getDelAction();
			if(delAction==null){
				mBtnDel.setVisibility(View.GONE);
			}else{
				mBtnDel.setVisibility(View.VISIBLE);
				mBtnDel.setOnClickListener(delAction);
			}
		}else{
			mTitleLayout.setVisibility(View.GONE);
		}
	}

	protected OnClickListener getDelAction(){
		return null;
	}

	protected boolean isShowTitleLayout(){
		return true;
	}

	@Override
	public boolean isShowTitle() {
		return false;
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_image_viewpager;
	}

	@Override
	public void initData() {
		if(imageList.size()>0){
			adapter = new SamplePagerAdapter();
			mViewPager.setAdapter(adapter);
			if(index >0) mViewPager.setCurrentItem(index,false);
		}
		mViewPager.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTvTitle.setText("(" + (index + 1) + "/" + imageList.size() + ")");
		mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				index = position;
				mTvTitle.setText("(" + (position + 1) + "/" + imageList.size() + ")");
			}
		});
	}

	protected int getCurrentIndex(){
		return index;
	}

	class SamplePagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return ImageViewPagerActivity.this.getCount();
		}
		@Override
		public View instantiateItem(ViewGroup container, final int position) {
			return getItemView(container, position);
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	}

	protected int getCount() {
		return imageList ==null?0: imageList.size();
	}

	@NonNull
	protected View getItemView(ViewGroup container, int position) {
		View layout = getLayoutInflater().inflate(R.layout.rela_viewpager, container,false);
		final PhotoView photoView = layout.findViewById(R.id.rela_viewpager_photoView);
		GlideUtil.instance().setDefaultImage(mActivity,getUrl(position), photoView,R.drawable.trans,false,true,null);
		photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                finish(true);
            }
        });
		container.addView(layout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		return layout;
	}

	protected String getUrl(int position) {
		return imageList.get(position);
	}

	protected void finish(boolean isFinishByClick){
		finish();
		if(isFinishByClick)
			overridePendingTransition(R.anim.alpha_null, R.anim.zoom_out_exit);
	}

	protected void notifyDataSetChanged(){
		adapter.notifyDataSetChanged();
	}

	@Override
	protected String getPageName() {
		return "看大图";
	}
}
