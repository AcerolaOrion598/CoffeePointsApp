package com.djaphar.coffeepointapp.ViewModels;

import android.app.Application;
import android.widget.Toast;

import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.ApiBuilder;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.Point;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.PointUpdateModel;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.PointsApi;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.SupervisorModel;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.User;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.UserDao;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.UserRoom;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapViewModel extends AndroidViewModel {

    private LiveData<User> userLiveData;
    private MutableLiveData<ArrayList<Point>> pointsMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<SupervisorModel> supervisorModelMutableLiveData = new MutableLiveData<>();
    private PointsApi pointsApi;

    public MapViewModel(@NonNull Application application) {
        super(application);
        UserRoom userRoom = UserRoom.getDatabase(application);
        UserDao userDao = userRoom.userDao();
        userLiveData = userDao.getUserLiveData();
        pointsApi = ApiBuilder.getPointsApi();
    }

    public MutableLiveData<ArrayList<Point>> getPoints() {
        return pointsMutableLiveData;
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public MutableLiveData<SupervisorModel> getSupervisor() {
        return supervisorModelMutableLiveData;
    }

    public void requestPointsInBox(LatLngBounds bounds) {
        String box = bounds.southwest.latitude + "," + bounds.southwest.longitude + "," + bounds.northeast.latitude + "," + bounds.northeast.longitude;
        Call<ArrayList<Point>> call = pointsApi.requestPointsInBox(box);
        call.enqueue(new Callback<ArrayList<Point>>() {
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
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestSupervisor(String supervisorId) {
        Call<SupervisorModel> call = pointsApi.requestSupervisor(supervisorId);
        call.enqueue(new Callback<SupervisorModel>() {
            @Override
            public void onResponse(@NonNull Call<SupervisorModel> call, @NonNull Response<SupervisorModel> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                supervisorModelMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<SupervisorModel> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestUpdatePoint(String pointId, HashMap<String, String> headersMap, PointUpdateModel pointUpdateModel, LatLngBounds latLngBounds) {
        Call<Void> call = pointsApi.requestUpdatePoint(pointId, headersMap, pointUpdateModel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                requestPointsInBox(latLngBounds);
                Toast.makeText(getApplication(), getApplication().getString(R.string.point_update_success), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
