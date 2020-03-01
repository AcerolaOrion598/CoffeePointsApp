package com.djaphar.coffeepointapp.ui.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.djaphar.coffeepointapp.AuthActivity;
import com.djaphar.coffeepointapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class RegisterFragment extends Fragment {

    private Button registerButton;
    private NavController navController;
    private AuthActivity authActivity;
    private TextView logInTv, wrongCredentialsRegisterTv;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        registerButton = root.findViewById(R.id.register_btn);
        logInTv = root.findViewById(R.id.log_in_tv);
        wrongCredentialsRegisterTv = root.findViewById(R.id.wrong_credentials_register_tv);
        authActivity = (AuthActivity) getActivity();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        logInTv.setOnClickListener(lView -> navController.navigate(R.id.navigation_log_in));

        registerButton.setOnClickListener(lView -> wrongCredentialsRegisterTv.setVisibility(View.VISIBLE));
    }
}
