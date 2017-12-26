package com.dupleit.mapmarkers.dynamicmapmarkers.Signup;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.PreferenceManager;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.checkInternetState;
import com.dupleit.mapmarkers.dynamicmapmarkers.Login.LoginActivity;
import com.dupleit.mapmarkers.dynamicmapmarkers.Login.modal.LoginDatum;
import com.dupleit.mapmarkers.dynamicmapmarkers.Login.modal.UserLogin;
import com.dupleit.mapmarkers.dynamicmapmarkers.MainActivity;
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.APIService;
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.ApiClient;
import com.dupleit.mapmarkers.dynamicmapmarkers.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup extends AppCompatActivity {

    @BindView(R.id.etUserName)
    EditText etUserName;

    @BindView(R.id.etMobile)
    EditText etMobile;

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;
    boolean checkbox = false;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        progress_bar.setVisibility(View.GONE);
        ctx = this;

    }
    @OnClick(R.id.alreadyMember)
    public void skip(){
        startActivity(new Intent(this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    @OnClick(R.id.show_hide_password)
    public void hideShow(){
        if (!checkbox){
            etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            checkbox = true;
        }else{
            etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
            checkbox = false;
        }
    }

    @OnClick(R.id.signupBtn)
    public void SignUP(){
        if(validateData()){
            //progress_bar_login.setVisibility(View.GONE);
            RegisterUserOnDB(etUserName.getText().toString(),etPassword.getText().toString(),etEmail.getText().toString(),etMobile.getText().toString());
        }else{
            progress_bar.setVisibility(View.GONE);
        }
    }

    private void RegisterUserOnDB(String name, String password, String email, String mobile) {
        if (!checkInternetState.getInstance(this).isOnline()) {
            Toast.makeText(this, "Cannot connect to network", Toast.LENGTH_SHORT).show();
        }else {
            APIService service = ApiClient.getClient().create(APIService.class);
            Call<UserLogin> userCall = service.registeruser_request(email,password,name,mobile);
            userCall.enqueue(new Callback<UserLogin>() {
                @Override
                public void onResponse(Call<UserLogin> call, Response<UserLogin> response) {
                    if (response.body().getStatus()) {
                        List<LoginDatum> data = response.body().getLoginData();
                        for (LoginDatum userData: data) {
                            new PreferenceManager(ctx).saveUserDetails(userData.getUSEREMAIL(),userData.getUSERNAME(),userData.getUSERIMAGE(),userData.getUSERMOBILE(),userData.getUSERID());
                            ctx.startActivity(new Intent(ctx,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        }
                    }else{
                        Toast.makeText(ctx, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserLogin> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                }
            });
        }
    }

    private boolean validateData() {
        if (etUserName.getText().toString().equals("")){
            etUserName.setError("Enter full name");
            return false;
        }

        if (etMobile.getText().toString().equals("")){
            etMobile.setError("enter mobile no");
            return false;
        }else if(etMobile.getText().length()!=10){
            etMobile.setError("enter 10 digit mobile no");

        }

        if (etEmail.getText().toString().equals("") ){
            etEmail.setError("Enter Email");
            return false;
        }else if (!isEmailValid(etEmail.getText().toString())){
            etEmail.setError("Email is not valid");
            return false;
        }

        if (etPassword.getText().toString().equals("")){
            etPassword.setError("Enter Password");
            return false;
        }else if (etPassword.getText().length()<6){
            etPassword.setError("Enter Strong Password");
            return false;
        }

        return true;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
