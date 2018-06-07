package com.jiujie.base.jk;

public interface IProgressCallback<T> {
	
	void onSucceed(T result);
	void onProgress(long total, long current, float progress);
	void onFail(String error);

}
