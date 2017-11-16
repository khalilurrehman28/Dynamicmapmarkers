package com.dupleit.mapmarkers.dynamicmapmarkers.Network;

import com.dupleit.mapmarkers.dynamicmapmarkers.AddPostToDatabase.Model.UploadImageResponse;
import com.dupleit.mapmarkers.dynamicmapmarkers.modal.UsersMaps;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface APIService {
    //for login
    @GET("getUsers")
    Call<UsersMaps> UserLogin();

    @Multipart
    @POST("post_request")
    Call<UploadImageResponse> upload_post_request(
             @Part MultipartBody.Part file, @Part("USER_ID") int USER_ID,
             @Part("POST_DESCRIPTION") String POST_DESCRIPTION, @Part("POST_LATITUDE") double POST_LATITUDE,
             @Part("POST_LONGITUDE") double POST_LONGITUDE);

}