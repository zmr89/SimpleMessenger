package com.example.simplemessenger;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends ViewModel {

    private static final String TAG = "LoginViewModel";
    private FirebaseAuth mAuth;
    private final MutableLiveData<FirebaseUser> firebaseUserLD = new MutableLiveData<>();
    private final MutableLiveData<String> errorSignInLD = new MutableLiveData<>();

    public LoginViewModel() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    firebaseUserLD.setValue(firebaseAuth.getCurrentUser());
                }
            }
        });
    }

    public void signInWithEmailAndPassword(String emailLogin, String passwordLogin) {
        mAuth.signInWithEmailAndPassword(emailLogin, passwordLogin).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
// if registration success onAuthStateChanged (CurrentUser() != null)
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                errorSignInLD.setValue(e.getMessage());
            }
        });
    }

    public LiveData<FirebaseUser> getFirebaseUserLD() {
        return firebaseUserLD;
    }

    public LiveData<String> getErrorSignInLD() {
        return errorSignInLD;
    }

}
