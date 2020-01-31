package com.djaphar.coffeepointapp.SupportClasses;

import com.google.android.gms.maps.model.LatLng;

public class Point {

    private LatLng coordinates;
    private String name, about, hint, owner;
    private boolean active;
    private int ownerId;

    public Point(LatLng coordinates, String name, String about, String hint, String owner, boolean active, int ownerId) {
        this.coordinates = coordinates;
        this.name = name;
        this.about = about;
        this.hint = hint;
        this.owner = owner;
        this.active = active;
        this.ownerId = ownerId;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public String getName() {
        return name;
    }

    public String getAbout() {
        return about;
    }

    public String getHint() {
        return hint;
    }

    public String getOwner() {
        return owner;
    }

    public boolean isActive() {
        return active;
    }

    public int getOwnerId() {
        return ownerId;
    }
}
