package com.example.simplemessenger;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordViewModel extends ViewModel {
    private FirebaseAuth mAuth;
    private final MutableLiveData<Boolean> isSuccessSendPasswordResetEmailLD = new MutableLiveData<>();
    private final MutableLiveData<String> errorSendToEmailLD = new MutableLiveData<>();

    public ForgotPasswordViewModel() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void sendPasswordResetEmail(String emailForgot) {
        mAuth.sendPasswordResetEmail(emailForgot).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            isSuccessSendPasswordResetEmailLD.setValue(true);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            errorSendToEmailLD.setValue(e.getMessage());
                        }
                    });
    }

    public LiveData<Boolean> getIsSuccessSendPasswordResetEmailLD() {
        return isSuccessSendPasswordResetEmailLD;
    }

    public LiveData<String> getErrorSendToEmailLD() {
        return errorSendToEmailLD;
    }
}
