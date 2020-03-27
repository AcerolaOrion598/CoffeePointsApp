package com.djaphar.coffeepointapp.Fragments;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.djaphar.coffeepointapp.Activities.MainActivity;
import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.Adapters.PointsRecyclerViewAdapter;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.ViewDriver;
import com.djaphar.coffeepointapp.ViewModels.MainViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private ConstraintLayout pointEditLayout, addPointWindow;
    private EditText pointNameFormEd, pointAboutFormEd;
    private Button pointEditSaveButton, pointEditBackButton, addPointCancelBtn;
    private ImageButton addPointBtn;
    private float pointEditLayoutCorrectionX, pointEditLayoutEndMotionX, pointEditLayoutStartLimit, addWindowEndMotionY,
            addPointTopLimit, addPointBottomLimit, addWindowCorrectionY;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        View root = inflater.inflate(R.layout.fragment_points, container, false);
        recyclerView = root.findViewById(R.id.points_recycler_view);
        pointListLayout = root.findViewById(R.id.points_list_layout);
        pointEditLayout = root.findViewById(R.id.point_edit_layout);
        addPointWindow = root.findViewById(R.id.add_point_window);
        pointNameFormEd = root.findViewById(R.id.point_name_form_ed);
        pointAboutFormEd = root.findViewById(R.id.point_about_form_ed);
        pointEditSaveButton = root.findViewById(R.id.point_edit_save_btn);
        pointEditBackButton = root.findViewById(R.id.point_edit_back_btn);
        addPointCancelBtn = root.findViewById(R.id.add_point_cancel_btn);
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
        pointEditLayoutStartLimit = pointEditLayout.getX();

        mainViewModel.getPoints().observe(getViewLifecycleOwner(), mPoints -> {
            PointsRecyclerViewAdapter adapter = new PointsRecyclerViewAdapter(mPoints, pointListLayout, pointEditLayout,
                    mainActivity, pointNameFormEd, pointAboutFormEd);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
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

        addPointWindow.setTranslationY(getResources().getDimension(R.dimen.add_point_window_translation_y));
        addPointTopLimit = addPointWindow.getY();
        addPointWindow.setTranslationY(getResources().getDimension(R.dimen.add_point_window_expanded_translation_y));
        addPointBottomLimit = addPointWindow.getY();

        pointEditSaveButton.setOnClickListener(lView -> {});
        pointEditBackButton.setOnClickListener(lView -> backWasPressed());
        addPointBtn.setOnClickListener(lView -> {
            addPointWindow.setTranslationY(getResources().getDimension(R.dimen.add_point_window_translation_y));
            ViewDriver.hideView(addPointBtn, R.anim.bottom_view_hide_animation, context);
            ViewDriver.showView(addPointWindow, R.anim.top_view_show_animation, context);
        });
        addPointCancelBtn.setOnClickListener(lView -> {
            ViewDriver.hideView(addPointWindow, R.anim.top_view_hide_animation, context);
            ViewDriver.showView(addPointBtn, R.anim.bottom_view_show_animation, context);
        });
        pointEditLayout.setOnTouchListener(this);
        addPointWindow.setOnTouchListener(this);
    }

    public void backWasPressed() {
        if (pointEditLayout.getVisibility() == View.VISIBLE) {
            pointEditLayout.setClickable(false);
            mainActivity.setActionBarTitle(getString(R.string.title_points));
            pointListLayout.setVisibility(View.VISIBLE);
            ViewDriver.hideView(pointEditLayout, R.anim.hide_right_animation, context);
        } else if (addPointWindow.getVisibility() == View.VISIBLE) {
            ViewDriver.hideView(addPointWindow, R.anim.top_view_hide_animation, context);
            ViewDriver.showView(addPointBtn, R.anim.bottom_view_show_animation, context);
        }
    }

    public ConstraintLayout getPointEditLayout() {
        return pointEditLayout;
    }

    public ConstraintLayout getAddPointWindow() {
        return addPointWindow;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (view == pointEditLayout) {
            return handlePointEditLayoutMotion(view, motionEvent);
        }

        if (view == addPointWindow) {
            return handleAddPointWindowMotion(view, motionEvent);
        }

        return false;
    }

    private boolean handlePointEditLayoutMotion(View view, MotionEvent motionEvent) {
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

    private boolean handleAddPointWindowMotion(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float addWindowStartMotionY = motionEvent.getRawY();
                addWindowCorrectionY = view.getY() - addWindowStartMotionY;
                break;
            case MotionEvent.ACTION_MOVE:
                addWindowEndMotionY = motionEvent.getRawY();
                if (addWindowEndMotionY + addWindowCorrectionY < addPointTopLimit || addWindowEndMotionY + addWindowCorrectionY > addPointBottomLimit) {
                    break;
                }
                view.setY(addWindowEndMotionY + addWindowCorrectionY);
                break;
            case MotionEvent.ACTION_UP:
                float bottomDiff = addWindowEndMotionY + addWindowCorrectionY - addPointBottomLimit;
                if (bottomDiff > -200) {
                    view.animate().y(addPointBottomLimit).setDuration(200);
                    break;
                }

                float topDiff = addWindowEndMotionY + addWindowCorrectionY - addPointTopLimit;
                if (topDiff < 200) {
                    view.animate().y(addPointTopLimit).setDuration(200);
                    break;
                }
                break;
        }
        return false;
    }
}