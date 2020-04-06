package com.djaphar.coffeepointapp.SupportClasses.ApiClasses;

public class PointDeleteModel {

    private String courierId;

    public PointDeleteModel(String courierId) {
        this.courierId = courierId;
    }

    public String getCourierId() {
        return courierId;
    }

    public void setCourierId(String courierId) {
        this.courierId = courierId;
    }
}
