package com.example.retrofitforserver_client1_2;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface FileApi {


    @Multipart
    @POST("uploadFile")
    Call<Respond> uploadImage(@Part MultipartBody.Part file);

    @GET("downloadFile/{fileName}")
    Call<ResponseBody> downloadFile(@Path("fileName") String fileName);
}