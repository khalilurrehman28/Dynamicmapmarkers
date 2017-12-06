package com.dupleit.mapmarkers.dynamicmapmarkers.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.PreferenceManager;
import com.dupleit.mapmarkers.dynamicmapmarkers.Constant.checkInternetState;
import com.dupleit.mapmarkers.dynamicmapmarkers.Login.modal.LoginDatum;
import com.dupleit.mapmarkers.dynamicmapmarkers.Login.modal.UserLogin;
import com.dupleit.mapmarkers.dynamicmapmarkers.MainActivity;
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.APIService;
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.ApiClient;
import com.dupleit.mapmarkers.dynamicmapmarkers.R;
import com.dupleit.mapmarkers.dynamicmapmarkers.Signup.Signup;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etLEmail)
    EditText etLEmail;

    @BindView(R.id.etLPassword)
    EditText etLPassword;

    @BindView(R.id.progress_bar_login)
    ProgressBar progress_bar_login;

    boolean checkbox = false;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        progress_bar_login.setVisibility(View.GONE);
        ctx = this;

    }

    @OnClick(R.id.newMember)
    public void registerNow(){
        startActivity(new Intent(this, Signup.class));
    }

    @OnClick(R.id.loginBtn)
    public void loginBtn(){
        if(validateData()){
            //progress_bar_login.setVisibility(View.GONE);
            CheckLoginDataOnNet(etLEmail.getText().toString(),etLPassword.getText().toString());
        }else{
            progress_bar_login.setVisibility(View.GONE);
        }
    }

    private void CheckLoginDataOnNet(String email, String password) {
        if (!checkInternetState.getInstance(this).isOnline()) {
            Toast.makeText(this, "Cannot connect to network", Toast.LENGTH_SHORT).show();
        }else {
            APIService service = ApiClient.getClient().create(APIService.class);
            Call<UserLogin> userCall = service.checkUserLogin_request(email,password);
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
                        Toast.makeText(ctx, "Incorrect User Credential", Toast.LENGTH_SHORT).show();
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
        //Toast.makeText(this, "Hello" +etLPassword.getText().length(), Toast.LENGTH_SHORT).show();
        if (etLEmail.getText().toString().equals("") || !isEmailValid((etLEmail.getText().toString()))){
            etLEmail.setError("Enter Email");
            return false;
        }

        if (etLPassword.getText().toString().equals("") || etLPassword.getText().length()<6){
            etLPassword.setError("Enter Password");
            return false;
        }
        return true;
    }

    @OnClick(R.id.show_hide_passwordLogin)
    public void showhide(){
        if (!checkbox){
            etLPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            checkbox = true;
        }else{
            etLPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
            checkbox = false;
        }
    }


    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
