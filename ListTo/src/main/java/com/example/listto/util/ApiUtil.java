package com.example.listto.util;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtil {
    private static Retrofit token = null;
    private static Retrofit push = null;
    public static final String BASE_URL_PUSH_MSG = "https://push-api.cloud.huawei.com/";
    public static final String BASE_URL_LOGIN = "https://oauth-login.cloud.huawei.com/";

    public static Retrofit getToken() {
        if (token==null) {
            token = new Retrofit.Builder()
                    .baseUrl(BASE_URL_LOGIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return token;
    }


    public static Retrofit getPushMessage() {
        if (push==null) {
            push = new Retrofit.Builder()
                    .baseUrl(BASE_URL_PUSH_MSG)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return push;
    }
}
