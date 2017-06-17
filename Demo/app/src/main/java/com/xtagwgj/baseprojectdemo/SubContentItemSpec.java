package com.xtagwgj.baseprojectdemo;

import com.facebook.litho.Column;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.ComponentLayout;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.annotations.ResType;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaEdge;

/**
 * 编译后生成的类名为文件名去掉Spec
 * Layout Spec在逻辑上等同于Android的View的组合.它简单的把一些已经存在的component组合到一个不可变的布局树中.
 * Created by xtagwgj on 2017/6/17.
 */

@LayoutSpec
public class SubContentItemSpec {

    @OnCreateLayout
    static ComponentLayout onCreateLayout(
            ComponentContext c,
            @Prop(resType = ResType.COLOR) int color,
            @Prop(optional = true) String title,
            @Prop(optional = true) String subContent) {

        return Column.create(c)
                .paddingDip(YogaEdge.ALL, 16)
                .backgroundColor(color)
                .child(
                        Text.create(c)
                                .text(title)
                                .textSizeSp(18)
                                .textColor(c.getResources().getColor(R.color.colorAccent))
                                .build()
                )
                .child(
                        Text.create(c)
                                .text(subContent)
                                .textSizeSp(14)
                                .build()
                )
                .build();
    }
}
