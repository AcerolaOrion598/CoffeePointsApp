package com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class UserRoom extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 4;
    public abstract UserDao userDao();
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static volatile UserRoom INSTANCE;

    public static UserRoom getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UserRoom.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), UserRoom.class, "user_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
