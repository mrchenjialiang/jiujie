package com.jiujie.base.util;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public abstract class TaskManager<T> {

    public TaskManager() {
    }

    public void start(){
        getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<T>() {
                    @Override
                    public void call(T t) {
                        runOnUIThread(t);
                    }
                });
    }

    private Observable<T> getObservable(){
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                subscriber.onNext(runOnBackgroundThread());
            }
        });
    }

    public abstract T runOnBackgroundThread();
    public abstract void runOnUIThread(T t);
}