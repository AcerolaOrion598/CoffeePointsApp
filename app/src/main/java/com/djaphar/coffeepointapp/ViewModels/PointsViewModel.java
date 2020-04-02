package com.djaphar.coffeepointapp.ViewModels;

import android.app.Application;
import android.widget.Toast;

import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.BindCourierModel;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.Point;
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

public class PointsViewModel extends AndroidViewModel {

//    private MutableLiveData<ArrayList<OldPoint>> points = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Point>> pointsMutableLiveData = new MutableLiveData<>();
    private LiveData<User> userLiveData;
    private final static String baseUrl = "http://212.109.219.69:3007/";

    public PointsViewModel(@NonNull Application application) {
        super(application);

//        ArrayList<OldPoint> oldPointList = new ArrayList<>();
//        oldPointList.clear();
//        OldPoint oldPoint1 = new OldPoint(new LatLng(55.665954, 37.503766), "Моя точка",
//                "Лучшая точка в этом районе", "", "Владелец: Aye", true, 3, 1);
//        OldPoint oldPoint2 = new OldPoint(new LatLng(55.890356, 37.722421), "Какое-то уникальное название",
//                "Короткое описание, которое заставит меня выбрать именно эту точку.",
//                "", "Владелец: Salamulya", false, 2, 2);
//        OldPoint oldPoint3 = new OldPoint(new LatLng(55.718324, 37.810124), "Сюда иди да",
//                "Короткое описание, которое заставит меня выбрать именно эту точку сто проц просто",
//                "", "Владелец: Patsanva", true, 1, 3);
//        OldPoint oldPoint4 = new OldPoint(new LatLng(55.779971, 37.525455), "Я же по любому тебя настигну",
//                "Короткое описание, которое заставит меня выбрать именно эту точку сто проц просто",
//                "Отошёл на 15 минут", "Владелец: Patsanva", true, 1, 4);
//        OldPoint oldPoint5 = new OldPoint(new LatLng(55.818181, 37.513095), "Че ты убегаешь да",
//                "Короткое описание, которое заставит меня выбрать именно эту точку сто проц просто",
//                "", "Владелец: Aye", true, 3, 5);
//        OldPoint oldPoint6 = new OldPoint(new LatLng(55.678955, 37.431020), "Ща да подожди",
//                "Короткое описание, которое заставит меня выбрать именно эту точку сто проц просто",
//                "", "Владелец: Patsanva", false, 1, 6);
//        OldPoint oldPoint7 = new OldPoint(new LatLng(55.800243, 37.391118), "Э ты нарвался да",
//                "Короткое описание, которое заставит меня выбрать именно эту точку сто проц просто",
//                "", "Владелец: Patsanva", false, 1, 7);
//        OldPoint oldPoint8 = new OldPoint(new LatLng(55.899181, 37.934192), "Ты кому сказал да э?",
//                "Короткое описание, которое заставит меня выбрать именно эту точку сто проц просто",
//                "", "Владелец: Aye", true, 3, 8);
//        OldPoint oldPoint9 = new OldPoint(new LatLng(55.692994, 37.664097), "Нормально говори да по-братски",
//                "Короткое описание, которое заставит меня выбрать именно эту точку сто проц просто",
//                "", "Владелец: Patsanva", true, 1, 9);
//        OldPoint oldPoint10 = new OldPoint(new LatLng(55.742886, 37.624700), "Не ты послушай да",
//                "Короткое описание, которое заставит меня выбрать именно эту точку сто проц просто",
//                "Я тебе говорю да", "Владелец: Aye", false, 3,10);
//
//        oldPointList.add(oldPoint1);
//        oldPointList.add(oldPoint2);
//        oldPointList.add(oldPoint3);
//        oldPointList.add(oldPoint4);
//        oldPointList.add(oldPoint5);
//        oldPointList.add(oldPoint6);
//        oldPointList.add(oldPoint7);
//        oldPointList.add(oldPoint8);
//        oldPointList.add(oldPoint9);
//        oldPointList.add(oldPoint10);

//        oldPoints = new MutableLiveData<>();
//        points.setValue(oldPointList);
        UserRoom userRoom = UserRoom.getDatabase(application);
        UserDao userDao = userRoom.userDao();
        userLiveData = userDao.getUserLiveData();
    }

    public MutableLiveData<ArrayList<Point>> getPoints() {
        return pointsMutableLiveData;
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public void requestBindCourier(User user, String phoneNumber) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PointsApi pointsApi = retrofit.create(PointsApi.class);
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Authorization", user.getToken());
        BindCourierModel bindCourierModel = new BindCourierModel(user.get_id(), phoneNumber);
        Call<Void> call = pointsApi.requestBindCourier(headersMap, bindCourierModel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplication(), R.string.mayoi_chan, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestMyPoints(String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PointsApi pointsApi = retrofit.create(PointsApi.class);
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Authorization", token);
        Call<ArrayList<Point>> call = pointsApi.requestMyPoints(headersMap);
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
}
