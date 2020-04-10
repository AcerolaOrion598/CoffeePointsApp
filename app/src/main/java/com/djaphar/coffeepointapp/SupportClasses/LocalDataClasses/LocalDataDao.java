package com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface LocalDataDao {

    @Insert
    void setUser(User user);

    @Query("SELECT * FROM user_table")
    LiveData<User> getUserLiveData();

    @Update
    void updateUser(User user);

    @Query("DELETE FROM user_table")
    void deleteUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setUserProducts(List<Product> products);

    @Insert
    void setUserProduct(Product product);

    @Query("SELECT * FROM product_table")
    LiveData<List<Product>> getUserProductsLiveData();

    @Query("DELETE FROM product_table")
    void deleteUserProducts();

    @Query("DELETE FROM product_table WHERE _id = (:id)")
    void deleteUserProduct(String id);

    @Insert
    void setLastBounds(LastBounds bounds);

    @Query("SELECT * FROM last_bounds_table")
    LiveData<LastBounds> getLastBoundsLiveData();

    @Query("UPDATE last_bounds_table SET north_lat = (:northLat), north_long = (:northLong), south_lat = (:southLat), south_long = (:southLong)")
    void updateLastBounds(double northLat, double northLong, double southLat, double southLong);

    @Query("DELETE FROM last_bounds_table")
    void deleteLastBounds();
}
