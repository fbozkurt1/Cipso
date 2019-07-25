package com.example.cipso.core.login;

import android.app.Activity;

public class LoginPresenter implements LoginContract.Presenter, LoginContract.OnLoginListener {
    private LoginContract.View loginView;
    private LoginInteractor loginInteractor;

    public LoginPresenter(LoginContract.View loginView) {
        this.loginView = loginView;
        loginInteractor = new LoginInteractor(this);
    }

    @Override
    public void login(Activity activity, String email, String password) {
        loginInteractor.performFirebaseLogin(activity, email, password);
    }

    @Override
    public void onSuccess(String message) {
        loginView.onLoginSuccess(message);
    }

    @Override
    public void onFailure(String message) {
        loginView.onLoginFailure(message);
    }
}
