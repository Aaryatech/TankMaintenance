package com.ats.tankmaintenance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MonthReport {
    @Expose
    private Integer paymentId;
    @SerializedName("monthName")
    @Expose
    private String monthName;
    @SerializedName("monthDate")
    @Expose
    private String monthDate;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("costRs")
    @Expose
    private Integer costRs;
    @SerializedName("totalAmt")
    @Expose
    private Integer totalAmt;
    @SerializedName("finalAmt")
    @Expose
    private Integer finalAmt;
    @SerializedName("discAmt")
    @Expose
    private Integer discAmt;

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getMonthDate() {
        return monthDate;
    }

    public void setMonthDate(String monthDate) {
        this.monthDate = monthDate;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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

    @Override
    public String toString() {
        return "MonthReport{" +
                "paymentId=" + paymentId +
                ", monthName='" + monthName + '\'' +
                ", monthDate='" + monthDate + '\'' +
                ", year='" + year + '\'' +
                ", costRs=" + costRs +
                ", totalAmt=" + totalAmt +
                ", finalAmt=" + finalAmt +
                ", discAmt=" + discAmt +
                '}';
    }
}
