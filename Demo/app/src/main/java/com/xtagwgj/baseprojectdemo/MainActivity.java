package com.xtagwgj.baseprojectdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.xtagwgj.baseproject.receiver.SmsReceiver;
import com.xtagwgj.baseproject.receiver.SmsVerifyCatcher;
import com.xtagwgj.baseproject.utils.LogUtils;
import com.xtagwgj.baseproject.view.Sneaker;

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

                Sneaker.with(MainActivity.this)
                        .setTitle("Success!")
                        .setMessage("Get SmsCode Success! resultCode = " + code)
                        .sneakSuccess();
            }
        });

        //手机号过滤
//        smsVerifyCatcher.setPhoneNumberFilter();
        //消息过滤
//        smsVerifyCatcher.setMessageFilter();
        //验证码的正则表达式
        smsVerifyCatcher.setCodeFilter("(?<![0-9])([0-9]{4})(?![0-9])");


        findViewById(R.id.numberRunning).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sneaker.with(MainActivity.this)
                        .setTitle("Warning!", R.color.day_background_color)
                        .setMessage("Try to get smsCode !!!",  R.color.day_background_color)
                        .setDuration(6000)
                        .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
//                        .setIcon(R.mipmap.ic_launcher)
                        .sneakError();
            }
        });


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