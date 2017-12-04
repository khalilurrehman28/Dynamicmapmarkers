package com.dupleit.demo.mapproject.Login.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Datum {

    @SerializedName("USER_ID")
    @Expose
    var userid: String? = null
    @SerializedName("USER_NAME")
    @Expose
    var username: String? = null
    @SerializedName("USER_ADDRESS")
    @Expose
    var useraddress: String? = null
    @SerializedName("USER_MOBILE")
    @Expose
    var usermobile: String? = null
    @SerializedName("USER_ALTERNATENUMBER")
    @Expose
    var useralternatenumber: String? = null
    @SerializedName("USER_SOCIAL_ID")
    @Expose
    var usersocialid: String? = null
    @SerializedName("USER_PASSWORD")
    @Expose
    var userpassword: String? = null
    @SerializedName("USER_EMAIL")
    @Expose
    var useremail: String? = null
    @SerializedName("USER_REGFROM")
    @Expose
    var userregfrom: String? = null
    @SerializedName("USER_BLOCK")
    @Expose
    var userblock: String? = null
    @SerializedName("USER_STATUS")
    @Expose
    var userstatus: String? = null
    @SerializedName("USER_NUMBER_VERIFY")
    @Expose
    var usernumberverify: String? = null
    @SerializedName("USER_EMAIL_VERIFY")
    @Expose
    var useremailverify: String? = null
    @SerializedName("USER_REGISTRATION_DATE")
    @Expose
    var userregistrationdate: String? = null
    @SerializedName("USER_IMAGE_ID")
    @Expose
    var userimageid: String? = null
    @SerializedName("USER_REFER_CODE")
    @Expose
    var userrefercode: String? = null
    @SerializedName("USER_LAST_UPDATE")
    @Expose
    var userlastupdate: String? = null
    @SerializedName("UPDATE_BY")
    @Expose
    var updateby: String? = null
    @SerializedName("IMAGE_ID")
    @Expose
    var imageid: String? = null
    @SerializedName("IMAGE_PATH")
    @Expose
    var imagepath: String? = null

}
