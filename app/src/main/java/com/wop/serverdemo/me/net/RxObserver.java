package com.wop.serverdemo.me.net;


import com.wop.common.core.rx.ErrorHandlers;
import com.wop.common.util.JSONUtils;
import com.wop.serverdemo.core.WopApplication;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author woniu
 * @title RxObserver
 * @description
 * @since 2018/11/20 下午6:26
 */
public class RxObserver<T> implements Observer<T> {

    @Override
    final public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T o) {
        String res = JSONUtils.toJson(o).toString();
        ErrorHandlers.displayError(WopApplication.getContext(), res);
        onNext(o);
    }

    @Override
    public void onError(Throwable e) {
        ErrorHandlers.displayError(WopApplication.getContext(), e);
        doOnError(e);
    }

    @Override
    final public void onComplete() {
        doOnComplete();
    }

    public void doOnError(Throwable e) {
    }

    public void doOnComplete() {
    }

}
