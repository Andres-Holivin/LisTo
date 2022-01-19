package com.example.listto.huaweiService;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;

public class PushMessage extends HmsMessageService {
    private static final String TAG = "PushLog";
    @Override
    public void onNewToken(String token) {
        Log.i(TAG, "received refresh token:" + token);

        if (!TextUtils.isEmpty(token)) {
            refreshedTokenToServer(token);
        }
    }
    // If the version of the Push SDK you integrated is 5.0.4.302 or later, you also need to override the method.
    @Override
    public void onNewToken(String token, Bundle bundle) {
        Log.i(TAG, "have received refresh token " + token);

        if (!TextUtils.isEmpty(token)) {
            refreshedTokenToServer(token);
        }
    }
    @Override
    public void onMessageReceived(RemoteMessage message) {
        Log.i(TAG, "onMessageReceived is called");
        if (message == null) {
            Log.e(TAG, "Received message entity is null!");
            return;
        }
        Log.i(TAG, "get Data: " + message.getData()
                + "\n getFrom: " + message.getFrom()
                + "\n getTo: " + message.getTo()
                + "\n getMessageId: " + message.getMessageId()
                + "\n getSendTime: " + message.getSentTime()
                + "\n getDataMap: " + message.getDataOfMap()
                + "\n getMessageType: " + message.getMessageType()
                + "\n getTtl: " + message.getTtl()
                + "\n getToken: " + message.getToken());
        Boolean judgeWhetherIn10s = false;
        if (judgeWhetherIn10s) {
            startWorkManagerJob(message);
        } else {
            processWithin10s(message);
        }
    }

    private void startWorkManagerJob(RemoteMessage message) {
        Log.d(TAG, "Start new job processing.");
    }
    private void processWithin10s(RemoteMessage message) {
        Log.d(TAG, "Processing now.");
    }

    private void refreshedTokenToServer(String token) {
        Log.i(TAG, "sending token to server. token:" + token);
    }

}
