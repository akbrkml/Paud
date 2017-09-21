package com.kemendikbud.paud.network;

import android.content.Context;

import com.kemendikbud.paud.network.config.ApiClient;
import com.kemendikbud.paud.network.config.ApiInterface;

import retrofit2.Callback;

/**
 * Created by akbar on 29/08/17.
 */

public class UploadService {
    private ApiInterface apiInterface;

    public UploadService() {
        apiInterface = ApiClient.builder()
                .create(ApiInterface.class);
    }

    public void doUpload(String foto_id,
                         String urlFoto,
                         Double latitude,
                         Double longitude,
                         String time,
                         String keterangan,
                         String idSekolah,
                         Integer idKategori,
                         String last_update,
                         Integer soft_delete,
                         String last_sync,
                         String updater_id,
                         Callback callback) {
        apiInterface.uploadPicture(foto_id, urlFoto,
                latitude,
                longitude,
                time,
                keterangan,
                idSekolah,
                idKategori,
                last_update,
                soft_delete,
                last_sync,
                updater_id).enqueue(callback);
    }
}
