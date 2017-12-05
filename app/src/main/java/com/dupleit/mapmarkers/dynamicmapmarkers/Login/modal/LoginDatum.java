
package com.dupleit.mapmarkers.dynamicmapmarkers.Login.modal;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginDatum implements Serializable
{

    @SerializedName("USER_ID")
    @Expose
    private String uSERID;
    @SerializedName("USER_NAME")
    @Expose
    private String uSERNAME;
    @SerializedName("USER_TYPE")
    @Expose
    private String uSERTYPE;
    @SerializedName("USER_IMAGE")
    @Expose
    private String uSERIMAGE;
    @SerializedName("USER_MOBILE")
    @Expose
    private String uSERMOBILE;
    @SerializedName("USER_ALTNUMBER")
    @Expose
    private String uSERALTNUMBER;
    @SerializedName("USER_EMAIL")
    @Expose
    private String uSEREMAIL;
    @SerializedName("USER_PASSWORD")
    @Expose
    private String uSERPASSWORD;
    @SerializedName("USER_ACTIVE")
    @Expose
    private String uSERACTIVE;
    private final static long serialVersionUID = 7215696652523004298L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public LoginDatum() {
    }

    /**
     * 
     * @param uSERMOBILE
     * @param uSERTYPE
     * @param uSERACTIVE
     * @param uSERPASSWORD
     * @param uSERALTNUMBER
     * @param uSERID
     * @param uSEREMAIL
     * @param uSERIMAGE
     * @param uSERNAME
     */
    public LoginDatum(String uSERID, String uSERNAME, String uSERTYPE, String uSERIMAGE, String uSERMOBILE, String uSERALTNUMBER, String uSEREMAIL, String uSERPASSWORD, String uSERACTIVE) {
        super();
        this.uSERID = uSERID;
        this.uSERNAME = uSERNAME;
        this.uSERTYPE = uSERTYPE;
        this.uSERIMAGE = uSERIMAGE;
        this.uSERMOBILE = uSERMOBILE;
        this.uSERALTNUMBER = uSERALTNUMBER;
        this.uSEREMAIL = uSEREMAIL;
        this.uSERPASSWORD = uSERPASSWORD;
        this.uSERACTIVE = uSERACTIVE;
    }

    public String getUSERID() {
        return uSERID;
    }

    public void setUSERID(String uSERID) {
        this.uSERID = uSERID;
    }

    public String getUSERNAME() {
        return uSERNAME;
    }

    public void setUSERNAME(String uSERNAME) {
        this.uSERNAME = uSERNAME;
    }

    public String getUSERTYPE() {
        return uSERTYPE;
    }

    public void setUSERTYPE(String uSERTYPE) {
        this.uSERTYPE = uSERTYPE;
    }

    public String getUSERIMAGE() {
        return uSERIMAGE;
    }

    public void setUSERIMAGE(String uSERIMAGE) {
        this.uSERIMAGE = uSERIMAGE;
    }

    public String getUSERMOBILE() {
        return uSERMOBILE;
    }

    public void setUSERMOBILE(String uSERMOBILE) {
        this.uSERMOBILE = uSERMOBILE;
    }

    public String getUSERALTNUMBER() {
        return uSERALTNUMBER;
    }

    public void setUSERALTNUMBER(String uSERALTNUMBER) {
        this.uSERALTNUMBER = uSERALTNUMBER;
    }

    public String getUSEREMAIL() {
        return uSEREMAIL;
    }

    public void setUSEREMAIL(String uSEREMAIL) {
        this.uSEREMAIL = uSEREMAIL;
    }

    public String getUSERPASSWORD() {
        return uSERPASSWORD;
    }

    public void setUSERPASSWORD(String uSERPASSWORD) {
        this.uSERPASSWORD = uSERPASSWORD;
    }

    public String getUSERACTIVE() {
        return uSERACTIVE;
    }

    public void setUSERACTIVE(String uSERACTIVE) {
        this.uSERACTIVE = uSERACTIVE;
    }

}
