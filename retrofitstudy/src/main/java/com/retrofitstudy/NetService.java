package com.retrofitstudy;


import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/7/3.
 */

public interface NetService {

    @FormUrlEncoded
    @POST("login")
    Observable<Object> LoginTask(@Field("tel") String tel, @Field("password") String password);

    @FormUrlEncoded
    @POST("check_version")
    Observable<Object> CheckUpdate(@Field("request") String quest, @Field("type") int type);

    @FormUrlEncoded
    @POST("get_userinfo")
    Observable<Object> getUserinfo(@Field("uid") String uid);


}
