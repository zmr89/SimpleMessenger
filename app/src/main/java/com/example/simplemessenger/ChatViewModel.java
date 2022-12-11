package com.example.simplemessenger;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {
    private final MutableLiveData<List<Message>> listMessagesLD = new MutableLiveData<>();
    private final MutableLiveData<User> otherUserLD = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSuccessSendMessageLD = new MutableLiveData<>();
    private final MutableLiveData<String> errorOnFailureLD = new MutableLiveData<>();
    private String currentUserId;
    private String otherUserId;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference refUsers;
    private DatabaseReference refMessages;

    public ChatViewModel(String currentUserId, String otherUserId) {
        this.currentUserId = currentUserId;
        this.otherUserId = otherUserId;

        firebaseDatabase = FirebaseDatabase.getInstance();
        refUsers = firebaseDatabase.getReference("Users");
        refMessages = firebaseDatabase.getReference("Messages");

        refUsers.child(otherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User otherUser = snapshot.getValue(User.class);
                otherUserLD.setValue(otherUser);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        refMessages.child(currentUserId).child(otherUserId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Message> messageList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Message message = dataSnapshot.getValue(Message.class);
                    messageList.add(message);
                }
                listMessagesLD.setValue(messageList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public void sendMessage(Message message){
        refMessages.child(message.getSenderUserId())
                .child(message.getReceiverUserId())
                .push().setValue(message)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                refMessages.child(message.getReceiverUserId())
                        .child(message.getSenderUserId())
                        .push()
                        .setValue(message)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        isSuccessSendMessageLD.setValue(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        errorOnFailureLD.setValue(e.getMessage());
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                errorOnFailureLD.setValue(e.getMessage());
            }
        });
    }

    public void setOnlineStatus(Boolean isOnline) {
        refUsers.child(currentUserId).child("onlineStatus").setValue(isOnline);
    }

    public LiveData<List<Message>> getListMessagesLD() {
        return listMessagesLD;
    }

    public LiveData<User> getOtherUserLD() {
        return otherUserLD;
    }

    public LiveData<Boolean> getIsSuccessSendMessageLD() {
        return isSuccessSendMessageLD;
    }

    public LiveData<String> getErrorOnFailureLD() {
        return errorOnFailureLD;
    }
}
