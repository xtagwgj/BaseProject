package com.xtagwgj.baseproject.utils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * Rx的工具类
 * Created by xtagwgj on 2017/6/15.
 */

public class RxUtils {

    private RxUtils() {
    }

    /**
     * 倒计时的工具类
     *
     * @param time 倒计时的时间
     * @return Observable<Integer>
     */
    public static Observable<Integer> countDown(int time) {
        final int countTime = time < 0 ? 0 : time;

        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long aLong) throws Exception {
                        return countTime - aLong.intValue();
                    }
                })
                .take(countTime + 1);
    }

    /**
     * 倒计时的工具类
     *
     * @param seconds 倒计时的时间
     * @return Flowable<Long>
     */
    public static Flowable<Long> countDownByMillis(long seconds) {
        final long countTime = seconds < 0 ? 0 : seconds * 100;

        return Flowable.interval(0L, 100, TimeUnit.MILLISECONDS)
                .onBackpressureDrop()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        LogUtils.e("time", "" + aLong);
                        return countTime - aLong * 100;
                    }
                })
                .take(countTime + 1);
    }
}
