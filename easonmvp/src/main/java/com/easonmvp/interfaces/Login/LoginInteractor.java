package com.easonmvp.interfaces.Login;

/**
 * Created by Administrator on 2018/2/28.
 */

public interface LoginInteractor {

    interface OnLoginFinishedListener{
        void onUsernameError();
        void onPasswordError();
        void onSuccess();
    }

    void login(String username,String password,OnLoginFinishedListener listener);
}
