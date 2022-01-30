package com.example.listto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.banner.BannerView;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.push.HmsMessaging;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;

import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private AccountAuthService mAuthService;
    private AccountAuthParams mAuthParam;
    private static final int REQUEST_CODE_SIGN_IN = 1003;
    private static final String TAG = "Account";
    private BannerView defaultBannerView;
    private static final int REFRESH_TIME = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HwAds.init(this);
        loadDefaultBannerAd();

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
    private void loadDefaultBannerAd() {
        defaultBannerView=new BannerView(this);
        // Obtain BannerView based on the configuration in layout/activity_main.xml.
        defaultBannerView = findViewById(R.id.hw_banner_view);
        defaultBannerView.setAdListener(adListener);
        defaultBannerView.setBannerRefresh(REFRESH_TIME);
        defaultBannerView.setBannerRefresh(30);
//        defaultBannerView.setAdId("teste9ih9j0rc3");
        defaultBannerView.setBannerAdSize(BannerAdSize.BANNER_SIZE_360_57);

        AdParam adParam = new AdParam.Builder().build();
        defaultBannerView.loadAd(adParam);
    }
    private AdListener adListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            // Called when an ad is loaded successfully.
            showToast("Ad loaded.");
        }

        @Override
        public void onAdFailed(int errorCode) {
            // Called when an ad fails to be loaded.
            showToast(String.format(Locale.ROOT, "Ad failed to load with error code %d.", errorCode));
        }

        @Override
        public void onAdOpened() {
            // Called when an ad is opened.
            showToast(String.format("Ad opened "));
        }

        @Override
        public void onAdClicked() {
            // Called when a user taps an ad.
            showToast("Ad clicked");
        }

        @Override
        public void onAdLeave() {
            // Called when a user has left the app.
            showToast("Ad Leave");
        }

        @Override
        public void onAdClosed() {
            // Called when an ad is closed.
            showToast("Ad closed");
        }
    };
    private void showToast(String message) {
//        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        Log.e("status", message );
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