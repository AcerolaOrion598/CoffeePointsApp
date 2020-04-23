package com.djaphar.coffeepointapp.SupportClasses.OtherClasses;

import android.os.Handler;

import com.djaphar.coffeepointapp.Fragments.PointsFragment;

public class PointsChangeChecker {

    private Handler handler;
    private PointsFragment pointsFragment;

    public PointsChangeChecker(Handler handler, PointsFragment pointsFragment) {
        this.handler = handler;
        this.pointsFragment = pointsFragment;
    }

    public void startPointsChangeCheck() {
        asyncPointsChangeChecker.run();
    }

    public void stopPointsChangeCheck() {
        handler.removeCallbacksAndMessages(null);
    }

    private Runnable asyncPointsChangeChecker = () -> {
        if (pointsFragment.getCheckedPointId() == null) {
            pointsFragment.requestMyPoints();
        } else {
            pointsFragment.requestSinglePoint();
        }
        handler.postDelayed(this::startPointsChangeCheck, 5000);
    };
}
