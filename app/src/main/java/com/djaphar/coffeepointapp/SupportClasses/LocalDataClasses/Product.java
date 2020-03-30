package com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "product_table")
public class Product {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "_id")
    private String _id;

    @NonNull
    @ColumnInfo(name = "type")
    private String type;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "supervisor")
    private String supervisor;

    public Product(@NonNull String _id, @NonNull String type, @NonNull String name, @NonNull String supervisor) {
        this._id = _id;
        this.type = type;
        this.name = name;
        this.supervisor = supervisor;
    }

    public void set_id(@NonNull String _id) {
        this._id = _id;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setSupervisor(@NonNull String supervisor) {
        this.supervisor = supervisor;
    }

    @NonNull
    public String get_id() {
        return _id;
    }

    @NonNull
    public String getType() {
        return type;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getSupervisor() {
        return supervisor;
    }
}
