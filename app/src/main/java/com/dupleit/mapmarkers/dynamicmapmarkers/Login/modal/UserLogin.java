
package com.dupleit.mapmarkers.dynamicmapmarkers.Login.modal;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLogin implements Serializable
{

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("loginData")
    @Expose
    private List<LoginDatum> loginData = null;
    private final static long serialVersionUID = -8133728979125691058L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public UserLogin() {
    }

    /**
     * 
     * @param message
     * @param status
     * @param code
     * @param loginData
     */
    public UserLogin(Boolean status, Integer code, String message, List<LoginDatum> loginData) {
        super();
        this.status = status;
        this.code = code;
        this.message = message;
        this.loginData = loginData;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<LoginDatum> getLoginData() {
        return loginData;
    }

    public void setLoginData(List<LoginDatum> loginData) {
        this.loginData = loginData;
    }

}
