package com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "last_bounds_table")
public class LastBounds {

    @PrimaryKey
    @ColumnInfo(name = "north_lat")
    private double northLat;

    @ColumnInfo(name = "north_long")
    private double northLong;

    @ColumnInfo(name = "south_lat")
    private double southLat;

    @ColumnInfo(name = "south_long")
    private double southLong;

    public LastBounds(double northLat, double northLong, double southLat, double southLong) {
        this.northLat = northLat;
        this.northLong = northLong;
        this.southLat = southLat;
        this.southLong = southLong;
    }

    public double getNorthLat() {
        return northLat;
    }

    public double getNorthLong() {
        return northLong;
    }

    public double getSouthLat() {
        return southLat;
    }

    public double getSouthLong() {
        return southLong;
    }
}
