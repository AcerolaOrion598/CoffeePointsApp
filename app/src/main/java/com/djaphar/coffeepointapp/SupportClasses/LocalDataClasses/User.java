package com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "_id")
    private String _id;

    @NonNull
    @ColumnInfo(name = "email")
    private String email;

    @NonNull
    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "role")
    private String role;

    @ColumnInfo(name = "about")
    private String about;

    @ColumnInfo(name = "token")
    private String token;

    public User(@NonNull String _id, @NonNull String email, @NonNull String password, String name, String role, String about, String token) {
        this._id = _id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.about = about;
        this.token = token;
    }

    @NonNull
    public String get_id() {
        return _id;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getAbout() {
        return about;
    }

    public String getToken() {
        return token;
    }
}
