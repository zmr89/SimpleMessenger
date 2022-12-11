package com.example.simplemessenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private TextView textViewChatTitle;
    private View viewOnlineStatus;
    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private ImageView imageViewSendMessage;
    private MessageAdapter messageAdapter;
    private static final String EXTRA_CURRENT_USER_ID = "currentUserId";
    private static final String EXTRA_OTHER_USER_ID = "otherUserId";
    private ChatViewModel chatViewModel;
    private ChatViewModelFactory chatViewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();

        String currentUserId = getIntent().getStringExtra(EXTRA_CURRENT_USER_ID);
        String otherUserId = getIntent().getStringExtra(EXTRA_OTHER_USER_ID);

        chatViewModelFactory = new ChatViewModelFactory(currentUserId, otherUserId);
        chatViewModel = new ViewModelProvider(this, chatViewModelFactory).get(ChatViewModel.class);

        messageAdapter = new MessageAdapter(currentUserId);
        recyclerViewMessages.setAdapter(messageAdapter);

        observeViewModels();

        imageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textMessage = editTextMessage.getText().toString().trim();
                Message message = new Message(textMessage, currentUserId, otherUserId);
                chatViewModel.sendMessage(message);
            }
        });
    }


    public static Intent newIntent(Context context, String currentUserId, String otherUserId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId);
        intent.putExtra(EXTRA_OTHER_USER_ID, otherUserId);
        return intent;
    }

    public void initViews() {
        textViewChatTitle = findViewById(R.id.textViewChatTitle);
        viewOnlineStatus = findViewById(R.id.viewOnlineStatus);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        imageViewSendMessage = findViewById(R.id.imageViewSendMessage);
    }

    public void observeViewModels() {
        chatViewModel.getListMessagesLD().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                messageAdapter.setMessageList(messages);
            }
        });
        chatViewModel.getOtherUserLD().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    String userInfo = String.format("%s, %s", user.getName(), user.getLastName());
                    textViewChatTitle.setText(userInfo);

                    int backgroundResId;
                    if (user.getOnlineStatus()) {
                        backgroundResId = R.drawable.status_green;
                    } else {
                        backgroundResId = R.drawable.status_red;
                    }
                    Drawable backgroundDrawable = ContextCompat.getDrawable(ChatActivity.this, backgroundResId);
                    viewOnlineStatus.setBackground(backgroundDrawable);
                }
            }
        });
        chatViewModel.getIsSuccessSendMessageLD().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccessSendMessage) {
                editTextMessage.setText("");
            }
        });
        chatViewModel.getErrorOnFailureLD().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                Toast.makeText(ChatActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        chatViewModel.setOnlineStatus(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        chatViewModel.setOnlineStatus(false);
    }

}