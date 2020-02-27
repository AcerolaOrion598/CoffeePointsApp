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
import android.widget.EditText;
import android.widget.ImageView;
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
import androidx.lifecycle.ViewModelProvider;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener {

    private MainViewModel mainViewModel;
    private ConstraintLayout pointInfoWindow, pointAddWindow;
    private TextView pointName, pointAbout, pointOwner, pointActive, pointActiveSwitchTv;
    private EditText pointNameEd, pointAboutEd;
    private Button pointAddBtn, pointAddCancelBtn, pointAddSaveBtn;
    private SwitchCompat pointActiveSwitch;
    private ImageView greenMarkerOnAdd, redMarkerOnAdd;
    private String statusTrueText, statusFalseText;
    private int whoMoved, statusTrueColor, statusFalseColor, topViewShow, topViewHide, bottomViewShow, bottomViewHide,
            myMarkerSize, markerSize;
    private ArrayList<Marker> markers = new ArrayList<>();
    private Context context;
    private Resources resources;
    private MainActivity mainActivity;
    private String[] perms = new String[2];
    private boolean alreadyOpened = false, addWindowHidden = false, pointAddWindowExpanded = false;
    private GoogleMap gMap;
    private SupportMapFragment supportMapFragment;
    private static final int ownerId = 3;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        pointInfoWindow = root.findViewById(R.id.point_info_window);
        pointAddWindow = root.findViewById(R.id.point_add_window);
        pointName = root.findViewById(R.id.point_name);
        pointAbout = root.findViewById(R.id.point_about);
        pointOwner = root.findViewById(R.id.point_owner);
        pointActive = root.findViewById(R.id.point_active);
        pointActiveSwitchTv = root.findViewById(R.id.point_active_switch_tv);
        pointAddBtn = root.findViewById(R.id.point_add_btn);
        pointNameEd = root.findViewById(R.id.point_name_ed);
        pointAboutEd = root.findViewById(R.id.point_about_ed);
        pointAddCancelBtn = root.findViewById(R.id.point_add_cancel_btn);
        pointAddSaveBtn = root.findViewById(R.id.point_add_save_btn);
        pointActiveSwitch = root.findViewById(R.id.point_active_switch);
        greenMarkerOnAdd = root.findViewById(R.id.green_marker_on_add);
        redMarkerOnAdd = root.findViewById(R.id.red_marker_on_add);
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
        topViewShow = R.anim.top_view_show_animation;
        topViewHide = R.anim.top_view_hide_animation;
        bottomViewShow = R.anim.bottom_view_show_animation;
        bottomViewHide = R.anim.bottom_view_hide_animation;
        myMarkerSize = (int) resources.getDimension(R.dimen.my_marker_size);
        markerSize = (int) resources.getDimension(R.dimen.marker_size);
        equalizeMarkers();

        pointAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equalizeMarkers();
                pointActiveSwitch.setChecked(false);
                pointNameEd.setText("");
                pointAboutEd.setText("");
                pointAddWindowToggle(View.GONE, false);
                ViewDriver.setStatusTvOptions(pointActiveSwitchTv, statusFalseText, statusFalseColor);
                ViewDriver.hideView(pointAddBtn, topViewHide, context);
                ViewDriver.hideView(pointInfoWindow, bottomViewHide, context);
                ViewDriver.showView(redMarkerOnAdd, bottomViewShow, context);
                ViewDriver.showView(pointAddWindow, topViewShow, context);
            }
        });

        pointNameEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (focused) {
                    pointAddWindowToggle(View.VISIBLE, true);
                }
            }
        });

        pointAddCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDriver.hideView(redMarkerOnAdd, bottomViewHide, context);
                ViewDriver.hideView(greenMarkerOnAdd, bottomViewHide, context);
                ViewDriver.hideView(pointAddWindow, topViewHide, context);
                ViewDriver.showView(pointAddBtn, topViewShow, context);
            }
        });

        pointAddSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainViewModel.addPoint();
                ViewDriver.hideView(redMarkerOnAdd, R.anim.fast_fade_out_animation, context);
                ViewDriver.hideView(greenMarkerOnAdd, R.anim.fast_fade_out_animation, context);
                ViewDriver.hideView(pointAddWindow, topViewHide, context);
                ViewDriver.showView(pointAddBtn, topViewShow, context);
                Toast.makeText(context, getString(R.string.ononoki_chan), Toast.LENGTH_SHORT).show();
            }
        });

        pointActiveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    swapMarkerIcons(redMarkerOnAdd, greenMarkerOnAdd);
                } else {
                    swapMarkerIcons(greenMarkerOnAdd, redMarkerOnAdd);
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
                for (Marker marker : markers) {
                    marker.remove();
                }
                markers.clear();
                for (Point point : points) {
                    Bitmap customIcon;
                    if (point.isActive()) {
                        customIcon = BitmapFactory.decodeResource(resources, R.drawable.green_marker);
                    } else {
                        customIcon = BitmapFactory.decodeResource(resources, R.drawable.red_marker);
                    }

                    Bitmap scaledCustomIcon;
                    if (point.getOwnerId() == ownerId) {
                        scaledCustomIcon = Bitmap.createScaledBitmap(customIcon, myMarkerSize, myMarkerSize, false);
                    } else {
                        scaledCustomIcon = Bitmap.createScaledBitmap(customIcon, markerSize, markerSize, false);
                    }
                    MarkerOptions options = new MarkerOptions();
                    options.position(point.getCoordinates())
                            .title(point.getHint())
                            .alpha(0.87f)
                            .icon(BitmapDescriptorFactory.fromBitmap(scaledCustomIcon));
                    Marker marker = gMap.addMarker(options);
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

                            ViewDriver.hideView(redMarkerOnAdd, bottomViewHide, context);
                            ViewDriver.hideView(greenMarkerOnAdd, bottomViewHide, context);
                            ViewDriver.hideView(pointAddWindow, topViewHide, context);
                            ViewDriver.showView(pointInfoWindow, bottomViewShow, context);
                            ViewDriver.showView(pointAddBtn, topViewShow, context);
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
            if (pointAddWindow.getVisibility() == View.VISIBLE) {
                addWindowHidden = true;
                if (greenMarkerOnAdd.getVisibility() == View.VISIBLE) {
                    ViewDriver.toggleViewInHalf(greenMarkerOnAdd, R.anim.fast_fade_out_half_animation, context);
                } else {
                    ViewDriver.toggleViewInHalf(redMarkerOnAdd, R.anim.fast_fade_out_half_animation, context);
                }
                ViewDriver.hideView(pointAddWindow, R.anim.fast_fade_out_animation, context);
            }

            ViewDriver.hideView(pointInfoWindow, bottomViewHide, context);
            equalizeMarkers();
        }
    }

    @Override
    public void onCameraIdle() {
        if (whoMoved == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            if (pointAddWindowExpanded) {
                pointAddWindowToggle(View.GONE, false);
            }

            if (addWindowHidden) {
                addWindowHidden = false;
                ViewDriver.showView(pointAddWindow, R.anim.fast_fade_in_animation, context);
                if (greenMarkerOnAdd.getVisibility() == View.VISIBLE) {
                    ViewDriver.toggleViewInHalf(greenMarkerOnAdd, R.anim.fast_fade_in_half_animation, context);
                } else {
                    ViewDriver.toggleViewInHalf(redMarkerOnAdd, R.anim.fast_fade_in_half_animation, context);
                }
            }
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
        LocationManager locationManager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);

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

    private void pointAddWindowToggle(int visibility, boolean isExpanded) {
        pointAboutEd.setVisibility(visibility);
        pointActiveSwitchTv.setVisibility(visibility);
        pointActiveSwitch.setVisibility(visibility);
        pointAddWindowExpanded = isExpanded;
    }

    private void swapMarkerIcons(ImageView markerToHide, ImageView markerToShow) {
        ViewDriver.hideView(markerToHide, bottomViewHide, context);
        ViewDriver.showView(markerToShow, bottomViewShow, context);
        if (markerToShow == greenMarkerOnAdd) {
            ViewDriver.setStatusTvOptions(pointActiveSwitchTv, statusTrueText, statusTrueColor);
        } else {
            ViewDriver.setStatusTvOptions(pointActiveSwitchTv, statusFalseText, statusFalseColor);
        }
    }

    public void equalizeMarkers() {
        for (Marker marker : markers) {
            marker.setAlpha(0.87f);
        }
    }

    public ConstraintLayout getPointAddWindow() {
        return pointAddWindow;
    }

    public ConstraintLayout getPointInfoWindow() {
        return pointInfoWindow;
    }

    public Button getPointAddBtn() {
        return pointAddBtn;
    }

    public ImageView getGreenMarkerOnAdd() {
        return greenMarkerOnAdd;
    }

    public ImageView getRedMarkerOnAdd() {
        return redMarkerOnAdd;
    }
}