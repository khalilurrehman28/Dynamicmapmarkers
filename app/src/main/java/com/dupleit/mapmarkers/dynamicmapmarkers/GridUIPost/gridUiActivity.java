package com.dupleit.mapmarkers.dynamicmapmarkers.GridUIPost;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.GridSpacingItemDecoration;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.checkInternetState;
import com.dupleit.mapmarkers.dynamicmapmarkers.GridUIPost.adapter.personAdapter;
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.APIService;
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.ApiClient;
import com.dupleit.mapmarkers.dynamicmapmarkers.R;
import com.dupleit.mapmarkers.dynamicmapmarkers.RecyclerTouchListener;
import com.dupleit.mapmarkers.dynamicmapmarkers.modal.Datum;
import com.dupleit.mapmarkers.dynamicmapmarkers.modal.UsersMapsMarkers;

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
    }
    private void initilize(View v) {
        personList = new ArrayList<>();

        adapter = new personAdapter(this, personList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        this.hitApi(getIntent().getParcelableArrayExtra("userlatlang"));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(gridUiActivity.this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

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

    private void hitApi(Parcelable[] userlatlangs) {
        if (!checkInternetState.getInstance(gridUiActivity.this).isOnline()) {
            Toast.makeText(this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();

        }else {

            for (int i = 0; i <userlatlangs.length ; i++) {
                Log.d("userLatLang",""+userlatlangs[i]);
            }

           /* APIService service = ApiClient.getClient().create(APIService.class);
            Call<UsersMapsMarkers> userCall = service.getpostonlatlang_request(userLatLang);
            userCall.enqueue(new Callback<UsersMapsMarkers>() {
                @Override
                public void onResponse(Call<UsersMapsMarkers> call, Response<UsersMapsMarkers> response) {

                    Log.d("homework"," "+response.body().getStatus());
                    if (response.isSuccessful()){
                        if (response.body().getStatus()) {
                            *//*to get homework of principal to all*//*


                        }else{
                          *//*  noHomeworkFound.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setRefreshing(false);*//*

                        }
                    }else{
                        *//*Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);*//*

                    }
                }

                @Override
                public void onFailure(Call<UsersMapsMarkers> call, Throwable t) {
                    //hidepDialog();
                    //swipeRefreshLayout.setRefreshing(false);
                    Log.d("onFailure", t.toString());
                }
            });*/
        }
        adapter.notifyDataSetChanged();
    }
}
