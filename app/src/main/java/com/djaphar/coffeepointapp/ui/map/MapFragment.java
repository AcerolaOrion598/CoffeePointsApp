package com.djaphar.coffeepointapp.ui.map;

import android.content.Context;
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
import com.djaphar.coffeepointapp.SupportClasses.Point;
import com.djaphar.coffeepointapp.SupportClasses.ViewDriver;
import com.djaphar.coffeepointapp.ViewModel.MainViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener {

    private MainViewModel mainViewModel;
    private GoogleMap gMap;
    private ConstraintLayout pointInfoWindow, pointAddWindow;
    private TextView pointName, pointAbout, pointOwner, pointActive, pointActiveSwitchTv;
    private Button pointAddBtn, pointAddCancelBtn, pointAddSaveBtn;
    private SwitchCompat pointActiveSwitch;
    private String statusTrueText, statusFalseText;
    private int whoMoved, statusTrueColor, statusFalseColor, btnShow, btnHide, windowShow, windowHide;
    private ArrayList<Marker> markers = new ArrayList<>();
    private Context context;

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
        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.title_map));
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        statusTrueColor = getResources().getColor(R.color.colorGreen60);
        statusFalseColor = getResources().getColor(R.color.colorRed60);
        statusTrueText = getString(R.string.point_status_true);
        statusFalseText = getString(R.string.point_status_false);
        btnShow = R.anim.add_btn_show_animation;
        btnHide = R.anim.add_btn_hide_animation;
        windowShow = R.anim.bottom_window_show_animation;
        windowHide = R.anim.bottom_window_hide_animation;
        context = getContext();

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_container);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }

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
                ViewDriver.showView(pointAddBtn, btnShow,context);
            }
        });

        pointAddSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainViewModel.addPoint();
                ViewDriver.hideView(pointAddWindow, windowHide, context);
                ViewDriver.showView(pointAddBtn, btnShow, context);
                Toast.makeText(context, "Точка добавлена (нет)", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        mainViewModel.sendScreenBounds(getScreenBounds());

        gMap.setOnCameraMoveStartedListener(this);
        gMap.setOnCameraIdleListener(this);
        mainViewModel.getPoints().observe(getViewLifecycleOwner(), new Observer<ArrayList<Point>>() {
            @Override
            public void onChanged(ArrayList<Point> points) {
                MarkerOptions options = new MarkerOptions();
                markers.clear();
                for (Point point:points) {
                    float color;
                    if (point.isActive()) {
                        color = BitmapDescriptorFactory.HUE_GREEN;
                    } else {
                        color = BitmapDescriptorFactory.HUE_RED;
                    }
                    Marker marker;
                    options.position(point
                            .getCoordinates())
                            .title(point.getHint())
                            .alpha(0.87f)
                            .icon(BitmapDescriptorFactory.defaultMarker(color));
                    marker = gMap.addMarker(options);
                    marker.setTag(point);
                    markers.add(marker);
                }

                gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        for (Marker m: markers) {
                            m.setAlpha(0.6f);
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
            for (Marker marker:markers) {
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

    public LatLngBounds getScreenBounds() {
        return gMap.getProjection().getVisibleRegion().latLngBounds;
    }
}