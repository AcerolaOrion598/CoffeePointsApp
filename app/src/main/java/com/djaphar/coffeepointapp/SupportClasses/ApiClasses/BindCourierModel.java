package com.djaphar.coffeepointapp.SupportClasses.ApiClasses;

public class BindCourierModel {

    private String supervisor, phoneNumber;

    public BindCourierModel(String supervisor, String phoneNumber) {
        this.supervisor = supervisor;
        this.phoneNumber = phoneNumber;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
