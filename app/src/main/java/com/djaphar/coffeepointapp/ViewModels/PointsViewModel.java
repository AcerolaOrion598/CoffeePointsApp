package com.djaphar.coffeepointapp.ViewModels;

import android.app.Application;
import android.widget.Toast;

import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.ApiBuilder;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.Point;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.PointUpdateModel;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.PointsApi;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.LocalDataRoom;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.User;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.LocalDataDao;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PointsViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Point>> pointsMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Point> singlePointMutableLiveData = new MutableLiveData<>();
    private LiveData<User> userLiveData;
    private PointsApi pointsApi;

    public PointsViewModel(@NonNull Application application) {
        super(application);
        LocalDataRoom room = LocalDataRoom.getDatabase(application);
        LocalDataDao dao = room.localDataDao();
        userLiveData = dao.getUserLiveData();
        pointsApi = ApiBuilder.getPointsApi();
    }

    public MutableLiveData<ArrayList<Point>> getPoints() {
        return pointsMutableLiveData;
    }

    public MutableLiveData<Point> getSinglePoint() {
        return singlePointMutableLiveData;
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public void requestBindCourier(String phoneNumber, HashMap<String, String> headersMap) {
        pointsApi.requestBindCourier(phoneNumber, headersMap).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    switch (response.code()) {
                        case 403:
                            Toast.makeText(getApplication(), R.string.courier_bind_error_toast, Toast.LENGTH_SHORT).show();
                            break;
                        case 404:
                            Toast.makeText(getApplication(), R.string.courier_not_found_error_toast, Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return;
                }
               requestMyPoints(headersMap);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), R.string.network_error_toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestMyPoints(HashMap<String, String> headersMap) {
        pointsApi.requestMyPoints(headersMap).enqueue(new Callback<ArrayList<Point>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Point>> call, @NonNull Response<ArrayList<Point>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                pointsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Point>> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), R.string.network_error_toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestUpdatePoint(String pointId, HashMap<String, String> headersMap, PointUpdateModel pointUpdateModel) {
        pointsApi.requestUpdatePoint(pointId, headersMap, pointUpdateModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                requestSinglePoint(pointId, headersMap);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), R.string.network_error_toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestDeletePoint(String courierId, HashMap<String, String> headersMap) {
        pointsApi.requestDeletePoint(courierId, headersMap).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                requestMyPoints(headersMap);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), R.string.network_error_toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestSinglePoint(String pointId, HashMap<String, String> headersMap) {
        pointsApi.requestSinglePoint(pointId, headersMap).enqueue(new Callback<Point>() {
            @Override
            public void onResponse(@NonNull Call<Point> call, @NonNull Response<Point> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                singlePointMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Point> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
