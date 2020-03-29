package com.djaphar.coffeepointapp.ViewModels;

import android.app.Application;
import android.widget.Toast;

import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.PointsApi;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.User;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.UserDao;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.UserRoom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<String>> products = new MutableLiveData<>();
    private LiveData<User> userLiveData;
    private UserDao userDao;
    private final static String baseUrl = "http://212.109.219.69:3007/";

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        UserRoom userRoom = UserRoom.getDatabase(application);
        userDao = userRoom.userDao();
        userLiveData = userDao.getUserLiveData();

        ArrayList<String> productList = new ArrayList<>();
        productList.clear();
        String product1 ="Кофеёк 1";
        String product2 ="Кофеёк 2";
        String product3 ="Мороженное";
        String product4 ="Кофеёк 3";
        String product5 ="Блинчики";
        String product6 ="Мороженка 2";
        String product7 ="Блинчики 2";
        String product8 ="Блинчики 3";
        String product9 ="Блинчики 4";
        String product10 ="Блинчики 5";
        String product11 ="Блинчики 6";
        String product12 ="Блинчики 7";

        productList.add(product1);
        productList.add(product2);
        productList.add(product3);
        productList.add(product4);
        productList.add(product5);
        productList.add(product6);
        productList.add(product7);
        productList.add(product8);
        productList.add(product9);
        productList.add(product10);
        productList.add(product11);
        productList.add(product12);

        products.setValue(productList);
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public MutableLiveData<ArrayList<String>> getProducts() {
        return products;
    }

    public void requestUpdateUser(User user) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PointsApi pointsApi = retrofit.create(PointsApi.class);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", user.getToken());
        Call<User> call = pointsApi.updateUser(user.get_id(), headerMap, user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                User updatedUser = response.body();
                if (updatedUser == null) {
                    return;
                }
                Integer updatedHash = updatedUser.determineHash();
                updatedUser.setUserHash(updatedHash);
                UserRoom.databaseWriteExecutor.execute(() -> userDao.updateUser(updatedUser));
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}