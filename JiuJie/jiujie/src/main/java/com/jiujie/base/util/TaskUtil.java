package com.jiujie.base.util;

import com.jiujie.base.jk.OnListener;
import com.jiujie.base.jk.OnSimpleListener;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by xxl on 2018/2/23.
 */

public class TaskUtil {

    public static void doInUi(final OnSimpleListener onSimpleListener){
        Observable
                .empty()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        if(onSimpleListener!=null){
                            onSimpleListener.onListen();
                        }
                    }
                })
                .subscribe();
    }

    public static void doInBack(final OnSimpleListener onSimpleListener){
        Observable
                .empty()
                .subscribeOn(Schedulers.io())
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        if(onSimpleListener!=null){
                            onSimpleListener.onListen();
                        }
                    }
                })
                .subscribe();
    }
}
