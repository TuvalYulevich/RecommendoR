package com.example.recommendor.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recommendor.MainActivity;
import com.example.recommendor.R;
import com.google.firebase.auth.FirebaseAuth;

public class UserMainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance();

        Button logoutButton = findViewById(R.id.btnLogout);

        // Log Out Button Listener
        logoutButton.setOnClickListener(v -> {
            // Sign out the user from Firebase Authentication
            auth.signOut();

            // Navigate to MainActivity
            Intent intent = new Intent(UserMainActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Optional: Show current user's email or other information (for demo purposes)
        if (auth.getCurrentUser() != null) {
            String userEmail = auth.getCurrentUser().getEmail();
            if (userEmail != null) {
                setTitle("Welcome, " + userEmail);
            }
        }
    }
}
