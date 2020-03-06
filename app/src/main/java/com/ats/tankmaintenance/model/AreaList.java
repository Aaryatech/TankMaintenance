package com.ats.tankmaintenance.model;

public class AreaList {
    String areaName;
    String pincode;

    public AreaList(String areaName, String pincode) {
        this.areaName = areaName;
        this.pincode = pincode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    @Override
    public String toString() {
        return "AreaList{" +
                "areaName='" + areaName + '\'' +
                ", pincode='" + pincode + '\'' +
                '}';
    }
}
