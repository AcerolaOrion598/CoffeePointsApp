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
    private TextView userNameTv, userAboutTv;
    private EditText userNameEd, userAboutEd;
    private Button editProfileBtn, saveProfileBtn, cancelProfileBtn;
    private ConstraintLayout editProfileContainer, profileContainer;
    private Context context;
    private String userName, userAbout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        userNameTv = root.findViewById(R.id.user_name_tv);
        userAboutTv = root.findViewById(R.id.user_about_tv);
        userNameEd = root.findViewById(R.id.user_name_ed);
        userAboutEd = root.findViewById(R.id.user_about_ed);
        editProfileBtn = root.findViewById(R.id.edit_profile_btn);
        saveProfileBtn = root.findViewById(R.id.save_profile_btn);
        cancelProfileBtn = root.findViewById(R.id.cancel_profile_btn);
        editProfileContainer = root.findViewById(R.id.edit_profile_container);
        profileContainer = root.findViewById(R.id.profile_container);
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
            if (user == null) {
                return;
            }
            userName = user.getName();
            userAbout = user.getAbout();
            userNameTv.setText(userName);
            userAboutTv.setText(userAbout);
        });

        editProfileBtn.setOnClickListener(lView -> {
            mainActivity.setActionBarTitle(getString(R.string.title_profile_edit));
            userNameEd.setText(userName);
            userAboutEd.setText(userAbout);
            ViewDriver.hideView(profileContainer, R.anim.top_view_hide_animation, context);
            ViewDriver.hideView(editProfileBtn, R.anim.bottom_view_hide_animation, context);
            ViewDriver.showView(editProfileContainer, R.anim.top_view_show_animation, context);
            ViewDriver.showView(saveProfileBtn, R.anim.bottom_view_show_animation, context);
            ViewDriver.showView(cancelProfileBtn, R.anim.bottom_view_show_animation, context);
        });

        saveProfileBtn.setOnClickListener(lView -> { });

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