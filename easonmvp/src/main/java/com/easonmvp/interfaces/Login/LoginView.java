package com.easonmvp.interfaces.Login;

/**
 * Created by Administrator on 2018/2/28.
 */

public interface LoginView {
    void showLoading();
    void hideLoading();
    void setUsernameError();
    void setPassWordError();
    void toHomeView();
}
