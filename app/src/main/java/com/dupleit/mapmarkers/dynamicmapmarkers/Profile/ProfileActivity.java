package com.dupleit.mapmarkers.dynamicmapmarkers.Profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.Appconstant;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.PreferenceManager;
import com.dupleit.mapmarkers.dynamicmapmarkers.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.UserProfileImage)
    CircleImageView userImage;

    @BindView(R.id.userEmail)
    EditText userEmail;

    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.etContact)
    EditText etContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        initializeView();
    }

    @OnClick(R.id.updateProfile)
    public void updateProfile(){
        Toast.makeText(this, "I am Clicked", Toast.LENGTH_SHORT).show();
    }

    private void initializeView() {
        Glide.with(this).load(Appconstant.weburl+(new PreferenceManager(this).getUserImage())).into(userImage);
        userEmail.setText((new PreferenceManager(getApplicationContext()).getUserEmail()));
        etName.setText((new PreferenceManager(getApplicationContext()).getUsername()));
        etContact.setText((new PreferenceManager(getApplicationContext()).getUserNumber()));
    }
}
