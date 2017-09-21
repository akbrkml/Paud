package com.kemendikbud.paud.network;

import android.content.Context;

import com.kemendikbud.paud.network.config.ApiClient;
import com.kemendikbud.paud.network.config.ApiInterface;

import retrofit2.Callback;

/**
 * Created by akbar on 28/08/17.
 */

public class PictureService {
    private ApiInterface apiInterface;

    public PictureService() {
        apiInterface = ApiClient.builder()
                .create(ApiInterface.class);
    }

    public void getPictures(String id_sekolah, Callback callback) {
        apiInterface.getDataPictures(id_sekolah).enqueue(callback);
    }
}
