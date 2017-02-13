package com.jiujie.base.jk;

public interface ICallback<T> {
	
	public void onSucceed(T result);
	
	public void onFail(String error);

}
