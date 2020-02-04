package com.djaphar.coffeepointapp.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djaphar.coffeepointapp.MainActivity;
import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.Point;
import com.djaphar.coffeepointapp.ViewModel.MainViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener {

    private MainViewModel mainViewModel;
    private GoogleMap gMap;
    private ConstraintLayout pointInfoWindow;
    private TextView pointName, pointAbout, pointOwner, pointActive;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        pointInfoWindow = root.findViewById(R.id.point_info_window);
        pointName = root.findViewById(R.id.point_name);
        pointAbout = root.findViewById(R.id.point_about);
        pointOwner = root.findViewById(R.id.point_owner);
        pointActive = root.findViewById(R.id.point_active);
        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.title_map));
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_container);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        gMap.setOnCameraMoveStartedListener(this);
        gMap.setOnCameraIdleListener(this);
        mainViewModel.getPoints().observe(getViewLifecycleOwner(), new Observer<ArrayList<Point>>() {
            @Override
            public void onChanged(ArrayList<Point> points) {
                MarkerOptions options = new MarkerOptions();
                for (Point point:points) {
                    if (point.isActive()) {

                    }
                    Marker marker;
                    options.position(point
                            .getCoordinates())
                            .title(point.getHint());
//                            .icon(mainViewModel.bitmapDescriptorFromVector(R.drawable.ic_pin_24dp));
                    marker = gMap.addMarker(options);
                    marker.setTag(point);
                }

                gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Point point = (Point) marker.getTag();
                        if (point != null) {
                            String status;
                            if (point.isActive()) {
                                status = "Открыта";
                                pointActive.setTextColor(getResources().getColor(R.color.colorGreen60));
                            } else {
                                pointActive.setTextColor(getResources().getColor(R.color.colorRed60));
                                status = "Закрыта";
                            }
                            pointName.setText(point.getName());
                            pointAbout.setText(point.getAbout());
                            pointOwner.setText(point.getOwner());
                            pointActive.setText(status);
                            pointInfoWindow.setVisibility(View.VISIBLE);
                        }
                        return false;
                    }
                });
            }
        });
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            pointInfoWindow.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCameraIdle() {
        mainViewModel.sendScreenBounds(gMap.getProjection().getVisibleRegion().latLngBounds);
    }
}