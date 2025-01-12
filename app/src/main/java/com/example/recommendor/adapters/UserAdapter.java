package com.example.recommendor.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.recommendor.R;
import com.example.recommendor.models.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class UserAdapter extends FirestoreRecyclerAdapter<User, UserAdapter.UserViewHolder> {

    private final OnUserClickListener listener;

    public UserAdapter(@NonNull FirestoreRecyclerOptions<User> options, OnUserClickListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User user) {
        String documentId = getSnapshots().getSnapshot(position).getId();
        user.setId(documentId); // Assign the Firestore document ID
        holder.usernameTextView.setText(user.getUsername());
        holder.fullNameTextView.setText(user.getFirstName() + " " + user.getLastName());
        holder.itemView.setOnClickListener(v -> listener.onUserClick(user));
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView, fullNameTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.tvUsername);
            fullNameTextView = itemView.findViewById(R.id.tvFullName);
        }
    }

    public interface OnUserClickListener {
        void onUserClick(User user);
    }
}
