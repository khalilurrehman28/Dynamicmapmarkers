package com.dupleit.mapmarkers.dynamicmapmarkers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UploadPostActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.goBackActivity) ImageView goback;
    @BindView(R.id.doCrop) ImageView doCrop;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_post);
        ButterKnife.bind(this);
        goback.setOnClickListener(this);
        doCrop.setOnClickListener(this);
        //fab_main.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.goBackActivity:
                onBackPressed();
                Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
                break;
            case R.id.doCrop:

                break;

        }
    }
}
