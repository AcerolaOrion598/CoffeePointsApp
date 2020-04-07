package com.djaphar.coffeepointapp.SupportClasses.ApiClasses;

public class PointUpdateModel {

    private String name;

    public PointUpdateModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
