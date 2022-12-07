package com.example.simplemessenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewForgot;
    private TextView textViewRegister;

    private static final String TAG = "LoginActivity";
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        setOnClickListeners();

        observeViewModels();




//        loginViewModel.getFirebaseUserLD().observe(this, new Observer<FirebaseUser>() {
//            @Override
//            public void onChanged(FirebaseUser firebaseUser) {
//                if (firebaseUser == null) {
//                    Log.d(TAG, "firebaseUser offline");
//                } else {
//                    Log.d(TAG, "firebaseUser online: " + firebaseUser.getUid());
//                }
//            }
//        });
//        loginViewModel.getAuthState();

    }

    private void initViews(){
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewForgot = findViewById(R.id.textViewForgot);
        textViewRegister = findViewById(R.id.textViewRegister);
    }

    private void observeViewModels(){
        loginViewModel.getFirebaseUserLD().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Intent intent = UsersActivity.newIntent(LoginActivity.this);
                    startActivity(intent);
                    finish();
                }
            }
        });
        loginViewModel.getErrorSignInLD().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null){
                    Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setOnClickListeners() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailLogin = editTextEmail.getText().toString().trim();
                String passwordLogin = editTextPassword.getText().toString().trim();
                if (emailLogin.isEmpty() || passwordLogin.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter email and password", Toast.LENGTH_SHORT).show();
                } else {
                    loginViewModel.signInWithEmailAndPassword(emailLogin, passwordLogin);
                }
            }
        });

        textViewForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString().trim();
                Intent intent = ForgotPasswordActivity.newIntent(LoginActivity.this, email);
                startActivity(intent);
            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = RegisterActivity.newIntent(LoginActivity.this);
                startActivity(intent);
            }
        });
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

}