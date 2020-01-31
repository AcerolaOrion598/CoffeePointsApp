package com.djaphar.coffeepointapp.ui.points;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PointsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PointsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}