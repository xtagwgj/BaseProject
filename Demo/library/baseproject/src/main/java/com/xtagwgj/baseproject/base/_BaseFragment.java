package com.xtagwgj.baseproject.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.jakewharton.rxbinding2.view.RxView;
import com.xtagwgj.baseproject.constant.BaseConstants;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Fragment基类
 * A simple {@link Fragment} subclass.
 */
public abstract class _BaseFragment extends com.trello.rxlifecycle2.components.support.RxFragment {

    protected View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
        }

        initView(savedInstanceState);
        initEventListener();

        return rootView;
    }


    //获取布局文件
    protected abstract int getLayoutId();

    //初始化view
    protected abstract void initView(Bundle savedInstanceState);

    //点击事件
    protected abstract void initEventListener();

    protected void clickEvent(View view, Consumer consumer) {
        if (view == null)
            return;

        RxView.clicks(view)
                .throttleFirst(BaseConstants.THROTTLE_TIME, TimeUnit.MILLISECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(consumer);
    }

    /**
     * 强制隐藏输入法键盘
     */
    protected void hideInput() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null)
            inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
    }
}
