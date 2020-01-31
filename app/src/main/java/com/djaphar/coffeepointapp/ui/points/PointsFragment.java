package com.djaphar.coffeepointapp.ui.points;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djaphar.coffeepointapp.MainActivity;
import com.djaphar.coffeepointapp.R;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class PointsFragment extends Fragment {

    private PointsViewModel pointsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        pointsViewModel = ViewModelProviders.of(this).get(PointsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_points, container, false);

        pointsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.title_points));

        return root;
    }
}