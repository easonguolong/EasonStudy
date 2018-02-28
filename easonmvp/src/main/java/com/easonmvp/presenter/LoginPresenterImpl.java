package com.easonmvp.presenter;

import com.easonmvp.interfaces.Login.LoginInteractor;
import com.easonmvp.interfaces.Login.LoginPresenter;
import com.easonmvp.interfaces.Login.LoginView;

/**
 * Created by Administrator on 2018/2/28.
 */

public class LoginPresenterImpl implements LoginPresenter,LoginInteractor.OnLoginFinishedListener {

    private LoginView loginView;
    private LoginInteractor loginInteractor;


    public LoginPresenterImpl(LoginView loginView,LoginInteractor loginInteractor){
        this.loginView = loginView;
        this.loginInteractor = loginInteractor;
    }

    @Override
    public void validateCredentials(String username, String password) {
        if (null!=loginView){
            loginView.showLoading();
        }
        loginInteractor.login(username,password,this);
    }

    @Override
    public void onDestroy() {
        if (null!=loginView)
            loginView = null;
    }

    @Override
    public void onUsernameError() {
        if (null!=loginView){
            loginView.setUsernameError();
            loginView.hideLoading();
        }
    }

    @Override
    public void onPasswordError() {
        if (null!=loginView){
            loginView.setPassWordError();
            loginView.hideLoading();
        }
    }

    @Override
    public void onSuccess() {
        if (null!=loginView){
            loginView.hideLoading();
            loginView.toHomeView();
        }
    }
}
