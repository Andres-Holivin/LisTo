package com.example.listto.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.listto.MainActivity;
import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.push.HmsMessaging;

public class PushMessage {
    private static final String TAG = "PushDemoLog";
    private Context context;
    private String tokenHms="";

    public PushMessage(Context context) {
        this.context = context;
    }

    private void pushMsg(String token){
        try {
            HmsMessaging.getInstance(context)
                    .subscribe(token)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.e(TAG,"subscribe Complete");
                            } else {
                                Log.e(TAG,"subscribe failed: ret=" + task.getException().getMessage());
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG,"subscribe failed: exception=" + e.getMessage());
        }
    }
    private void getToken() {
        Log.e(TAG,"getToken:begin");
        new Thread() {
            @Override
            public void run() {
                try {
                    // read from agconnect-services.json
                    String appId = "105260341";
                    String token = HmsInstanceId.getInstance(context).getToken(appId, "HCM");
                    Log.i(TAG, "get token:" + token);
                    if(!TextUtils.isEmpty(token)) {
                        sendRegTokenToServer(token);
                    }
                    Log.e(TAG,"get token:" + token);
                    tokenHms=token;
                } catch (ApiException e) {
                    Log.e(TAG, "get token failed, " + e);
                }
            }
        }.start();
    }
    private void sendRegTokenToServer(String token) {
        Log.i(TAG, "sending token to server. token:" + token);
    }
}
