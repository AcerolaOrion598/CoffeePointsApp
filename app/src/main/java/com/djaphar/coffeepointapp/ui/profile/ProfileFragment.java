package com.djaphar.coffeepointapp.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.djaphar.coffeepointapp.MainActivity;
import com.djaphar.coffeepointapp.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private MainActivity mainActivity;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setActionBarTitle(getString(R.string.title_profile));
        }
        return root;
    }
}