package com.djaphar.coffeepointapp.ViewModels;

import android.app.Application;
import android.widget.Toast;

import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.ApiBuilder;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.PointsApi;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.LocalDataDao;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.LocalDataRoom;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.Product;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {

    private LiveData<User> userLiveData;
    private LocalDataDao dao;
    private PointsApi pointsApi;

    public MainViewModel(@NonNull Application application) {
        super(application);
        LocalDataRoom room = LocalDataRoom.getDatabase(application);
        dao = room.localDataDao();
        userLiveData = dao.getUserLiveData();
        pointsApi = ApiBuilder.getPointsApi();
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public void requestUser(Map<String, String> headersMap, Integer oldHash) {
        Call<User> call = pointsApi.requestUser(headersMap);
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
                LocalDataRoom.databaseWriteExecutor.execute(() -> dao.updateUser(user));
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), R.string.network_error_toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestUserProducts(HashMap<String, String> headersMap) {
        Call<List<Product>> call = pointsApi.requestUserProducts(headersMap);
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

                LocalDataRoom.databaseWriteExecutor.execute(() -> dao.setUserProducts(response.body()));
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), R.string.network_error_toast, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
