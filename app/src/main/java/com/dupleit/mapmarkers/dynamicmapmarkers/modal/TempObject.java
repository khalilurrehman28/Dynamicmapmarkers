package com.dupleit.mapmarkers.dynamicmapmarkers.modal;

import retrofit2.Response;

/**
 * Created by rajesh on 4/11/17.
 */

public class TempObject {
    Response<UsersMapsMarkers> response;

    public TempObject(Response<UsersMapsMarkers> response) {
        this.response = response;
    }

    public Response<UsersMapsMarkers> getResponse() {
        return response;
    }

}
