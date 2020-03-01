package com.djaphar.coffeepointapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.UserDao;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.UserRoom;
import com.djaphar.coffeepointapp.ui.map.MapFragment;
import com.djaphar.coffeepointapp.ui.points.PointsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.ref.WeakReference;
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
            currentFragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
        }

        FragmentNames names = FragmentNames.valueOf(currentFragment.getClass().getSimpleName());
        switch (names) {
            case MapFragment:
                MapFragment mapFragment = ((MapFragment) currentFragment);
                ConstraintLayout pointInfoWindow = mapFragment.getPointInfoWindow();
                if (!(mapFragment.addMarkerVisible()) && !(pointInfoWindow.getVisibility() == View.VISIBLE)) {
                    DeleteUser deleteUser = new DeleteUser(this);
                    deleteUser.execute();
                    super.onBackPressed();
                } else {
                    mapFragment.backWasPressed();
                }
                break;

            case PointsFragment:
                PointsFragment pointsFragment = ((PointsFragment) currentFragment);
                ConstraintLayout editLayout = pointsFragment.getPointEditLayout();
                if (!(editLayout.getVisibility() == View.VISIBLE)) {
                    super.onBackPressed();
                } else {
                    pointsFragment.backWasPressed();
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

    static class DeleteUser extends AsyncTask<Void, Void, Void> {
        private WeakReference<MainActivity> weakActivity;

        DeleteUser(MainActivity activity) {
            weakActivity = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            UserRoom userRoom = UserRoom.getDatabase(weakActivity.get());
            UserDao userDao = userRoom.userDao();
            userDao.deleteUser();
            return null;
        }
    }
}
