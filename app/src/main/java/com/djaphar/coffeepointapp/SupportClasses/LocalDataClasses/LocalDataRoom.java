package com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Product.class, LastBounds.class}, version = 1, exportSchema = false)
public abstract class LocalDataRoom extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 4;
    public abstract LocalDataDao localDataDao();
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static volatile LocalDataRoom INSTANCE;

    public static LocalDataRoom getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LocalDataRoom.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), LocalDataRoom.class, "user_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
