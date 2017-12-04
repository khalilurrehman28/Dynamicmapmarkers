package com.dupleit.mapmarkers.dynamicmapmarkers.AddPostToDatabase.SignUp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.InputType
import android.text.InputType.*
import android.util.Log
import android.view.View
import com.dupleit.mapmarkers.dynamicmapmarkers.AddPostToDatabase.Login.LoginActivity
import com.dupleit.mapmarkers.dynamicmapmarkers.AddPostToDatabase.SignUp.Model.SignupModel
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.APIService
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.ApiClient
import com.dupleit.mapmarkers.dynamicmapmarkers.R
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity(),View.OnClickListener {
    var togglepassword: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        progress_bar.setVisibility(View.GONE)
        signupBtn.setOnClickListener(this)
        show_hide_password.setOnClickListener(this)
        goLogin.setOnClickListener(this)

    }
    override fun onClick(p0: View?) {
        if (p0==signupBtn){
            if (validateData()) {
                hitApi()
            }
        }
        if (p0==show_hide_password){
            if (togglepassword ==false) {
                etPassword?.inputType =TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                togglepassword = true
                show_hide_password.text="Hide Password"
            } else {
                etPassword?.inputType =InputType.TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD
                togglepassword = false
                show_hide_password.text="Show Password"
            }
        }
        if (p0== goLogin){
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    private fun hitApi() {
        signupBtn.setText("Wait...")
        progress_bar.setVisibility(View.VISIBLE)
        val service = ApiClient.getClient()!!.create(APIService::class.java)
        val userCall = service.signUpRequest(etEmail?.text.toString(), etPassword?.text.toString(), etUserName?.text.toString(), etMobile?.text.toString())
        userCall.enqueue(object : Callback<SignupModel> {
            override fun onResponse(call: Call<SignupModel>, response: Response<SignupModel>) {
                signupBtn.setText("SIGN UP")
                progress_bar.setVisibility(View.GONE)
                if (response.body().status) {
                    showSnackBar(response.body().message)
                } else {
                    showSnackBar(response.body().message)

                    //Toast.makeText(applicationContext, ""+response.body().message, Toast.LENGTH_SHORT).show();
                }
            }

            override fun onFailure(call: Call<SignupModel>, t: Throwable) {
                Log.d("onFailure", t.toString())
            }
        })

    }

    private fun validateData(): Boolean {
        val inputEmail = etEmail?.text.toString().trim()
        val inputPassword = etPassword?.text.toString().trim()

        if (etUserName?.text.isNullOrEmpty()) {
            etUserName?.error = "Name is empty"
            return false
        }
        if (etMobile?.text.isNullOrEmpty()) {
            etMobile?.error = "Mobile number is empty"
            return false
        }else if (etMobile?.length()!=10) {
            etMobile?.error = "10 Digit required"
            return false
        }
        if (inputEmail.isNullOrBlank()) {
            etEmail?.error = "Email is empty"
            return false
        }
        if (inputPassword.isNullOrBlank()) {
            etPassword?.error = "Password is empty"
            return false
        }else if (inputPassword.length!! <4) {
            etPassword?.error = "Please fill strong password"
            return false
        }
        return true
    }
    fun showSnackBar(message: String) {
        val snackbar = Snackbar
                .make(signUp_layout, "" + message, Snackbar.LENGTH_LONG)

        snackbar.show()
    }
}
