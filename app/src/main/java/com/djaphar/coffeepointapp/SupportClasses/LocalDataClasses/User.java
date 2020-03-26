package com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {

    @NonNull
    @ColumnInfo(name = "role")
    private String role;

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "phone_number")
    private String phoneNumber;

    @NonNull
    @ColumnInfo(name = "supervisor_status")
    private String supervisorStatus;

    User(@NonNull String role, @NonNull String phoneNumber, @NonNull String supervisorStatus) {
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.supervisorStatus = supervisorStatus;
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

    public void setRole(@NonNull String role) {
        this.role = role;
    }

    public void setPhoneNumber(@NonNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setSupervisorStatus(@NonNull String supervisorStatus) {
        this.supervisorStatus = supervisorStatus;
    }
}
