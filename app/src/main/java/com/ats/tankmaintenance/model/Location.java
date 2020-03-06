package com.ats.tankmaintenance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("locationId")
    @Expose
    private Integer locationId;
    @SerializedName("locationName")
    @Expose
    private String locationName;
    @SerializedName("locationDetails")
    @Expose
    private String locationDetails;
    @SerializedName("delStatus")
    @Expose
    private Integer delStatus;
    @SerializedName("isUsed")
    @Expose
    private Integer isUsed;
    @SerializedName("addOnRate")
    @Expose
    private Integer addOnRate;
    @SerializedName("pinCode")
    @Expose
    private String pinCode;
    @SerializedName("exInt1")
    @Expose
    private Object exInt1;
    @SerializedName("exInt2")
    @Expose
    private Object exInt2;
    @SerializedName("exInt3")
    @Expose
    private Object exInt3;
    @SerializedName("exVar1")
    @Expose
    private Object exVar1;
    @SerializedName("exVar2")
    @Expose
    private Object exVar2;
    @SerializedName("exVar3")
    @Expose
    private Object exVar3;
    @SerializedName("msg")
    @Expose
    private Object msg;
    @SerializedName("error")
    @Expose
    private Boolean error;

    public Location(Integer locationId, String locationName, String locationDetails, Integer delStatus, Integer isUsed, Integer addOnRate, String pinCode, Object exInt1, Object exInt2, Object exInt3, Object exVar1, Object exVar2, Object exVar3) {
        this.locationId = locationId;
        this.locationName = locationName;
        this.locationDetails = locationDetails;
        this.delStatus = delStatus;
        this.isUsed = isUsed;
        this.addOnRate = addOnRate;
        this.pinCode = pinCode;
        this.exInt1 = exInt1;
        this.exInt2 = exInt2;
        this.exInt3 = exInt3;
        this.exVar1 = exVar1;
        this.exVar2 = exVar2;
        this.exVar3 = exVar3;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(String locationDetails) {
        this.locationDetails = locationDetails;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    public Integer getAddOnRate() {
        return addOnRate;
    }

    public void setAddOnRate(Integer addOnRate) {
        this.addOnRate = addOnRate;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public Object getExInt1() {
        return exInt1;
    }

    public void setExInt1(Object exInt1) {
        this.exInt1 = exInt1;
    }

    public Object getExInt2() {
        return exInt2;
    }

    public void setExInt2(Object exInt2) {
        this.exInt2 = exInt2;
    }

    public Object getExInt3() {
        return exInt3;
    }

    public void setExInt3(Object exInt3) {
        this.exInt3 = exInt3;
    }

    public Object getExVar1() {
        return exVar1;
    }

    public void setExVar1(Object exVar1) {
        this.exVar1 = exVar1;
    }

    public Object getExVar2() {
        return exVar2;
    }

    public void setExVar2(Object exVar2) {
        this.exVar2 = exVar2;
    }

    public Object getExVar3() {
        return exVar3;
    }

    public void setExVar3(Object exVar3) {
        this.exVar3 = exVar3;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Location{" +
                "locationId=" + locationId +
                ", locationName='" + locationName + '\'' +
                ", locationDetails='" + locationDetails + '\'' +
                ", delStatus=" + delStatus +
                ", isUsed=" + isUsed +
                ", addOnRate=" + addOnRate +
                ", pinCode='" + pinCode + '\'' +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exInt3=" + exInt3 +
                ", exVar1=" + exVar1 +
                ", exVar2=" + exVar2 +
                ", exVar3=" + exVar3 +
                ", msg=" + msg +
                ", error=" + error +
                '}';
    }
}
