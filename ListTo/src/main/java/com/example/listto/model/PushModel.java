package com.example.listto.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PushModel {
    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("msg")
    @Expose
    private Integer msg;

    @SerializedName("requestId")
    @Expose
    private String requestId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getMsg() {
        return msg;
    }

    public void setMsg(Integer msg) {
        this.msg = msg;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
