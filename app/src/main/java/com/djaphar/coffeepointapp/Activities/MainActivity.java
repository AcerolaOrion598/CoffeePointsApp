package com.djaphar.coffeepointapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.djaphar.coffeepointapp.Fragments.MapFragment;
import com.djaphar.coffeepointapp.Fragments.OtherFragment;
import com.djaphar.coffeepointapp.Fragments.PointsFragment;
import com.djaphar.coffeepointapp.Fragments.ProfileFragment;
import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.MyFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private TextView actionBarTitle;
    private MyFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
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
            currentFragment = (MyFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
        }

        FragmentNames names = FragmentNames.valueOf(currentFragment.getClass().getSimpleName());
        switch (names) {
            case MapFragment:
                MapFragment mapFragment = ((MapFragment) currentFragment);
                ConstraintLayout pointInfoWindow = mapFragment.getPointInfoWindow();
                ConstraintLayout pointEditWindow = mapFragment.getPointEditWindow();
                if (!(pointInfoWindow.getVisibility() == View.VISIBLE) && !(pointEditWindow.getVisibility() == View.VISIBLE)) {
                    super.onBackPressed();
                    return;
                }
                break;

            case PointsFragment:
                PointsFragment pointsFragment = ((PointsFragment) currentFragment);
                ConstraintLayout editLayout = pointsFragment.getPointEditLayout();
                if (!(editLayout.getVisibility() == View.VISIBLE)) {
                    super.onBackPressed();
                    return;
                }
                break;

            case ProfileFragment:
                ProfileFragment profileFragment = ((ProfileFragment) currentFragment);
                ConstraintLayout editProfileContainer = profileFragment.getEditProfileContainer();
                if (!(editProfileContainer.getVisibility() == View.VISIBLE)) {
                    super.onBackPressed();
                    return;
                }
                break;

            case OtherFragment:
                OtherFragment otherFragment = ((OtherFragment) currentFragment);
                ConstraintLayout aboutAppContainer = otherFragment.getAboutAppContainer();
                if (!(aboutAppContainer.getVisibility() == View.VISIBLE)) {
                    super.onBackPressed();
                    return;
                }
                break;
        }
        currentFragment.backWasPressed();
    }

    public void setActionBarTitle(String title) {
        actionBarTitle.setText(title);
    }

    public void logout() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    enum FragmentNames {
        MapFragment, PointsFragment, ProfileFragment, OtherFragment
    }
}
