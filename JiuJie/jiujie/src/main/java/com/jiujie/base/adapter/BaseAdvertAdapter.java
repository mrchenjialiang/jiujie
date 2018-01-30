package com.jiujie.base.adapter;

import android.annotation.SuppressLint;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseAdvertAdapter extends PagerAdapter {
	@SuppressLint("UseSparseArrays")
	private Map<Integer,View> viewMap = new HashMap<>();//这里因数量有限，所以每个都保存
	private int maxZone = 1000;//放大倍数
	public BaseAdvertAdapter() {
	}

	private OnNotifyDataSetChangedListen onNotifyDataSetChangedListen;

	public interface OnNotifyDataSetChangedListen{
		void changed();
	}

	public void setOnNotifyDataSetChangedListen(OnNotifyDataSetChangedListen onNotifyDataSetChangedListen) {
		this.onNotifyDataSetChangedListen = onNotifyDataSetChangedListen;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		viewMap.clear();
		if(onNotifyDataSetChangedListen!=null) onNotifyDataSetChangedListen.changed();
	}

	public void setMaxZone(int maxZone) {
		this.maxZone = maxZone;
	}

	public int getMaxZone() {
		return maxZone;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;//官方提示这样写
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		int size = getSize();
		position = position% size;
		View view;


		if(size==3){
			view = getView(container, position);
			bindView(view, position);
		}else{
			if(viewMap.containsKey(position)){
				view = viewMap.get(position);
			}else{
				view = getView(container, position);
				bindView(view, position);
				viewMap.put(position, view);
			}
		}
		if(view.getParent()!=null){
			container.removeView(view);
			container.addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);//这句别漏了!!!!
			return view;
		}else{
			container.addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);//这句别漏了!!!!
			return view;
		}
	}
	public abstract void bindView(View view, int position);
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
//		position = position%getSize();
//		if(viewMap.containsKey(position)){
//			viewMap.remove(position);
//			UIHelper.showLog("instantiateItem destroyItem position:"+position);
//		}
	}
	@Override
	public final int getCount() {
		int size = getSize();
		if(size>2){
			return size*maxZone;
		}else{
			return size;
		}
	}

	public abstract int getSize();

	public abstract View getView(ViewGroup container, int position);

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
}
