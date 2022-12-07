package com.example.simplemessenger;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsersViewModel extends ViewModel {

    private FirebaseAuth mAuth;
    private final MutableLiveData<FirebaseUser> firebaseUserLD = new MutableLiveData<>();

    public UsersViewModel() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    firebaseUserLD.setValue(firebaseAuth.getCurrentUser());
                }
            }
        });
    }

    public void signOut() {
        mAuth.signOut();
    }

    public LiveData<FirebaseUser> getFirebaseUserLD() {
        return firebaseUserLD;
    }

}
