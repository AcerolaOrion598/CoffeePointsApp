package com.djaphar.coffeepointapp.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.djaphar.coffeepointapp.Activities.MainActivity;
import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.Adapters.AddProductSpinnerAdapter;
import com.djaphar.coffeepointapp.SupportClasses.Adapters.ProductsRecyclerViewAdapter;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.Product;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.User;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.ViewDriver;
import com.djaphar.coffeepointapp.ViewModels.ProfileViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

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
    private TextView userNameTv, userRatingTv;
    private EditText editUserNameEd, addProductEd;
    private RecyclerView productsRecyclerView;
    private Spinner addProductSpinner;
    private User user;
    private HashMap<String, String> authHeaderMap = new HashMap<>();
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
        userRatingTv = root.findViewById(R.id.user_rating_tv);
        editUserNameEd = root.findViewById(R.id.edit_user_name_ed);
        addProductEd = root.findViewById(R.id.add_product_ed);
        productsRecyclerView = root.findViewById(R.id.products_recycler_view);
        addProductSpinner = root.findViewById(R.id.add_product_spinner);
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
            authHeaderMap.put(getString(R.string.authorization_header), user.getToken());

            Float rating = user.getAvgRating();
            if (rating == null) {
                userRatingTv.setTextColor(resources.getColor(R.color.colorBlack30));
                userRatingTv.setText(R.string.rating_is_null_text);
            } else {
                userRatingTv.setTextColor(resources.getColor(R.color.colorBlack60));
                userRatingTv.setText(String.format(Locale.US, "%.2f", rating));
            }

            String name = user.getName();
            if (name == null) {
                userNameTv.setTextColor(resources.getColor(R.color.colorBlack30));
                userNameTv.setText(R.string.some_string_is_null_text);
                return;
            }
            userNameTv.setTextColor(resources.getColor(R.color.colorBlack60));
            userNameTv.setText(name);
        });

        profileViewModel.getUserProducts().observe(getViewLifecycleOwner(), products -> {
            productsRecyclerView.setAdapter(new ProductsRecyclerViewAdapter(products, this));
            productsRecyclerView.setNestedScrollingEnabled(false);
            productsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        });

        ArrayList<String> productTypes = new ArrayList<>();
        productTypes.add("Кофе");
        productTypes.add("Мороженое");
        productTypes.add("Другое");
        AddProductSpinnerAdapter adapter = new AddProductSpinnerAdapter(context, productTypes);
        addProductSpinner.setAdapter(adapter);

        editUserNameBtn.setOnClickListener(lView -> {
            editUserNameEd.setText(user.getName());
            editUserNameWindow.setTranslationY(resources.getDimension(R.dimen.add_point_window_expanded_translation_y));
            toggleTopWindow(addProductWindow, addProductBtn, true);
            toggleTopWindow(editUserNameWindow, editUserNameBtn, false);
        });

        addProductBtn.setOnClickListener(lView -> {
            addProductEd.setText("");
            addProductWindow.setTranslationY(resources.getDimension(R.dimen.add_point_window_expanded_translation_y));
            toggleTopWindow(editUserNameWindow, editUserNameBtn, true);
            toggleTopWindow(addProductWindow, addProductBtn, false);
        });

        editUserNameSaveBtn.setOnClickListener(lView -> {
            user.setName(editUserNameEd.getText().toString());
            profileViewModel.requestUpdateUser(user, authHeaderMap);
            toggleTopWindow(editUserNameWindow, editUserNameBtn, true);
        });

        addProductSaveBtn.setOnClickListener(lView -> {
            profileViewModel.requestAddProduct(new Product("0", addProductSpinner.getSelectedItem().toString(),
                    addProductEd.getText().toString(), "0"), authHeaderMap);
            toggleTopWindow(addProductWindow, addProductBtn, true);
        });

        editUserNameCancelBtn.setOnClickListener(lView -> toggleTopWindow(editUserNameWindow, editUserNameBtn, true));
        addProductCancelBtn.setOnClickListener(lView -> toggleTopWindow(addProductWindow, addProductBtn, true));
        editUserNameWindow.setOnTouchListener(this);
        addProductWindow.setOnTouchListener(this);

        editUserNameEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                editUserNameSaveBtn.setEnabled(!editable.toString().equals(""));
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
        });

        addProductEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                addProductSaveBtn.setEnabled(!editable.toString().equals(""));
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
        });
    }

    public boolean everythingIsClosed() {
        return !(editUserNameWindow.getVisibility() == View.VISIBLE) && !(addProductWindow.getVisibility() == View.VISIBLE);
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

    public void createDeleteProductDialog(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.delete_dialog_title)
                .setMessage(R.string.delete_product_dialog_message)
                .setNegativeButton(R.string.dialog_negative_btn, (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(R.string.dialog_positive_btn, (dialogInterface, i) -> profileViewModel.requestDeleteProduct(id, authHeaderMap))
                .show();
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
}