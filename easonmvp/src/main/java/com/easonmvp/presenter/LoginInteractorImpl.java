package com.easonmvp.presenter;

import android.os.Handler;
import android.text.TextUtils;

import com.easonmvp.interfaces.Login.LoginInteractor;

/**
 * Created by Administrator on 2018/2/28.
 */

public class LoginInteractorImpl implements LoginInteractor {

    @Override
    public void login(final String username, final String password, final OnLoginFinishedListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(username)){
                    listener.onUsernameError();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    listener.onPasswordError();
                    return;
                }
                listener.onSuccess();
            }
        },2000);
    }
}
