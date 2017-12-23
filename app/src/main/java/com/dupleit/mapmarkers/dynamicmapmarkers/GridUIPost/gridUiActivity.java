package com.dupleit.mapmarkers.dynamicmapmarkers.GridUIPost;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.GridSpacingItemDecoration;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.checkInternetState;
import com.dupleit.mapmarkers.dynamicmapmarkers.GridUIPost.adapter.personAdapter;
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.APIService;
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.ApiClient;
import com.dupleit.mapmarkers.dynamicmapmarkers.R;
import com.dupleit.mapmarkers.dynamicmapmarkers.ReadPost.ReadPostActivity;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.RecyclerTouchListener;
import com.dupleit.mapmarkers.dynamicmapmarkers.modal.Datum;
import com.dupleit.mapmarkers.dynamicmapmarkers.modal.UsersMapsMarkers;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class gridUiActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private List<Datum> personList;
    private personAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_ui);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initilize();

        //setTitle("");
    }
    private void initilize() {
        ArrayList<LatLng> coordinates = getIntent().getParcelableArrayListExtra("userlatlang");
        ArrayList<LatLng> corDin = new ArrayList<>();
        for (LatLng data: coordinates) {
            Log.d("userDataLatLang",""+data);
            corDin.add(new LatLng(data.latitude,data.longitude));
        }

        this.hitApi(corDin);
        personList = new ArrayList<>();
        adapter = new personAdapter(this, personList);
        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(3), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(gridUiActivity.this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                startActivity(new Intent(gridUiActivity.this, ReadPostActivity.class).putExtra("PostID",personList.get(position).getPOSTID()));
            }

            @Override
            public void onLongClick(View view, final int position) {

            }
        }));
    }

    private int dpToPx(int i) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, r.getDisplayMetrics()));
    }

    private void hitApi(ArrayList<LatLng> userlatlangs) {
        if (!checkInternetState.getInstance(gridUiActivity.this).isOnline()) {
            Toast.makeText(this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }else {
            APIService service = ApiClient.getClient().create(APIService.class);
            Call<UsersMapsMarkers> userCall = service.getpostonlatlang_request(userlatlangs);
            userCall.enqueue(new Callback<UsersMapsMarkers>() {
                @Override
                public void onResponse(Call<UsersMapsMarkers> call, Response<UsersMapsMarkers> response) {
                    Log.d("homework"," "+response.body().getStatus());
                    if (response.isSuccessful()){
                        if (response.body().getStatus()) {
                            List<Datum> users = response.body().getData();
                            for (Datum data: users) {
                                Log.d("userData",""+data.getPOSTID());
                                LatLng latLng = new LatLng(Double.parseDouble(data.getPOSTLATITUDE()),Double.parseDouble(data.getPOSTLONGITUDE()));
                                personList.add(new Datum(data.getPOSTID(),data.getUSERID(),data.getPOSTIMAGEURL(),data.getPOSTDESCRIPTION(),data.getPOSTBLOCK(),data.getPOSTDELETE(),data.getPOSTDATETIME(),data.getUSERID(),data.getUSERNAME(),data.getUSERTYPE(),data.getUSERIMAGE(),data.getUSERMOBILE(),data.getUSERALTNUMBER(),data.getUSEREMAIL(),data.getUSERPASSWORD(),data.getUSERACTIVE(),latLng));
                            }
                            adapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(gridUiActivity.this, "Failed Query", Toast.LENGTH_SHORT).show();
                            Log.d("userMessage",""+response.body().getMessage());
                        }
                    }else{
                        Toast.makeText(gridUiActivity.this, "Unable to connect to api", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UsersMapsMarkers> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                }
            });
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}