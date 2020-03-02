package com.djaphar.coffeepointapp.Fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.djaphar.coffeepointapp.Activities.MainActivity;
import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.Adapters.PointsRecyclerViewAdapter;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.Point;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.ViewDriver;
import com.djaphar.coffeepointapp.ViewModels.MainViewModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PointsFragment extends Fragment {

    private MainViewModel mainViewModel;
    private RecyclerView recyclerView;
    private Context context;
    private Resources resources;
    private RelativeLayout pointListLayout;
    private ConstraintLayout pointEditLayout;
    private ArrayList<Point> points;
    private MainActivity mainActivity;
    private EditText pointNameFormEd, pointAboutFormEd;
    private TextView pointActiveSwitchFormTv;
    private SwitchCompat pointActiveSwitchForm;
    private String statusTrueText, statusFalseText;
    private int statusTrueColor, statusFalseColor;
    private Button pointEditSaveButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        View root = inflater.inflate(R.layout.fragment_points, container, false);
        recyclerView = root.findViewById(R.id.points_recycler_view);
        pointListLayout = root.findViewById(R.id.points_list_layout);
        pointEditLayout = root.findViewById(R.id.point_edit_layout);
        pointNameFormEd = root.findViewById(R.id.point_name_form_ed);
        pointAboutFormEd = root.findViewById(R.id.point_about_form_ed);
        pointActiveSwitchFormTv = root.findViewById(R.id.point_active_switch_form_tv);
        pointActiveSwitchForm = root.findViewById(R.id.point_active_switch_form);
        pointEditSaveButton = root.findViewById(R.id.point_edit_save_btn);
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setActionBarTitle(getString(R.string.title_points));
        }
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


        mainViewModel.getPoints().observe(getViewLifecycleOwner(), mPoints -> {
            points = mPoints;
            PointsRecyclerViewAdapter adapter = new PointsRecyclerViewAdapter(points, pointListLayout, pointEditLayout,
                                        mainActivity, pointNameFormEd, pointAboutFormEd, pointActiveSwitchFormTv, pointActiveSwitchForm);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        });

        pointActiveSwitchForm.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                ViewDriver.setStatusTvOptions(pointActiveSwitchFormTv, statusTrueText, statusTrueColor);
            } else {
                ViewDriver.setStatusTvOptions(pointActiveSwitchFormTv, statusFalseText, statusFalseColor);
            }
        });

        pointNameFormEd.addTextChangedListener(new TextWatcher() {
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

        pointEditSaveButton.setOnClickListener(view1 -> { });

    }

    public void backWasPressed() {
        mainActivity.setActionBarTitle(getString(R.string.title_points));
        pointEditLayout.setBackgroundColor(resources.getColor(R.color.colorWhite));
        pointListLayout.setVisibility(View.VISIBLE);
        ViewDriver.hideView(pointEditLayout, R.anim.full_screen_hide_animation, context);
    }

    public ConstraintLayout getPointEditLayout() {
        return pointEditLayout;
    }
}