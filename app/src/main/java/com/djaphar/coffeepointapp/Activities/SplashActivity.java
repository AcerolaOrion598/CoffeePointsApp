package com.djaphar.coffeepointapp.Activities;

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
                startNextActivity(new Intent(this, MainActivity.class));
            } else {
                startNextActivity(new Intent(this, AuthActivity.class));
            }
        });
    }

    public void startNextActivity(Intent intent) {
        startActivity(intent);
        finish();
    }
}
