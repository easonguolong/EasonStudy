package com.retrofit_gson.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/1/31.
 */

public class Login {
    @SerializedName("user_id")
    private String userId;
    @SerializedName("device_id")
    private String deviceId;
    @SerializedName("timestamp")
    private String timestamp;
    private String nonce;
    private String signature;
    public Login(String userId,String deviceId,String timestamp,String nonce,String signature){
        this.userId = userId;
        this.deviceId = deviceId;
        this.timestamp = timestamp;
        this.nonce = nonce;
        this.signature = signature;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
