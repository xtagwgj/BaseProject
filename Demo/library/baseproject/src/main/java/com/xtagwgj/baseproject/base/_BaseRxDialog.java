package com.xtagwgj.baseproject.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.jakewharton.rxbinding2.view.RxView;
import com.trello.rxlifecycle2.components.support.RxDialogFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

import static com.xtagwgj.baseproject.constant.BaseConstants.THROTTLE_TIME;

/**
 * 对话框
 * Created by xtagwgj on 2017/7/26.
 */
public abstract class _BaseRxDialog extends RxDialogFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        return initContentView(inflater, getLayoutId(), null);
    }


    //获取Fragment的view，需要的时候可修改支持
    protected View initContentView(LayoutInflater inflater, @LayoutRes int layoutRes, @Nullable ViewGroup container) {
        return inflater.inflate(layoutRes, container);
    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Dialog dialog = new Dialog(getActivity(), R.style.ActionSheetDialogStyle);
//        View inflate = LayoutInflater.from(getActivity()).inflate(getLayoutId(), null);
//
//        dialog.setContentView(inflate);
//
//        Window dialogWindow = dialog.getWindow();
//        if (dialogWindow != null) {
//            WindowManager.LayoutParams lp = setDialogSetting(dialogWindow);
//            dialogWindow.setAttributes(lp);
//        }
//
//        return dialog;
//    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

    @NonNull
    public WindowManager.LayoutParams setDialogSetting(Window dialogWindow) {
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        return lp;
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected void clickEvent(View view, Consumer consumer) {
        if (view == null)
            return;

        RxView.clicks(view)
                .throttleFirst(THROTTLE_TIME, TimeUnit.MILLISECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(consumer);
    }
}
