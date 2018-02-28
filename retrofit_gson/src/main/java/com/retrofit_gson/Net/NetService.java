package com.retrofit_gson.Net;


import com.retrofit_gson.model.BaseResponse;
import com.retrofit_gson.model.Login;
import com.retrofit_gson.model.LoginResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2018/1/31.
 */

public interface NetService {

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("/auction/api/login/token?")
    Observable<BaseResponse<LoginResult>> getTokens(@Body Login login);

}
