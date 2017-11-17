package com.dupleit.mapmarkers.dynamicmapmarkers.Network;

import com.dupleit.mapmarkers.dynamicmapmarkers.AddPostToDatabase.Model.UploadImageResponse;
import com.dupleit.mapmarkers.dynamicmapmarkers.ReadPost.model.UserPost;
import com.dupleit.mapmarkers.dynamicmapmarkers.modal.UsersMapsMarkers;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {

    @GET("getpostonmap_request")
    Call<UsersMapsMarkers> getUserPostOnMap();

    @FormUrlEncoded
    @POST("getpostdatabyid_request")
    Call<UserPost> getUserDataByPost(@Field("POST_ID") String postID);

    @Multipart
    @POST("post_request")
    Call<UploadImageResponse> upload_post_request(
             @Part MultipartBody.Part file, @Part("USER_ID") int USER_ID,
             @Part("POST_DESCRIPTION") String POST_DESCRIPTION, @Part("POST_LATITUDE") double POST_LATITUDE,
             @Part("POST_LONGITUDE") double POST_LONGITUDE);

}