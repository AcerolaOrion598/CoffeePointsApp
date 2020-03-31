package com.djaphar.coffeepointapp.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.FirstCredentials;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.SecondCredentials;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.ViewDriver;
import com.djaphar.coffeepointapp.ViewModels.AuthViewModel;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Objects;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class AuthActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private EditText phoneNumberEd, smsCodeEd;
    private Button requestNumberBtn, loginBtn;
    private TextView authInstructionTv, authInstructionPhoneNumberTv;
    private String token;
    private SecondCredentials secondCredentials = new SecondCredentials(null, null);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        TextView actionBarTitle = findViewById(R.id.action_bar_title);
        actionBarTitle.setText(R.string.title_log_in);

        phoneNumberEd = findViewById(R.id.phone_number_ed);
        smsCodeEd = findViewById(R.id.sms_code_ed);
        requestNumberBtn = findViewById(R.id.request_code_btn);
        loginBtn = findViewById(R.id.login_btn);
        authInstructionTv = findViewById(R.id.auth_instruction_tv);
        authInstructionPhoneNumberTv = findViewById(R.id.auth_instruction_phone_number_tv);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getSecondCredentials().observe(this, credentials -> {
            if (credentials == null) {
                return;
            }
            secondCredentials.setCodeId(credentials.getCodeId());
            if (secondCredentials.getCode() == null) {
                return;
            }
            loginBtn.performClick();
        });

        authViewModel.getUser().observe(this, user -> {
            if (user == null) {
                return;
            }
            startMainActivity();
        });

        phoneNumberEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) {
                    requestNumberBtn.setEnabled(false);
                } else {
                    requestNumberBtn.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
        });

        smsCodeEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) {
                    loginBtn.setEnabled(false);
                } else {
                    loginBtn.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
        });

        requestNumberBtn.setOnClickListener(lView -> FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Exception e = task.getException();
                if (e != null) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return;
            }

            InstanceIdResult result = task.getResult();
            if (result != null) {
                smsCodeEd.setText("");
                toggleTexts(getString(R.string.auth_instruction_tv_code_text), phoneNumberEd.getText().toString());
                toggleViews(phoneNumberEd, smsCodeEd, R.anim.hide_left_animation, R.anim.show_right_animation);
                toggleViews(requestNumberBtn, loginBtn, R.anim.hide_left_animation, R.anim.show_right_animation);
                token = result.getToken();
                authViewModel.requestCode(new FirstCredentials(phoneNumberEd.getText().toString(), token));
            }
        }));

        loginBtn.setOnClickListener(lView -> {
            secondCredentials.setCode(smsCodeEd.getText().toString());
            authViewModel.login(secondCredentials);
        });
    }

    private void toggleViews(View toHide, View toShow, int hideAnimation, int showAnimation) {
        ViewDriver.hideView(toHide, hideAnimation, this);
        ViewDriver.showView(toShow, showAnimation, this);
    }

    private void toggleTexts(String firstTvText, String secondTvText) {
        authInstructionTv.setText(firstTvText);
        authInstructionPhoneNumberTv.setText(secondTvText);
    }

    @Override
    public void onBackPressed() {
        if (loginBtn.getVisibility() == View.VISIBLE) {
            phoneNumberEd.setText("");
            toggleTexts(getString(R.string.auth_instruction_tv_num_text), "");
            toggleViews(smsCodeEd, phoneNumberEd, R.anim.hide_right_animation, R.anim.show_left_animation);
            toggleViews(loginBtn, requestNumberBtn, R.anim.hide_right_animation, R.anim.show_left_animation);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("SmsCode"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                smsCodeEd.setText(extras.getString("code"));
                secondCredentials.setCode(extras.getString("code"));
                loginBtn.setEnabled(true);
                if (secondCredentials.getCodeId() == null) {
                    return;
                }
                loginBtn.performClick();
            }
        }
    };
}
