package com.djaphar.coffeepointapp.SupportClasses.ApiClasses;

public class SupervisorModel {

    private String name;
    private Float avgRating;

    public SupervisorModel(String name, Float avgRating) {
        this.name = name;
        this.avgRating = avgRating;
    }

    public String getName() {
        return name;
    }

    public Float getAvgRating() {
        return avgRating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvgRating(Float avgRating) {
        this.avgRating = avgRating;
    }
}
