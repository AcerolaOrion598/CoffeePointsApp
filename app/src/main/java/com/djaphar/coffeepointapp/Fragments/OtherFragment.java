package com.djaphar.coffeepointapp.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import com.djaphar.coffeepointapp.Activities.MainActivity;
import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.ViewDriver;
import com.djaphar.coffeepointapp.ViewModels.OtherViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class OtherFragment extends Fragment {

    private OtherViewModel otherViewModel;
    private MainActivity mainActivity;
    private TextView aboutAppTv, exitTv;
    private Context context;
    private Animation animation;
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
            logout();
        });

        aboutAppTv.setOnClickListener(lView -> {
            mainActivity.setActionBarTitle(getString(R.string.about_app));
            animation = ViewDriver.showView(aboutAppContainer, R.anim.full_screen_show_animation, context);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    otherContainer.setVisibility(View.INVISIBLE);
                    aboutAppContainer.setBackgroundColor(context.getResources().getColor(R.color.colorWhite87));
                }

                @Override
                public void onAnimationStart(Animation animation) { }

                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
        });

        exitTv.setOnClickListener(lView -> createDialog());
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.exit_dialog_title)
                .setMessage(R.string.exit_dialog_message)
                .setNegativeButton(R.string.dialog_negative_btn, (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(R.string.dialog_positive_btn, (dialogInterface, i) -> otherViewModel.logout())
                .show();
    }

    private void logout() {
        mainActivity.logout();
    }

    public void backWasPressed() {
        mainActivity.setActionBarTitle(getString(R.string.title_other));
        aboutAppContainer.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        otherContainer.setVisibility(View.VISIBLE);
        ViewDriver.hideView(aboutAppContainer, R.anim.full_screen_hide_animation, context);
    }

    public ConstraintLayout getAboutAppContainer() {
        return aboutAppContainer;
    }
}
