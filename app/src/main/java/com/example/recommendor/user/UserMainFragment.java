package com.example.recommendor.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.recommendor.R;
import com.google.firebase.auth.FirebaseAuth;

public class UserMainFragment extends Fragment {

    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("FragmentLifecycle", "Fragment loaded: " + this.getClass().getSimpleName());

        auth = FirebaseAuth.getInstance();

        Button logoutButton = view.findViewById(R.id.btnLogout);
        TextView welcomeMessage = view.findViewById(R.id.welcomeMessage);

        // Show the user's email in the welcome message
        if (auth.getCurrentUser() != null) {
            String userEmail = auth.getCurrentUser().getEmail();
            if (userEmail != null) {
                welcomeMessage.setText("Welcome, " + userEmail);
            }
        }
        logoutButton.setOnClickListener(v -> {
            auth.signOut();
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_userMainFragment_to_welcomeFragment);
        });
    }
}
