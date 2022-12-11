package com.example.simplemessenger;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl;

public class ChatViewModelFactory implements ViewModelProvider.Factory {

    String currentUserId;
    String otherUserId;

    public ChatViewModelFactory(String currentUserId, String otherUserId) {
        this.currentUserId = currentUserId;
        this.otherUserId = otherUserId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ChatViewModel(currentUserId, otherUserId);
    }
}
