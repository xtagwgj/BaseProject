package com.xtagwgj.baseprojectdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;

import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.ComponentInfo;
import com.facebook.litho.LithoView;
import com.facebook.litho.widget.LinearLayoutInfo;
import com.facebook.litho.widget.Recycler;
import com.facebook.litho.widget.RecyclerBinder;
import com.facebook.soloader.SoLoader;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        findViewById(R.id.numberRunning).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Sneaker.with(MainActivity.this)
//                        .setTitle("Warning!", R.color.day_background_color)
//                        .setMessage("Try to get smsCode !!!", R.color.day_background_color)
//                        .setDuration(6000)
//                        .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
//                        .sneakError();
//            }
//        });

        //初始化
        SoLoader.init(this, false);

        final ComponentContext c = new ComponentContext(this);

//        LithoView lithoView = LithoView.create(
//                this,
//                Text.create(c)
//                        .text("add a predefined Text Litho component to an activity")
//                        .textSizeSp(18)
//                        .textColor(getResources().getColor(android.R.color.black))
//                        .build()
//        );
//        setContentView(lithoView);

        //列表的item 单个自定义的Component
//        Component listItem = SubContentItem.create(c).build();
//        setContentView(LithoView.create(this, listItem));


        //创建一个列表
        RecyclerBinder recyclerBinder = new RecyclerBinder(c, new LinearLayoutInfo(this, OrientationHelper.VERTICAL, false));
        Component<Recycler> recyclerComponent = Recycler.create(c).binder(recyclerBinder).build();
        //使用item填充binder上
        addContent(recyclerBinder, c);

        setContentView(LithoView.create(this, recyclerComponent));

    }

    /**
     * 给recycler添加数据
     */
    private static void addContent(RecyclerBinder recyclerBinder, ComponentContext context) {
        for (int i = 0; i < 32; i++) {
            recyclerBinder.insertItemAt(
                    i,
                    ComponentInfo.create()
                            .component(
                                    SubContentItem.create(context)
                                            .color(i % 2 == 0 ? Color.WHITE : Color.LTGRAY)
                                            .title("Your First Custom Component " + (i + 1))
                                            .subContent("Ready? It’s time to dive in and build this component. In Litho, you write Spec classes to declare the layout for your components. The framework then generates the underlying component class that you use in your code to create a component instance.")
                                            .build()
                            )
                            .build());
        }
    }
}