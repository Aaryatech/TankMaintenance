package com.ats.tankmaintenance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentDetail {

    @SerializedName("paymentId")
    @Expose
    private Integer paymentId;
    @SerializedName("paymentDate")
    @Expose
    private String paymentDate;
    @SerializedName("costRs")
    @Expose
    private Integer costRs;
    @SerializedName("totalAmt")
    @Expose
    private Integer totalAmt;
    @SerializedName("customerId")
    @Expose
    private Integer customerId;

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Integer getCostRs() {
        return costRs;
    }

    public void setCostRs(Integer costRs) {
        this.costRs = costRs;
    }

    public Integer getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Integer totalAmt) {
        this.totalAmt = totalAmt;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "PaymentDetail{" +
                "paymentId=" + paymentId +
                ", paymentDate='" + paymentDate + '\'' +
                ", costRs=" + costRs +
                ", totalAmt=" + totalAmt +
                ", customerId=" + customerId +
                '}';
    }
}
