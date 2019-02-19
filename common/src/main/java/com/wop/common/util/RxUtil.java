package com.wop.common.util;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author woniu
 * @title RxUtil
 * @description
 * @since 2017/12/28 下午9:40
 */
final public class RxUtil {

    private RxUtil(){}

    public static void doOnIO(Runnable runnable) {
        Schedulers.io().createWorker().schedule(runnable);
    }

    public static void doOnMain(Runnable runnable) {
        AndroidSchedulers.mainThread().createWorker().schedule(runnable);
    }

    public static void doOnIODelay(Runnable runnable, long time) {
        Schedulers.io().createWorker().schedule(runnable, time, TimeUnit.MILLISECONDS);
    }

    public static void doOnMainDelay(Runnable runnable, long time) {
        AndroidSchedulers.mainThread().createWorker().schedule(runnable, time, TimeUnit.MILLISECONDS);
    }

    public static void doOnIODelay(Runnable runnable, long time, TimeUnit timeUnit) {
        Schedulers.io().createWorker().schedule(runnable, time, timeUnit);
    }

    public static void doOnMainDelay(Runnable runnable, long time, TimeUnit timeUnit) {
        AndroidSchedulers.mainThread().createWorker().schedule(runnable, time, timeUnit);
    }


}
