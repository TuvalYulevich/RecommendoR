package com.example.recommendor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        findViewById(R.id.btnRegister).setOnClickListener(v -> {
            EditText firstNameInput = findViewById(R.id.inputFirstName);
            EditText lastNameInput = findViewById(R.id.inputLastName);
            EditText usernameInput = findViewById(R.id.inputUsername);
            EditText emailInput = findViewById(R.id.inputEmail);
            EditText passwordInput = findViewById(R.id.inputPassword);
            EditText dobInput = findViewById(R.id.inputDateOfBirth);

            String firstName = firstNameInput.getText().toString();
            String lastName = lastNameInput.getText().toString();
            String username = usernameInput.getText().toString();
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            String dateOfBirth = dobInput.getText().toString();

            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || dateOfBirth.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {
                                String userId = firebaseUser.getUid();

                                // Store additional user info in Firestore
                                Map<String, Object> userDetails = new HashMap<>();
                                userDetails.put("firstName", firstName);
                                userDetails.put("lastName", lastName);
                                userDetails.put("id", userId);
                                userDetails.put("username", username);
                                userDetails.put("email", email);
                                userDetails.put("dateOfBirth", dateOfBirth);

                                db.collection("users").document(userId)
                                        .set(userDetails)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(this, MainActivity.class));
                                            finish();
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(this, "Error saving user info", Toast.LENGTH_SHORT).show());
                            }
                        } else {
                            Toast.makeText(this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        findViewById(R.id.btnGoBack).setOnClickListener(v -> finish());
    }
}
