package com.xtagwgj.baseprojectdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xtagwgj.baseproject.receiver.PhoneReceiver;
import com.xtagwgj.baseproject.utils.LogUtils;

public class MainActivity extends AppCompatActivity implements PhoneReceiver.PhoneListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        PhoneReceiver phoneReceiver = new PhoneReceiver();
        phoneReceiver.registerReceiver(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPhoneStateChanged(PhoneReceiver.CallState state, String number) {
        LogUtils.e("MainPhone", number + "-->" + state);
    }
}

