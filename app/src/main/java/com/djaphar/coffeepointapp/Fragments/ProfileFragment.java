package com.djaphar.coffeepointapp.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.djaphar.coffeepointapp.Activities.MainActivity;
import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.Adapters.ProductsRecyclerViewAdapter;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.Product;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.User;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.ViewDriver;
import com.djaphar.coffeepointapp.ViewModels.ProfileViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileFragment extends MyFragment implements View.OnTouchListener {

    private ProfileViewModel profileViewModel;
    private Button editUserNameBtn, editUserNameCancelBtn, editUserNameSaveBtn, addProductBtn, addProductCancelBtn, addProductSaveBtn;
    private ConstraintLayout editUserNameWindow, addProductWindow;
    private Context context;
    private Resources resources;
    private TextView userNameTv;
    private EditText editUserNameEd, addProductEd;
    private RecyclerView productsRecyclerView;
    private User user;
    private float editUserNameWindowCorrectionY, editUserNameWindowEndMotionY, editUserNameWindowStartMotionY,
            addProductWindowCorrectionY, addProductWindowEndMotionY, addProductWindowStartMotionY;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        editUserNameBtn = root.findViewById(R.id.edit_user_name_btn);
        editUserNameCancelBtn = root.findViewById(R.id.edit_user_name_cancel_btn);
        editUserNameSaveBtn = root.findViewById(R.id.edit_user_name_save_btn);
        addProductBtn = root.findViewById(R.id.add_product_btn);
        addProductCancelBtn = root.findViewById(R.id.add_product_cancel_btn);
        addProductSaveBtn = root.findViewById(R.id.add_product_save_btn);
        editUserNameWindow = root.findViewById(R.id.edit_user_name_window);
        addProductWindow = root.findViewById(R.id.add_product_window);
        userNameTv = root.findViewById(R.id.user_name_tv);
        editUserNameEd = root.findViewById(R.id.edit_user_name_ed);
        addProductEd = root.findViewById(R.id.add_product_ed);
        productsRecyclerView = root.findViewById(R.id.products_recycler_view);
        context = getContext();
        resources = getResources();
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setActionBarTitle(getString(R.string.title_profile));
        }
        return root;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            this.user = user;
            if (user.getName() == null) {
                userNameTv.setText("Вы не указали имя пользователя");
                return;
            }
            userNameTv.setText(user.getName());
        });

        profileViewModel.getUserProducts().observe(getViewLifecycleOwner(), products -> {
            productsRecyclerView.setAdapter(new ProductsRecyclerViewAdapter(products));
            productsRecyclerView.setNestedScrollingEnabled(false);
            productsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        });

        editUserNameBtn.setOnClickListener(lView -> {
            editUserNameEd.setText(user.getName());
            editUserNameWindow.setTranslationY(resources.getDimension(R.dimen.add_point_window_expanded_translation_y));
            toggleTopWindow(addProductWindow, addProductBtn, true);
            toggleTopWindow(editUserNameWindow, editUserNameBtn, false);
        });

        addProductBtn.setOnClickListener(lView -> {
            addProductWindow.setTranslationY(resources.getDimension(R.dimen.add_point_window_expanded_translation_y));
            toggleTopWindow(editUserNameWindow, editUserNameBtn, true);
            toggleTopWindow(addProductWindow, addProductBtn, false);
        });

        editUserNameSaveBtn.setOnClickListener(lView -> {
            user.setName(editUserNameEd.getText().toString());
            profileViewModel.requestUpdateUser(user);
            toggleTopWindow(editUserNameWindow, editUserNameBtn, true);
        });

        addProductSaveBtn.setOnClickListener(lView -> {
            profileViewModel.requestAddProduct(new Product("0", "Кофе", addProductEd.getText().toString(), "0"), user);
            toggleTopWindow(addProductWindow, addProductBtn, true);
        });

        editUserNameCancelBtn.setOnClickListener(lView -> toggleTopWindow(editUserNameWindow, editUserNameBtn, true));
        addProductCancelBtn.setOnClickListener(lView -> toggleTopWindow(addProductWindow, addProductBtn, true));
        editUserNameWindow.setOnTouchListener(this);
        addProductWindow.setOnTouchListener(this);
    }

    public void backWasPressed() {
        if (editUserNameWindow.getVisibility() == View.VISIBLE) {
            toggleTopWindow(editUserNameWindow, editUserNameBtn, true);
            return;
        }

        if (addProductWindow.getVisibility() == View.VISIBLE) {
            toggleTopWindow(addProductWindow, addProductBtn, true);
        }
    }

    private void toggleTopWindow(ConstraintLayout window, Button button, boolean enabled) {
        if (enabled) {
            ViewDriver.hideView(window, R.anim.top_view_hide_animation, context);
        } else {
            ViewDriver.showView(window, R.anim.top_view_show_animation, context);
        }
        button.setEnabled(enabled);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view == editUserNameWindow) {
            return handleEditUserNameWindowMotion(view, motionEvent);
        }

        if (view == addProductWindow) {
            return handleAddProductWindowMotion(view, motionEvent);
        }

        return false;
    }

    private boolean handleEditUserNameWindowMotion(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                editUserNameWindowStartMotionY = motionEvent.getRawY();
                editUserNameWindowCorrectionY = view.getY() - editUserNameWindowStartMotionY;
                break;
            case MotionEvent.ACTION_MOVE:
                editUserNameWindowEndMotionY = motionEvent.getRawY();
                if (editUserNameWindowStartMotionY < editUserNameWindowEndMotionY) {
                    break;
                }
                view.setY(editUserNameWindowEndMotionY + editUserNameWindowCorrectionY);
                break;
            case MotionEvent.ACTION_UP:
                if (editUserNameWindowEndMotionY != 0 && editUserNameWindowStartMotionY - editUserNameWindowEndMotionY > 200) {
                    ViewDriver.hideView(view, R.anim.top_view_hide_animation, context);
                    editUserNameBtn.setEnabled(true);
                    break;
                } else {
                    view.animate().y(editUserNameWindowCorrectionY + editUserNameWindowStartMotionY).setDuration(200);
                    break;
                }
        }
        return false;
    }

    private boolean handleAddProductWindowMotion(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                addProductWindowStartMotionY = motionEvent.getRawY();
                addProductWindowCorrectionY = view.getY() - addProductWindowStartMotionY;
                break;
            case MotionEvent.ACTION_MOVE:
                addProductWindowEndMotionY = motionEvent.getRawY();
                if (addProductWindowStartMotionY < addProductWindowEndMotionY) {
                    break;
                }
                view.setY(addProductWindowEndMotionY + addProductWindowCorrectionY);
                break;
            case MotionEvent.ACTION_UP:
                if (addProductWindowEndMotionY != 0 && addProductWindowStartMotionY - addProductWindowEndMotionY > 200) {
                    ViewDriver.hideView(view, R.anim.top_view_hide_animation, context);
                    addProductBtn.setEnabled(true);
                    break;
                } else {
                    view.animate().y(addProductWindowCorrectionY + addProductWindowStartMotionY).setDuration(200);
                    break;
                }
        }
        return false;
    }

    public ConstraintLayout getEditUserNameWindow() {
        return editUserNameWindow;
    }

    public ConstraintLayout getAddProductWindow() {
        return addProductWindow;
    }
}