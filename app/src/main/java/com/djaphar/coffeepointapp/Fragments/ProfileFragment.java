package com.djaphar.coffeepointapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.djaphar.coffeepointapp.Activities.MainActivity;
import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.User;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.ViewDriver;
import com.djaphar.coffeepointapp.ViewModels.ProfileViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

public class ProfileFragment extends MyFragment {

    private ProfileViewModel profileViewModel;
    private MainActivity mainActivity;
    private Button editProfileBtn, saveProfileBtn, cancelProfileBtn;
    private ConstraintLayout editProfileContainer, profileContainer;
    private Context context;
    private TextView userNameTv;
    private EditText userNameEd;
    private User user;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        editProfileBtn = root.findViewById(R.id.edit_profile_btn);
        saveProfileBtn = root.findViewById(R.id.save_profile_btn);
        cancelProfileBtn = root.findViewById(R.id.cancel_profile_btn);
        editProfileContainer = root.findViewById(R.id.edit_profile_container);
        profileContainer = root.findViewById(R.id.profile_container);
        userNameTv = root.findViewById(R.id.user_name_tv);
        userNameEd = root.findViewById(R.id.user_name_ed);
        context = getContext();
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setActionBarTitle(getString(R.string.title_profile));
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            this.user = user;
            userNameTv.setText(user.getName());
        });

        editProfileBtn.setOnClickListener(lView -> {
            userNameEd.setText(userNameTv.getText());
            mainActivity.setActionBarTitle(getString(R.string.title_profile_edit));
            ViewDriver.hideView(profileContainer, R.anim.top_view_hide_animation, context);
            ViewDriver.hideView(editProfileBtn, R.anim.bottom_view_hide_animation, context);
            ViewDriver.showView(editProfileContainer, R.anim.top_view_show_animation, context);
            ViewDriver.showView(saveProfileBtn, R.anim.bottom_view_show_animation, context);
            ViewDriver.showView(cancelProfileBtn, R.anim.bottom_view_show_animation, context);
        });

        saveProfileBtn.setOnClickListener(lView -> {
            user.setName(userNameEd.getText().toString());
            profileViewModel.requestUpdateUser(user);
            backWasPressed();
        });

        cancelProfileBtn.setOnClickListener(lView -> backWasPressed());
    }

    public void backWasPressed() {
        mainActivity.setActionBarTitle(getString(R.string.title_profile));
        ViewDriver.hideView(editProfileContainer, R.anim.top_view_hide_animation, context);
        ViewDriver.hideView(saveProfileBtn, R.anim.bottom_view_hide_animation, context);
        ViewDriver.hideView(cancelProfileBtn, R.anim.bottom_view_hide_animation, context);
        ViewDriver.showView(profileContainer, R.anim.top_view_show_animation, context);
        ViewDriver.showView(editProfileBtn, R.anim.bottom_view_show_animation, context);
    }

    public ConstraintLayout getEditProfileContainer() {
        return editProfileContainer;
    }
}