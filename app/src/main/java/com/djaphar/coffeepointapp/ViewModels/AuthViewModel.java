package com.djaphar.coffeepointapp.ViewModels;

import android.app.Application;
import android.widget.Toast;

import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.PointsApi;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.SecondaryCredentials;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.FirstCredentials;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.User;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.UserDao;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.UserRoom;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthViewModel extends AndroidViewModel {

    private LiveData<User> testUser;
    private MutableLiveData<SecondaryCredentials> secondaryCredentials = new MutableLiveData<>();
    private UserDao userDao;
    private final static String baseUrl = "http://212.109.219.69:3007/";

    public AuthViewModel(@NonNull Application application) {
        super(application);
        UserRoom userRoom = UserRoom.getDatabase(application);
        userDao = userRoom.userDao();
        testUser = userDao.getUser();
    }

    public LiveData<User> getUser() {
        return testUser;
    }

    public LiveData<SecondaryCredentials> getSecondaryCredentials() {
        return secondaryCredentials;
    }

    public void requestCode(FirstCredentials testCredentials) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PointsApi pointsApi = retrofit.create(PointsApi.class);
        Call<SecondaryCredentials> call = pointsApi.testGetCode(testCredentials);
        call.enqueue(new Callback<SecondaryCredentials>() {
            @Override
            public void onResponse(@NonNull Call<SecondaryCredentials> call, @NonNull Response<SecondaryCredentials> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                }
                secondaryCredentials.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<SecondaryCredentials> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void login(SecondaryCredentials credentials) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PointsApi pointsApi = retrofit.create(PointsApi.class);
        Call<User> call = pointsApi.testLogIn(credentials);
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
