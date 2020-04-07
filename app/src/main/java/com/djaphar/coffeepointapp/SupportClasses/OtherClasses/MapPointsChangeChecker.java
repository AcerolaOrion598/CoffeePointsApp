package com.djaphar.coffeepointapp.SupportClasses.OtherClasses;

import android.os.Handler;

import com.djaphar.coffeepointapp.Fragments.MapFragment;

public class MapPointsChangeChecker {

    private Handler handler;
    private MapFragment mapFragment;

    public MapPointsChangeChecker(Handler handler, MapFragment mapFragment) {
        this.handler = handler;
        this.mapFragment = mapFragment;
    }

    public void startMapPointsChangeCheck() {
        asyncMapPointsChangeChecker.run();
    }

    public void stopMapPointsChangeCheck() {
        handler.removeCallbacksAndMessages(null);
    }

    private Runnable asyncMapPointsChangeChecker = () -> {
        mapFragment.requestPointsInBox();
        handler.postDelayed(this::startMapPointsChangeCheck, 60000);
    };
}
