package com.dupleit.mapmarkers.dynamicmapmarkers.Profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.dupleit.mapmarkers.dynamicmapmarkers.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    /*@BindView(R.id.UserProfileImage)
    CircleImageView UserProfileImage;

    @BindView(R.id.userEmail)
    EditText email;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
    }
}
