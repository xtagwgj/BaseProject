package com.xtagwgj.baseproject.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.xtagwgj.baseproject.base.AppManager;
import com.xtagwgj.baseproject.R;
import com.xtagwgj.baseproject.constant.BaseConstants;
import com.xtagwgj.baseproject.utils.StringUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * 标题栏
 * Created by xtagwgj on 2017/5/12.
 */

public class TitleLayout extends RelativeLayout {

    //返回按钮、更过按钮
    private ImageView backImageView, moreImageView;

    //标题栏
    private TextView titleTextView;

    //标题栏
    private RelativeLayout titleLayout;

    //是否显示返回按钮
    private boolean showBackImageView = true;

    //返回按钮的图片资源
    private int backRes = R.mipmap.btn_arrow_back;

    //是否显示更多按钮
    private boolean showMoreImageView = false;

    //更多按钮的资源
    private int moreRes = R.mipmap.btn_add_new;

    //标题的背景色
    private int bgColor = R.color.colorPrimaryDark;

    public TitleLayout(Context context) {
        this(context, null);
    }

    public TitleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.title, this);
        backImageView = (ImageView) rootView.findViewById(R.id.backArrowImageView);
        moreImageView = (ImageView) rootView.findViewById(R.id.rightImageView);
        titleTextView = (TextView) rootView.findViewById(R.id.titleTextView);
        titleLayout = (RelativeLayout) rootView.findViewById(R.id.titleLayout);

        if (isInEditMode())
            return;

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.TitleLayout, defStyle, 0);
        if (array != null) {
            showBackImageView = array.getBoolean(R.styleable.TitleLayout_arrowImgShow, true);
            backRes = array.getResourceId(R.styleable.TitleLayout_arrowImgRes, backRes);
            showMoreImageView = array.getBoolean(R.styleable.TitleLayout_rightImgShow, false);
            moreRes = array.getResourceId(R.styleable.TitleLayout_rightImgRes, moreRes);
            bgColor = array.getColor(R.styleable.TitleLayout_bgColorRes, getResources().getColor(bgColor));

            titleLayout.setBackgroundColor(bgColor);

            String titleName = array.getString(R.styleable.TitleLayout_titleName);
            setTitleText(StringUtils.null2Length0(titleName));

            array.recycle();
        }


        //设置返回按钮的状态
        setBackImageViewRes(backRes);
        setBackImageViewVisiable(showBackImageView);
        setBackClickListener(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {

                Activity activity = AppManager.getAppManager().currentActivity();
                activity.setResult(Activity.RESULT_CANCELED);
                AppManager.getAppManager().finishActivity(activity);
                activity.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            }
        });

        //设置更多按钮的状态
        setMoreImageViewRes(moreRes);
        setMoreImageViewVisiable(showMoreImageView);
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitleText(@NonNull String title) {
        if (titleTextView != null)
            titleTextView.setText(StringUtils.null2Length0(title));
    }

    /**
     * 设置标题
     *
     * @param titleResId 标题id
     */
    public void setTitleText(@StringRes int titleResId) {
        if (titleTextView != null)
            titleTextView.setText(titleResId);
    }

    /**
     * 设置返回按钮的图像
     *
     * @param resId 返回按钮资源
     */
    public void setBackImageViewRes(@DrawableRes int resId) {
        setImageRes(backImageView, resId);
    }

    /**
     * 返回按钮的点击事件
     *
     * @param backClickConsumer 点击响应的事件
     */
    public void setBackClickListener(@NonNull Consumer backClickConsumer) {
        if (backImageView == null)
            return;

        clickEvent(backImageView, backClickConsumer);
    }

    /**
     * 设置更多按钮的图片
     *
     * @param resId 更多按钮的资源
     */
    public void setMoreImageViewRes(@DrawableRes int resId) {
        setImageRes(moreImageView, resId);
    }

    /**
     * 设置更多按钮的点击事件
     *
     * @param consumer 点击响应的事件
     */
    public void setMoreClickListener(@NonNull Consumer consumer) {
        if (moreImageView == null)
            return;

        clickEvent(moreImageView, consumer);
    }

    /**
     * 设置返回按钮是否可见
     *
     * @param visibility 是否可见
     */
    public void setBackImageViewVisiable(boolean visibility) {
        if (backImageView != null)
            backImageView.setVisibility(visibility ? VISIBLE : INVISIBLE);
    }

    /**
     * 设置更多按钮是否可见
     *
     * @param visibility 是否可见
     */
    public void setMoreImageViewVisiable(boolean visibility) {
        if (moreImageView != null)
            moreImageView.setVisibility(visibility ? VISIBLE : INVISIBLE);
    }


    private void setImageRes(@NonNull ImageView imageView, @DrawableRes int resId) {
        imageView.setImageResource(resId);
    }


    private void clickEvent(@NonNull View view, @NonNull Consumer consumer) {
        RxView.clicks(view)
                .throttleFirst(BaseConstants.THROTTLE_TIME, TimeUnit.MILLISECONDS)
                .subscribe(consumer);
    }

}