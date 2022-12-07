package com.example.simplemessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String EXTRA_EMAIL = "email";
    private EditText editTextEmailForgot;
    private Button buttonResetPassword;
    private ForgotPasswordViewModel forgotPasswordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initViews();
        String email = getIntent().getStringExtra(EXTRA_EMAIL);
        editTextEmailForgot.setText(email);

        forgotPasswordViewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);
        observeViewModel();

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailForgot = editTextEmailForgot.getText().toString().trim();
                if (emailForgot.isEmpty()){
                    Toast.makeText(ForgotPasswordActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                } else {
                    forgotPasswordViewModel.sendPasswordResetEmail(emailForgot);
                }
            }
        });
    }


    public static Intent newIntent(Context context, String email) {
        Intent intent = new Intent(context, ForgotPasswordActivity.class);
        intent.putExtra(EXTRA_EMAIL, email);
        return intent;
    }

    private void observeViewModel(){
        forgotPasswordViewModel.getIsSuccessSendPasswordResetEmailLD().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean IsSuccessSendPasswordResetEmail) {
                if (IsSuccessSendPasswordResetEmail) {
                    Intent intent = LoginActivity.newIntent(ForgotPasswordActivity.this);
                    startActivity(intent);
                }
            }
        });
        forgotPasswordViewModel.getErrorSendToEmailLD().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    Toast.makeText(ForgotPasswordActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initViews(){
        editTextEmailForgot = findViewById(R.id.editTextEmailForgot);
        buttonResetPassword = findViewById(R.id.buttonResetPassword);
    }

}