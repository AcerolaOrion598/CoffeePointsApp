package com.djaphar.coffeepointapp.ui.other;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djaphar.coffeepointapp.MainActivity;
import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.ViewModel.OtherViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class OtherFragment extends Fragment {

    private OtherViewModel otherViewModel;
    private MainActivity mainActivity;
    private TextView aboutAppTv, exitTv;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        otherViewModel = new ViewModelProvider(this).get(OtherViewModel.class);
        View root = inflater.inflate(R.layout.fragment_other, container, false);
        aboutAppTv = root.findViewById(R.id.about_app_tv);
        exitTv = root.findViewById(R.id.exit_tv);
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
            if (user == null) {
                logout();
            }
        });

//        aboutAppTv.setOnClickListener(lView -> Toast.makeText(mainActivity, R.string.mayoi_chan, Toast.LENGTH_SHORT).show());

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
}
