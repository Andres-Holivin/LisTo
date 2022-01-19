package com.example.listto.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.example.listto.model.APIService;
import com.example.listto.model.DatabaseHandler;
import com.example.listto.model.HuaweiAuth;
import com.example.listto.model.PushModel;
import com.example.listto.model.TaskModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationUtil extends BroadcastReceiver {
    Call<HuaweiAuth> data;
    Call<PushModel> pushData;
    APIService apiService;
    String access_token="";
    String device_token="";
    private Context context;
    TaskModel task=new TaskModel();

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        task=DatabaseHandler.getInstance(context).getTaskById(Integer.valueOf(intent.getStringExtra("Id")));
        getDeviceToken();
        updateStatusModel(String.valueOf(task.getId()));
        Log.e("Content schedule", "schedule done "+task.getDateFormat() );
        Toast.makeText(context, "schedule done", Toast.LENGTH_SHORT).show();
    }
    public void sendPushMessage(){
        HashMap<String, String> headers = new HashMap<String, String>();
        Log.e("APi", "Run Once");
        Log.e("access_token", access_token);
        Log.e("device_token", device_token);
        headers.put("Authorization", "Bearer "+access_token);
        String json="{\n" +
                "    \"message\": {\n" +
                "        \"android\": {\n" +
                "            \"ttl\": \"1296000\",\n" +
                "            \"collapse_key\": 10,\n" +
                "            \"notification\": {\n" +
                "                \"visibility\": \"PUBLIC\",\n" +
                "                \"importance\": \"NORMAL\",\n" +
                "                \"foreground_show\": true,\n" +
                "                \"title\": \""+task.getTopic()+"\",\n" +
                "                \"body\": \""+task.getDateFormat()+" "+task.getDescription()+"\",\n" +
                "                \"click_action\": {\n" +
                "                    \"type\": 3\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        \"token\": [\n" +
                "            \""+device_token+"\"\n" +
                "        ]\n" +
                "    }\n" +
                "}";
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject=(JsonObject)jsonParser.parse(json);
        apiService = ApiUtil.getPushMessage().create(APIService.class);
        pushData = apiService.setDataPushMsg(headers,jsonObject);
        pushData.enqueue(new Callback<PushModel>() {
            @Override
            public void onResponse(Call<PushModel> call, Response<PushModel> response) {
                Log.d("DataModel", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    Log.e("Send Notification", "success");
                }else{
                    Log.d("errt", ""+response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<PushModel> call, Throwable t) {
                Log.d("DataModel", ""+t.getMessage());
            }
        });
    }
    public void updateStatusModel(String Id){
        boolean status=DatabaseHandler.getInstance(context).updateStatusActivity(Id);
        if(status) Log.d("Change Status", "Status Success Change");
        else Log.d("Change Status", "Status Failed Change");
    }
    public void getApiToken(){
        apiService = ApiUtil.getToken().create(APIService.class);
        data = apiService.setDataToken("105260341","client_credentials","e39fe176c85d8a533807cc374fb9a09381b6d0e2227526ce6fffc4fb19d4c8bc");
        data.enqueue(new Callback<HuaweiAuth>() {
            @Override
            public void onResponse(Call<HuaweiAuth> call, Response<HuaweiAuth> response) {
                Log.d("AccessToken", ""+response.body().getAccess_token());
                if (response.isSuccessful()){
                    access_token=response.body().getAccess_token();
                    Log.d("AccessToken", ""+access_token);
                    sendPushMessage();
                }else{
                    Log.d("errt", ""+response.errorBody());;
                }
            }
            @Override
            public void onFailure(Call<HuaweiAuth> call, Throwable t) {
                Log.d("DataModel", ""+t.getMessage());
            }
        });
    }
    private void getDeviceToken() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String appId = AGConnectServicesConfig.fromContext(context).getString("105260341");
                    String tokenScope = "HCM";
                    String token = HmsInstanceId.getInstance(context).getToken(appId, tokenScope);
                    Log.i("Device token", "get token: " + token);
                    if(!TextUtils.isEmpty(token)) {
                        device_token=token;
                        getApiToken();
                        sendPushMessage();
                    }
                } catch (ApiException e) {
                    Log.e("Device token", "get token failed, " + e);
                }
            }
        }.start();
    }


}
