package com.ats.tankmaintenance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WorkHistory {
    @SerializedName("workId")
    @Expose
    private Integer workId;
    @SerializedName("customerId")
    @Expose
    private Integer customerId;
    @SerializedName("employeeId")
    @Expose
    private String employeeId;
    @SerializedName("workDate")
    @Expose
    private String workDate;
    @SerializedName("workTime")
    @Expose
    private String workTime;
    @SerializedName("sequenceNumber")
    @Expose
    private Integer sequenceNumber;
    @SerializedName("noOfLowerTank")
    @Expose
    private Integer noOfLowerTank;
    @SerializedName("noOfUpperTank")
    @Expose
    private Integer noOfUpperTank;
    @SerializedName("noOfHrSpend")
    @Expose
    private Integer noOfHrSpend;
    @SerializedName("amtLowerTank")
    @Expose
    private Integer amtLowerTank;
    @SerializedName("amtUpperTank")
    @Expose
    private Integer amtUpperTank;
    @SerializedName("finalAmt")
    @Expose
    private Integer finalAmt;
    @SerializedName("discAmt")
    @Expose
    private Integer discAmt;
    @SerializedName("totalAmt")
    @Expose
    private Integer totalAmt;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("nextDate")
    @Expose
    private String nextDate;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("customerFrequency")
    @Expose
    private Integer customerFrequency;
    @SerializedName("billNumber")
    @Expose
    private String billNumber;
    @SerializedName("delStatus")
    @Expose
    private Integer delStatus;
    @SerializedName("isUsed")
    @Expose
    private Integer isUsed;
    @SerializedName("exInt1")
    @Expose
    private Integer exInt1;
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
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("customerAddress")
    @Expose
    private String customerAddress;
    @SerializedName("customerPhone")
    @Expose
    private String customerPhone;
    @SerializedName("customerContactName")
    @Expose
    private String customerContactName;
    @SerializedName("customerContactNumber")
    @Expose
    private String customerContactNumber;
    @SerializedName("user")
    @Expose
    private List<User> user = null;
    int visibleStatus;

    public Integer getWorkId() {
        return workId;
    }

    public void setWorkId(Integer workId) {
        this.workId = workId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Integer getNoOfLowerTank() {
        return noOfLowerTank;
    }

    public void setNoOfLowerTank(Integer noOfLowerTank) {
        this.noOfLowerTank = noOfLowerTank;
    }

    public Integer getNoOfUpperTank() {
        return noOfUpperTank;
    }

    public void setNoOfUpperTank(Integer noOfUpperTank) {
        this.noOfUpperTank = noOfUpperTank;
    }

    public Integer getNoOfHrSpend() {
        return noOfHrSpend;
    }

    public void setNoOfHrSpend(Integer noOfHrSpend) {
        this.noOfHrSpend = noOfHrSpend;
    }

    public Integer getAmtLowerTank() {
        return amtLowerTank;
    }

    public void setAmtLowerTank(Integer amtLowerTank) {
        this.amtLowerTank = amtLowerTank;
    }

    public Integer getAmtUpperTank() {
        return amtUpperTank;
    }

    public void setAmtUpperTank(Integer amtUpperTank) {
        this.amtUpperTank = amtUpperTank;
    }

    public Integer getFinalAmt() {
        return finalAmt;
    }

    public void setFinalAmt(Integer finalAmt) {
        this.finalAmt = finalAmt;
    }

    public Integer getDiscAmt() {
        return discAmt;
    }

    public void setDiscAmt(Integer discAmt) {
        this.discAmt = discAmt;
    }

    public Integer getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Integer totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNextDate() {
        return nextDate;
    }

    public void setNextDate(String nextDate) {
        this.nextDate = nextDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCustomerFrequency() {
        return customerFrequency;
    }

    public void setCustomerFrequency(Integer customerFrequency) {
        this.customerFrequency = customerFrequency;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
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

    public Integer getExInt1() {
        return exInt1;
    }

    public void setExInt1(Integer exInt1) {
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

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public int getVisibleStatus() {
        return visibleStatus;
    }

    public void setVisibleStatus(int visibleStatus) {
        this.visibleStatus = visibleStatus;
    }

    @Override
    public String toString() {
        return "WorkHistory{" +
                "workId=" + workId +
                ", customerId=" + customerId +
                ", employeeId='" + employeeId + '\'' +
                ", workDate='" + workDate + '\'' +
                ", workTime='" + workTime + '\'' +
                ", sequenceNumber=" + sequenceNumber +
                ", noOfLowerTank=" + noOfLowerTank +
                ", noOfUpperTank=" + noOfUpperTank +
                ", noOfHrSpend=" + noOfHrSpend +
                ", amtLowerTank=" + amtLowerTank +
                ", amtUpperTank=" + amtUpperTank +
                ", finalAmt=" + finalAmt +
                ", discAmt=" + discAmt +
                ", totalAmt=" + totalAmt +
                ", remark='" + remark + '\'' +
                ", nextDate='" + nextDate + '\'' +
                ", status=" + status +
                ", customerFrequency=" + customerFrequency +
                ", billNumber='" + billNumber + '\'' +
                ", delStatus=" + delStatus +
                ", isUsed=" + isUsed +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exInt3=" + exInt3 +
                ", exVar1=" + exVar1 +
                ", exVar2=" + exVar2 +
                ", exVar3=" + exVar3 +
                ", customerName='" + customerName + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", customerContactName='" + customerContactName + '\'' +
                ", customerContactNumber='" + customerContactNumber + '\'' +
                ", user=" + user +
                ", visibleStatus=" + visibleStatus +
                '}';
    }
}
