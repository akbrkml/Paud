package com.kemendikbud.paud.network;

import com.kemendikbud.paud.network.config.ApiClient;
import com.kemendikbud.paud.network.config.ApiInterface;

import retrofit2.Callback;

/**
 * Created by akbar on 20/09/17.
 */

public class DeleteService {
    private ApiInterface apiInterface;

    public DeleteService() {
        apiInterface = ApiClient.builder()
                .create(ApiInterface.class);
    }

    public void doDelete(String foto_id, int soft_delete, Callback callback) {
        apiInterface.deletePictures(foto_id, soft_delete).enqueue(callback);
    }
}
