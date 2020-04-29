package com.djaphar.coffeepointapp.SupportClasses.ApiClasses;

import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.Product;

import java.util.ArrayList;

public class Point {

    private String _id, phoneNumber, supervisor, name, hint;
    private Boolean isActive, isCurrentlyNotHere;
    private ArrayList<Product> productList;
    private Float avgRating;
    private Integer amount;
    private ArrayList<Double> coordinates;
//    private Coordinates coordinates;

    public Point(String _id, String phoneNumber, String supervisor, String name, String hint, Boolean isActive,
                 Boolean isCurrentlyNotHere, ArrayList<Product> productList, Float avgRating, Integer amount, ArrayList<Double> coordinates) {
        this._id = _id;
        this.phoneNumber = phoneNumber;
        this.supervisor = supervisor;
        this.name = name;
        this.hint = hint;
        this.isActive = isActive;
        this.isCurrentlyNotHere = isCurrentlyNotHere;
        this.productList = productList;
        this.avgRating = avgRating;
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

    public Boolean getCurrentlyNotHere() {
        return isCurrentlyNotHere;
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public Float getAvgRating() {
        return avgRating;
    }

    public Integer getAmount() {
        return amount;
    }

    public ArrayList<Double> getCoordinates() {
        return coordinates;
    }
    //    public Coordinates getCoordinates() {
//        return coordinates;
//    }

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

    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setCurrentlyNotHere(Boolean currentlyNotHere) {
        isCurrentlyNotHere = currentlyNotHere;
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }

    public void setAvgRating(Float avgRating) {
        this.avgRating = avgRating;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setCoordinates(ArrayList<Double> coordinates) {
        this.coordinates = coordinates;
    }

    //    public void setCoordinates(Coordinates coordinates) {
//        this.coordinates = coordinates;
//    }
}
