package com.dupleit.mapmarkers.dynamicmapmarkers.AddPostToDatabase.Login

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Toast
import com.dupleit.demo.mapproject.Login.model.LoginModel
import com.dupleit.mapmarkers.dynamicmapmarkers.AddPostToDatabase.SignUp.SignUpActivity
import com.dupleit.mapmarkers.dynamicmapmarkers.MainActivity
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.APIService
import com.dupleit.mapmarkers.dynamicmapmarkers.Network.ApiClient
import com.dupleit.mapmarkers.dynamicmapmarkers.R
import com.dupleit.mapmarkers.dynamicmapmarkers.R.id.frame
import kotlinx.android.synthetic.main.activity_login.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity(), View.OnClickListener {
    var togglepassword: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progress_bar_login.setVisibility(View.VISIBLE)
        loginBtn.setOnClickListener(this)
        show_hide_passwordLogin.setOnClickListener(this)
        goSignUp.setOnClickListener(this)

        title = "Welcome"
    }
    override fun onClick(p0: View?) {
        if (p0 == loginBtn){
            if (validateData()) {
                hitApi()
            }
        }
        if (p0==show_hide_passwordLogin){
            if (togglepassword ==false) {
                etLPassword?.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                togglepassword = true
                show_hide_passwordLogin.text="Hide Password"
            } else {
                etLPassword?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                togglepassword = false
                show_hide_passwordLogin.text="Show Password"
            }
        }
        if(p0 == goSignUp){
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
    private fun hitApi() {
        loginBtn.setText("Wait...");
        progress_bar_login.setVisibility(View.VISIBLE)
        val inputLEmail = etLEmail?.text.toString().trim()
        val inputLPassword = etLPassword?.text.toString().trim()
        val service = ApiClient.getClient()!!.create(APIService::class.java)
        val userCall = service.loginRequest(inputLEmail,inputLPassword)
        userCall.enqueue(object : Callback<LoginModel> {
            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                loginBtn.setText("LOG IN");

                progress_bar_login.setVisibility(View.GONE)
                if (response.isSuccessful) {
                    if (response.body().status!!) {
                        toast("hello")
                        //Toast.makeText(this@LoginActivity,"",Toast.LENGTH_LONG).show()
                        val i = Intent(this@LoginActivity, MainActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(i)
                        finish()

                    }else{
                        showSnackBar(response.body().message!!)
                    }

                } else {
                    showSnackBar("Something went wrong")
                    //Toast.makeText(applicationContext, ""+response.body().message, Toast.LENGTH_SHORT).show();
                }
            }

            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                Log.d("onFailure", t.toString())
            }
        })

    }
    fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }
    private fun validateData(): Boolean {
        val inputLEmail = etLEmail?.text.toString().trim()
        val inputLPassword = etLPassword?.text.toString().trim()
        if (inputLEmail.isNullOrEmpty()) {
            etLEmail?.error = "Email is empty"
            return false
        }

        if (inputLPassword.isNullOrEmpty()) {
            etLPassword?.error = "Password is empty"
            return false
        }else if (inputLPassword.length!! <4) {
            etLPassword?.error = "Please fill strong password"
            return false
        }
        return true
    }
    fun showSnackBar(message: String) {
        val snackbar = Snackbar
                .make(frame, "" + message, Snackbar.LENGTH_LONG)

        snackbar.show()
    }

}
