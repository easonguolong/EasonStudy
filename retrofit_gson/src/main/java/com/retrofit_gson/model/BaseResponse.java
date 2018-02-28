package com.retrofit_gson.model;

/**
 * Created by Administrator on 2018/1/31.
 */

public class BaseResponse<T> {
    private int code;
    private T data;
    private String msg;
    public boolean isSuccess(){
        return code ==0;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
