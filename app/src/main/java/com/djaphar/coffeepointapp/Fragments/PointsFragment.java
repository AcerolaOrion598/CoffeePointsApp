package com.djaphar.coffeepointapp.Fragments;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.djaphar.coffeepointapp.Activities.MainActivity;
import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.Adapters.PointsRecyclerViewAdapter;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.ViewDriver;
import com.djaphar.coffeepointapp.ViewModels.MainViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PointsFragment extends MyFragment implements View.OnTouchListener {

    private MainViewModel mainViewModel;
    private MainActivity mainActivity;
    private Context context;
    private RecyclerView recyclerView;
    private RelativeLayout pointListLayout;
    private ConstraintLayout pointEditLayout;
    private EditText pointNameFormEd, pointAboutFormEd;
    private TextView pointActiveSwitchFormTv;
    private SwitchCompat pointActiveSwitchForm;
    private String statusTrueText, statusFalseText;
    private Button pointEditSaveButton, pointEditBackButton;
//    private ArrayList<Point> points;
    private int statusTrueColor, statusFalseColor;
    private float pointEditLayoutCorrectionX, pointEditLayoutEndMotionX, pointEditLayoutStartLimit;

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
        pointEditBackButton = root.findViewById(R.id.point_edit_back_btn);
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
        Resources resources = getResources();
        statusTrueColor = resources.getColor(R.color.colorGreen60);
        statusFalseColor = resources.getColor(R.color.colorRed60);
        statusTrueText = getString(R.string.point_status_true);
        statusFalseText = getString(R.string.point_status_false);
        pointEditLayoutStartLimit = pointEditLayout.getX();

        mainViewModel.getPoints().observe(getViewLifecycleOwner(), mPoints -> {
//            points = mPoints;
            PointsRecyclerViewAdapter adapter = new PointsRecyclerViewAdapter(mPoints, pointListLayout, pointEditLayout,
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

        pointEditSaveButton.setOnClickListener(lView -> {});
        pointEditBackButton.setOnClickListener(lView -> backWasPressed());
        pointEditLayout.setOnTouchListener(this);
    }

    public void backWasPressed() {
        pointEditLayout.setClickable(false);
        mainActivity.setActionBarTitle(getString(R.string.title_points));
        pointListLayout.setVisibility(View.VISIBLE);
        ViewDriver.hideView(pointEditLayout, R.anim.hide_right_animation, context);
    }

    public ConstraintLayout getPointEditLayout() {
        return pointEditLayout;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float pointEditLayoutStartMotionX = motionEvent.getRawX();
                pointEditLayoutCorrectionX = view.getX() - pointEditLayoutStartMotionX;
                break;
            case MotionEvent.ACTION_MOVE:
                pointEditLayoutEndMotionX = motionEvent.getRawX();
                pointListLayout.setVisibility(View.VISIBLE);
                if (pointEditLayoutEndMotionX + pointEditLayoutCorrectionX < pointEditLayoutStartLimit) {
                    break;
                }
                view.setX(pointEditLayoutEndMotionX + pointEditLayoutCorrectionX);
                break;
            case MotionEvent.ACTION_UP:
                float startDiff = pointEditLayoutEndMotionX + pointEditLayoutCorrectionX - pointEditLayoutStartLimit;
                if (startDiff > 300) {
                    view.setClickable(false);
                    mainActivity.setActionBarTitle(getString(R.string.title_points));
                    view.setX(pointEditLayoutEndMotionX + pointEditLayoutCorrectionX);
                    ViewDriver.hideView(view, R.anim.hide_right_animation, context);
                    break;
                }
                ViewPropertyAnimator animator = view.animate().x(pointEditLayoutStartLimit).setDuration(200);
                animator.setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        pointListLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationStart(Animator animator) { }

                    @Override
                    public void onAnimationCancel(Animator animator) { }

                    @Override
                    public void onAnimationRepeat(Animator animator) { }
                });
                break;
        }
        return false;
    }
}