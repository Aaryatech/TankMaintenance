package com.ats.tankmaintenance.model;

public class Work {

    private int workId;
    private int customerId;
    private String employeeId;
    private String workDate;
    private String workTime;
    private int sequenceNumber;
    private float noOfLowerTank;
    private float noOfUpperTank;
    private float noOfHrSpend ;
    private float amtLowerTank;
    private float amtUpperTank;
    private float finalAmt;
    private float discAmt;
    private float totalAmt;
    private String remark;
    private String nextDate;
    private int status;
    private float customerFrequency;
    private String billNumber;
    private int delStatus;
    private int isUsed;
    private Integer exInt1;
    private Integer exInt2;
    private Integer exInt3;
    private String exVar1;
    private String exVar2;
    private String exVar3;
    private String msg;

    public Work(int workId, int customerId, String employeeId, String workDate, String workTime, int sequenceNumber, float noOfLowerTank, float noOfUpperTank, float noOfHrSpend, float amtLowerTank, float amtUpperTank, float finalAmt, float discAmt, float totalAmt, String remark, String nextDate, int status, float customerFrequency, String billNumber, int delStatus, int isUsed, Integer exInt1, Integer exInt2, Integer exInt3, String exVar1, String exVar2, String exVar3, String msg) {
        this.workId = workId;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.workDate = workDate;
        this.workTime = workTime;
        this.sequenceNumber = sequenceNumber;
        this.noOfLowerTank = noOfLowerTank;
        this.noOfUpperTank = noOfUpperTank;
        this.noOfHrSpend = noOfHrSpend;
        this.amtLowerTank = amtLowerTank;
        this.amtUpperTank = amtUpperTank;
        this.finalAmt = finalAmt;
        this.discAmt = discAmt;
        this.totalAmt = totalAmt;
        this.remark = remark;
        this.nextDate = nextDate;
        this.status = status;
        this.customerFrequency = customerFrequency;
        this.billNumber = billNumber;
        this.delStatus = delStatus;
        this.isUsed = isUsed;
        this.exInt1 = exInt1;
        this.exInt2 = exInt2;
        this.exInt3 = exInt3;
        this.exVar1 = exVar1;
        this.exVar2 = exVar2;
        this.exVar3 = exVar3;
        this.msg = msg;
    }

    public int getWorkId() {
        return workId;
    }

    public void setWorkId(int workId) {
        this.workId = workId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
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

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public float getNoOfLowerTank() {
        return noOfLowerTank;
    }

    public void setNoOfLowerTank(float noOfLowerTank) {
        this.noOfLowerTank = noOfLowerTank;
    }

    public float getNoOfUpperTank() {
        return noOfUpperTank;
    }

    public void setNoOfUpperTank(float noOfUpperTank) {
        this.noOfUpperTank = noOfUpperTank;
    }

    public float getNoOfHrSpend() {
        return noOfHrSpend;
    }

    public void setNoOfHrSpend(float noOfHrSpend) {
        this.noOfHrSpend = noOfHrSpend;
    }

    public float getAmtLowerTank() {
        return amtLowerTank;
    }

    public void setAmtLowerTank(float amtLowerTank) {
        this.amtLowerTank = amtLowerTank;
    }

    public float getAmtUpperTank() {
        return amtUpperTank;
    }

    public void setAmtUpperTank(float amtUpperTank) {
        this.amtUpperTank = amtUpperTank;
    }

    public float getFinalAmt() {
        return finalAmt;
    }

    public void setFinalAmt(float finalAmt) {
        this.finalAmt = finalAmt;
    }

    public float getDiscAmt() {
        return discAmt;
    }

    public void setDiscAmt(float discAmt) {
        this.discAmt = discAmt;
    }

    public float getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(float totalAmt) {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getCustomerFrequency() {
        return customerFrequency;
    }

    public void setCustomerFrequency(float customerFrequency) {
        this.customerFrequency = customerFrequency;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public int getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(int delStatus) {
        this.delStatus = delStatus;
    }

    public int getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(int isUsed) {
        this.isUsed = isUsed;
    }

    public Integer getExInt1() {
        return exInt1;
    }

    public void setExInt1(Integer exInt1) {
        this.exInt1 = exInt1;
    }

    public Integer getExInt2() {
        return exInt2;
    }

    public void setExInt2(Integer exInt2) {
        this.exInt2 = exInt2;
    }

    public Integer getExInt3() {
        return exInt3;
    }

    public void setExInt3(Integer exInt3) {
        this.exInt3 = exInt3;
    }

    public String getExVar1() {
        return exVar1;
    }

    public void setExVar1(String exVar1) {
        this.exVar1 = exVar1;
    }

    public String getExVar2() {
        return exVar2;
    }

    public void setExVar2(String exVar2) {
        this.exVar2 = exVar2;
    }

    public String getExVar3() {
        return exVar3;
    }

    public void setExVar3(String exVar3) {
        this.exVar3 = exVar3;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Work{" +
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
                ", exVar1='" + exVar1 + '\'' +
                ", exVar2='" + exVar2 + '\'' +
                ", exVar3='" + exVar3 + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
