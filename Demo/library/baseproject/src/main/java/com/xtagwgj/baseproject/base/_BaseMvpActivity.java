package com.xtagwgj.baseproject.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xtagwgj.baseproject.utils.EmptyUtils;
import com.xtagwgj.baseproject.utils.StringUtils;
import com.xtagwgj.baseproject.utils.TUtil;

import java.util.List;

/**
 * mvp的基类Activity
 * Created by xtagwgj on 2017/4/9.
 */

public abstract class _BaseMvpActivity<P extends _BaseMvpPresenter, M extends _BaseMvpModel> extends _BaseActivity {

    protected P mPresenter;
    protected M mModel;
    protected Context mContext;
    protected RxManager mRxManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxManager = new RxManager();

        doBeforeSetContentView();
        setContentView(getLayoutId());
        doAfterSetContentView();

        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);

        mContext = this;
        if (mPresenter != null) {
            mPresenter.mContext = this;
        }

        this.initView(savedInstanceState);
        this.initEventListener();
        this.initPresenter();
    }

    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
    public abstract void initPresenter();

    protected String nullStrToEmpty(String str) {
        return StringUtils.null2Length0(str);
    }

    protected boolean isEmpty(String str) {
        return EmptyUtils.isEmpty(str);
    }

    protected boolean isEmpty(List list) {
        return EmptyUtils.isEmpty(list);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null)
            mPresenter.onDestroy();
        mRxManager.clear();
        super.onDestroy();
    }
}