package com.dupleit.mapmarkers.dynamicmapmarkers.Network;

import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.Appconstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by android on 10/11/17.
 */

public class ApiClient {

    public static final String BASE_URL = Appconstant.weburl+"api/request/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(logging).build();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (retrofit==null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}