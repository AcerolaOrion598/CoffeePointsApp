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

    User(@NonNull String _id, @NonNull String email, @NonNull String password, String name, String role, String about, String token) {
        this._id = _id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.about = about;
        this.token = token;
    }

    @NonNull
    String get_id() {
        return _id;
    }

    @NonNull
    String getEmail() {
        return email;
    }

    @NonNull
    String getPassword() {
        return password;
    }

    String getName() {
        return name;
    }

    String getRole() {
        return role;
    }

    String getAbout() {
        return about;
    }

    String getToken() {
        return token;
    }
}
