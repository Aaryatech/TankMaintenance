package com.ats.tankmaintenance.model;

public class ScheduleList {
    String name;
    String address;
    String contact;
    String date;
    String upperTank;
    String lowerTank;

    public ScheduleList(String name, String address, String contact, String date, String upperTank, String lowerTank) {
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.date = date;
        this.upperTank = upperTank;
        this.lowerTank = lowerTank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUpperTank() {
        return upperTank;
    }

    public void setUpperTank(String upperTank) {
        this.upperTank = upperTank;
    }

    public String getLowerTank() {
        return lowerTank;
    }

    public void setLowerTank(String lowerTank) {
        this.lowerTank = lowerTank;
    }

    @Override
    public String toString() {
        return "ScheduleList{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", date='" + date + '\'' +
                ", upperTank='" + upperTank + '\'' +
                ", lowerTank='" + lowerTank + '\'' +
                '}';
    }
}
