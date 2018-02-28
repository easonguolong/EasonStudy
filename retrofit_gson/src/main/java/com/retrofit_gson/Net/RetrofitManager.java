package com.retrofit_gson.Net;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSink;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/1/31.
 */

public class RetrofitManager {
    private static RetrofitManager instance;
    private final OkHttpClient OK_HTTP_CLIENT;

    private final Retrofit RETROFIT;
    private static String baseUrl;
    private static boolean DEBUG = true;
    private static String userToken;

    public RetrofitManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.retryOnConnectionFailure(false);
        if (DEBUG){
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.e("OkHttp",message);
                }
            });
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(httpLoggingInterceptor);
        }
        //请求头
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                RequestBody body =new RequestBody() {
                    @Nullable
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public void writeTo(BufferedSink sink) throws IOException {

                    }
                };
                Request request = chain.request().newBuilder()
                        //.addHeader("tyd_mobile_user_token", userToken)
                        // .post(body)
                        .build();
                return chain.proceed(request);
            }
        });
        OK_HTTP_CLIENT = builder.build();
        RETROFIT = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(OK_HTTP_CLIENT)
                .baseUrl(baseUrl)
                .build();
    }

    public static RetrofitManager getInstance(){
        if (instance == null){
            instance = new RetrofitManager();
        }
        return instance;
    }
    public static void setDEBUG(boolean debug){
        RetrofitManager.DEBUG = debug;
    }

    public static void setBaseUrl(String url){
        RetrofitManager.baseUrl = url;
    }


    public static void setUserToken(String userToken) {
        RetrofitManager.userToken = userToken;
    }

    public static Retrofit getRetrofit() {
        return getInstance().RETROFIT;
    }

    public static OkHttpClient getOkHttpClient() {
        return getInstance().OK_HTTP_CLIENT;
    }

    public static <T> T createService(final Class<T> service){
        return getInstance().RETROFIT.create(service);
    }
}
