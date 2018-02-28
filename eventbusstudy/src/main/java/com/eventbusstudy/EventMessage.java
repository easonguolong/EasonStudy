package com.eventbusstudy;

/**
 * Created by Administrator on 2017/7/12.
 */

public class EventMessage {
    private String msg ;
    public EventMessage(String msg){
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
