package com.ats.tankmaintenance.model;

public class EmployeeList {
    String name;
    String mob;
    int img;
    String address;

    public EmployeeList(String name, String mob, int img, String address) {
        this.name = name;
        this.mob = mob;
        this.img = img;
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

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "EmployeeList{" +
                "name='" + name + '\'' +
                ", mob='" + mob + '\'' +
                ", img='" + img + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
