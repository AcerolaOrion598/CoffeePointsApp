package com.djaphar.coffeepointapp;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.djaphar.coffeepointapp.SupportClasses.ViewDriver;
import com.djaphar.coffeepointapp.ui.points.PointsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private TextView actionBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        actionBarTitle = findViewById(R.id.action_bar_title);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = null;
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
        }

        if (fragment instanceof PointsFragment) {
            ConstraintLayout editLayout = ((PointsFragment) fragment).getPointEditLayout();
            if (editLayout.getVisibility() == View.VISIBLE) {
                setActionBarTitle(getString(R.string.title_points));
                RelativeLayout listLayout = ((PointsFragment) fragment).getPointListLayout();
                editLayout.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                listLayout.setVisibility(View.VISIBLE);
                ViewDriver.hideView(editLayout, R.anim.full_screen_hide_animation, this);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    public void setActionBarTitle(String title) {
        actionBarTitle.setText(title);
    }
}
