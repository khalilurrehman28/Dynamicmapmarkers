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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        //List<Datum> objUser1 = new ArrayList<>();
        //List<Datum> objUser1Check = new ArrayList<>();
        Double lat =  0.0;
        Double lang = 0.0;
        int count = 0;
     /*   float COORDINATE_OFFSET = 0.0002f;
        List<LatLng> userPos = new ArrayList<>();*/

        for (Datum quizShow : objUser) {
            LatLng latLng = new LatLng(Double.parseDouble(quizShow.getPOSTLATITUDE()),Double.parseDouble(quizShow.getPOSTLONGITUDE()));
            lat = lat+Double.parseDouble(quizShow.getPOSTLATITUDE());
            lang = lang+Double.parseDouble(quizShow.getPOSTLONGITUDE());
            //userPos.add(latLng);
            Log.d("latlangServer",""+latLng);
            mClusterManager.addItem(new Datum(quizShow.getPOSTID(),quizShow.getUSERID(),quizShow.getPOSTIMAGEURL(),quizShow.getPOSTDESCRIPTION(),quizShow.getPOSTBLOCK(),quizShow.getPOSTDELETE(),quizShow.getPOSTDATETIME(),quizShow.getUSERID(),quizShow.getUSERNAME(),quizShow.getUSERTYPE(),quizShow.getUSERIMAGE(),quizShow.getUSERMOBILE(),quizShow.getUSERALTNUMBER(),quizShow.getUSEREMAIL(),quizShow.getUSERPASSWORD(),quizShow.getUSERACTIVE(),latLng));
            count++;
        }
        /*Random r;
        for (Datum UserData : objUser1) {
            if (userPos.contains(UserData.getPosition())){
                r = new Random();
                int i1 = r.nextInt(3 - 1) + 1;
                LatLng latLng= UserData.getPosition();
                double lat1 = latLng.latitude;
                double lang1 = latLng.longitude;
                Log.d("latlangBefore",""+lat1+"--"+lang1);
                lat1 = lat1 - i1 * COORDINATE_OFFSET;
                lang1 = lang1 - i1 * COORDINATE_OFFSET;
                Log.d("latlangAfter",""+lat1+"--"+lang1);
                latLng = new LatLng(lat1,lang1);
                userPos.add(latLng);
                mClusterManager.addItem(new Datum(UserData.getPOSTID(),UserData.getUSERID(),UserData.getPOSTIMAGEURL(),UserData.getPOSTDESCRIPTION(),UserData.getPOSTBLOCK(),UserData.getPOSTDELETE(),UserData.getPOSTDATETIME(),UserData.getUSERID(),UserData.getUSERNAME(),UserData.getUSERTYPE(),UserData.getUSERIMAGE(),UserData.getUSERMOBILE(),UserData.getUSERALTNUMBER(),UserData.getUSEREMAIL(),UserData.getUSERPASSWORD(),UserData.getUSERACTIVE(),latLng));
               // mClusterManager.addItem(UserData);
            }else{

           *//*     BigDecimal aa = new BigDecimal(a);
                BigDecimal bb = new BigDecimal(b);
                aa = aa.setScale(4, BigDecimal.ROUND_DOWN);
                bb = bb.setScale(4, BigDecimal.ROUND_DOWN);*//*

                userPos.add(UserData.getPosition());
                mClusterManager.addItem(new Datum(UserData.getPOSTID(),UserData.getUSERID(),UserData.getPOSTIMAGEURL(),UserData.getPOSTDESCRIPTION(),UserData.getPOSTBLOCK(),UserData.getPOSTDELETE(),UserData.getPOSTDATETIME(),UserData.getUSERID(),UserData.getUSERNAME(),UserData.getUSERTYPE(),UserData.getUSERIMAGE(),UserData.getUSERMOBILE(),UserData.getUSERALTNUMBER(),UserData.getUSEREMAIL(),UserData.getUSERPASSWORD(),UserData.getUSERACTIVE(),UserData.getPosition()));
            }
        }*/

        // final LatLngBounds bounds =
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng((lat/count),(lang/count)))    // Sets the center of the map to Mountain View
                .zoom(4)                   // Sets the zoom
                .bearing(16)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
