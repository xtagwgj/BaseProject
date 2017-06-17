package com.xtagwgj.baseproject.receiver;

import android.Manifest;
import android.app.Activity;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

/**
 * 短信验证
 * Created by xtagwgj on 2017/6/17.
 */
/*

        smsVerifyCatcher = new SmsVerifyCatcher(this, new SmsReceiver.OnSmsCatchListener() {
            @Override
            public void onSmsCatch(String message) {
                LogUtils.d("onSmsCatch", message);
            }

            @Override
            public void onSmsCodeCatch(String code) {
                LogUtils.d("onSmsCodeCatch", code);
            }
        });
        //手机号过滤
        smsVerifyCatcher.setPhoneNumberFilter();
        //消息过滤
        smsVerifyCatcher.setMessageFilter();
        //验证码的正则表达式
        smsVerifyCatcher.setCodeFilter("(?<![0-9])([0-9]{4})(?![0-9])");

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

 */
public class SmsVerifyCatcher {
    private final static int PERMISSION_REQUEST_CODE = 12;

    private Activity activity;
    private Fragment fragment;
    private SmsReceiver.OnSmsCatchListener onSmsCatchListener;
    private SmsReceiver smsReceiver;
    private String phoneNumber;
    private String messageFilter;
    private String codeFilter;

    public SmsVerifyCatcher(Activity activity, SmsReceiver.OnSmsCatchListener onSmsCatchListener) {
        this.activity = activity;
        this.onSmsCatchListener = onSmsCatchListener;
        smsReceiver = new SmsReceiver();
        smsReceiver.setCallback(this.onSmsCatchListener);
    }

    public SmsVerifyCatcher(Activity activity, Fragment fragment, SmsReceiver.OnSmsCatchListener onSmsCatchListener) {
        this(activity, onSmsCatchListener);
        this.fragment = fragment;
    }

    public void onStart() {
        if (isStoragePermissionGranted(activity, fragment)) {
            registerReceiver();
        }
    }

    private void registerReceiver() {
        smsReceiver = new SmsReceiver();
        smsReceiver.setCallback(onSmsCatchListener);
        smsReceiver.setPhoneNumberFilter(phoneNumber);
        smsReceiver.setMessageFilter(messageFilter);
        smsReceiver.setCodeFilter(codeFilter);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        activity.registerReceiver(smsReceiver, intentFilter);
    }


    public void onStop() {
        try {
            activity.unregisterReceiver(smsReceiver);
        } catch (IllegalArgumentException ignore) {
            //receiver not registered
        }
    }

    /**
     * 设置发送手机号码的正则验证 通过的才会进行处理
     *
     * @param phoneNumber 手机号
     */
    public void setPhoneNumberFilter(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * 设置信息内容的监听 不需要可不设置
     *
     * @param regexp regexp
     */
    public void setMessageFilter(String regexp) {
        this.messageFilter = regexp;
    }

    /**
     * 设置验证码的正则
     *
     * @param codeFilter 验证码的正则表达式 为null将不回调onSmsCodeCatch
     *                   "(?<![0-9])([0-9]{xx})(?![0-9])" 表示长度为xx的全为数字的验证码
     */
    public void setCodeFilter(String codeFilter) {
        this.codeFilter = codeFilter;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                registerReceiver();
            }
        }
    }

    //For fragments
    public static boolean isStoragePermissionGranted(Activity activity, Fragment fragment) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECEIVE_SMS)
                    == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS)
                            == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                if (fragment == null) {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.RECEIVE_SMS,
                                    Manifest.permission.READ_SMS}, PERMISSION_REQUEST_CODE);
                } else {
                    fragment.requestPermissions(
                            new String[]{Manifest.permission.RECEIVE_SMS,
                                    Manifest.permission.READ_SMS}, PERMISSION_REQUEST_CODE);
                }
                return false;
            }
        } else {
            return true;
        }
    }
}