package com.xtagwgj.baseprojectdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.xtagwgj.baseproject.receiver.SmsReceiver;
import com.xtagwgj.baseproject.receiver.SmsVerifyCatcher;
import com.xtagwgj.baseproject.utils.LogUtils;

public class MainActivity extends AppCompatActivity {

    SmsVerifyCatcher smsVerifyCatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
//        smsVerifyCatcher.setPhoneNumberFilter();
        //消息过滤
//        smsVerifyCatcher.setMessageFilter();
        //验证码的正则表达式
        smsVerifyCatcher.setCodeFilter("(?<![0-9])([0-9]{4})(?![0-9])");
    }

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

}