package com.easonmvp.interfaces.Login;

/**
 * Created by Administrator on 2018/2/28.
 */

public interface LoginPresenter {
    void validateCredentials(String username, String password);

    void onDestroy();
}
