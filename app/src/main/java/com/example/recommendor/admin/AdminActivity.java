package com.example.recommendor.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.recommendor.R;
import com.example.recommendor.adapters.UserAdapter;
import com.example.recommendor.models.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView userRecyclerView;
    private TextView tvNoUsers;
    private UserAdapter adapter;
    private FirebaseFirestore firestore;
    private CollectionReference usersCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        userRecyclerView = findViewById(R.id.userRecyclerView);
        tvNoUsers = findViewById(R.id.tvNoUsers);
        firestore = FirebaseFirestore.getInstance();
        usersCollection = firestore.collection("users");

        findViewById(R.id.btnGoBack).setOnClickListener(v -> {
            v.setEnabled(false); // Prevent multiple clicks
            if (!isTaskRoot()) {
                finish();
            } else {
                Intent intent = new Intent(this, com.example.recommendor.MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        // Create the query to fetch data from Firestore
        Query query = usersCollection.orderBy("username", Query.Direction.ASCENDING);

        // FirestoreRecyclerOptions
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        // Set up UserAdapter
        adapter = new UserAdapter(options, user -> {
            Intent intent = new Intent(AdminActivity.this, AdminUserActionsActivity.class);
            intent.putExtra("userId", user.getId());
            startActivity(intent);
        });

        // Set the adapter to the RecyclerView
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userRecyclerView.setAdapter(adapter);

        // Handle empty data
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (adapter.getItemCount() == 0) {
                    userRecyclerView.setVisibility(RecyclerView.GONE);
                    tvNoUsers.setVisibility(TextView.VISIBLE);
                } else {
                    userRecyclerView.setVisibility(RecyclerView.VISIBLE);
                    tvNoUsers.setVisibility(TextView.GONE);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) adapter.startListening(); // Ensure adapter listens for real-time updates
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshRecyclerView(); // Refresh the data when returning to this activity
    }

    private void refreshRecyclerView() {
        if (adapter != null) {
            adapter.stopListening(); // Stop listening to the old query
            setupRecyclerView(); // Re-setup the RecyclerView
            adapter.startListening(); // Start listening to the new query
            Log.d("AdminActivity", "RecyclerView refreshed");
        }
    }
}
