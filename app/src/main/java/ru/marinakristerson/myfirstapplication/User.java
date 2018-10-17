package ru.marinakristerson.myfirstapplication;

import java.io.Serializable;

public class User implements Serializable {
    private String mLogin;
    private String mPassword;
    private String mPhotoUri;
    private boolean mHasSuccessLogin;

    public User(String login, String password) {
        this.mLogin = login;
        this.mPassword = password;
    }

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String login) {
        this.mLogin = login;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public String getPhotoUri() {
        return mPhotoUri;
    }

    public void setPhotoUri(String photoUri) {
        mPhotoUri = photoUri;
    }

    public boolean hasSuccessLogin() {
        return mHasSuccessLogin;
    }

    public void setHasSuccessLogin(boolean hasSuccessLogin) {
        mHasSuccessLogin = hasSuccessLogin;
    }
}
