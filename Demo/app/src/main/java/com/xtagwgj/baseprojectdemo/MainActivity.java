package com.xtagwgj.baseprojectdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xtagwgj.baseproject.view.NumberRunningTextView;

public class MainActivity extends AppCompatActivity {

    private NumberRunningTextView viewById;

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



    }
}
