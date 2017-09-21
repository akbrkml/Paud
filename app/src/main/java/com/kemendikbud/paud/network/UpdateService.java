package com.kemendikbud.paud.network;

import com.kemendikbud.paud.network.config.ApiClient;
import com.kemendikbud.paud.network.config.ApiInterface;

import retrofit2.Callback;

/**
 * Created by akbar on 20/09/17.
 */

public class UpdateService {
    private ApiInterface apiInterface;

    public UpdateService() {
        apiInterface = ApiClient.builder()
                .create(ApiInterface.class);
    }

    public void doUpdate(String foto_id, String keterangan, int kategori_id, String last_update, Callback callback) {
        apiInterface.updatePictures(foto_id, keterangan, kategori_id, last_update).enqueue(callback);
    }
}
