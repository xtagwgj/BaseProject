package com.xtagwgj.baseproject.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.xtagwgj.baseproject.utils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 短信息的接收者
 * Created by xtagwgj on 2017/6/17.
 */

public class SmsReceiver extends BroadcastReceiver {
    private OnSmsCatchListener callback;
    private String phoneNumberFilter;
    private String messageFilter;
    private String codeFilter;

    /**
     * 回调监听
     *
     * @param callback OnSmsCatchListener
     */
    public void setCallback(OnSmsCatchListener callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage = getIncomingMessage(aPdusObj, bundle);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    if (phoneNumberFilter != null && !phoneNumber.equals(phoneNumberFilter)) {
                        return;
                    }
                    String message = currentMessage.getDisplayMessageBody();
                    if (messageFilter != null && !message.matches(messageFilter)) {
                        return;
                    }

                    if (callback != null) {
                        callback.onSmsCatch(message);
                        if (!StringUtils.isEmpty(codeFilter)) {
                            String code = parseCode(message);
                            if (!StringUtils.isEmpty(code))
                                callback.onSmsCodeCatch(parseCode(message));
                        }

                    }
                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }
    }

    private SmsMessage getIncomingMessage(Object aObject, Bundle bundle) {
        SmsMessage currentSMS;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String format = bundle.getString("format");
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject, format);
        } else {
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject);
        }
        return currentSMS;
    }

    /**
     * 设置发送手机号码的正则验证 通过的才会进行处理
     *
     * @param phoneNumberFilter 手机号
     */
    public void setPhoneNumberFilter(String phoneNumberFilter) {
        this.phoneNumberFilter = phoneNumberFilter;
    }

    /**
     * 设置信息内容的监听 不需要可不设置
     *
     * @param regularExpression regexp
     */
    public void setMessageFilter(String regularExpression) {
        this.messageFilter = regularExpression;
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

    public String parseCode(String message) {
        //Pattern pattern = Pattern.compile("(?<![0-9])([0-9]{" + 2 + "})(?![0-9])");
        Pattern pattern = Pattern.compile(codeFilter);
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(0);
        }

        return null;
    }

    public interface OnSmsCatchListener {
        void onSmsCatch(String message);

        void onSmsCodeCatch(String code);
    }
}