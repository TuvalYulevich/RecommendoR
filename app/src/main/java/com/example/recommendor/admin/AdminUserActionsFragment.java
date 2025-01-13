package com.example.recommendor.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.recommendor.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminUserActionsFragment extends Fragment {

    private String userId;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_user_actions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        userId = requireArguments().getString("userId");
        if (userId == null) {
            Log.e("AdminUserActionsFragment", "userId is null. Cannot proceed.");
            return;
        }
        Log.d("AdminUserActionsFragment", "Navigated with userId: " + userId);

        setupButtons(view);
    }

    private void setupButtons(View view) {
        view.findViewById(R.id.btnEditDetails).setOnClickListener(v -> navigateToEditDetails(v));
        view.findViewById(R.id.btnViewUserInfo).setOnClickListener(v -> navigateToViewUserInfo(v));
        view.findViewById(R.id.btnDeleteUser).setOnClickListener(v -> deleteUser(view));
        view.findViewById(R.id.btnGoBack).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            Navigation.findNavController(view).navigate(R.id.action_adminUserActionsFragment_to_adminFragment, bundle);
        });
    }

    private void navigateToEditDetails(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        Navigation.findNavController(view).navigate(R.id.action_adminUserActionsFragment_to_userDetailsFragment, bundle);
    }

    private void navigateToViewUserInfo(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        Navigation.findNavController(view).navigate(R.id.action_adminUserActionsFragment_to_viewUserInfoFragment, bundle);
    }

    private void deleteUser(View view) {
        db.collection("users").document(userId)
                .delete()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(requireContext(), "User deleted successfully!", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigate(R.id.action_adminUserActionsFragment_to_adminFragment);
                })
                .addOnFailureListener(e -> {
                    Log.e("AdminUserActionsFragment", "Failed to delete user.", e);
                    Toast.makeText(requireContext(), "Failed to delete user.", Toast.LENGTH_SHORT).show();
                });
    }
}
