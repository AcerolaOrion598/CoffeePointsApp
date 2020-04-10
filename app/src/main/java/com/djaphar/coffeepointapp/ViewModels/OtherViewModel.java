package com.djaphar.coffeepointapp.ViewModels;

import android.app.Application;

import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.LocalDataRoom;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.User;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.LocalDataDao;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class OtherViewModel extends AndroidViewModel {

    private LocalDataDao dao;
    private LiveData<User> userLiveData;

    public OtherViewModel(@NonNull Application application) {
        super(application);
        LocalDataRoom room = LocalDataRoom.getDatabase(application);
        dao = room.localDataDao();
        userLiveData = dao.getUserLiveData();
    }

    public void logout() {
        LocalDataRoom.databaseWriteExecutor.execute(() -> dao.deleteLastBounds());
        LocalDataRoom.databaseWriteExecutor.execute(() -> dao.deleteUserProducts());
        LocalDataRoom.databaseWriteExecutor.execute(() -> dao.deleteUser());
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }
}
