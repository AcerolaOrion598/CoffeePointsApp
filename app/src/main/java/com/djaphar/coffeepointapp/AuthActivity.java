package com.djaphar.coffeepointapp;

import android.content.Intent;
import android.os.Bundle;

import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.Credentials;
import com.djaphar.coffeepointapp.ViewModel.AuthViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class AuthActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getUser().observe(this, user -> {
            if (user != null) {
                startMainActivity();
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void logIn(Credentials credentials) {
        authViewModel.logIn(credentials);
    }
}
