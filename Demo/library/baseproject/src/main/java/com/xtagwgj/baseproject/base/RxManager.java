package com.xtagwgj.baseproject.base;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 用于管理单个presenter的RxBus的事件和Rxjava相关代码的生命周期处理
 * Created by xtagwgj on 2017/4/9.
 */

public class RxManager {
    private RxBus mRxBus = RxBus.getInstance();
    //管理rxbus订阅
    private Map<String, Observable<?>> mObservables = new HashMap<>();
    /*管理Observables 和 Subscribers订阅*/
    private CompositeDisposable mDisposable = new CompositeDisposable();

    public void on(String eventName, Consumer<? super Object> consumer) {
        Observable<Object> mObservable = mRxBus.register(eventName);
        mObservables.put(eventName, mObservable);
        mDisposable.add(
                mObservable
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(consumer, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                            }
                        })

        );
    }

    /**
     * 单纯的Observables 和 Subscribers管理
     */
    public void add(Disposable disposable) {
        mDisposable.add(disposable);
    }

    public void clear() {
        mDisposable.clear();
        for (Map.Entry<String, Observable<?>> entry : mObservables.entrySet()) {
            mRxBus.unRegister(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 发送rxbus
     */
    public void post(Object tag, Object content) {
        mRxBus.post(tag, content);
    }

}