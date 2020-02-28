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
    private EditText pointNameEd, pointHintEd, pointAboutEd;
    private Button pointAddBtn, pointAddCancelBtn, pointAddSaveBtn, pointEditBtn, pointDeleteBtn;
    private SwitchCompat pointActiveSwitch, pointActiveInfoWindowSwitch;
    private ImageView greenMarkerOnAdd, redMarkerOnAdd;
    private String statusTrueText, statusFalseText;
    private int whoMoved, statusTrueColor, statusFalseColor, topViewShow, topViewHide, bottomViewShow, bottomViewHide,
            myMarkerSize, markerSize;
    private ArrayList<Marker> markers = new ArrayList<>();
    private Context context;
    private Resources resources;
    private MainActivity mainActivity;
    private String[] perms = new String[2];
    private boolean alreadyOpened = false, addWindowHidden = false, pointAddWindowExpanded = false, editMode = false, editableMarkerRemoved = false;
    private GoogleMap gMap;
    private SupportMapFragment supportMapFragment;
    private Marker focusedMarker = null;
    private Point focusedMarkerInfo = null;
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
        pointHintEd = root.findViewById(R.id.point_hint_ed);
        pointAboutEd = root.findViewById(R.id.point_about_ed);
        pointAddCancelBtn = root.findViewById(R.id.point_add_cancel_btn);
        pointAddSaveBtn = root.findViewById(R.id.point_add_save_btn);
        pointEditBtn = root.findViewById(R.id.point_edit_btn);
        pointDeleteBtn = root.findViewById(R.id.point_delete_btn);
        pointActiveSwitch = root.findViewById(R.id.point_active_switch);
        pointActiveInfoWindowSwitch = root.findViewById(R.id.point_active_info_window_switch);
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
        equalizeMarkers(0.87f);

        pointAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusedMarker = null;
                focusedMarkerInfo = null;
                equalizeMarkers(0.4f);
                addPointModeStart(false, "", "", "", redMarkerOnAdd, true);
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
                addPointModeEnd(bottomViewHide, false);
            }
        });

        pointAddSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedMarker == null) {
                    addPoint(); //Значит добавляем новую точку
                    Toast.makeText(context, R.string.ononoki_chan, Toast.LENGTH_SHORT).show();
                } else {
                    editPoint(); //Значит изменяем уже существующую
                    Toast.makeText(context, R.string.shinobu_chan, Toast.LENGTH_SHORT).show();
                }
                addPointModeEnd(R.anim.fast_fade_out_half_animation,true);
            }
        });

        pointEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedMarkerInfo != null) {
                    editMode = true;
                    if (focusedMarkerInfo.isActive()) {
                        addPointModeStart(focusedMarkerInfo.isActive(), focusedMarkerInfo.getName(), focusedMarkerInfo.getHint(),
                                focusedMarkerInfo.getAbout(), greenMarkerOnAdd, false);
                    } else {
                        addPointModeStart(focusedMarkerInfo.isActive(), focusedMarkerInfo.getName(), focusedMarkerInfo.getHint(),
                                focusedMarkerInfo.getAbout(), redMarkerOnAdd, false);
                    }
                    focusedMarker.remove();
                    editableMarkerRemoved = true;
                }
            }
        });

        pointDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedMarker != null) {
                    Toast.makeText(context, R.string.mayoi_chan, Toast.LENGTH_SHORT).show();
                    focusedMarker.remove();
                    equalizeMarkers(0.87f);
                    ViewDriver.hideView(pointInfoWindow, bottomViewHide, context);
                }
            }
        });

        pointActiveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!editMode || editableMarkerRemoved) {
                    if (isChecked) {
                        swapMarkerIcons(redMarkerOnAdd, greenMarkerOnAdd);
                    } else {
                        swapMarkerIcons(greenMarkerOnAdd, redMarkerOnAdd);
                    }
                }
            }
        });

        pointActiveInfoWindowSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) { //Отправляем isChecked шоб апдейтить бд
                    ViewDriver.setStatusTvOptions(pointActive, statusTrueText, statusTrueColor);
                } else {
                    ViewDriver.setStatusTvOptions(pointActive, statusFalseText, statusFalseColor);
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
                removeMarkers();
                markers.clear();
                drawMarkers(points);

                gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        showPointInfo(marker);
                        focusedMarker = marker;
                        focusedMarkerInfo = (Point) marker.getTag();
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
                ViewDriver.hideView(pointAddWindow, R.anim.fast_fade_out_animation, context);
                if (greenMarkerOnAdd.getVisibility() == View.VISIBLE) {
                    ViewDriver.toggleViewInHalf(greenMarkerOnAdd, R.anim.fast_fade_out_half_animation, context);
                } else {
                    ViewDriver.toggleViewInHalf(redMarkerOnAdd, R.anim.fast_fade_out_half_animation, context);
                }
            } else {
                equalizeMarkers(0.87f);
            }

            ViewDriver.hideView(pointInfoWindow, bottomViewHide, context);
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
                focusOnMe(myPosition);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) { }

        @Override
        public void onProviderEnabled(String s) { }

        @Override
        public void onProviderDisabled(String s) { }
    };

    private void focusOnMe(LatLng latLng) {
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, (float) 15.0));
            alreadyOpened = true;
    }

    private void pointAddWindowToggle(int visibility, boolean isExpanded) {
        pointHintEd.setVisibility(visibility);
        pointAboutEd.setVisibility(visibility);
        pointAddWindowExpanded = isExpanded;
    }

    private void infoWindowEditElementsToggle(int visibility, int constraintBottom) {
        pointDeleteBtn.setVisibility(visibility);
        pointEditBtn.setVisibility(visibility);
        pointActiveInfoWindowSwitch.setVisibility(visibility);
        ConstraintLayout.LayoutParams paramsTv = (ConstraintLayout.LayoutParams) pointActive.getLayoutParams();
        paramsTv.bottomToBottom = constraintBottom;
        pointActive.setLayoutParams(paramsTv);
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

    private void showPointInfo(Marker marker) {
        Point point = (Point) marker.getTag();
        if (point != null) {
            if (point.isActive()) {
                pointActiveInfoWindowSwitch.setChecked(true);
                ViewDriver.setStatusTvOptions(pointActive, statusTrueText, statusTrueColor);
            } else {
                pointActiveInfoWindowSwitch.setChecked(false);
                ViewDriver.setStatusTvOptions(pointActive, statusFalseText, statusFalseColor);
            }

            if (point.getOwnerId() == ownerId) {
                infoWindowEditElementsToggle(View.VISIBLE, ConstraintLayout.LayoutParams.UNSET);
            } else {
                infoWindowEditElementsToggle(View.GONE, R.id.point_info_window);
            }

            pointName.setText(point.getName());
            pointAbout.setText(point.getAbout());
            pointOwner.setText(point.getOwner());

            addPointModeEnd(bottomViewHide, false);
            ViewDriver.showView(pointInfoWindow, bottomViewShow, context);
            equalizeMarkers(0.4f);
            marker.setAlpha(1.0f);
        }
    }

    private void addPointModeStart(boolean isActive, String pointName, String pointHint, String pointAbout, ImageView markerToShow, boolean withMarkerAnimation) {
        if (isActive) {
            ViewDriver.setStatusTvOptions(pointActiveSwitchTv, statusTrueText, statusTrueColor);
        } else {
            ViewDriver.setStatusTvOptions(pointActiveSwitchTv, statusFalseText, statusFalseColor);
        }
        pointActiveSwitch.setChecked(isActive);
        pointNameEd.setText(pointName);
        pointHintEd.setText(pointHint);
        pointAboutEd.setText(pointAbout);
        pointAddWindowToggle(View.GONE, false);
        ViewDriver.hideView(pointAddBtn, topViewHide, context);
        ViewDriver.hideView(pointInfoWindow, bottomViewHide, context);
        if (withMarkerAnimation) {
            ViewDriver.showView(markerToShow, bottomViewShow, context);
        } else {
            markerToShow.setVisibility(View.VISIBLE);
        }
        ViewDriver.showView(pointAddWindow, topViewShow, context);
    }

    private void addPointModeEnd(int animationResource, boolean saveCalled) {
        ViewDriver.hideView(redMarkerOnAdd, animationResource, context);
        ViewDriver.hideView(greenMarkerOnAdd, animationResource, context);
        ViewDriver.hideView(pointAddWindow, topViewHide, context);
        ViewDriver.showView(pointAddBtn, topViewShow, context);
        equalizeMarkers(0.87f);
        if (!saveCalled && editMode) {
            if (focusedMarkerInfo != null) {
                Marker marker = gMap.addMarker(setMarkerOptions(focusedMarkerInfo));
                marker.setTag(focusedMarkerInfo);
                markers.add(marker);
            }
        }
        editMode = false;
        editableMarkerRemoved = false;
    }

    private void drawMarkers(ArrayList<Point> points) {
        for (Point point : points) {
            Marker marker = gMap.addMarker(setMarkerOptions(point));
            marker.setTag(point);
            markers.add(marker);
        }
    }

    private MarkerOptions setMarkerOptions(Point point) {
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
        return options;
    }

    private void removeMarkers() {
        for (Marker marker : markers) {
            marker.remove();
        }
    }

    private void equalizeMarkers(float opacity) {
        for (Marker marker : markers) {
            marker.setAlpha(opacity);
        }
    }

    private void addPoint() {
        mainViewModel.addPoint();
    }

    private void editPoint() {
        mainViewModel.editPoint();
    }

    public void backWasPressed() {
        if (pointAddWindow.getVisibility() == View.VISIBLE) {
            addPointModeEnd(bottomViewHide, false);
        } else if (pointInfoWindow.getVisibility() == View.VISIBLE) {
            ViewDriver.hideView(pointInfoWindow, bottomViewHide, context);
            equalizeMarkers(0.87f);
        }
    }

    public boolean addMarkerVisible() {
        return greenMarkerOnAdd.getVisibility() == View.VISIBLE || redMarkerOnAdd.getVisibility() == View.VISIBLE;
    }

    public ConstraintLayout getPointInfoWindow() {
        return pointInfoWindow;
    }
}