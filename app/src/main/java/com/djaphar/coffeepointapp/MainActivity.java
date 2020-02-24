package com.djaphar.coffeepointapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.djaphar.coffeepointapp.SupportClasses.ViewDriver;
import com.djaphar.coffeepointapp.ui.map.MapFragment;
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
    private Fragment currentFragment;

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
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            currentFragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
        }

        FragmentNames names = FragmentNames.valueOf(currentFragment.getClass().getSimpleName());
        switch (names) {
            case MapFragment:
                MapFragment mapFragment = ((MapFragment) currentFragment);
                ConstraintLayout pointAddWindow = mapFragment.getPointAddWindow();
                ConstraintLayout pointInfoWindow = mapFragment.getPointInfoWindow();
                Button pointAddButton = mapFragment.getPointAddBtn();
                if (!(pointAddWindow.getVisibility() == View.VISIBLE) && !(pointInfoWindow.getVisibility() == View.VISIBLE)) {
                    super.onBackPressed();
                } else if (pointAddWindow.getVisibility() == View.VISIBLE) {
                    ViewDriver.hideView(pointAddWindow, R.anim.bottom_window_hide_animation, this);
                    ViewDriver.showView(pointAddButton, R.anim.add_btn_show_animation, this);
                } else {
                    mapFragment.equalizeMarkers();
                    ViewDriver.hideView(pointInfoWindow, R.anim.bottom_window_hide_animation, this);
                }
                break;

            case PointsFragment:
                PointsFragment pointsFragment = ((PointsFragment) currentFragment);
                ConstraintLayout editLayout = pointsFragment.getPointEditLayout();
                if (!(editLayout.getVisibility() == View.VISIBLE)) {
                    super.onBackPressed();
                } else {
                    setActionBarTitle(getString(R.string.title_points));
                    RelativeLayout listLayout = pointsFragment.getPointListLayout();
                    editLayout.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    listLayout.setVisibility(View.VISIBLE);
                    ViewDriver.hideView(editLayout, R.anim.full_screen_hide_animation, this);
                }
                break;

            case ProfileFragment:
                super.onBackPressed();
                break;
        }
    }

    public void setActionBarTitle(String title) {
        actionBarTitle.setText(title);
    }

    enum FragmentNames {
        MapFragment, PointsFragment, ProfileFragment
    }
}
