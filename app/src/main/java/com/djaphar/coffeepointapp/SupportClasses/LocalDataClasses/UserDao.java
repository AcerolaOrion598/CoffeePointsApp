package com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {

    @Insert
    void setUser(User user);

    @Query("SELECT * FROM user_table")
    LiveData<User> getUser();

    @Query("DELETE FROM user_table")
    void deleteUser();
}
