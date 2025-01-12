package com.example.recommendor.user;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.recommendor.R;
import com.example.recommendor.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewUserInfoActivity extends AppCompatActivity {

    private String userId; // Firestore user document ID
    private FirebaseFirestore firestore; // Firestore instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_info);

        // Get the user ID from the intent
        userId = getIntent().getStringExtra("userId");
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Invalid User ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        firestore = FirebaseFirestore.getInstance();

        TextView tvUserInfo = findViewById(R.id.tvUserInfo);

        // Fetch and display user details
        firestore.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            tvUserInfo.setText(
                                    "Username: " + user.getUsername() + "\n" +
                                            "First Name: " + user.getFirstName() + "\n" +
                                            "Last Name: " + user.getLastName() + "\n" +
                                            "Email: " + user.getEmail() + "\n" +
                                            "Date of Birth: " + user.getDateOfBirth()
                            );
                        }
                    } else {
                        Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to fetch user info: " + e.getMessage(), Toast.LENGTH_SHORT).show());

        findViewById(R.id.btnGoBack).setOnClickListener(v -> finish());
    }
}
