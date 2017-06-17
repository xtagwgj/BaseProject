package com.xtagwgj.baseprojectdemo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.facebook.litho.ComponentContext;
import com.facebook.litho.ComponentLayout;
import com.facebook.litho.Diff;
import com.facebook.litho.Output;
import com.facebook.litho.Size;
import com.facebook.litho.SizeSpec;
import com.facebook.litho.annotations.MountSpec;
import com.facebook.litho.annotations.OnCreateMountContent;
import com.facebook.litho.annotations.OnMeasure;
import com.facebook.litho.annotations.OnMount;
import com.facebook.litho.annotations.OnPrepare;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.annotations.ShouldUpdate;

import static android.R.attr.width;

/**
 * Mount Spec只有在你需要把自己的view/drawable集成到Component 框架中的时候才应当被创建
 * Created by xtagwgj on 2017/6/17.
 * <p>
 * mount spec component的生命周期如下:
 * <p>
 * 在布局计算之前,运行@OnPrepare一次
 * 在布局计算过程中,可选择的运行OnMeasure.
 * 在布局计算之后,运行@OnBoundsDefined一次.
 * 在component添加到托管视图之前,运行@OnCreateMountContent
 * 在component添加到托管视图之前,运行@OnMount
 * 在component添加到托管视图之后,运行@OnBind
 * 在从托管视图移除component之前,运行@OnUnBind
 * 在从托管视图移除component之前,可选择的运行@OnUnmount
 */

@MountSpec
public class ColorComponentSpec {

    /**
     * 可以通过把重操作(耗时操作)移动到@OnPrepare方法中,来减轻UI线程的压力.这个方法只会在布局计算前执行一次,并且可以在后台线程中执行.
     * 在@MountSpec方法中使用Output<?>会自动的创建一个输入在下一个阶段中.在这种情况下,一个@OnPrepare的输出就会在@OnMount中创建一个输入.
     */
    @OnPrepare
    static void onPrepare(
            Context context,
            @Prop String colorName,
            Output<Integer> color) {
        color.set(Color.parseColor(colorName));
    }

    /**
     * 需要在布局计算阶段定义如何测量你的component
     * 假设我们需要我们的ColorComponent有一个默认的宽度,并且当它的高度未定义的时候,能够强制执行一定的宽高比.
     */
    @OnMeasure
    static void onMeasure(
            ComponentContext context,
            ComponentLayout layout,
            int widthSpec,
            int heightSpec,
            Size size) {
        // If width is undefined, set default size.
        if (SizeSpec.getMode(widthSpec) == SizeSpec.UNSPECIFIED) {
            size.width = 40;
        } else {
            size.width = SizeSpec.getSize(widthSpec);
        }
        // If height is undefined, use 1.5 aspect ratio.
        if (SizeSpec.getMode(heightSpec) == SizeSpec.UNSPECIFIED) {
            size.height = (int) (width * 1.5);
        } else {
            size.height = SizeSpec.getSize(heightSpec);
        }
    }

    /**
     * onCreateMountContent的返回类型应该始终和onMount的第二个参数的类型相一致。它必须为View或Drawable的子类。这在编译时由注释处理器去验证。
     * onCreateMountContent不能使用@Prop或任何其他带注释的参数。
     */
    @OnCreateMountContent
    static ColorDrawable onCreateMountContent(ComponentContext c) {
        return new ColorDrawable();
    }

    /**
     * 挂载总是发生在主线程中因为它可能需要处理Android Views(它们被绑定在主线程中).
     * 鉴于@OnMount方法始终在UI线程中运行，因此不应执行耗时的操作。
     */
    @OnMount
    static void onMount(ComponentContext context,
                        ColorDrawable colorDrawable,
                        @Prop String colorName) {
        colorDrawable.setColor(Color.parseColor(colorName));
    }

    /**
     * 使用@ShouldUpdate注释定义一个方法来避免在更新时进行重新测试和重新挂载。
     * ShouldUpdate的调用的前提是component是”纯渲染函数”。
     * 一个组件如果是纯渲染函数,那么它的渲染结果只取决于它的prop和状态.这意味着在@OnMount期间，组件不应该访问任何可变的全局变量。
     * 一个@MountSpec可以通过使用@MountSpec注释的pureRender参数来定自己为”纯渲染的”。只有纯渲染的Component可以假设当prop不更改时就不需要重新挂载。
     */
    @ShouldUpdate
    public boolean shouldUpdate(Diff<String> someStringDiff) {
        return !someStringDiff.getPrevious().equals(someStringDiff.getNext());
    }

}
