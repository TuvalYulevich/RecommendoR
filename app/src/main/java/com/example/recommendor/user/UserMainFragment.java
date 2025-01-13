package com.example.recommendor.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.recommendor.R;
import com.example.recommendor.databinding.FragmentUserMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class UserMainFragment extends Fragment {

    private FirebaseAuth auth;
    private FragmentUserMainBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("FragmentLifecycle", "Fragment loaded: " + this.getClass().getSimpleName());

        auth = FirebaseAuth.getInstance();

        // Show the user's email in the welcome message
        if (auth.getCurrentUser() != null) {
            String userEmail = auth.getCurrentUser().getEmail();
            if (userEmail != null) {
                binding.welcomeMessage.setText("Welcome, " + userEmail);
            }
        }

        binding.btnLogout.setOnClickListener(v -> {
            auth.signOut();
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_userMainFragment_to_welcomeFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
