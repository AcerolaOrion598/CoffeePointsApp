package com.djaphar.coffeepointapp.ViewModels;

import android.app.Application;
import android.widget.Toast;

import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.ApiBuilder;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.PointsApi;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.Product;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.User;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.UserDao;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.UserRoom;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {

    private LiveData<User> userLiveData;
    private UserDao userDao;
    private PointsApi pointsApi;

    public MainViewModel(@NonNull Application application) {
        super(application);
        UserRoom userRoom = UserRoom.getDatabase(application);
        userDao = userRoom.userDao();
        userLiveData = userDao.getUserLiveData();
        pointsApi = ApiBuilder.getPointsApi();
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public void editPoint() {
        //Тут лезем апдейтить бд
    }

    public void requestUser(String id, Integer oldHash) {
        Call<User> call = pointsApi.requestUser(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.body() == null) {
                    return;
                }

                User user = response.body();
                Integer newHash = user.determineHash();
                if (oldHash.equals(newHash)) {
                    return;
                }
                user.setUserHash(newHash);
                UserRoom.databaseWriteExecutor.execute(() -> userDao.updateUser(user));
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestUserProducts(String id, HashMap<String, String> headersMap) {
        Call<List<Product>> call = pointsApi.requestUserProducts(id, headersMap);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.body() == null) {
                    return;
                }

                UserRoom.databaseWriteExecutor.execute(() -> userDao.setUserProducts(response.body()));
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
