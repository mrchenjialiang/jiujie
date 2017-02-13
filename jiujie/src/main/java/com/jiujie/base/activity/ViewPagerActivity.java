/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.jiujie.base.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.jiujie.base.R;
import com.jiujie.base.util.ImageUtil;
import com.jiujie.base.util.UIHelper;
import com.jiujie.base.util.glide.GlideUtil;
import com.jiujie.base.widget.HackyViewPager;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Lock/Unlock button is added to the ActionBar.
 * Use it to temporarily disable ViewPager navigation in order to correctly interact with ImageView by gestures.
 * Lock/Unlock state of ViewPager is saved and restored on configuration changes.
 * 
 * Julia Zudikova
 */

public class ViewPagerActivity extends BaseNoTitleActivity {

	private static final String IS_LOCKED_ARG = "isLocked";
	private HackyViewPager mViewPager;
	private MenuItem menuLockItem;
	private ArrayList<String> dataList;
	private Dialog waitingDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_view_pager);

		setToolBarBackGround(Color.BLACK);

		waitingDialog = UIHelper.getWaitingDialog(mActivity);
		mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
		if (savedInstanceState != null) {
			boolean isLocked = savedInstanceState.getBoolean(IS_LOCKED_ARG, false);
			mViewPager.setLocked(isLocked);
		}

		Intent intent = getIntent();
		String currentUrl = intent.getStringExtra("url");
		dataList = intent.getStringArrayListExtra("urlList");
		int currentPosition = dataList.indexOf(currentUrl);
		if(currentUrl!=null&&dataList.size()>0){
			mViewPager.setAdapter(new SamplePagerAdapter());
			mViewPager.setCurrentItem(currentPosition);
		}
		mViewPager.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				UIHelper.setJumpAnimation(mActivity, 2);
			}
		});
    }
	
	@Override
	public int getLayoutId() {
		return R.layout.activity_view_pager;
	}

	@Override
	public void initData() {

	}

	class SamplePagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return dataList==null?0:dataList.size();
		}
		@Override
		public View instantiateItem(ViewGroup container, final int position) {
			if(position>=dataList.size()){
				return null;
			}

			View layout = getLayoutInflater().inflate(R.layout.rela_viewpager, container,false);
			final PhotoView photoView = (PhotoView) layout.findViewById(R.id.rela_viewpager_photoView);
//			final RoundProgressBar progressBar = (RoundProgressBar) layout.findViewById(R.id.rela_viewpager_roundProgressBar1);
//			final ImageView imageView = (ImageView) layout.findViewById(R.id.rela_viewpager_imageView);

			GlideUtil.instance().setDefaultImage(ViewPagerActivity.this, dataList.get(position), photoView);

			photoView.setOnLongClickListener(new View.OnLongClickListener() {
				AlertDialog alertDialog;
				@Override
				public boolean onLongClick(View v) {
					final Bitmap bitmap = ImageUtil.instance().getImageBitmapFromNet(ViewPagerActivity.this, dataList.get(position));
					if(bitmap==null)return false;

					if(alertDialog==null){
						alertDialog = new AlertDialog.Builder(v.getContext()).setItems(new String[]{"保存到手机"}, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								waitingDialog.show();
								final ImageUtil imageUtil = ImageUtil.instance();
								final String imageLocalDic = imageUtil.getImageLocalDic(mActivity);
								final String imageName = imageUtil.getImageName();
								new AsyncTask<Void,Void,Void>(){
									@Override
									protected Void doInBackground(Void... params) {
										imageUtil.saveImageToLocal(imageLocalDic, imageName,bitmap);
										return null;
									}

									@Override
									protected void onPostExecute(Void aVoid) {
										super.onPostExecute(aVoid);
										waitingDialog.dismiss();
										UIHelper.showToastShort(mActivity,"图片已保存至"+imageLocalDic+imageName);
									}
								}.execute();
							}
						}).create();
					}
					alertDialog.show();
					return false;
				}
			});
	        
	        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
				@Override
				public void onPhotoTap(View arg0, float arg1, float arg2) {
//					System.out.println("photoView被点击了");
					ViewPagerActivity.this.finish();
					UIHelper.setJumpAnimation(mActivity, 2);
				}
			});
	        
			container.addView(layout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			return layout;
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
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		UIHelper.setJumpAnimation(mActivity, 2);
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viewpager_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menuLockItem = menu.findItem(R.id.menu_lock);
        toggleLockBtnTitle();
        menuLockItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				toggleViewPagerScrolling();
				toggleLockBtnTitle();
				return true;
			}
		});

        return super.onPrepareOptionsMenu(menu);
    }
    
    private void toggleViewPagerScrolling() {
    	if (isViewPagerActive()) {
    		mViewPager.toggleLock();
    	}
    }
    
    private void toggleLockBtnTitle() {
    	boolean isLocked = false;
    	if (isViewPagerActive()) {
    		isLocked = mViewPager.isLocked();
    	}
    	String title = (isLocked) ? getString(R.string.menu_unlock) : getString(R.string.menu_lock);
    	if (menuLockItem != null) {
    		menuLockItem.setTitle(title);
    	}
    }

    private boolean isViewPagerActive() {
    	return (mViewPager != null);
    }
    
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (isViewPagerActive()) {
			outState.putBoolean(IS_LOCKED_ARG, mViewPager.isLocked());
    	}
		super.onSaveInstanceState(outState);
	}
    
}
