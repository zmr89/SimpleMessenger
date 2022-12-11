package com.example.simplemessenger;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersViewModel extends ViewModel {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference refUsers;
    private final MutableLiveData<FirebaseUser> firebaseUserLD = new MutableLiveData<>();
    private final MutableLiveData<List<User>> listUsersLD = new MutableLiveData<>();

    public UsersViewModel() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
// if call the signOut() method, Auth state will change and firebaseAuth.getCurrentUser() == null
                if (firebaseAuth.getCurrentUser() == null) {
                    firebaseUserLD.setValue(firebaseAuth.getCurrentUser());
                }
            }
        });
        database = FirebaseDatabase.getInstance();
        refUsers = database.getReference("Users");
        getAllUsers();
    }

    public void signOut() {
        setOnlineStatus(false);
        mAuth.signOut();
    }

    public void getAllUsers() {
        refUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                List<User> userList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null && currentUser != null && !user.getId().equals(currentUser.getUid())) {
                        userList.add(user);
                    }
                }
                listUsersLD.setValue(userList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setOnlineStatus(Boolean isOnline) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            return;
        }
        refUsers.child(currentUser.getUid()).child("onlineStatus").setValue(isOnline);
    }

    public LiveData<FirebaseUser> getFirebaseUserLD() {
        return firebaseUserLD;
    }

    public LiveData<List<User>> getListUsersLD() {
        return listUsersLD;
    }
}
