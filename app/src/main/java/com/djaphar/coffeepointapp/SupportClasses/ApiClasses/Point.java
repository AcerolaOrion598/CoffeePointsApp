package com.djaphar.coffeepointapp.SupportClasses.ApiClasses;

import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.Product;

import java.util.ArrayList;

public class Point {

    private String _id, phoneNumber, supervisor, name, hint;
    private Boolean isActive, isAway;
    private ArrayList<Product> productList;
    private Float rating;
    private Integer amount;
    private ArrayList<Double> coordinates;

    public Point(String _id, String phoneNumber, String supervisor, String name, String hint, Boolean isActive,
                 Boolean isAway, ArrayList<Product> productList, Float rating, Integer amount, ArrayList<Double> coordinates) {
        this._id = _id;
        this.phoneNumber = phoneNumber;
        this.supervisor = supervisor;
        this.name = name;
        this.hint = hint;
        this.isActive = isActive;
        this.isAway = isAway;
        this.productList = productList;
        this.rating = rating;
        this.amount = amount;
        this.coordinates = coordinates;
    }

    public String get_id() {
        return _id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public String getName() {
        return name;
    }

    public String getHint() {
        return hint;
    }

    public Boolean getActive() {
        return isActive;
    }

    public Boolean isAway() {
        return isAway;
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public Float getRating() {
        return rating;
    }

    public Integer getAmount() {
        return amount;
    }

    public ArrayList<Double> getCoordinates() {
        return coordinates;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setAway(Boolean isAway) {
        this.isAway = isAway;
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setCoordinates(ArrayList<Double> coordinates) {
        this.coordinates = coordinates;
    }
}
