package com.ats.tankmaintenance.model;

public class AssignEmpList {
    String empName;
    boolean isChecked;

    public AssignEmpList(String empName, boolean isChecked) {
        this.empName = empName;
        this.isChecked = isChecked;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "AssignEmpList{" +
                "empName='" + empName + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
