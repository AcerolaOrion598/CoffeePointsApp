package com.djaphar.coffeepointapp.ViewModels;

import android.app.Application;

import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.User;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.UserDao;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.UserRoom;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class OtherViewModel extends AndroidViewModel {

    private UserDao userDao;
    private LiveData<User> userLiveData;

    public OtherViewModel(@NonNull Application application) {
        super(application);
        UserRoom userRoom = UserRoom.getDatabase(application);
        userDao = userRoom.userDao();
        userLiveData = userDao.getUserLiveData();
    }

    public void logout() {
        UserRoom.databaseWriteExecutor.execute(() -> userDao.deleteUserProducts());
        UserRoom.databaseWriteExecutor.execute(() -> userDao.deleteUser());
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }
}
