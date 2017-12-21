package com.dupleit.mapmarkers.dynamicmapmarkers.backgroundOperations;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dupleit.mapmarkers.dynamicmapmarkers.Network.APIService;
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.ApiClient;
import com.dupleit.mapmarkers.dynamicmapmarkers.modal.Datum;
import com.dupleit.mapmarkers.dynamicmapmarkers.modal.TempObject;
import com.dupleit.mapmarkers.dynamicmapmarkers.modal.UsersMapsMarkers;
import com.google.android.gms.maps.GoogleMap;
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
    Context ctx;

    public backgroundoperation(ClusterManager<Datum> mClusterManager, GoogleMap mMap, Context mContext) {
        this.mClusterManager = mClusterManager;
        this.mMap = mMap;
        this.ctx = mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(ctx, "Loading User Data", Toast.LENGTH_SHORT).show();
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

        for (Datum quizShow : objUser) {
            LatLng latLng = new LatLng(Double.parseDouble(quizShow.getPOSTLATITUDE()),Double.parseDouble(quizShow.getPOSTLONGITUDE()));
            mClusterManager.addItem(new Datum(quizShow.getPOSTID(),quizShow.getUSERID(),quizShow.getPOSTIMAGEURL(),quizShow.getPOSTDESCRIPTION(),quizShow.getPOSTBLOCK(),quizShow.getPOSTDELETE(),quizShow.getPOSTDATETIME(),quizShow.getUSERID(),quizShow.getUSERNAME(),quizShow.getUSERTYPE(),quizShow.getUSERIMAGE(),quizShow.getUSERMOBILE(),quizShow.getUSERALTNUMBER(),quizShow.getUSEREMAIL(),quizShow.getUSERPASSWORD(),quizShow.getUSERACTIVE(),latLng));
        }

        mClusterManager.cluster();
    }
}
