package com.ats.tankmaintenance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payment {

    @SerializedName("customerId")
    @Expose
    private Integer customerId;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("customerAddress")
    @Expose
    private String customerAddress;
    @SerializedName("customerPhone")
    @Expose
    private String customerPhone;
    @SerializedName("customerPhone2")
    @Expose
    private String customerPhone2;
    @SerializedName("customerContactName")
    @Expose
    private String customerContactName;
    @SerializedName("customerContactNumber")
    @Expose
    private String customerContactNumber;
    @SerializedName("noOfUpperTank")
    @Expose
    private Integer noOfUpperTank;
    @SerializedName("noOfLowerTank")
    @Expose
    private Integer noOfLowerTank;
    @SerializedName("costUppertankPerpieces")
    @Expose
    private Integer costUppertankPerpieces;
    @SerializedName("costLowertankPerpieces")
    @Expose
    private Integer costLowertankPerpieces;
    @SerializedName("areaId")
    @Expose
    private Integer areaId;
    @SerializedName("frequency")
    @Expose
    private Integer frequency;
    @SerializedName("delStatus")
    @Expose
    private Integer delStatus;
    @SerializedName("isUsed")
    @Expose
    private Integer isUsed;
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
    @SerializedName("exFloat1")
    @Expose
    private Integer exFloat1;
    @SerializedName("remark")
    @Expose
    private Object remark;
    @SerializedName("workAmt")
    @Expose
    private Integer workAmt;
    @SerializedName("payAmt")
    @Expose
    private Integer payAmt;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerPhone2() {
        return customerPhone2;
    }

    public void setCustomerPhone2(String customerPhone2) {
        this.customerPhone2 = customerPhone2;
    }

    public String getCustomerContactName() {
        return customerContactName;
    }

    public void setCustomerContactName(String customerContactName) {
        this.customerContactName = customerContactName;
    }

    public String getCustomerContactNumber() {
        return customerContactNumber;
    }

    public void setCustomerContactNumber(String customerContactNumber) {
        this.customerContactNumber = customerContactNumber;
    }

    public Integer getNoOfUpperTank() {
        return noOfUpperTank;
    }

    public void setNoOfUpperTank(Integer noOfUpperTank) {
        this.noOfUpperTank = noOfUpperTank;
    }

    public Integer getNoOfLowerTank() {
        return noOfLowerTank;
    }

    public void setNoOfLowerTank(Integer noOfLowerTank) {
        this.noOfLowerTank = noOfLowerTank;
    }

    public Integer getCostUppertankPerpieces() {
        return costUppertankPerpieces;
    }

    public void setCostUppertankPerpieces(Integer costUppertankPerpieces) {
        this.costUppertankPerpieces = costUppertankPerpieces;
    }

    public Integer getCostLowertankPerpieces() {
        return costLowertankPerpieces;
    }

    public void setCostLowertankPerpieces(Integer costLowertankPerpieces) {
        this.costLowertankPerpieces = costLowertankPerpieces;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
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

    public Integer getExFloat1() {
        return exFloat1;
    }

    public void setExFloat1(Integer exFloat1) {
        this.exFloat1 = exFloat1;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public Integer getWorkAmt() {
        return workAmt;
    }

    public void setWorkAmt(Integer workAmt) {
        this.workAmt = workAmt;
    }

    public Integer getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(Integer payAmt) {
        this.payAmt = payAmt;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", customerPhone2='" + customerPhone2 + '\'' +
                ", customerContactName='" + customerContactName + '\'' +
                ", customerContactNumber='" + customerContactNumber + '\'' +
                ", noOfUpperTank=" + noOfUpperTank +
                ", noOfLowerTank=" + noOfLowerTank +
                ", costUppertankPerpieces=" + costUppertankPerpieces +
                ", costLowertankPerpieces=" + costLowertankPerpieces +
                ", areaId=" + areaId +
                ", frequency=" + frequency +
                ", delStatus=" + delStatus +
                ", isUsed=" + isUsed +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exInt3=" + exInt3 +
                ", exVar1=" + exVar1 +
                ", exVar2=" + exVar2 +
                ", exVar3=" + exVar3 +
                ", exFloat1=" + exFloat1 +
                ", remark=" + remark +
                ", workAmt=" + workAmt +
                ", payAmt=" + payAmt +
                '}';
    }
}
