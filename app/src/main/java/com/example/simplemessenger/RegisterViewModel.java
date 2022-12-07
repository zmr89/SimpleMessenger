package com.example.simplemessenger;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterViewModel extends ViewModel {

    private static final String TAG = "RegisterViewModel";
    private FirebaseAuth mAuth;
    private final MutableLiveData <String> errorRegistrationLD = new MutableLiveData<>();
    private final MutableLiveData <FirebaseUser> firebaseUserLD = new MutableLiveData<>();

    public RegisterViewModel() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    firebaseUserLD.setValue(firebaseAuth.getCurrentUser());
                }
            }
        });
    }

    public void registrationUser(String emailRegistration, String passwordRegistration,
                                 String nameRegistration, String lastNameRegistration, String ageRegistration) {

        mAuth.createUserWithEmailAndPassword(emailRegistration, passwordRegistration)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
// if registration success onAuthStateChanged (CurrentUser() != null)
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                errorRegistrationLD.setValue(e.getMessage());
            }
        });
    }


    public LiveData<String> getErrorRegistrationLD() {
        return errorRegistrationLD;
    }

    public LiveData<FirebaseUser> getFirebaseUserLD() {
        return firebaseUserLD;
    }

}
