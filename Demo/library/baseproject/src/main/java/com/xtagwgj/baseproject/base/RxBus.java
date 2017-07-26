package com.xtagwgj.baseproject.base;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * 用RxJava实现的EventBus
 * Created by xtagwgj on 2017/4/9.
 * <p>
 * <p>
 * 发送数据
 * RxBus.getInstance().post(TAG,"aaa");
 * <p>
 * 接收数据
 * RxBus.getInstance().register(TAG)
 * .subscribe(new Consumer<Object>() {
 *
 * @Override public void accept(Object o) throws Exception {
 * <p>
 * }
 * }, new Consumer<Throwable>() {
 * @Override public void accept(Throwable throwable) throws Exception {
 * <p>
 * }
 * });
 */

public class RxBus {
    private static volatile RxBus instance;

    private RxBus() {
    }

    public static RxBus getInstance() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    private ConcurrentHashMap<Object, List<Subject>> subjectMapper = new ConcurrentHashMap<>();

    /**
     * 订阅事件源
     *
     * @return
     */
    public RxBus onEvent(Observable<?> mObservable, Consumer<Object> consumer) {
        mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });

        return getInstance();
    }

    /**
     * 注册事件源
     *
     * @return
     */
    public <T> Observable<T> register(@NonNull Object tag) {
        List<Subject> subjectList = subjectMapper.get(tag);
        if (subjectList == null) {
            subjectList = new ArrayList<>();
            subjectMapper.put(tag, subjectList);
        }

        Subject<T> subject;
        subjectList.add(subject = PublishSubject.create());
        return subject;
    }

    /**
     * 反注册事件源
     */
    public void unRegister(@NonNull Object tag) {
        List<Subject> subjectList = subjectMapper.get(tag);
        if (subjectList != null) {
            subjectMapper.remove(tag);
        }
    }

    /**
     * 反注册所有的事件源
     */
    public void unRegisterAll() {
        if (subjectMapper == null || subjectMapper.size() == 0)
            return;

        subjectMapper.clear();
    }

    /**
     * 反注册事件源
     */
    public RxBus unRegister(@NonNull Object tag, @NonNull Observable<?> observable) {
        if (observable == null) {
            return getInstance();
        }

        List<Subject> subjectList = subjectMapper.get(tag);
        if (subjectList != null) {
            subjectList.remove(observable);
            if (isEmpty(subjectList)) {
                subjectMapper.remove(tag);
            }
        }

        return getInstance();
    }

    public void post(@NonNull Object content) {
        post(content.getClass().getName(), content);
    }

    /**
     * 触发事件
     */

    public void post(@NonNull Object tag, @NonNull Object content) {
        List<Subject> subjectList = subjectMapper.get(tag);
        if (!isEmpty(subjectList)) {
            for (Subject subject : subjectList) {
                subject.onNext(content);
            }
        }
    }

    private boolean isEmpty(Collection<Subject> collection) {
        return null == collection || collection.isEmpty();
    }

}
