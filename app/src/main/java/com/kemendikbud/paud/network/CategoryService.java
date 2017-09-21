package com.kemendikbud.paud.network;

import android.content.Context;

import com.kemendikbud.paud.network.config.ApiClient;
import com.kemendikbud.paud.network.config.ApiInterface;

import retrofit2.Callback;

/**
 * Created by akbar on 29/08/17.
 */

public class CategoryService {
    private ApiInterface apiInterface;

    public CategoryService() {
        apiInterface = ApiClient.builder()
                .create(ApiInterface.class);
    }

    public void getCategories(Callback callback) {
        apiInterface.getDataCategory().enqueue(callback);
    }
}
