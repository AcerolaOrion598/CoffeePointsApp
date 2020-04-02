package com.djaphar.coffeepointapp.SupportClasses.ApiClasses;

import com.google.android.gms.maps.model.LatLng;

public class OldPoint {

    private LatLng coordinates;
    private String name, about, hint, owner;
    private boolean active;
    private int ownerId, id;

    public OldPoint(LatLng coordinates, String name, String about, String hint, String owner, boolean active, int ownerId, int id) {
        this.coordinates = coordinates;
        this.name = name;
        this.about = about;
        this.hint = hint;
        this.owner = owner;
        this.active = active;
        this.ownerId = ownerId;
        this.id = id;
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

    public void setId(int id) {
        this.id = id;
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

    public int getId() {
        return id;
    }
}
