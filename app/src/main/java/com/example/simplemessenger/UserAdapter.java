package com.example.simplemessenger;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter  extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList = new ArrayList<>();
    private OnUserClickListener onUserClickListener;

    public void setUserList(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    public void setOnUserClickListener(OnUserClickListener onUserClickListener) {
        this.onUserClickListener = onUserClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        String userInfo = String.format("%s %s, %s" , user.getName(), user.getLastName(), user.getAge());
        holder.textViewUserInfo.setText(userInfo);

        int backgroundResId;
        if (user.getOnlineStatus()) {
            backgroundResId = R.drawable.status_green;
        } else {
            backgroundResId = R.drawable.status_red;
        }
        Drawable backgroundDrawable = ContextCompat.getDrawable(holder.itemView.getContext(), backgroundResId);
        holder.viewOnlineStatus.setBackground(backgroundDrawable);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onUserClickListener != null) {
                    onUserClickListener.OnUserClick(user);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUserInfo;
        View viewOnlineStatus;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserInfo = itemView.findViewById(R.id.textViewUserInfo);
            viewOnlineStatus = itemView.findViewById(R.id.viewOnlineStatus);
        }
    }

    interface OnUserClickListener {
        void OnUserClick(User user);
    }


}
