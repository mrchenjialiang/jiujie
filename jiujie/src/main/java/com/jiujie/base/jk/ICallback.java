package com.jiujie.base.jk;

public interface ICallback<T> {
	
	void onSucceed(T result);
	
	void onFail(String error);

}
