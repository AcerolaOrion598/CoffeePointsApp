package com.djaphar.coffeepointapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.djaphar.coffeepointapp.Fragments.MapFragment;
import com.djaphar.coffeepointapp.Fragments.OtherFragment;
import com.djaphar.coffeepointapp.Fragments.PointsFragment;
import com.djaphar.coffeepointapp.Fragments.ProfileFragment;
import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.User;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.UserChangeChecker;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.coffeepointapp.ViewModels.MainViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private TextView actionBarTitle;
    private MyFragment currentFragment;
    private UserChangeChecker userChangeChecker;
    private MainViewModel mainViewModel;
    private User user;
    Integer userHash;

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
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getUser().observe(this, user -> {
            if (user == null) {
                return;
            }
            this.user = user;
            userHash = user.getUserHash();
        });
        userChangeChecker = new UserChangeChecker(new Handler(), this);
        userChangeChecker.startUserChangeCheck();
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
                ConstraintLayout addPointWindow = pointsFragment.getAddPointWindow();
                if (!(editLayout.getVisibility() == View.VISIBLE) && !(addPointWindow.getVisibility() == View.VISIBLE)) {
                    super.onBackPressed();
                    return;
                }
                break;

            case ProfileFragment:
                ProfileFragment profileFragment = ((ProfileFragment) currentFragment);
                ConstraintLayout editUserNameWindow = profileFragment.getEditUserNameWindow();
                ConstraintLayout addProductWindow = profileFragment.getAddProductWindow();
                if (!(editUserNameWindow.getVisibility() == View.VISIBLE) && !(addProductWindow.getVisibility() == View.VISIBLE)) {
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

    @Override
    protected void onResume() {
        super.onResume();
        userChangeChecker.startUserChangeCheck();
    }

    @Override
    protected void onPause() {
        super.onPause();
        userChangeChecker.stopUserChangeCheck();
    }

    public void requestUser() {
        if (user == null) {
            return;
        }
        mainViewModel.requestUser(user.get_id(), userHash);
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
