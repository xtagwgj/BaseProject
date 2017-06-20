package com.xtagwgj.baseproject.base;

import android.app.ActionBar;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.jakewharton.rxbinding2.view.RxView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.xtagwgj.baseproject.R;
import com.xtagwgj.baseproject.constant.BaseConstants;
import com.xtagwgj.baseproject.utils.StringUtils;
import com.xtagwgj.baseproject.utils.ToastUtils;
import com.xtagwgj.baseproject.view.loadingdialog.view.LoadingDialog;
import com.xtagwgj.baseproject.widget.StatusBarUtil;
import com.xtagwgj.baseproject.widget.daynightmode.ChangeModeController;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * activity的基类
 * Created by xtagwgj on 2017/5/10.
 */

public abstract class _BaseActivity extends RxAppCompatActivity {

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        doBeforeSetContentView();
        setContentView(getLayoutId());
        doAfterSetContentView();

        initView(savedInstanceState);
        initEventListener();
    }

    protected void initStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary), 0);
    }

    /**
     * 设置layout前配置
     */
    protected void doBeforeSetContentView() {
        //设置昼夜主题
        initTheme();

        // 把activity放到栈中管理
        AppManager.getAppManager().addActivity(this);

        //设置没有标题栏和ActionBar
        setNoTitleOrActionBar();

        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //设置状态栏颜色
        initStatusBar();
    }

    protected void doAfterSetContentView() {
        //设置状态栏颜色
        initStatusBar();
    }

    /**
     * 没有标题栏和ActionBar
     */
    private void setNoTitleOrActionBar() {
        // 无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        ActionBar actionBar = getActionBar();
        if (actionBar != null)
            actionBar.hide();

        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null)
            supportActionBar.hide();
    }

    /*********************
     * 子类实现
     *****************************/
    //获取布局文件
    public abstract int getLayoutId();

    //初始化view
    public abstract void initView(Bundle savedInstanceState);

    //初始化点击事件
    public abstract void initEventListener();


    /**
     * 设置主题
     */
    protected void initTheme() {
        ChangeModeController.setTheme(this, R.style.DayTheme, R.style.NightTheme);
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

    protected void clickEvent(View view, Consumer consumer) {
        if (view == null || consumer == null)
            return;

        RxView.clicks(view)
                .throttleFirst(BaseConstants.THROTTLE_TIME, TimeUnit.MILLISECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(consumer);
    }

    protected void showLoadingDialog(int loadingTextRes, int successTextRes) {
        showLoadingDialog(getString(loadingTextRes), getString(successTextRes));
    }

    protected void showLoadingDialog(String loadingText, String successText) {
        showLoadingDialog(loadingText, successText, false);
    }

    protected void showLoadingDialog(String loadingText, String successText, boolean interceptBack) {
        closeLoadingDialog();

        loadingDialog = new LoadingDialog(this);
        loadingDialog.setLoadingText(loadingText)
                .setSuccessText(successText)
                .setInterceptBack(interceptBack)
                .closeSuccessAnim()
                .show();
    }


    protected void setFailLoadingText(String failLoadingText) {
        if (loadingDialog != null)
            loadingDialog.setFailedText(failLoadingText);
    }

    protected void closeLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.close();
            loadingDialog = null;
        }
    }

    protected void successLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadingDialog != null) {
                    loadingDialog.loadSuccess();
                }
            }
        });

    }

    protected void failLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadingDialog != null)
                    loadingDialog.loadFailed();

            }
        });
    }

    protected void showToast(int resId) {
//        Snackbar.make(getCurrentFocus(), msg, Snackbar.LENGTH_SHORT).show();
        ToastUtils.showShortToast(this, getString(resId));
    }

    protected void showToast(String msg) {
        ToastUtils.showShortToast(this, StringUtils.null2Length0(msg));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}