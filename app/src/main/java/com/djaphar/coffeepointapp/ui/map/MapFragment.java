package com.djaphar.coffeepointapp.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.djaphar.coffeepointapp.MainActivity;
import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.PermissionDriver;
import com.djaphar.coffeepointapp.SupportClasses.Point;
import com.djaphar.coffeepointapp.SupportClasses.ViewDriver;
import com.djaphar.coffeepointapp.ViewModel.MainViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener {

    private MainViewModel mainViewModel;
    private ConstraintLayout pointInfoWindow, pointAddWindow;
    private TextView pointName, pointAbout, pointOwner, pointActive, pointActiveSwitchTv;
    private Button pointAddBtn, pointAddCancelBtn, pointAddSaveBtn;
    private SwitchCompat pointActiveSwitch;
    private String statusTrueText, statusFalseText;
    private int whoMoved, statusTrueColor, statusFalseColor, btnShow, btnHide, windowShow, windowHide;
    private ArrayList<Marker> markers = new ArrayList<>();
    private Context context;
    private Resources resources;
    private MainActivity mainActivity;
    private String[] perms = new String[2];
    private LocationManager locationManager;
    private boolean alreadyOpened = false;
    private GoogleMap gMap;
    private SupportMapFragment supportMapFragment;
    private int myMarkerSize, markerSize;

    private static final int ownerId = 3;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        pointInfoWindow = root.findViewById(R.id.point_info_window);
        pointAddWindow = root.findViewById(R.id.point_add_window);
        pointName = root.findViewById(R.id.point_name);
        pointAbout = root.findViewById(R.id.point_about);
        pointOwner = root.findViewById(R.id.point_owner);
        pointActive = root.findViewById(R.id.point_active);
        pointActiveSwitchTv = root.findViewById(R.id.point_active_switch_tv);
        pointAddBtn = root.findViewById(R.id.point_add_btn);
        pointAddCancelBtn = root.findViewById(R.id.point_add_cancel_btn);
        pointAddSaveBtn = root.findViewById(R.id.point_add_save_btn);
        pointActiveSwitch = root.findViewById(R.id.point_active_switch);
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setActionBarTitle(getString(R.string.title_map));
        }
        perms[0] = Manifest.permission.ACCESS_COARSE_LOCATION;
        perms[1] = Manifest.permission.ACCESS_FINE_LOCATION;
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        resources = getResources();
        statusTrueColor = resources.getColor(R.color.colorGreen60);
        statusFalseColor = resources.getColor(R.color.colorRed60);
        statusTrueText = getString(R.string.point_status_true);
        statusFalseText = getString(R.string.point_status_false);
        btnShow = R.anim.add_btn_show_animation;
        btnHide = R.anim.add_btn_hide_animation;
        windowShow = R.anim.bottom_window_show_animation;
        windowHide = R.anim.bottom_window_hide_animation;
        myMarkerSize =  (int) resources.getDimension(R.dimen.my_marker_size);
        markerSize =  (int) resources.getDimension(R.dimen.marker_size);

        pointAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pointActiveSwitch.setChecked(false);
                ViewDriver.setStatusTvOptions(pointActiveSwitchTv, statusFalseText, statusFalseColor);
                ViewDriver.hideView(pointAddBtn, btnHide, context);
                ViewDriver.hideView(pointInfoWindow, windowHide, context);
                ViewDriver.showView(pointAddWindow, windowShow, context);
            }
        });

        pointAddCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDriver.hideView(pointAddWindow, windowHide, context);
                ViewDriver.showView(pointAddBtn, btnShow, context);
            }
        });

        pointAddSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainViewModel.addPoint();
                ViewDriver.hideView(pointAddWindow, windowHide, context);
                ViewDriver.showView(pointAddBtn, btnShow, context);
                Toast.makeText(context, getString(R.string.ononoki_chan), Toast.LENGTH_SHORT).show();
            }
        });

        pointActiveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    ViewDriver.setStatusTvOptions(pointActiveSwitchTv, statusTrueText, statusTrueColor);
                } else {
                    ViewDriver.setStatusTvOptions(pointActiveSwitchTv, statusFalseText, statusFalseColor);
                }
            }
        });

        if (supportMapFragment == null) {
            supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_container);
            if (supportMapFragment != null) {
                supportMapFragment.getMapAsync(this);
            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        if (PermissionDriver.hasPerms(perms, context)) {
            getDeviceLocation();
            gMap.setMyLocationEnabled(true);
            gMap.getUiSettings().setMyLocationButtonEnabled(false);
        } else {
            PermissionDriver.requestPerms(this, perms);
        }

        gMap.setOnCameraMoveStartedListener(this);
        gMap.setOnCameraIdleListener(this);

        mainViewModel.sendScreenBounds(getScreenBounds());
        mainViewModel.getPoints().observe(getViewLifecycleOwner(), new Observer<ArrayList<Point>>() {
            @Override
            public void onChanged(ArrayList<Point> points) {
                MarkerOptions options = new MarkerOptions();
                for (Marker marker : markers) {
                    marker.remove();
                }
                markers.clear();
                for (Point point : points) {
                    Bitmap customIcon;
                    Bitmap scaledCustomIcon;
                    if (point.isActive()) {
                        customIcon = BitmapFactory.decodeResource(resources, R.drawable.green_marker);
                    } else {
                        customIcon = BitmapFactory.decodeResource(resources, R.drawable.red_marker);
                    }

                    if (point.getOwnerId() == ownerId) {
                        scaledCustomIcon = Bitmap.createScaledBitmap(customIcon, myMarkerSize, myMarkerSize, false);
                    } else {
                        scaledCustomIcon = Bitmap.createScaledBitmap(customIcon, markerSize, markerSize, false);
                    }
                    Marker marker;
                    options.position(point
                            .getCoordinates())
                            .title(point.getHint())
                            .alpha(0.87f)
                            .icon(BitmapDescriptorFactory.fromBitmap(scaledCustomIcon));
                    marker = gMap.addMarker(options);
                    marker.setTag(point);
                    markers.add(marker);
                }

                gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        for (Marker m : markers) {
                            m.setAlpha(0.4f);
                        }
                        marker.setAlpha(1.0f);
                        Point point = (Point) marker.getTag();
                        if (point != null) {
                            if (point.isActive()) {
                                ViewDriver.setStatusTvOptions(pointActive, statusTrueText, statusTrueColor);
                            } else {
                                ViewDriver.setStatusTvOptions(pointActive, statusFalseText, statusFalseColor);
                            }
                            pointName.setText(point.getName());
                            pointAbout.setText(point.getAbout());
                            pointOwner.setText(point.getOwner());

                            ViewDriver.hideView(pointAddWindow, windowHide, context);
                            ViewDriver.showView(pointInfoWindow, windowShow, context);
                            ViewDriver.showView(pointAddBtn, btnShow, context);
                        }

                        return false;
                    }
                });
            }
        });
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        whoMoved = reason;
        if (whoMoved == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            ViewDriver.hideView(pointInfoWindow, windowHide, context);
            for (Marker marker : markers) {
                marker.setAlpha(0.87f);
            }
        }
    }

    @Override
    public void onCameraIdle() {
        if (whoMoved == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            mainViewModel.sendScreenBounds(getScreenBounds());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        getDeviceLocation();
        gMap.getUiSettings().setMyLocationButtonEnabled(false);
        gMap.setMyLocationEnabled(true);
    }

    private void getDeviceLocation() {
        locationManager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);

        try {
            if (ActivityCompat.checkSelfPermission(context, perms[0]) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, perms[0]) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, locationListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LatLngBounds getScreenBounds() {
        return gMap.getProjection().getVisibleRegion().latLngBounds;
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LatLng myPosition = new LatLng(location.getLatitude(), location.getLongitude());
            if (!alreadyOpened) {
                focusOnMe(myPosition, 15f);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) { }

        @Override
        public void onProviderEnabled(String s) { }

        @Override
        public void onProviderDisabled(String s) { }
    };

    private void focusOnMe(LatLng latLng, float zoom) {
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
            alreadyOpened = true;
    }
}