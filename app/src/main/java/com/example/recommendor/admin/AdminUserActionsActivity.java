package com.example.recommendor.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.recommendor.R;
import com.example.recommendor.user.UserDetailsActivity;
import com.example.recommendor.user.ViewUserInfoActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminUserActionsActivity extends AppCompatActivity {

    private String userId; // Firestore document ID
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    private CollectionReference usersCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_actions);
        auth = FirebaseAuth.getInstance();
        userId = getIntent().getStringExtra("userId");
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        CollectionReference usersCollection = firestore.collection("users");

        Button btnViewUserInfo = findViewById(R.id.btnViewUserInfo);
        Button btnEditDetails = findViewById(R.id.btnEditDetails);
        Button btnDeleteUser = findViewById(R.id.btnDeleteUser);
        Button btnGoBack = findViewById(R.id.btnGoBack);

        btnViewUserInfo.setOnClickListener(v -> {
            Intent intent = new Intent(this, ViewUserInfoActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        btnEditDetails.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserDetailsActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        btnDeleteUser.setOnClickListener(v -> {
            // Directly delete from Firestore
            usersCollection.document(userId).delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d("FirestoreDelete", "User deleted from Firestore!");

                        // Logically skip FirebaseAuth delete for admin
                        Toast.makeText(this, "User deleted successfully!", Toast.LENGTH_SHORT).show();
                        finish(); // Go back to the previous screen
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FirestoreDelete", "Failed to delete user from Firestore: " + e.getMessage());
                        Toast.makeText(this, "Failed to delete user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        btnGoBack.setOnClickListener(v -> finish());
    }
}