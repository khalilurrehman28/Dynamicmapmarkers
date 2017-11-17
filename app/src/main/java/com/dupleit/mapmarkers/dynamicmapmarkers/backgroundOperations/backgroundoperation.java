package com.dupleit.mapmarkers.dynamicmapmarkers.backgroundOperations;

import android.os.AsyncTask;
import android.util.Log;


import com.dupleit.mapmarkers.dynamicmapmarkers.Network.APIService;
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.ApiClient;
import com.dupleit.mapmarkers.dynamicmapmarkers.modal.Datum;
import com.dupleit.mapmarkers.dynamicmapmarkers.modal.TempObject;
import com.dupleit.mapmarkers.dynamicmapmarkers.modal.UsersMapsMarkers;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by android on 10/11/17.
 */

public class backgroundoperation extends AsyncTask<Void, TempObject, String> {

    public ClusterManager<Datum> mClusterManager;
    public GoogleMap mMap;


    public backgroundoperation(ClusterManager<Datum> mClusterManager, GoogleMap mMap) {
        this.mClusterManager = mClusterManager;
        this.mMap = mMap;
    }

    @Override
    protected String doInBackground(Void... params) {
        APIService service = ApiClient.getClient().create(APIService.class);
        Call<UsersMapsMarkers> userCall = service.getUserPostOnMap();
        userCall.enqueue(new Callback<UsersMapsMarkers>() {
            @Override
            public void onResponse(Call<UsersMapsMarkers> call, retrofit2.Response<UsersMapsMarkers> response) {
                if (response.isSuccessful()){
                        //Toast.makeText(MainActivity.this, "Hello from api", Toast.LENGTH_SHORT).show();
                        onProgressUpdate(new TempObject(response));

                }
            }

            @Override
            public void onFailure(Call<UsersMapsMarkers> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
        return null;
    }

    @Override
    public void onProgressUpdate(TempObject... value) {
        super.onProgressUpdate(value);
        retrofit2.Response<UsersMapsMarkers> response = value[0].getResponse();
        List<Datum> objUser = response.body().getData();
        Double lat =  0.0;
        Double lang = 0.0;
        int count = 0;

        for (Datum quizShow : objUser) {
            LatLng latLng = new LatLng(Double.parseDouble(quizShow.getPOSTLATITUDE()),Double.parseDouble(quizShow.getPOSTLONGITUDE()));
            lat = lat+Double.parseDouble(quizShow.getPOSTLATITUDE());
            lang = lang+Double.parseDouble(quizShow.getPOSTLONGITUDE());
            mClusterManager.addItem(new Datum(quizShow.getPOSTID(),quizShow.getUSERID(),quizShow.getPOSTIMAGEURL(),quizShow.getPOSTDESCRIPTION(),quizShow.getPOSTBLOCK(),quizShow.getPOSTDELETE(),quizShow.getPOSTDATETIME(),quizShow.getUSERID(),quizShow.getUSERNAME(),quizShow.getUSERTYPE(),quizShow.getUSERIMAGE(),quizShow.getUSERMOBILE(),quizShow.getUSERALTNUMBER(),quizShow.getUSEREMAIL(),quizShow.getUSERPASSWORD(),quizShow.getUSERACTIVE(),latLng));
            count++;
        }
        // final LatLngBounds bounds =
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng((lat/count),(lang/count)))    // Sets the center of the map to Mountain View
                .zoom(4)                   // Sets the zoom
                .bearing(16)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mClusterManager.cluster();

    }
}
