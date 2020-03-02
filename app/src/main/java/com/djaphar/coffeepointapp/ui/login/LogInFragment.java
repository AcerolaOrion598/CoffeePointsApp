package com.djaphar.coffeepointapp.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.djaphar.coffeepointapp.AuthActivity;
import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.Credentials;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class LogInFragment extends Fragment  {

    private Button logInButton;
    private NavController navController;
    private AuthActivity authActivity;
    private TextView registerTv, wrongCredentialsTv;
    private EditText logInEmailEd, loginPasswordEd;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_log_in, container, false);
        logInButton = root.findViewById(R.id.log_in_btn);
        registerTv = root.findViewById(R.id.register_tv);
        wrongCredentialsTv = root.findViewById(R.id.wrong_credentials_tv);
        logInEmailEd = root.findViewById(R.id.log_in_email_ed);
        loginPasswordEd = root.findViewById(R.id.log_in_password_ed);
        authActivity = (AuthActivity) getActivity();
        if (authActivity != null) {
            authActivity.setActionBarTitle(getString(R.string.title_log_in));
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        registerTv.setOnClickListener(lView -> navController.navigate(R.id.navigation_register));
        logInButton.setOnClickListener(lView -> authActivity.logIn(new Credentials(logInEmailEd.getText().toString(), loginPasswordEd.getText().toString())));
    }
}
