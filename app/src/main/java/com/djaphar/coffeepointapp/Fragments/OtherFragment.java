package com.djaphar.coffeepointapp.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.djaphar.coffeepointapp.Activities.MainActivity;
import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.ViewDriver;
import com.djaphar.coffeepointapp.ViewModels.OtherViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

public class OtherFragment extends MyFragment {

    private OtherViewModel otherViewModel;
    private MainActivity mainActivity;
    private TextView aboutAppTv, exitTv;
    private Context context;
    private Button backProfileBtn;
    private ConstraintLayout aboutAppContainer, otherContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        otherViewModel = new ViewModelProvider(this).get(OtherViewModel.class);
        View root = inflater.inflate(R.layout.fragment_other, container, false);
        aboutAppTv = root.findViewById(R.id.about_app_tv);
        exitTv = root.findViewById(R.id.exit_tv);
        aboutAppContainer = root.findViewById(R.id.about_app_container);
        otherContainer = root.findViewById(R.id.other_container);
        backProfileBtn = root.findViewById(R.id.back_profile_button);
        backProfileBtn.setStateListAnimator(null);
        context = getContext();
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setActionBarTitle(getString(R.string.title_other));
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        otherViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                return;
            }
            mainActivity.logout();
        });

        aboutAppTv.setOnClickListener(lView -> {
            mainActivity.setActionBarTitle(getString(R.string.about_app));
            ViewDriver.hideView(otherContainer, R.anim.top_view_hide_animation, context);
            ViewDriver.showView(aboutAppContainer, R.anim.top_view_show_animation, context);
            ViewDriver.showView(backProfileBtn, R.anim.bottom_view_show_animation, context);
        });

        backProfileBtn.setOnClickListener(lView -> backWasPressed());

        exitTv.setOnClickListener(lView -> createLogoutDialog());
    }

    private void createLogoutDialog() {
        new AlertDialog.Builder(context).setTitle(R.string.exit_dialog_title)
                .setMessage(R.string.exit_dialog_message)
                .setNegativeButton(R.string.dialog_negative_btn, (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(R.string.dialog_positive_btn, (dialogInterface, i) -> otherViewModel.logout())
                .show();
    }

    public boolean everythingIsClosed() {
        return !(aboutAppContainer.getVisibility() == View.VISIBLE);
    }

    public void backWasPressed() {
        mainActivity.setActionBarTitle(getString(R.string.title_other));
        ViewDriver.hideView(aboutAppContainer, R.anim.top_view_hide_animation, context);
        ViewDriver.hideView(backProfileBtn, R.anim.bottom_view_hide_animation, context);
        ViewDriver.showView(otherContainer, R.anim.top_view_show_animation, context);
    }
}
