package com.xtagwgj.baseprojectdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xtagwgj.baseproject.utils.NetWorkUtils;
import com.xtagwgj.baseproject.view.NumberRunningTextView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private NumberRunningTextView viewById;

    private Boolean isNetConn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        viewById = (NumberRunningTextView) findViewById(R.id.numberRunning);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        registerReceiver(mReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            NetWorkUtils.isAvailableByPing()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            isNetConn = aBoolean;
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                        }
                    });
        }
    };
}
