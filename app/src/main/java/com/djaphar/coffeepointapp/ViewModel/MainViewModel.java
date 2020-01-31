package com.djaphar.coffeepointapp.ViewModel;

import android.app.Application;

import com.djaphar.coffeepointapp.SupportClasses.Point;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Point>> points;

    public MainViewModel(@NonNull Application application) {
        super(application);

        ArrayList<Point> pointList = new ArrayList<>();
        Point point1 = new Point(new LatLng(55.665954, 37.503766), "Какое-то уникальное название 1",
                "Лучшая точка в этом районе", "", "Владелец: Aye", true, 3);
        Point point2 = new Point(new LatLng(55.890356, 37.722421), "Какое-то уникальное название 2",
                "Короткое описание, которое заставит\n" +
                        "меня выбрать именно эту точку.", "", "Владелец: Salamulya", false, 2);
        Point point3 = new Point(new LatLng(55.718324, 37.810124), "Какое-то уникальное название 3",
                "Короткое описание, которое заставит\n" +
                        "меня выбрать именно эту точку 2.", "Отошёл на 10 минут", "Владелец: Patsanva", true, 1);

        pointList.add(point1);
        pointList.add(point2);
        pointList.add(point3);

        points = new MutableLiveData<>();
        points.setValue(pointList);
    }

    public MutableLiveData<ArrayList<Point>> getPoints() {
        return points;
    }

    public void sendScreenBounds(LatLngBounds bounds) {
        //Тут цепляем границы экрана и шлём их на сервер
    }

//    public BitmapDescriptor bitmapDescriptorFromVector(int vectorResId) {
//        Drawable vectorDrawable = ContextCompat.getDrawable(getApplication(),  vectorResId);
//        Objects.requireNonNull(vectorDrawable).setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
//        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        vectorDrawable.draw(canvas);
//        return BitmapDescriptorFactory.fromBitmap(bitmap);
//    }
}
