package com.dupleit.mapmarkers.dynamicmapmarkers.Profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.Appconstant;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.PreferenceManager;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.checkInternetState;
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.APIService;
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.ApiClient;
import com.dupleit.mapmarkers.dynamicmapmarkers.R;
import com.dupleit.mapmarkers.dynamicmapmarkers.ReadComments.Model.CommentResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.UserProfileImage)
    CircleImageView userImage;

    @BindView(R.id.userEmail)
    TextView userEmail;

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
        if (!checkInternetState.getInstance(this).isOnline()) {
            Toast.makeText(this, "Cannot connect to network", Toast.LENGTH_SHORT).show();
        }else {
            APIService service = ApiClient.getClient().create(APIService.class);
            Call<CommentResponse> userCall = service.updateuserprofile_request(etName.getText().toString(),etContact.getText().toString(),Integer.parseInt((new PreferenceManager(getApplicationContext()).getUserID())));
            userCall.enqueue(new Callback<CommentResponse>() {
                @Override
                public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                    if (response.body().getStatus()) {
                        new PreferenceManager(getApplicationContext()).saveUserName(etName.getText().toString());
                        new PreferenceManager(getApplicationContext()).saveUserMobile(etContact.getText().toString());
                        Toast.makeText(ProfileActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Sorry Please try again", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CommentResponse> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                }
            });
        }
    }

    private void initializeView() {
        Glide.with(this).load(Appconstant.weburl+(new PreferenceManager(this).getUserImage())).into(userImage);
        userEmail.setText((new PreferenceManager(getApplicationContext()).getUserEmail()));
        etName.setText((new PreferenceManager(getApplicationContext()).getUsername()));
        etContact.setText((new PreferenceManager(getApplicationContext()).getUserNumber()));
    }

    @OnClick(R.id.UserProfileImage)
    public void UserProfileImage(){
        Toast.makeText(this, "I am Clicked", Toast.LENGTH_SHORT).show();
    }

}
