package com.example.recommendor;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("FragmentLifecycle", "Fragment loaded: " + this.getClass().getSimpleName());
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        view.findViewById(R.id.btnRegister).setOnClickListener(v -> {
            EditText firstNameInput = view.findViewById(R.id.inputFirstName);
            EditText lastNameInput = view.findViewById(R.id.inputLastName);
            EditText usernameInput = view.findViewById(R.id.inputUsername);
            EditText emailInput = view.findViewById(R.id.inputEmail);
            EditText passwordInput = view.findViewById(R.id.inputPassword);

            String firstName = firstNameInput.getText().toString();
            String lastName = lastNameInput.getText().toString();
            String username = usernameInput.getText().toString();
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(username)
                    || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {
                                String userId = firebaseUser.getUid();

                                Map<String, Object> userDetails = new HashMap<>();
                                userDetails.put("firstName", firstName);
                                userDetails.put("lastName", lastName);
                                userDetails.put("username", username);
                                userDetails.put("email", email);

                                db.collection("users").document(userId).set(userDetails)
                                        .addOnSuccessListener(aVoid -> {
                                            NavController navController = Navigation.findNavController(view);
                                            navController.navigate(R.id.action_registerFragment_to_welcomeFragment);
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(requireContext(), "Error saving user info", Toast.LENGTH_SHORT).show());
                            }
                        } else {
                            Toast.makeText(requireContext(), "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        view.findViewById(R.id.btnGoBack).setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigateUp();
        });
    }
}
