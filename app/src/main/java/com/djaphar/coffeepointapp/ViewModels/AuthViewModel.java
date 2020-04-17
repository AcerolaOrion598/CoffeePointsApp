package com.djaphar.coffeepointapp.ViewModels;

import android.app.Application;
import android.widget.Toast;

import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.ApiBuilder;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.FirstCredentials;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.PointsApi;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.SecondCredentials;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.LocalDataDao;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.User;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.LocalDataRoom;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthViewModel extends AndroidViewModel {

    private LiveData<User> userLiveData;
    private MutableLiveData<SecondCredentials> secondCredentialsMutableLiveData = new MutableLiveData<>();
    private LocalDataDao dao;
    private PointsApi pointsApi;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        LocalDataRoom room = LocalDataRoom.getDatabase(application);
        dao = room.localDataDao();
        userLiveData = dao.getUserLiveData();
        pointsApi = ApiBuilder.getPointsApi();
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public LiveData<SecondCredentials> getSecondCredentials() {
        return secondCredentialsMutableLiveData;
    }

    public void requestCode(FirstCredentials credentials) {
        Call<SecondCredentials> call = pointsApi.getCode(credentials);
        call.enqueue(new Callback<SecondCredentials>() {
            @Override
            public void onResponse(@NonNull Call<SecondCredentials> call, @NonNull Response<SecondCredentials> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                secondCredentialsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<SecondCredentials> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), R.string.network_error_toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void login(SecondCredentials credentials) {
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
                LocalDataRoom.databaseWriteExecutor.execute(() -> dao.setUser(user));
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), R.string.network_error_toast, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
