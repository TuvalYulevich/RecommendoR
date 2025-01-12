package com.example.recommendor.user;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.recommendor.R;
import com.example.recommendor.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserDetailsActivity extends AppCompatActivity {

    private String userId; // Firestore user document ID
    private FirebaseFirestore firestore; // Firestore instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        firestore = FirebaseFirestore.getInstance();

        userId = getIntent().getStringExtra("userId");
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Invalid User ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // UI elements
        EditText etFirstName = findViewById(R.id.etFirstName);
        EditText etLastName = findViewById(R.id.etLastName);
        EditText etUsername = findViewById(R.id.etUsername);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etDateOfBirth = findViewById(R.id.etDateOfBirth);

        Button btnSave = findViewById(R.id.btnSave);
        Button btnGoBack = findViewById(R.id.btnGoBack);

        // Fetch user details
        firestore.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            etFirstName.setText(user.getFirstName());
                            etLastName.setText(user.getLastName());
                            etUsername.setText(user.getUsername());
                            etEmail.setText(user.getEmail());
                            etDateOfBirth.setText(user.getDateOfBirth());
                        }
                    } else {
                        Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to fetch user details: " + e.getMessage(), Toast.LENGTH_SHORT).show());

        // Save changes
        btnSave.setOnClickListener(v -> {
            String updatedFirstName = etFirstName.getText().toString().trim();
            String updatedLastName = etLastName.getText().toString().trim();
            String updatedUsername = etUsername.getText().toString().trim();
            String updatedEmail = etEmail.getText().toString().trim();
            String updatedDateOfBirth = etDateOfBirth.getText().toString().trim();

            if (updatedFirstName.isEmpty() || updatedLastName.isEmpty() || updatedUsername.isEmpty() || updatedEmail.isEmpty() || updatedDateOfBirth.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            User updatedUser = new User();
            updatedUser.setFirstName(updatedFirstName);
            updatedUser.setLastName(updatedLastName);
            updatedUser.setUsername(updatedUsername);
            updatedUser.setEmail(updatedEmail);
            updatedUser.setDateOfBirth(updatedDateOfBirth);

            firestore.collection("users").document(userId).set(updatedUser)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("FirestoreUpdate", "User updated successfully!");
                        Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Return to the previous activity
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FirestoreUpdate", "Error updating user: " + e.getMessage());
                        Toast.makeText(this, "Failed to update user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        // Back button
        btnGoBack.setOnClickListener(v -> finish());
    }
}
