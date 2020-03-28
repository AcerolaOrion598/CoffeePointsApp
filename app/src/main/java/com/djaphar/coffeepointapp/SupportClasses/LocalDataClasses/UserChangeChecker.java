package com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses;

import android.os.Handler;

import com.djaphar.coffeepointapp.Activities.MainActivity;

public class UserChangeChecker {

    private Handler handler;
    private MainActivity mainActivity;

    public UserChangeChecker(Handler handler, MainActivity mainActivity) {
        this.handler = handler;
        this.mainActivity = mainActivity;
    }

    public void startUserChangeCheck() {
        asyncUserChangeChecker.run();
    }

    public void stopUserChangeCheck() {
        handler.removeCallbacksAndMessages(null);
    }

    private Runnable asyncUserChangeChecker = () -> {
        mainActivity.requestUser();
        handler.postDelayed(this::startUserChangeCheck, 30000);
    };
}
