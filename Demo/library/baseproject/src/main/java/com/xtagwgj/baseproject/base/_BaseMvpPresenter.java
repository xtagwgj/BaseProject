package com.xtagwgj.baseproject.base;

import android.content.Context;

import com.xtagwgj.baseproject.RxManager;

/**
 * Created by xtagwgj on 2017/3/20.
 */

public abstract class _BaseMvpPresenter<V extends _BaseMvpView, M extends _BaseMvpModel> {
    public Context mContext;
    public M mModel;
    public V mView;
    public RxManager mRxManage = new RxManager();

    public void setVM(V v, M m) {
        this.mView = v;
        this.mModel = m;
        this.onStart();
    }

    public void onStart() {
    }

    public void onDestroy() {
        mRxManage.clear();
    }
}
