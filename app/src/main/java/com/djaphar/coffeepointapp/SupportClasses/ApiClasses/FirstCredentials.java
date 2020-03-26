package com.djaphar.coffeepointapp.SupportClasses.ApiClasses;

public class FirstCredentials {

    private String phoneNumber, fcmToken;

    public FirstCredentials(String phoneNumber, String fcmToken) {
        this.phoneNumber = phoneNumber;
        this.fcmToken = fcmToken;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFcmToken() {
        return fcmToken;
    }
}
