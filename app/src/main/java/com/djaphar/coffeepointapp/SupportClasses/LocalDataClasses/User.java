package com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "_id")
    private String _id;

    @NonNull
    @ColumnInfo(name = "token")
    private String token;

    @NonNull
    @ColumnInfo(name = "role")
    private String role;

    @NonNull
    @ColumnInfo(name = "phone_number")
    private String phoneNumber;

    @NonNull
    @ColumnInfo(name = "supervisor_status")
    private String supervisorStatus;

    @ColumnInfo(name = "user_hash")
    private Integer userHash;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "avg_rating")
    private Float avgRating;

    public User(@NonNull String _id, @NonNull String token, @NonNull String role, @NonNull String phoneNumber,
         @NonNull String supervisorStatus, String name, Float avgRating) {
        this._id = _id;
        this.token = token;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.supervisorStatus = supervisorStatus;
        this.name = name;
        this.avgRating = avgRating;
    }

    @NonNull
    public String get_id() {
        return _id;
    }

    @NonNull
    public String getToken() {
        return token;
    }

    @NonNull
    public String getRole() {
        return role;
    }

    @NonNull
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @NonNull
    public String getSupervisorStatus() {
        return supervisorStatus;
    }

    public Integer getUserHash() {
        return userHash;
    }

    public String getName() {
        return name;
    }

    public Float getAvgRating() {
        return avgRating;
    }

    public void set_id(@NonNull String _id) {
        this._id = _id;
    }

    public void setToken(@NonNull String token) {
        this.token = token;
    }

    public void setRole(@NonNull String role) {
        this.role = role;
    }

    public void setPhoneNumber(@NonNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setSupervisorStatus(@NonNull String supervisorStatus) {
        this.supervisorStatus = supervisorStatus;
    }

    public void setUserHash(Integer userHash) {
        this.userHash = userHash;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvgRating(Float avgRating) {
        this.avgRating = avgRating;
    }

    public Integer determineHash() {
        String data = get_id() + getToken() + getRole() + getSupervisorStatus() + getPhoneNumber();

        if (getName() != null) {
            data += getName();
        }

        if (getAvgRating() != null) {
            data += getAvgRating();
        }

        return data.hashCode();
    }
}
