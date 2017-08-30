package com.xtagwgj.baseproject.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import com.xtagwgj.baseproject.R;
import com.xtagwgj.baseproject.utils.AndroidDeviceUtil;
import com.xtagwgj.baseproject.widget.StatusBarUtil;

/**
 * activity的基类
 * Created by xtagwgj on 2017/5/10.
 */
public abstract class _BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doBeforeSetContentView();

        initContentView(getLayoutId());

        initView(savedInstanceState);
        initEventListener();
    }

    /**
     * 设置layout前配置
     */
    protected void doBeforeSetContentView() {

        Activity currAty = AppManager.getAppManager().currentActivity();
        // 把activity放到栈中管理
        AppManager.getAppManager().addActivity(this);
        if (currAty != null && AndroidDeviceUtil.getSDKVersion() < Build.VERSION_CODES.M) {
            currAty.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }

        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //设置状态栏颜色
        initStatusBar();

    }


    /*********************
     * 子类实现
     *****************************/
    //获取布局文件
    public abstract int getLayoutId();

    //初始化view
    public abstract void initView(@Nullable Bundle savedInstanceState);

    //初始化点击事件
    public abstract void initEventListener();

    //设置内容的视图，方便dataBinding使用
    protected void initContentView(@LayoutRes int layoutResID) {
        setContentView(layoutResID);
    }

    protected void initStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary), 0);
    }

    /**
     * 强制隐藏输入法键盘
     */
    protected void hideInput() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null)
            inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    @CallSuper
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        if (AndroidDeviceUtil.getSDKVersion() < Build.VERSION_CODES.M) {
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        }

    }
}