package com.kemendikbud.paud.network.config;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kemendikbud.paud.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.kemendikbud.paud.util.Constant.BASE_URL;

/**
 * Created by kamal on 08/02/2017.
 */

public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit builder() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
