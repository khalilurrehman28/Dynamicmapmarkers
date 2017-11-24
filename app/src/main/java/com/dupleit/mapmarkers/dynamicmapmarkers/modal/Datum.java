
package com.dupleit.mapmarkers.dynamicmapmarkers.modal;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

public class Datum implements ClusterItem, Parcelable {

    @SerializedName("POST_ID")
    @Expose
    private String pOSTID;
    @SerializedName("USER_ID_FK")
    @Expose
    private String uSERIDFK;
    @SerializedName("POST_IMAGE_URL")
    @Expose
    private String pOSTIMAGEURL;
    @SerializedName("POST_DESCRIPTION")
    @Expose
    private String pOSTDESCRIPTION;
    @SerializedName("POST_LATITUDE")
    @Expose
    private String pOSTLATITUDE;
    @SerializedName("POST_LONGITUDE")
    @Expose
    private String pOSTLONGITUDE;
    @SerializedName("POST_BLOCK")
    @Expose
    private String pOSTBLOCK;
    @SerializedName("POST_DELETE")
    @Expose
    private String pOSTDELETE;
    @SerializedName("POST_DATETIME")
    @Expose
    private String pOSTDATETIME;
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

    LatLng userPostLatLang;

    public Datum(String pOSTID, String uSERIDFK, String pOSTIMAGEURL, String pOSTDESCRIPTION, String pOSTBLOCK, String pOSTDELETE, String pOSTDATETIME, String uSERID, String uSERNAME, String uSERTYPE, String uSERIMAGE, String uSERMOBILE, String uSERALTNUMBER, String uSEREMAIL, String uSERPASSWORD, String uSERACTIVE, LatLng userPostLatLang) {
        this.pOSTID = pOSTID;
        this.uSERIDFK = uSERIDFK;
        this.pOSTIMAGEURL = pOSTIMAGEURL;
        this.pOSTDESCRIPTION = pOSTDESCRIPTION;
        this.pOSTBLOCK = pOSTBLOCK;
        this.pOSTDELETE = pOSTDELETE;
        this.pOSTDATETIME = pOSTDATETIME;
        this.uSERID = uSERID;
        this.uSERNAME = uSERNAME;
        this.uSERTYPE = uSERTYPE;
        this.uSERIMAGE = uSERIMAGE;
        this.uSERMOBILE = uSERMOBILE;
        this.uSERALTNUMBER = uSERALTNUMBER;
        this.uSEREMAIL = uSEREMAIL;
        this.uSERPASSWORD = uSERPASSWORD;
        this.uSERACTIVE = uSERACTIVE;
        this.userPostLatLang = userPostLatLang;
    }

    public LatLng getUserPostLatLang() {
        return userPostLatLang;
    }

    public void setUserPostLatLang(LatLng userPostLatLang) {
        this.userPostLatLang = userPostLatLang;
    }

    public String getPOSTID() {
        return pOSTID;
    }

    public void setPOSTID(String pOSTID) {
        this.pOSTID = pOSTID;
    }

    public String getUSERIDFK() {
        return uSERIDFK;
    }

    public void setUSERIDFK(String uSERIDFK) {
        this.uSERIDFK = uSERIDFK;
    }

    public String getPOSTIMAGEURL() {
        return pOSTIMAGEURL;
    }

    public void setPOSTIMAGEURL(String pOSTIMAGEURL) {
        this.pOSTIMAGEURL = pOSTIMAGEURL;
    }

    public String getPOSTDESCRIPTION() {
        return pOSTDESCRIPTION;
    }

    public void setPOSTDESCRIPTION(String pOSTDESCRIPTION) {
        this.pOSTDESCRIPTION = pOSTDESCRIPTION;
    }

    public String getPOSTLATITUDE() {
        return pOSTLATITUDE;
    }

    public void setPOSTLATITUDE(String pOSTLATITUDE) {
        this.pOSTLATITUDE = pOSTLATITUDE;
    }

    public String getPOSTLONGITUDE() {
        return pOSTLONGITUDE;
    }

    public void setPOSTLONGITUDE(String pOSTLONGITUDE) {
        this.pOSTLONGITUDE = pOSTLONGITUDE;
    }

    public String getPOSTBLOCK() {
        return pOSTBLOCK;
    }

    public void setPOSTBLOCK(String pOSTBLOCK) {
        this.pOSTBLOCK = pOSTBLOCK;
    }

    public String getPOSTDELETE() {
        return pOSTDELETE;
    }

    public void setPOSTDELETE(String pOSTDELETE) {
        this.pOSTDELETE = pOSTDELETE;
    }

    public String getPOSTDATETIME() {
        return pOSTDATETIME;
    }

    public void setPOSTDATETIME(String pOSTDATETIME) {
        this.pOSTDATETIME = pOSTDATETIME;
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

    @Override
    public LatLng getPosition() {
        return userPostLatLang;
    }

    @Override
    public String getTitle() {
        return uSERNAME;
    }

    @Override
    public String getSnippet() {
        return pOSTDESCRIPTION;
    }


    protected Datum(Parcel in) {
        pOSTID = in.readString();
        uSERIDFK = in.readString();
        pOSTIMAGEURL = in.readString();
        pOSTDESCRIPTION = in.readString();
        pOSTLATITUDE = in.readString();
        pOSTLONGITUDE = in.readString();
        pOSTBLOCK = in.readString();
        pOSTDELETE = in.readString();
        pOSTDATETIME = in.readString();
        uSERID = in.readString();
        uSERNAME = in.readString();
        uSERTYPE = in.readString();
        uSERIMAGE = in.readString();
        uSERMOBILE = in.readString();
        uSERALTNUMBER = in.readString();
        uSEREMAIL = in.readString();
        uSERPASSWORD = in.readString();
        uSERACTIVE = in.readString();
        userPostLatLang = (LatLng) in.readValue(LatLng.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pOSTID);
        dest.writeString(uSERIDFK);
        dest.writeString(pOSTIMAGEURL);
        dest.writeString(pOSTDESCRIPTION);
        dest.writeString(pOSTLATITUDE);
        dest.writeString(pOSTLONGITUDE);
        dest.writeString(pOSTBLOCK);
        dest.writeString(pOSTDELETE);
        dest.writeString(pOSTDATETIME);
        dest.writeString(uSERID);
        dest.writeString(uSERNAME);
        dest.writeString(uSERTYPE);
        dest.writeString(uSERIMAGE);
        dest.writeString(uSERMOBILE);
        dest.writeString(uSERALTNUMBER);
        dest.writeString(uSEREMAIL);
        dest.writeString(uSERPASSWORD);
        dest.writeString(uSERACTIVE);
        dest.writeValue(userPostLatLang);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Datum> CREATOR = new Parcelable.Creator<Datum>() {
        @Override
        public Datum createFromParcel(Parcel in) {
            return new Datum(in);
        }

        @Override
        public Datum[] newArray(int size) {
            return new Datum[size];
        }
    };
}