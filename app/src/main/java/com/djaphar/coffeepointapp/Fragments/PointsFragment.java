package com.djaphar.coffeepointapp.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.widget.TextView;

import com.djaphar.coffeepointapp.Activities.MainActivity;
import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.Adapters.PointProductsRecyclerViewAdapter;
import com.djaphar.coffeepointapp.SupportClasses.Adapters.PointsRecyclerViewAdapter;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.Point;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.PointUpdateModel;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.Product;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.User;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.PointsChangeChecker;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.ViewDriver;
import com.djaphar.coffeepointapp.ViewModels.PointsViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

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
    private ConstraintLayout singlePointInfoContainer, addPointWindow, editPointWindow;
    private TextView singlePointNameTv, singlePointRatingTv;
    private EditText pointEditNameFormEd, newPointPhoneNumberEd;
    private Button pointEditBtn, singlePointInfoCancelBtn, addPointCancelBtn, addPointSaveBtn, editPointSaveBtn, editPointCancelBtn;
    private ImageButton addPointBtn;
    private Resources resources;
    private User user;
    private ArrayList<Point> visiblePoints = new ArrayList<>(), invisiblePoints = new ArrayList<>();
    private String checkedPointId, checkedPointName;
    private HashMap<String, String> authHeaderMap = new HashMap<>();
    private float addWindowEndMotionY, addWindowCorrectionY, addWindowStartMotionY, editWindowEndMotionY, editWindowCorrectionY, editWindowStartMotionY;

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
        editPointWindow = root.findViewById(R.id.edit_point_window);
        singlePointNameTv = root.findViewById(R.id.single_point_name_tv);
        singlePointRatingTv = root.findViewById(R.id.single_point_rating_tv);
        pointEditNameFormEd = root.findViewById(R.id.point_edit_name_form_ed);
        pointEditBtn = root.findViewById(R.id.point_edit_btn);
        singlePointInfoCancelBtn = root.findViewById(R.id.single_point_info_cancel_btn);
        addPointCancelBtn = root.findViewById(R.id.add_point_cancel_btn);
        addPointSaveBtn = root.findViewById(R.id.add_point_save_btn);
        editPointSaveBtn = root.findViewById(R.id.edit_point_save_btn);
        editPointCancelBtn = root.findViewById(R.id.edit_point_cancel_btn);
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
            authHeaderMap.put(getString(R.string.authorization_header), user.getToken());
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
                    mainActivity, this);
            PointsRecyclerViewAdapter invisibleAdapter = new PointsRecyclerViewAdapter(invisiblePoints, nullPointString, singlePointInfoContainer,
                    mainActivity,this);
            visiblePointsRecyclerView.setAdapter(visibleAdapter);
            invisiblePointsRecyclerView.setAdapter(invisibleAdapter);
            visiblePointsRecyclerView.setNestedScrollingEnabled(false);
            invisiblePointsRecyclerView.setNestedScrollingEnabled(false);
            visiblePointsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            invisiblePointsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        });

        pointsViewModel.getSinglePoint().observe(getViewLifecycleOwner(), point -> {
            if (point == null) {
                return;
            }
            String name = point.getName();
            if (name == null || name.equals("")) {
                name = point.getPhoneNumber();
            } else {
                checkedPointName = name;
            }
            singlePointNameTv.setText(name);
            setPointProductRecyclerView(point.getProductList());
            setSinglePointRatingTv(point.getRating());
        });

        pointEditNameFormEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                editPointSaveBtn.setEnabled(!editable.toString().equals(""));
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
        });

        newPointPhoneNumberEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                addPointSaveBtn.setEnabled(!editable.toString().equals(""));
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
        });

        singlePointInfoCancelBtn.setOnClickListener(lView -> backWasPressed());

        addPointBtn.setOnClickListener(lView -> {
            addPointWindow.setTranslationY(resources.getDimension(R.dimen.add_point_window_expanded_translation_y));
            newPointPhoneNumberEd.setText("");
            ViewDriver.hideView(addPointBtn, R.anim.bottom_view_hide_animation, context);
            ViewDriver.showView(addPointWindow, R.anim.top_view_show_animation, context);
        });

        addPointCancelBtn.setOnClickListener(lView -> {
            ViewDriver.hideView(addPointWindow, R.anim.top_view_hide_animation, context);
            ViewDriver.showView(addPointBtn, R.anim.bottom_view_show_animation, context);
        });

        addPointSaveBtn.setOnClickListener(lView -> {
            pointsViewModel.requestBindCourier(newPointPhoneNumberEd.getText().toString(), authHeaderMap);
            ViewDriver.hideView(addPointWindow, R.anim.top_view_hide_animation, context);
            ViewDriver.showView(addPointBtn, R.anim.bottom_view_show_animation, context);
        });

        pointEditBtn.setOnClickListener(lView -> {
            editPointWindow.setTranslationY(resources.getDimension(R.dimen.add_point_window_expanded_translation_y));
            pointEditNameFormEd.setText(checkedPointName);
            toggleEditWindow(false);
        });

        editPointCancelBtn.setOnClickListener(lView -> toggleEditWindow(true));

        editPointSaveBtn.setOnClickListener(lView -> {
            pointsViewModel.requestUpdatePoint(checkedPointId, authHeaderMap, new PointUpdateModel(pointEditNameFormEd.getText().toString()));
            toggleEditWindow(true);
        });

        addPointWindow.setOnTouchListener(this);
        editPointWindow.setOnTouchListener(this);
    }

    private void toggleEditWindow(boolean btnState) {
        if (btnState) {
            ViewDriver.hideView(editPointWindow, R.anim.top_view_hide_animation, context);
        } else {
            ViewDriver.showView(editPointWindow, R.anim.top_view_show_animation, context);
        }
        pointEditBtn.setEnabled(btnState);
        singlePointInfoCancelBtn.setEnabled(btnState);
    }

    public void setSinglePointNameTv(String name) {
        singlePointNameTv.setText(name);
    }

    public void setCheckedPointName(String checkedPointName) {
        this.checkedPointName = checkedPointName;
    }

    public void setCheckedPointId(String checkedPointId) {
        this.checkedPointId = checkedPointId;
    }

    public void setSinglePointRatingTv(Float rating) {
        if (rating == null) {
            singlePointRatingTv.setTextColor(resources.getColor(R.color.colorBlack30));
            singlePointRatingTv.setText(R.string.rating_is_null_text);
            return;
        }
        singlePointRatingTv.setTextColor(resources.getColor(R.color.colorBlack60));
        singlePointRatingTv.setText(String.format(Locale.US, "%.2f", rating));
    }

    public void setPointProductRecyclerView(ArrayList<Product> products) {
        PointProductsRecyclerViewAdapter adapter = new PointProductsRecyclerViewAdapter(products, getString(R.string.point_product_null));
        pointProductsRecyclerView.setAdapter(adapter);
        pointProductsRecyclerView.setNestedScrollingEnabled(false);
        pointProductsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public void createProductDeleteDialog(String pointId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.delete_dialog_title)
                .setMessage(R.string.delete_point_dialog_message)
                .setNegativeButton(R.string.dialog_negative_btn, (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(R.string.dialog_positive_btn, (dialogInterface, i) ->
                        pointsViewModel.requestDeletePoint(pointId, authHeaderMap))
                .show();
    }

    public void requestMyPoints() {
        if (user == null) {
            return;
        }
        pointsViewModel.requestMyPoints(authHeaderMap);
    }

    public void requestSinglePoint() {
        pointsViewModel.requestSinglePoint(checkedPointId, authHeaderMap);
    }

    public boolean everythingIsClosed() {
        return !(singlePointInfoContainer.getVisibility() == View.VISIBLE) && !(addPointWindow.getVisibility() == View.VISIBLE);
    }

    public void backWasPressed() {
        if (editPointWindow.getVisibility() == View.VISIBLE) {
            toggleEditWindow(true);
            return;
        }

        if (singlePointInfoContainer.getVisibility() == View.VISIBLE) {
            pointsViewModel.requestMyPoints(authHeaderMap);
            singlePointInfoContainer.setClickable(false);
            mainActivity.setActionBarTitle(getString(R.string.title_points));
            ViewDriver.hideView(singlePointInfoContainer, R.anim.hide_right_animation, context);
            checkedPointId = null;
            return;
        }

        if (addPointWindow.getVisibility() == View.VISIBLE) {
            ViewDriver.hideView(addPointWindow, R.anim.top_view_hide_animation, context);
            ViewDriver.showView(addPointBtn, R.anim.bottom_view_show_animation, context);
        }
    }

    public String getCheckedPointId() {
        return checkedPointId;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (view == addPointWindow) {
            return handleAddPointWindowMotion(view, motionEvent);
        }

        if (view == editPointWindow) {
            return handleEditWindowMotion(view, motionEvent);
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

    private boolean handleEditWindowMotion(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                editWindowStartMotionY = motionEvent.getRawY();
                editWindowCorrectionY = view.getY() - editWindowStartMotionY;
                break;
            case MotionEvent.ACTION_MOVE:
                editWindowEndMotionY = motionEvent.getRawY();
                if (editWindowStartMotionY < editWindowEndMotionY) {
                    break;
                }
                view.setY(editWindowEndMotionY + editWindowCorrectionY);
                break;
            case MotionEvent.ACTION_UP:
                if (editWindowEndMotionY != 0 && editWindowStartMotionY - editWindowEndMotionY > 200) {
                    toggleEditWindow(true);
                } else {
                    view.animate().y(editWindowStartMotionY + editWindowCorrectionY).setDuration(200);
                }
                break;
        }
        return false;
    }
}