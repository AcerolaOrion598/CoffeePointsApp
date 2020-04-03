package com.djaphar.coffeepointapp.SupportClasses.ApiClasses;

public class PointUpdateModel {

    String name, supervisor;

    public PointUpdateModel(String name, String supervisor) {
        this.name = name;
        this.supervisor = supervisor;
    }

    public String getName() {
        return name;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }
}
