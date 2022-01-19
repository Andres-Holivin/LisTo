package com.example.listto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.listto.model.DatabaseHandler;
import com.example.listto.model.TaskModel;
import com.example.listto.util.CreateSchedule;
import com.example.listto.util.NotificationUtil;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.push.HmsMessaging;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private AccountAuthService mAuthService;
    private AccountAuthParams mAuthParam;
    private static final int REQUEST_CODE_SIGN_IN = 1003;
    private static final String TAG = "Account";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.HuaweiIdAuthButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuthParam = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                        .setIdToken()
                        .setAccessToken()
                        .createParams();
                Log.e("Auth Param", String.valueOf(mAuthParam.getSignInParams()));
                mAuthService = AccountAuthManager.getService(MainActivity.this, mAuthParam);
                startActivityForResult(mAuthService.getSignInIntent(), REQUEST_CODE_SIGN_IN);
            }
        });
    }
    private void setReceiveNotifyMsg(final boolean enable) {
        showLog("Control the display of notification messages:begin");
        if (enable) {
            HmsMessaging.getInstance(this).turnOnPush().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    if (task.isSuccessful()) {
                        showLog("turnOnPush Complete");
                    } else {
                        showLog("turnOnPush failed: cause=" + task.getException().getMessage());
                    }
                }
            });
        } else {
            HmsMessaging.getInstance(this).turnOffPush().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    if (task.isSuccessful()) {
                        showLog("turnOffPush Complete");
                    } else {
                        showLog("turnOffPush  failed: cause =" + task.getException().getMessage());
                    }
                }
            });
        }
    }
    public void showLog(String text){
        Log.e(TAG, text );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
            if (authAccountTask.isSuccessful()) {
                AuthAccount authAccount = authAccountTask.getResult();
                Log.i(TAG, authAccount.getDisplayName() + " signIn success ");
                Log.i(TAG, "AccessToken: " + authAccount.getAccessToken());
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            } else {
                Log.e(TAG, "sign in failed : " +((ApiException)authAccountTask.getException()).getStatusCode());
            }
        }
    }

    private void initDb(){
        //        DatabaseHandler db=DatabaseHandler.getInstance(this);
////        Log.d("Insert: ", "Inserting ..");
////        db.addNewTask(new TaskModel("test", "test",new Date(),000000,0));
////        db.addNewTask(new TaskModel("test 1", "test",new Date(),000000,1));
////        db.addNewTask(new TaskModel("test 2", "test",new Date(),000000,0));
////        db.addNewTask(new TaskModel("test 3", "test",new Date(),000000,1));
//
//        // Reading all contacts
//        Log.d("Reading: ", "Reading all contacts..");
//        Vector<TaskModel> tasks = db.getAllTask();
//
//        for (TaskModel task : tasks) {
//            String log = task.getId()+" "+task.getTopic()+" "+task.getSchedule();
//            Log.d("Name: ", log);
//        }
    }
}