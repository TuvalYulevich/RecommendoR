package com.example.recommendor.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.recommendor.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserDetailsFragment extends Fragment {

    private String userId;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        userId = requireArguments().getString("userId");
        if (userId == null) {
            Log.e("UserDetailsFragment", "userId is null. Cannot fetch user details.");
            return;
        }

        EditText etFirstName = view.findViewById(R.id.etFirstName);
        EditText etLastName = view.findViewById(R.id.etLastName);
        EditText etUsername = view.findViewById(R.id.etUsername);
        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etDateOfBirth = view.findViewById(R.id.etDateOfBirth);

        db.collection("users").document(userId).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        etFirstName.setText(snapshot.getString("firstName"));
                        etLastName.setText(snapshot.getString("lastName"));
                        etUsername.setText(snapshot.getString("username"));
                        etEmail.setText(snapshot.getString("email"));
                        etDateOfBirth.setText(snapshot.getString("dateOfBirth"));
                    }
                });

        view.findViewById(R.id.btnSave).setOnClickListener(v -> {
            db.collection("users").document(userId).update(
                            "firstName", etFirstName.getText().toString(),
                            "lastName", etLastName.getText().toString(),
                            "username", etUsername.getText().toString(),
                            "email", etEmail.getText().toString(),
                            "dateOfBirth", etDateOfBirth.getText().toString())
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(requireContext(), "User updated successfully.", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putString("userId", userId);
                        Navigation.findNavController(view).navigate(R.id.action_userDetailsFragment_to_adminUserActionsFragment, bundle);
                    })
                    .addOnFailureListener(e -> Toast.makeText(requireContext(), "Failed to update user.", Toast.LENGTH_SHORT).show());
        });

        view.findViewById(R.id.btnGoBack).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("userId", userId);
            Navigation.findNavController(view).navigate(R.id.action_userDetailsFragment_to_adminUserActionsFragment, bundle);
        });
    }
}
