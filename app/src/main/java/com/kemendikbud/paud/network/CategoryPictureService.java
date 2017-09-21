package com.kemendikbud.paud.network;

import com.kemendikbud.paud.network.config.ApiClient;
import com.kemendikbud.paud.network.config.ApiInterface;

import retrofit2.Callback;

/**
 * Created by akbar on 21/09/17.
 */

public class CategoryPictureService {
    private ApiInterface apiInterface;

    public CategoryPictureService() {
        apiInterface = ApiClient.builder()
                .create(ApiInterface.class);
    }

    public void getCategoryPictures(String sekolah_id, int kategori_id, Callback callback) {
        apiInterface.getCategoryPicture(sekolah_id, kategori_id).enqueue(callback);
    }
}
