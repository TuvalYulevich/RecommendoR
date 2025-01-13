package com.example.recommendor;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.recommendor.databinding.FragmentRegisterBinding;
import com.example.recommendor.models.UserModel;
import com.example.recommendor.repositories.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterFragment extends Fragment {

    private FirebaseAuth auth;
    private UserRepository userRepository; // Declare a UserRepository instance
    private FragmentRegisterBinding binding; // Declare a binding instance

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout using View Binding
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("FragmentLifecycle", "Fragment loaded: " + this.getClass().getSimpleName());

        // Initialize FirebaseAuth and UserRepository
        auth = FirebaseAuth.getInstance();
        userRepository = new UserRepository();

        // Set up button click listeners
        binding.btnRegister.setOnClickListener(v -> {
            String firstName = binding.inputFirstName.getText().toString();
            String lastName = binding.inputLastName.getText().toString();
            String username = binding.inputUsername.getText().toString();
            String email = binding.inputEmail.getText().toString();
            String password = binding.inputPassword.getText().toString();

            // Validate inputs
            if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(username)
                    || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new user with Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {
                                String userId = firebaseUser.getUid();

                                // Create a User object and save using UserRepository
                                UserModel newUser = new UserModel();
                                newUser.setId(userId);
                                newUser.setFirstName(firstName);
                                newUser.setLastName(lastName);
                                newUser.setUsername(username);
                                newUser.setEmail(email);

                                userRepository.updateUser(userId, newUser, updateTask -> {
                                    if (updateTask.isSuccessful()) {
                                        NavController navController = Navigation.findNavController(view);
                                        navController.navigate(R.id.action_registerFragment_to_welcomeFragment);
                                    } else {
                                        Toast.makeText(requireContext(), "Error saving user info.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(requireContext(), "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        binding.btnGoBack.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigateUp();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
    }
}
