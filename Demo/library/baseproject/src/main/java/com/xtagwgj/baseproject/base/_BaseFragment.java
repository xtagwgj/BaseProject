package com.xtagwgj.baseproject.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

/**
 * Fragment基类
 * A simple {@link Fragment} subclass.
 */
public abstract class _BaseFragment extends Fragment {

    protected View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = initContentView(getLayoutId(), inflater, container);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(savedInstanceState);
        initEventListener();
    }

    //获取Fragment的view，需要的时候可修改支持
    protected View initContentView(@LayoutRes int layoutRes, LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(layoutRes, container, false);
    }

    //获取布局文件
    protected abstract int getLayoutId();

    //初始化view
    protected abstract void initView(@Nullable Bundle savedInstanceState);

    //点击事件
    protected abstract void initEventListener();

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
