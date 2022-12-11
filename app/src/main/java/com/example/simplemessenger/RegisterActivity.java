package com.example.simplemessenger;

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

import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private TextView textViewRegistration;
    private EditText editTextEmailRegistration;
    private EditText editTextPasswordRegistration;
    private EditText editTextNameRegistration;
    private EditText editTextLastNameRegistration;
    private EditText editTextAgeRegistration;
    private Button buttonSignUp;
    private static final String TAG = "RegisterActivity";
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        observeViewModels();
        setOnClickListener();
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        return intent;
    }

    private void initViews(){
        textViewRegistration = findViewById(R.id.textViewRegistration);
        editTextEmailRegistration = findViewById(R.id.editTextEmailRegistration);
        editTextPasswordRegistration = findViewById(R.id.editTextPasswordRegistration);
        editTextNameRegistration = findViewById(R.id.editTextNameRegistration);
        editTextLastNameRegistration = findViewById(R.id.editTextLastNameRegistration);
        editTextAgeRegistration = findViewById(R.id.editTextAgeRegistration);
        buttonSignUp = findViewById(R.id.buttonSignUp);
    }

    private void observeViewModels() {
        registerViewModel.getFirebaseUserLD().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Intent intent = UsersActivity.newIntent(RegisterActivity.this, firebaseUser.getUid());
                    startActivity(intent);
                    finish();
                }
            }
        });

        registerViewModel.getErrorRegistrationLD().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setOnClickListener() {
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailRegistration = getTrimmedString(editTextEmailRegistration);
                String passwordRegistration = getTrimmedString(editTextPasswordRegistration);
                String nameRegistration = getTrimmedString(editTextNameRegistration);
                String lastNameRegistration = getTrimmedString(editTextLastNameRegistration);
                String ageRegistration = getTrimmedString(editTextAgeRegistration);

                if (emailRegistration.isEmpty()
                        || passwordRegistration.isEmpty()
                        || nameRegistration.isEmpty()
                        || lastNameRegistration.isEmpty()
                        || ageRegistration.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Fill in the fields", Toast.LENGTH_SHORT).show();
                } else {
                    registerViewModel.registrationUser(emailRegistration, passwordRegistration,
                            nameRegistration, lastNameRegistration, ageRegistration);
                }
            }
        });
    }

    private String getTrimmedString(EditText editText){
        return editText.getText().toString().trim();
    }

}