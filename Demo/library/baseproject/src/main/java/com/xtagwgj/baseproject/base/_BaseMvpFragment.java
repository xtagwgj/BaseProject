package com.xtagwgj.baseproject.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.RxFragment;
import com.xtagwgj.baseproject.utils.TUtil;
import com.xtagwgj.baseproject.utils.ToastUtils;

import java.util.List;


/**
 * mvp的基类Fragment
 * Created by xtagwgj on 2017/3/22.
 */

public abstract class _BaseMvpFragment<P extends _BaseMvpPresenter, M extends _BaseMvpModel> extends RxFragment {
    public P mPresenter;
    public M mModel;
    public RxManager mRxManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(getLayoutId(), null);

        mRxManager = new RxManager();
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        if (mPresenter != null) {
            mPresenter.mContext = this.getActivity();
        }

        initView(savedInstanceState);
        initEventListener();
        initPresenter();
        return view;
    }


    //获取布局文件
    protected abstract int getLayoutId();

    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
    public abstract void initPresenter();

    //初始化view
    protected abstract void initView(@Nullable Bundle savedInstanceState);

    protected abstract void initEventListener();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null)
            mPresenter.onDestroy();
        mRxManager.clear();
    }

    protected void showToast(String msg) {
        ToastUtils.showShortToast(msg);
    }

    protected void showToast(int strId) {
        ToastUtils.showShortToast(getString(strId));
    }

    protected String nullStrToEmpty(String str) {
        return isEmpty(str) ? "" : str;
    }

    protected boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    protected boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }
}
