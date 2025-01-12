package com.example.recommendor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.recommendor.admin.AdminActivity;
import com.example.recommendor.user.UserMainActivity;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            EditText emailInput = findViewById(R.id.inputEmail);
            EditText passwordInput = findViewById(R.id.inputPassword);

            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (email.equals("Admin") && password.equals("123456")) {
                Toast.makeText(this, "Welcome Admin!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, AdminActivity.class));
                finish();
            } else {
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                if (firebaseUser != null) {
                                    String userId = firebaseUser.getUid();

                                    // Fetch user details from Firestore
                                    db.collection("users").document(userId)
                                            .get()
                                            .addOnSuccessListener(documentSnapshot -> {
                                                if (documentSnapshot.exists()) {
                                                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(this, UserMainActivity.class));
                                                    finish();
                                                } else {
                                                    Toast.makeText(this, "User data not found!", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(this, "Error fetching user data", Toast.LENGTH_SHORT).show());
                                }
                            } else {
                                Toast.makeText(this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        findViewById(R.id.btnGoBack).setOnClickListener(v -> finish());
    }
}
