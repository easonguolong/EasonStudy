package com.retrofit_gson.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/1/31.
 */

public class LoginResult {
    @SerializedName("user_id")
    private String userId;
    private String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
