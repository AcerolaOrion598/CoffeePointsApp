package com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {

    @Insert
    void setUser(User user);

    @Query("SELECT * FROM user_table")
    LiveData<User> getUserLiveData();

    @Update
    void updateUser(User user);

    @Query("DELETE FROM user_table")
    void deleteUser();
}
