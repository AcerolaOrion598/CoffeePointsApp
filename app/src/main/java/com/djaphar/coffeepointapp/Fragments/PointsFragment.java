package com.djaphar.coffeepointapp.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.djaphar.coffeepointapp.Activities.MainActivity;
import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.Adapters.PointProductsRecyclerViewAdapter;
import com.djaphar.coffeepointapp.SupportClasses.Adapters.PointsRecyclerViewAdapter;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.BindCourierModel;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.Point;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.Product;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.User;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.PointsChangeChecker;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.ViewDriver;
import com.djaphar.coffeepointapp.ViewModels.PointsViewModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PointsFragment extends MyFragment implements View.OnTouchListener {

    private PointsChangeChecker pointsChangeChecker;
    private PointsViewModel pointsViewModel;
    private MainActivity mainActivity;
    private Context context;
    private RecyclerView visiblePointsRecyclerView, invisiblePointsRecyclerView, pointProductsRecyclerView;
    private ConstraintLayout singlePointInfoContainer, addPointWindow;
    private EditText pointEditNameFormEd, pointEditAboutFormEd, newPointPhoneNumberEd;
    private Button pointEditSaveButton, singlePointInfoCancelBtn, addPointCancelBtn, addPointSaveBtn;
    private ImageButton addPointBtn;
    private Resources resources;
    private User user;
    private ArrayList<Point> visiblePoints = new ArrayList<>(), invisiblePoints = new ArrayList<>();
    private float addWindowEndMotionY, addWindowCorrectionY, addWindowStartMotionY;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pointsChangeChecker = new PointsChangeChecker(new Handler(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        pointsChangeChecker.startPointsChangeCheck();
    }

    @Override
    public void onPause() {
        super.onPause();
        pointsChangeChecker.stopPointsChangeCheck();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pointsViewModel = new ViewModelProvider(this).get(PointsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_points, container, false);
        visiblePointsRecyclerView = root.findViewById(R.id.visible_points_recycler_view);
        invisiblePointsRecyclerView = root.findViewById(R.id.invisible_points_recycler_view);
        pointProductsRecyclerView = root.findViewById(R.id.point_products_recycler_view);
        singlePointInfoContainer = root.findViewById(R.id.single_point_info_container);
        addPointWindow = root.findViewById(R.id.add_point_window);
        pointEditNameFormEd = root.findViewById(R.id.point_edit_name_form_ed);
        pointEditAboutFormEd = root.findViewById(R.id.point_edit_about_form_ed);
        pointEditSaveButton = root.findViewById(R.id.point_edit_save_btn);
        singlePointInfoCancelBtn = root.findViewById(R.id.single_point_info_cancel_btn);
        addPointCancelBtn = root.findViewById(R.id.add_point_cancel_btn);
        addPointSaveBtn = root.findViewById(R.id.add_point_save_btn);
        newPointPhoneNumberEd = root.findViewById(R.id.new_point_phone_number_ed);
        addPointBtn = root.findViewById(R.id.add_point_btn);
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setActionBarTitle(getString(R.string.title_points));
        }
        return root;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        resources = getResources();

        pointsViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                return;
            }
            this.user = user;
            requestMyPoints();
        });

        pointsViewModel.getPoints().observe(getViewLifecycleOwner(), points -> {
            visiblePoints.clear();
            invisiblePoints.clear();

            for (Point point : points) {
                if (point.getActive()) {
                    visiblePoints.add(point);
                } else {
                    invisiblePoints.add(point);
                }
            }

            String nullPointString = getString(R.string.point_null);
            PointsRecyclerViewAdapter visibleAdapter = new PointsRecyclerViewAdapter(visiblePoints, nullPointString, singlePointInfoContainer,
                    mainActivity, this, pointEditNameFormEd, pointEditAboutFormEd);
            PointsRecyclerViewAdapter invisibleAdapter = new PointsRecyclerViewAdapter(invisiblePoints, nullPointString, singlePointInfoContainer,
                    mainActivity,this, pointEditNameFormEd, pointEditAboutFormEd);
            visiblePointsRecyclerView.setAdapter(visibleAdapter);
            invisiblePointsRecyclerView.setAdapter(invisibleAdapter);
            visiblePointsRecyclerView.setNestedScrollingEnabled(false);
            invisiblePointsRecyclerView.setNestedScrollingEnabled(false);
            visiblePointsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            invisiblePointsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        });

        pointEditNameFormEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) {
                    pointEditSaveButton.setEnabled(false);
                } else {
                    pointEditSaveButton.setEnabled(true);
                }
            }
        });

        newPointPhoneNumberEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) {
                    addPointSaveBtn.setEnabled(false);
                } else {
                    addPointSaveBtn.setEnabled(true);
                }
            }
        });

        pointEditSaveButton.setOnClickListener(lView -> {});
        singlePointInfoCancelBtn.setOnClickListener(lView -> backWasPressed());
        addPointBtn.setOnClickListener(lView -> {
            addPointWindow.setTranslationY(resources.getDimension(R.dimen.add_point_window_expanded_translation_y));
//            newPointPhoneNumberEd.setText("");
            ViewDriver.hideView(addPointBtn, R.anim.bottom_view_hide_animation, context);
            ViewDriver.showView(addPointWindow, R.anim.top_view_show_animation, context);
        });
        addPointCancelBtn.setOnClickListener(lView -> {
            ViewDriver.hideView(addPointWindow, R.anim.top_view_hide_animation, context);
            ViewDriver.showView(addPointBtn, R.anim.bottom_view_show_animation, context);
        });
        addPointSaveBtn.setOnClickListener(lView -> {
            pointsViewModel.requestBindCourier(user.getToken(), new BindCourierModel(user.get_id(), newPointPhoneNumberEd.getText().toString()));
            ViewDriver.hideView(addPointWindow, R.anim.top_view_hide_animation, context);
            ViewDriver.showView(addPointBtn, R.anim.bottom_view_show_animation, context);
        });
        singlePointInfoContainer.setOnTouchListener(this);
        addPointWindow.setOnTouchListener(this);
    }

    public void requestMyPoints() {
        if (user == null) {
            return;
        }
        pointsViewModel.requestMyPoints(user.getToken());
    }

    public void setPointProductRecyclerView(ArrayList<Product> products) {
        PointProductsRecyclerViewAdapter adapter = new PointProductsRecyclerViewAdapter(products, getString(R.string.point_product_null));
        pointProductsRecyclerView.setAdapter(adapter);
        pointProductsRecyclerView.setNestedScrollingEnabled(false);
        pointProductsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public void backWasPressed() {
        if (singlePointInfoContainer.getVisibility() == View.VISIBLE) {
            pointsViewModel.requestMyPoints(user.getToken());
            singlePointInfoContainer.setClickable(false);
            mainActivity.setActionBarTitle(getString(R.string.title_points));
            ViewDriver.hideView(singlePointInfoContainer, R.anim.hide_right_animation, context);
        } else if (addPointWindow.getVisibility() == View.VISIBLE) {
            ViewDriver.hideView(addPointWindow, R.anim.top_view_hide_animation, context);
            ViewDriver.showView(addPointBtn, R.anim.bottom_view_show_animation, context);
        }
    }

    public ConstraintLayout getSinglePointInfoContainer() {
        return singlePointInfoContainer;
    }

    public ConstraintLayout getAddPointWindow() {
        return addPointWindow;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (view == addPointWindow) {
            return handleAddPointWindowMotion(view, motionEvent);
        }

        return false;
    }

    private boolean handleAddPointWindowMotion(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                addWindowStartMotionY = motionEvent.getRawY();
                addWindowCorrectionY = view.getY() - addWindowStartMotionY;
                break;
            case MotionEvent.ACTION_MOVE:
                addWindowEndMotionY = motionEvent.getRawY();
                if (addWindowStartMotionY < addWindowEndMotionY) {
                    break;
                }
                view.setY(addWindowEndMotionY + addWindowCorrectionY);
                break;
            case MotionEvent.ACTION_UP:
                if (addWindowEndMotionY != 0 && addWindowStartMotionY - addWindowEndMotionY > 200) {
                    ViewDriver.hideView(view, R.anim.top_view_hide_animation, context);
                    ViewDriver.showView(addPointBtn, R.anim.bottom_view_show_animation, context);
                    break;
                } else {
                    view.animate().y(addWindowStartMotionY + addWindowCorrectionY).setDuration(200);
                    break;
                }
        }
        return false;
    }
}