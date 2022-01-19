package com.example.listto.model;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface APIService {
    @FormUrlEncoded
    @POST("oauth2/v3/token")
    @Headers({"Content-type:application/x-www-form-urlencoded"})
    Call<HuaweiAuth> setDataToken(
            @Field("client_id") String client_id,
            @Field("grant_type") String grant_type,
            @Field("client_secret") String client_secret
    );
    @POST("v1/105260341/messages:send")
    @Headers({"Content-type:application/json"})
    Call<PushModel> setDataPushMsg(
            @HeaderMap HashMap<String, String> header,
            @Body JsonObject jsonObject
    );

}
