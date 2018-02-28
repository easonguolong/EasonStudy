package com.easonmvp.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.easonmvp.MainActivity;
import com.easonmvp.R;
import com.easonmvp.interfaces.Login.LoginPresenter;
import com.easonmvp.interfaces.Login.LoginView;
import com.easonmvp.presenter.LoginInteractorImpl;
import com.easonmvp.presenter.LoginPresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/2/26.
 */

public class LoginActivity extends AppCompatActivity implements LoginView, View.OnClickListener{

    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.button)
    Button login;
    @BindView(R.id.progress)
    ProgressBar progressBar;

    LoginPresenter loginPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        ButterKnife.bind(this);
        login.setOnClickListener(this);
        loginPresenter = new LoginPresenterImpl(this,new LoginInteractorImpl());
    }

    @Override
    public void onClick(View view) {
        loginPresenter.validateCredentials(username.getText().toString().trim(),password.getText().toString().trim());
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setUsernameError() {
        username.setError("UserName is Empty");
    }

    @Override
    public void setPassWordError() {
        password.setError("Password is Empty");
    }

    @Override
    public void toHomeView() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        this.finish();
    }


    @Override
    protected void onDestroy() {
        loginPresenter.onDestroy();
        super.onDestroy();
    }
}
