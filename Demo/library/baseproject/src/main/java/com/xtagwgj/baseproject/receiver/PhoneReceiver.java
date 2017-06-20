package com.xtagwgj.baseproject.receiver;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.xtagwgj.baseproject.utils.StringUtils;

/**
 * 在Android M 以上系统记得添加权限请求
 * new RxPermissions(this)
 * .requestEach(Manifest.permission.PROCESS_OUTGOING_CALLS)
 * .subscribe(new Consumer<Permission>() {
 *
 * @Override public void accept(Permission permission) throws Exception {
 * <p>
 * }
 * });
 *
 * <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
 * <uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS" />
 * <p/>
 * action: android.intent.action.PHONE_STATE;  android.intent.action.NEW_OUTGOING_CALL;
 * <p/>
 * 去电时：
 * 未接：phone_state=OFFHOOK;
 * 挂断：phone_state=IDLE
 * 来电时:
 * 未接：phone_state=RINGING
 * 已接：phone_state=OFFHOOK;
 * <p>
 * Created by xtagwgj on 2017/6/19.
 */

public class PhoneReceiver extends BroadcastReceiver {


    private static final String TAG = "PhoneReceiver";

    private static final String NEW_OUTGOING_CALL = Intent.ACTION_NEW_OUTGOING_CALL;
    private PhoneListener phoneListener;
    private boolean isDialOut;
    private String number;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (NEW_OUTGOING_CALL.equals(intent.getAction())) {
            isDialOut = true;
            String outNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            if (!StringUtils.isEmpty(outNumber)) {
                this.number = outNumber;
            }
            if (phoneListener != null) {
                phoneListener.onPhoneStateChanged(CallState.Outgoing, number);
            }
        } else {

            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            tm.listen(new PhoneStateListener() {
                @Override
                public void onCallStateChanged(int state, String incomingNumber) {
                    super.onCallStateChanged(state, incomingNumber);

                    switch (state) {
                        case TelephonyManager.CALL_STATE_RINGING:
                            isDialOut = false;
                            //输出来电号码
                            number = incomingNumber;
                            System.out.println("响铃:来电号码" + incomingNumber);

                            if (phoneListener != null) {
                                phoneListener.onPhoneStateChanged(CallState.IncomingRing, number);
                            }

                            break;

                        case TelephonyManager.CALL_STATE_IDLE:
                            System.out.println("挂断");
                            if (phoneListener != null) {
                                phoneListener.onPhoneStateChanged(
                                        isDialOut ? CallState.OutgoingEnd : CallState.IncomingEnd,
                                        number);
                            }

                            break;
                        case TelephonyManager.CALL_STATE_OFFHOOK:
                            System.out.println("接听");
                            if (!isDialOut && phoneListener != null) {
                                phoneListener.onPhoneStateChanged(CallState.Incoming, number);
                            }
                            break;

                    }
                }
            }, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    /**
     * 去电时：
     * 未接：phone_state=OFFHOOK;
     * 挂断：phone_state=IDLE
     * 来电时:
     * 未接：phone_state=RINGING
     * 已接：phone_state=OFFHOOK;
     * 挂断：phone_state=IDLE
     */
    public void registerReceiver(Activity context, PhoneListener phoneListener) {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.intent.action.PHONE_STATE");
            filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
            filter.setPriority(Integer.MAX_VALUE);
            context.registerReceiver(this, filter);
            this.phoneListener = phoneListener;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unRegisterReceiver(Activity context) {
        try {
            context.unregisterReceiver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface PhoneListener {
        void onPhoneStateChanged(CallState state, String number);
    }

    /**
     * 分别是：
     * <p/>
     * 播出电话
     * 播出电话结束
     * 接入电话铃响
     * 接入通话中
     * 接入通话完毕
     */
    public enum CallState {
        Outgoing,
        OutgoingEnd,
        IncomingRing,
        Incoming,
        IncomingEnd
    }

}
