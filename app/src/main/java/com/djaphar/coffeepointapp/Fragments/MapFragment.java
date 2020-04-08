package com.djaphar.coffeepointapp.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.djaphar.coffeepointapp.Activities.MainActivity;
import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.Adapters.MapPointProductsRecyclerAdapter;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.Point;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.PointUpdateModel;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.User;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.MapPointsChangeChecker;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.PermissionDriver;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.ViewDriver;
import com.djaphar.coffeepointapp.ViewModels.MapViewModel;
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
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MapFragment extends MyFragment implements OnMapReadyCallback, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener,
        View.OnTouchListener {

    private MapPointsChangeChecker mapPointsChangeChecker;
    private MapViewModel mapViewModel;
    private MainActivity mainActivity;
    private Context context;
    private Resources resources;
    private ConstraintLayout pointInfoWindow, pointEditWindow;
    private RecyclerView mapPointProductsRecyclerView;
    private TextView pointName, pointOwner, pointActive;
    private EditText pointNameEd;
    private Button pointEditCancelBtn, pointEditSaveBtn, pointEditBtn;
    private SupportMapFragment supportMapFragment;
    private GoogleMap gMap;
    private ArrayList<Marker> markers = new ArrayList<>(), tempMarkers = new ArrayList<>();
    private Point focusedMarkerInfo = null;
    private String statusTrueText, statusFalseText;
    private String[] perms = new String[2];
    private User user;
    private HashMap<String, String> authHeaderMap = new HashMap<>();
    private float infoWindowCorrectionY, infoWindowStartMotionY, infoWindowEndMotionY, editWindowCorrectionY, editWindowEndMotionY, pointEditTopLimit, pointEditBottomLimit;
    private int whoMoved, statusTrueColor, statusFalseColor, myMarkerSize, markerSize;
    private boolean alreadyFocused = false, editWindowHidden = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapPointsChangeChecker = new MapPointsChangeChecker(new Handler(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapPointsChangeChecker.startMapPointsChangeCheck();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapPointsChangeChecker.stopMapPointsChangeCheck();
        if (mapViewModel == null || gMap == null) {
            return;
        }
        LatLngBounds bounds = gMap.getProjection().getVisibleRegion().latLngBounds;
        mapViewModel.setLastScreenBounds(bounds.northeast.latitude, bounds.northeast.longitude, bounds.southwest.latitude, bounds.southwest.longitude);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        pointInfoWindow = root.findViewById(R.id.point_info_window);
        pointEditWindow = root.findViewById(R.id.point_edit_window);
        mapPointProductsRecyclerView = root.findViewById(R.id.map_point_products_recycler_view);
        pointName = root.findViewById(R.id.point_name);
        pointOwner = root.findViewById(R.id.point_owner);
        pointActive = root.findViewById(R.id.point_active);
        pointNameEd = root.findViewById(R.id.point_name_ed);
        pointEditCancelBtn = root.findViewById(R.id.point_edit_cancel_btn);
        pointEditSaveBtn = root.findViewById(R.id.point_edit_save_btn);
        pointEditBtn = root.findViewById(R.id.point_edit_btn);
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setActionBarTitle(getString(R.string.title_map));
        }
        perms[0] = Manifest.permission.ACCESS_COARSE_LOCATION;
        perms[1] = Manifest.permission.ACCESS_FINE_LOCATION;
        mapViewModel.getLastBounds().observe(getViewLifecycleOwner(), lastBounds -> {
            if (lastBounds == null || gMap == null) {
                return;
            }
            double northLat, northLong, southLat, southLong;
            northLat = lastBounds.getNorthLat();
            northLong = lastBounds.getNorthLong();
            southLat = lastBounds.getSouthLat();
            southLong = lastBounds.getSouthLong();
            LatLngBounds bounds = new LatLngBounds(new LatLng(southLat, southLong), new LatLng(northLat, northLong));
            gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
        });
        return root;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        resources = getResources();
        statusTrueColor = resources.getColor(R.color.colorGreen60);
        statusFalseColor = resources.getColor(R.color.colorRed60);
        statusTrueText = getString(R.string.point_status_true);
        statusFalseText = getString(R.string.point_status_false);
        myMarkerSize = (int) resources.getDimension(R.dimen.my_marker_size);
        markerSize = (int) resources.getDimension(R.dimen.marker_size);
        pointEditWindow.setTranslationY(resources.getDimension(R.dimen.point_edit_translation_y));
        pointEditTopLimit = pointEditWindow.getY();
        pointEditWindow.setTranslationY(resources.getDimension(R.dimen.point_edit_expanded_translation_y));
        pointEditBottomLimit = pointEditWindow.getY();
        equalizeMarkers(0.87f);

        mapViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                return;
            }
            this.user = user;
            authHeaderMap.put(getString(R.string.authorization_header), user.getToken());
        });

        mapViewModel.getSupervisor().observe(getViewLifecycleOwner(), supervisor -> {
            if (supervisor == null) {
                return;
            }
            String name = supervisor.getName();
            if (name == null || name.equals("")) {
                pointOwner.setText(R.string.some_string_is_null_text);
            } else {
                pointOwner.setText(supervisor.getName());
            }
        });

        pointNameEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) {
                    pointEditSaveBtn.setEnabled(false);
                } else {
                    pointEditSaveBtn.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
        });

        pointEditCancelBtn.setOnClickListener(lView -> editPointModeEnd());

        pointEditSaveBtn.setOnClickListener(lView -> {
            mapViewModel.requestUpdatePoint(focusedMarkerInfo.get_id(), authHeaderMap,
                    new PointUpdateModel(pointNameEd.getText().toString()), gMap.getProjection().getVisibleRegion().latLngBounds);
            editPointModeEnd();
        });

        pointEditBtn.setOnClickListener(lView -> {
            if (focusedMarkerInfo != null) {
                editPointModeStart(focusedMarkerInfo.getName());
            }
        });

        pointInfoWindow.setOnTouchListener(this);
        pointEditWindow.setOnTouchListener(this);

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

        mapViewModel.getPoints().observe(getViewLifecycleOwner(), points -> {
            drawMarkers(points);
            removeMarkers();
            rewriteMarkerList();
            gMap.setOnMarkerClickListener(marker -> {
                showPointInfo(marker);
                focusedMarkerInfo = (Point) marker.getTag();
                return false;
            });
        });
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        whoMoved = reason;
        if (whoMoved == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            if (pointEditWindow.getVisibility() == View.VISIBLE) {
                editWindowHidden = true;
                ViewDriver.hideView(pointEditWindow, R.anim.fast_fade_out_animation, context);
            } else {
                removeFocusFromMarker();
            }

            ViewDriver.hideView(pointInfoWindow, R.anim.bottom_view_hide_animation, context);
        }
    }

    @Override
    public void onCameraIdle() {
        if (whoMoved == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {

            if (editWindowHidden) {
                editWindowHidden = false;
                ViewDriver.showView(pointEditWindow, R.anim.fast_fade_in_animation, context);
            }

            requestPointsInBox();
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

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LatLng myPosition = new LatLng(location.getLatitude(), location.getLongitude());
            if (!alreadyFocused) {
                alreadyFocused = true;
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, (float) 15.0));
                requestPointsInBox();
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) { }

        @Override
        public void onProviderEnabled(String s) { }

        @Override
        public void onProviderDisabled(String s) { }
    };

    private void infoWindowEditElementsToggle(int visibility, int constraintBottom) {
        pointEditBtn.setVisibility(visibility);
        ConstraintLayout.LayoutParams paramsTv = (ConstraintLayout.LayoutParams) pointActive.getLayoutParams();
        paramsTv.bottomToBottom = constraintBottom;
        pointActive.setLayoutParams(paramsTv);
    }

    private void showPointInfo(Marker marker) {
        Point point = (Point) marker.getTag();
        if (point != null) {
            pointOwner.setText("");
            mapViewModel.requestSupervisor(point.getSupervisor());
            if (point.getCurrentlyNotHere()) {
                ViewDriver.setStatusTvOptions(pointActive, statusTrueText, statusTrueColor);
            } else {
                ViewDriver.setStatusTvOptions(pointActive, statusFalseText, statusFalseColor);
            }

            if (point.getSupervisor().equals(user.get_id())) {
                infoWindowEditElementsToggle(View.VISIBLE, ConstraintLayout.LayoutParams.UNSET);
            } else {
                infoWindowEditElementsToggle(View.GONE, R.id.point_info_window);
            }

            String name = point.getName();
            if (name == null || name.equals("")) {
                name = getString(R.string.point_name_null);
            }
            pointName.setText(name);

            if (point.getProductList().size() > 5) {
                mapPointProductsRecyclerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) resources.getDimension(R.dimen.map_point_products_recycler_view_max_height)));
            } else {
                mapPointProductsRecyclerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            MapPointProductsRecyclerAdapter adapter = new MapPointProductsRecyclerAdapter(point.getProductList(), getString(R.string.point_product_null));
            mapPointProductsRecyclerView.setAdapter(adapter);
            mapPointProductsRecyclerView.setLayoutManager(new LinearLayoutManager(context));

            editPointModeEnd();
            ViewDriver.showView(pointInfoWindow, R.anim.bottom_view_show_animation, context);

            equalizeMarkers(0.4f);
            marker.setAlpha(1.0f);
        }
    }

    private void editPointModeStart(String pointName) {
        pointNameEd.setText(pointName);
        ViewDriver.hideView(pointInfoWindow, R.anim.bottom_view_hide_animation, context);
        pointEditWindow.setTranslationY(resources.getDimension(R.dimen.point_edit_translation_y));
        ViewDriver.showView(pointEditWindow, R.anim.top_view_show_animation, context);
    }

    private void editPointModeEnd() {
        ViewDriver.hideView(pointEditWindow, R.anim.top_view_hide_animation, context);
        removeFocusFromMarker();
    }

    private void drawMarkers(ArrayList<Point> points) {
        for (Point point : points) {
            Marker marker = gMap.addMarker(setMarkerOptions(point));
            marker.setTag(point);
            tempMarkers.add(marker);
        }
    }

    private void  rewriteMarkerList() {
        markers.clear();
        markers.addAll(tempMarkers.subList(0, tempMarkers.size()));
        tempMarkers.clear();
    }

    private void removeFocusFromMarker() {
        focusedMarkerInfo = null;
        equalizeMarkers(0.87f);
    }

    private boolean focusedMarker(Point point) {
        if (focusedMarkerInfo == null) {
            return false;
        }
        LatLng focusedLatLng = new LatLng(focusedMarkerInfo.getCoordinates().getLat(), focusedMarkerInfo.getCoordinates().getLng());
        LatLng currentLatLng = new LatLng(point.getCoordinates().getLat(), point.getCoordinates().getLng());
        return focusedLatLng.latitude == currentLatLng.latitude && focusedLatLng.longitude == currentLatLng.longitude;
    }

    private MarkerOptions setMarkerOptions(Point point) {
        Bitmap customIcon;
        if (point.getCurrentlyNotHere()) {
            customIcon = BitmapFactory.decodeResource(resources, R.drawable.green_marker);
        } else {
            customIcon = BitmapFactory.decodeResource(resources, R.drawable.red_marker);
        }

        Bitmap scaledCustomIcon;
        if (point.getSupervisor().equals(user.get_id())) {
            scaledCustomIcon = Bitmap.createScaledBitmap(customIcon, myMarkerSize, myMarkerSize, false);
        } else {
            scaledCustomIcon = Bitmap.createScaledBitmap(customIcon, markerSize, markerSize, false);
        }

        float alphaValue;
        if (focusedMarkerInfo == null) {
            alphaValue = 0.87f;
        } else {
            if (focusedMarker(point)) {
                alphaValue = 1.0f;
            } else {
                alphaValue = 0.4f;
            }
        }
        MarkerOptions options = new MarkerOptions();
        options.position(new LatLng(point.getCoordinates().getLat(), point.getCoordinates().getLng()))
//                .title(point.getHint())
                .alpha(alphaValue)
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

    public void requestPointsInBox() {
        if (gMap == null) {
            return;
        }
        mapViewModel.requestPointsInBox(gMap.getProjection().getVisibleRegion().latLngBounds);
    }

    public void backWasPressed() {
        if (pointEditWindow.getVisibility() == View.VISIBLE) {
            editPointModeEnd();
        } else if (pointInfoWindow.getVisibility() == View.VISIBLE) {
            ViewDriver.hideView(pointInfoWindow, R.anim.bottom_view_hide_animation, context);
            removeFocusFromMarker();
        }
    }

    public ConstraintLayout getPointInfoWindow() {
        return pointInfoWindow;
    }

    public ConstraintLayout getPointEditWindow() {
        return pointEditWindow;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (view == pointInfoWindow) {
            return handleInfoWindowMotion(view, motionEvent);
        }

        if (view == pointEditWindow) {
            return handleEditWindowMotion(view, motionEvent);
        }

        return false;
    }

    private boolean handleInfoWindowMotion(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                infoWindowStartMotionY = motionEvent.getRawY();
                infoWindowCorrectionY = view.getY() - infoWindowStartMotionY;
                break;
            case MotionEvent.ACTION_MOVE:
                infoWindowEndMotionY = motionEvent.getRawY();
                if (infoWindowStartMotionY > infoWindowEndMotionY) {
                    break;
                }
                view.setY(infoWindowEndMotionY + infoWindowCorrectionY);
                break;
            case MotionEvent.ACTION_UP:
                if (infoWindowEndMotionY != 0 && infoWindowEndMotionY - infoWindowStartMotionY > 300) {
                    setAnimationForSwipedViewHide(view, infoWindowStartMotionY, infoWindowCorrectionY);
                    removeFocusFromMarker();
                    break;
                } else {
                    view.animate().y(infoWindowCorrectionY + infoWindowStartMotionY).setDuration(200);
                    break;
                }
        }
        return false;
    }

    private boolean handleEditWindowMotion(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float editWindowStartMotionY = motionEvent.getRawY();
                editWindowCorrectionY = view.getY() - editWindowStartMotionY;
                break;
            case MotionEvent.ACTION_MOVE:
                editWindowEndMotionY = motionEvent.getRawY();
                if (editWindowEndMotionY + editWindowCorrectionY < pointEditTopLimit || editWindowEndMotionY + editWindowCorrectionY > pointEditBottomLimit) {
                    break;
                }
                view.setY(editWindowEndMotionY + editWindowCorrectionY);
                break;
            case MotionEvent.ACTION_UP:
                float bottomDiff = editWindowEndMotionY + editWindowCorrectionY - pointEditBottomLimit;
                if (bottomDiff > -200) {
                    view.animate().y(pointEditBottomLimit).setDuration(200);
                    break;
                }

                float topDiff = editWindowEndMotionY + editWindowCorrectionY - pointEditTopLimit;
                if (topDiff < 200) {
                    view.animate().y(pointEditTopLimit).setDuration(200);
                    break;
                }
                break;
        }
        return false;
    }

    private void setAnimationForSwipedViewHide(View view, float start, float tempY) {
        Animation animation = ViewDriver.hideView(view, R.anim.bottom_view_hide_animation, context);
        if (animation == null) {
            return;
        }

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                view.setY(tempY + start);
            }

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }
}