package com.djaphar.coffeepointapp.ViewModels;

import android.app.Application;

import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.User;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.UserDao;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.UserRoom;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ProfileViewModel extends AndroidViewModel {

    private LiveData<User> user;
    private UserDao userDao;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        UserRoom userRoom = UserRoom.getDatabase(application);
        userDao = userRoom.userDao();
        user = userDao.getUser();
    }

    public LiveData<User> getUser() {
        return user;
    }
}