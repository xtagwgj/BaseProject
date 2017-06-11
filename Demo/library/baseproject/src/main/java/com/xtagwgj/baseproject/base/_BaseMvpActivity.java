package com.xtagwgj.baseproject.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Window;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.xtagwgj.baseproject.R;
import com.xtagwgj.baseproject.utils.StringUtils;
import com.xtagwgj.baseproject.utils.TUtil;
import com.xtagwgj.baseproject.utils.ToastUtils;
import com.xtagwgj.baseproject.widget.StatusBarCompat;
import com.xtagwgj.baseproject.widget.daynightmode.ChangeModeController;

import java.util.List;

/**
 * mvp的基类Activity
 * Created by xtagwgj on 2017/4/9.
 */


public abstract class _BaseMvpActivity<P extends _BaseMvpPresenter, M extends _BaseMvpModel> extends RxAppCompatActivity {

    protected P mPresenter;
    protected M mModel;
    protected Context mContext;
    protected RxManager mRxManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxManager = new RxManager();
        doBeforeSetContentView();
        initDataBindingWithLayout();

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

    /**
     * 设置layout前配置
     */
    protected void doBeforeSetContentView() {
        //设置昼夜主题
        initTheme();

        // 把actvity放到application栈中管理
        AppManager.getAppManager().addActivity(this);

        // 无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 设置横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // 默认着色状态栏
//        SetStatusBarColor();
        SetTranslanteBar();
    }

    /**
     * 设置主题
     */
    private void initTheme() {
        ChangeModeController.setTheme(this, R.style.DayTheme, R.style.NightTheme);
    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor() {
        SetStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor(int color) {
        StatusBarCompat.setStatusBarColor(this, color);
    }

    /**
     * 沉浸状态栏（4.4以上系统有效）
     */
    protected void SetTranslanteBar() {
        StatusBarCompat.translucentStatusBar(this);
    }


    protected abstract void initDataBindingWithLayout();

    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
    public abstract void initPresenter();

    //初始化view
    public abstract void initView(Bundle savedInstanceState);

    //初始化点击事件
    public abstract void initEventListener();

    protected void showToast(String msg) {
//        Snackbar.make(getCurrentFocus(), msg, Snackbar.LENGTH_SHORT).show();
        ToastUtils.showShortToast(this, nullStrToEmpty(msg));
    }

    protected void showToast(int strId) {
//        Snackbar.make(getCurrentFocus(), strId, Snackbar.LENGTH_SHORT).show();
        ToastUtils.showShortToast(this, getString(strId));
    }

    protected String nullStrToEmpty(String str) {
        return StringUtils.null2Length0(str);
    }

    protected boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    protected boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.onDestroy();
        mRxManager.clear();
        AppManager.getAppManager().finishActivity(this);
    }
}
