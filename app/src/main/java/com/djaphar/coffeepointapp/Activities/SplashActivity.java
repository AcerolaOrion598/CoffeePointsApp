package com.djaphar.coffeepointapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.djaphar.coffeepointapp.ViewModels.AuthViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class SplashActivity extends AppCompatActivity {

    AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getUser().observe(this, user -> {
            if (user != null) {
                startNextActivity(new MainActivity());
            } else {
                startNextActivity(new AuthActivity());
            }
        });
    }

    public void startNextActivity(Activity activity) {
        Intent intent = new Intent(this, activity.getClass());
        startActivity(intent);
        finish();
    }
}
