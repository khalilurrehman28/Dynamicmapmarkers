package com.dupleit.mapmarkers.dynamicmapmarkers.Network;

import com.dupleit.mapmarkers.dynamicmapmarkers.modal.UsersMaps;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;

public interface APIService {
    //for login
    @GET("getUsers")
    Call<UsersMaps> UserLogin();

    @Multipart
    Call PostByUser();

}