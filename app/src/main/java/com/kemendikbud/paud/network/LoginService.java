package com.kemendikbud.paud.network;

import android.content.Context;

import com.kemendikbud.paud.network.config.ApiClient;
import com.kemendikbud.paud.network.config.ApiInterface;

import retrofit2.Callback;

/**
 * Created by akbar on 28/08/17.
 */

public class LoginService {

    private ApiInterface apiInterface;

    public LoginService() {
        apiInterface = ApiClient.builder()
                .create(ApiInterface.class);
    }

    public void doLogin(String username, String password, Callback callback) {
        apiInterface.signIn(username, password).enqueue(callback);
    }
}
