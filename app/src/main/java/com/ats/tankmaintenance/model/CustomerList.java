package com.ats.tankmaintenance.model;

public class CustomerList {
    String name;
    String mob;
    String area;
    String address;

    public CustomerList(String name, String mob, String area, String address) {
        this.name = name;
        this.mob = mob;
        this.area = area;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "CustomerList{" +
                "name='" + name + '\'' +
                ", mob='" + mob + '\'' +
                ", area='" + area + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
