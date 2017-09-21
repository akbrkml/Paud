package com.kemendikbud.paud.network.config;



import com.kemendikbud.paud.model.Category;
import com.kemendikbud.paud.model.MessageResponse;
import com.kemendikbud.paud.model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by kamal on 08/02/2017.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login")
    Call<User> signIn(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("insert-pictures")
    Call<MessageResponse> uploadPicture(
            @Field("foto_id") String foto_id,
            @Field("url_foto") String urlFoto,
            @Field("latitude") Double latitude,
            @Field("longitude") Double longitude,
            @Field("date_insert") String date_insert,
            @Field("Keterangan") String keterangan,
            @Field("sekolah_id") String sekolah_id,
            @Field("kategori_id") int kategori_id,
            @Field("Last_update") String Last_update,
            @Field("Soft_delete") int Soft_delete,
            @Field("last_sync") String last_sync,
            @Field("Updater_ID") String Updater_ID
    );

    @GET("pictures/sekolah/{sekolah_id}")
    Call<User> getDataPictures(@Path("sekolah_id") String sekolah_id);

    @GET("category")
    Call<User> getDataCategory();

    @GET("pictures/{sekolah_id}/{kategori_id}")
    Call<User> getCategoryPicture(@Path("sekolah_id") String sekolah_id,
                                  @Path("kategori_id") int kategori_id);

    @FormUrlEncoded
    @PUT("delete-pictures/{foto_id}")
    Call<User> deletePictures(@Path("foto_id") String foto_id,
                              @Field("Soft_delete") int soft_delete);

    @FormUrlEncoded
    @PUT("update-pictures/{foto_id}")
    Call<User> updatePictures(@Path("foto_id") String foto_id,
                              @Field("Keterangan") String keterangan,
                              @Field("kategori_id") int kategori_id,
                              @Field("Last_update") String last_update);
}
