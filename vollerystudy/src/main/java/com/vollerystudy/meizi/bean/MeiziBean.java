package com.vollerystudy.meizi.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/7/1.
 */

public class MeiziBean {

    @SerializedName("_id")
    private String id;
    @SerializedName("url")
    private String imageUrl;
    @SerializedName("who")
    private String who;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public MeiziBean(String imageUrl){
        this.imageUrl = imageUrl;
    }

}
