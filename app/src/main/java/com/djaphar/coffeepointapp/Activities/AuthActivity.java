package com.djaphar.coffeepointapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.Credentials;
import com.djaphar.coffeepointapp.ViewModels.AuthViewModel;

import java.util.Objects;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class AuthActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private TextView actionBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        actionBarTitle = findViewById(R.id.action_bar_title);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getUser().observe(this, user -> {
            if (user == null) {
                return;
            }
            startMainActivity();
        });
    }

    public void setActionBarTitle(String title) {
        actionBarTitle.setText(title);
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
