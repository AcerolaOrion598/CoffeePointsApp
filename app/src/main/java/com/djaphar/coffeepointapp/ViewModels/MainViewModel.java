package com.djaphar.coffeepointapp.ViewModels;

import android.app.Application;

import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.Point;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Point>> points = new MutableLiveData<>();
    private ArrayList<Point> pointList = new ArrayList<>();

    public MainViewModel(@NonNull Application application) {
        super(application);

        pointList.clear();
        Point point1 = new Point(new LatLng(55.665954, 37.503766), "Моя точка",
                "Лучшая точка в этом районе", "", "Владелец: Aye", true, 3, 1);
        Point point2 = new Point(new LatLng(55.890356, 37.722421), "Какое-то уникальное название",
                "Короткое описание, которое заставит меня выбрать именно эту точку.",
                "", "Владелец: Salamulya", false, 2, 2);
        Point point3 = new Point(new LatLng(55.718324, 37.810124), "Сюда иди да",
                "Короткое описание, которое заставит меня выбрать именно эту точку сто проц просто",
                "", "Владелец: Patsanva", true, 1, 3);
        Point point4 = new Point(new LatLng(55.779971, 37.525455), "Я же по любому тебя настигну",
                "Короткое описание, которое заставит меня выбрать именно эту точку сто проц просто",
                "Отошёл на 15 минут", "Владелец: Patsanva", true, 1, 4);
        Point point5 = new Point(new LatLng(55.818181, 37.513095), "Че ты убегаешь да",
                "Короткое описание, которое заставит меня выбрать именно эту точку сто проц просто",
                "", "Владелец: Aye", true, 3, 5);
        Point point6 = new Point(new LatLng(55.678955, 37.431020), "Ща да подожди",
                "Короткое описание, которое заставит меня выбрать именно эту точку сто проц просто",
                "", "Владелец: Patsanva", false, 1, 6);
        Point point7 = new Point(new LatLng(55.800243, 37.391118), "Э ты нарвался да",
                "Короткое описание, которое заставит меня выбрать именно эту точку сто проц просто",
                "", "Владелец: Patsanva", false, 1, 7);
        Point point8 = new Point(new LatLng(55.899181, 37.934192), "Ты кому сказал да э?",
                "Короткое описание, которое заставит меня выбрать именно эту точку сто проц просто",
                "", "Владелец: Aye", true, 3, 8);
        Point point9 = new Point(new LatLng(55.692994, 37.664097), "Нормально говори да по-братски",
                "Короткое описание, которое заставит меня выбрать именно эту точку сто проц просто",
                "", "Владелец: Patsanva", true, 1, 9);
        Point point10 = new Point(new LatLng(55.742886, 37.624700), "Не ты послушай да",
                "Короткое описание, которое заставит меня выбрать именно эту точку сто проц просто",
                "Я тебе говорю да", "Владелец: Aye", false, 3,10);

        pointList.add(point1);
        pointList.add(point2);
        pointList.add(point3);
        pointList.add(point4);
        pointList.add(point5);
        pointList.add(point6);
        pointList.add(point7);
        pointList.add(point8);
        pointList.add(point9);
        pointList.add(point10);

//        points = new MutableLiveData<>();
        points.setValue(pointList);
    }

    public MutableLiveData<ArrayList<Point>> getPoints() {
        return points;
    }

    //Пробная часть
    public void testOnChange(Point testPoint) {
        if (pointList.size() == 11) {
            pointList.remove(10);
        } else {
            pointList.add(testPoint);
        }
        points.setValue(pointList);
    }
    //Пробная часть

    public void sendScreenBounds(LatLngBounds bounds) {
        //Тут цепляем границы экрана и шлём их на сервер
    }

    public void addPoint() {
        //Тут лезем инсёртить в бд
    }

    public void editPoint() {
        //Тут лезем апдейтить бд
    }
}
