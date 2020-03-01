package com.djaphar.coffeepointapp.ViewModel;

import android.app.Application;
import android.widget.Toast;

import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.Credentials;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.PointsApi;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.User;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.UserDao;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.UserRoom;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthViewModel extends AndroidViewModel {

    private LiveData<User> user;
    private UserDao userDao;
    private final static String baseUrl = "http://212.109.219.69:3007/";

    public AuthViewModel(@NonNull Application application) {
        super(application);
        UserRoom userRoom = UserRoom.getDatabase(application);
        userDao = userRoom.userDao();
        user = userDao.getUser();
    }

    public LiveData<User> getUser() {
        return user;
    }

    public void logIn(Credentials credentials) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PointsApi pointsApi = retrofit.create(PointsApi.class);
        Call<User> call = pointsApi.logIn(credentials);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                UserRoom.databaseWriteExecutor.execute(() -> userDao.setUser(response.body()));
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
