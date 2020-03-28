package com.djaphar.coffeepointapp.ViewModels;

import android.app.Application;
import android.widget.Toast;

import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.FirstCredentials;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.PointsApi;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.SecondCredentials;
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

    private LiveData<User> userLiveData;
    private MutableLiveData<SecondCredentials> secondaryCredentials = new MutableLiveData<>();
    private UserDao userDao;
    private final static String baseUrl = "http://212.109.219.69:3007/";

    public AuthViewModel(@NonNull Application application) {
        super(application);
        UserRoom userRoom = UserRoom.getDatabase(application);
        userDao = userRoom.userDao();
        userLiveData = userDao.getUserLiveData();
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public LiveData<SecondCredentials> getSecondaryCredentials() {
        return secondaryCredentials;
    }

    public void requestCode(FirstCredentials credentials) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PointsApi pointsApi = retrofit.create(PointsApi.class);
        Call<SecondCredentials> call = pointsApi.getCode(credentials);
        call.enqueue(new Callback<SecondCredentials>() {
            @Override
            public void onResponse(@NonNull Call<SecondCredentials> call, @NonNull Response<SecondCredentials> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                }
                secondaryCredentials.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<SecondCredentials> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void login(SecondCredentials credentials) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PointsApi pointsApi = retrofit.create(PointsApi.class);
        Call<User> call = pointsApi.login(credentials);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                User user = response.body();
                if (user == null) {
                    return;
                }
                Integer userHash = user.determineHash();
                user.setUserHash(userHash);
                UserRoom.databaseWriteExecutor.execute(() -> userDao.setUser(user));
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
