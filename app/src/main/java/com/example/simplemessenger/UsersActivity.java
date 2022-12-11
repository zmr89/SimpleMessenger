package com.example.simplemessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {
    private static final String TAG = "UsersActivity";
    private static final String EXTRA_CURRENT_USER_ID = "currentUserId";
    private UsersViewModel usersViewModel;
    private RecyclerView recyclerviewUsers;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        observeViewModel();

        recyclerviewUsers = findViewById(R.id.recyclerviewUsers);
        userAdapter = new UserAdapter();
        recyclerviewUsers.setAdapter(userAdapter);

        String currentUserId = getIntent().getStringExtra(EXTRA_CURRENT_USER_ID);
        userAdapter.setOnUserClickListener(new UserAdapter.OnUserClickListener() {
            @Override
            public void OnUserClick(User user) {
                Intent intent = ChatActivity.newIntent(UsersActivity.this, currentUserId, user.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.users_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        usersViewModel.signOut();
        return super.onOptionsItemSelected(item);
    }

    public static Intent newIntent(Context context, String currentUserId) {
        Intent intent = new Intent(context, UsersActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId);
        return intent;
    }

    private void observeViewModel(){
        usersViewModel.getFirebaseUserLD().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                    Intent intent = LoginActivity.newIntent(UsersActivity.this);
                    startActivity(intent);
                    finish();
            }
        });

        usersViewModel.getListUsersLD().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                userAdapter.setUserList(users);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        usersViewModel.setOnlineStatus(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        usersViewModel.setOnlineStatus(false);
    }

}

