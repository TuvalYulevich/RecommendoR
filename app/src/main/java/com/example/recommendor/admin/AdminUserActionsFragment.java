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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.example.recommendor.R;
import com.example.recommendor.databinding.FragmentAdminUserActionsBinding; // Import the binding class
import com.example.recommendor.repositories.UserRepository;
import com.example.recommendor.viewmodels.UserViewModel;

public class AdminUserActionsFragment extends Fragment {

    private String userId;
    private UserRepository userRepository; // Declare the UserRepository instance
    private FragmentAdminUserActionsBinding binding; // Declare the binding instance

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout using View Binding
        binding = FragmentAdminUserActionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AdminUserActionsFragmentArgs args = AdminUserActionsFragmentArgs.fromBundle(getArguments());
        userId = args.getUserId();

        if (userId == null) {
            Log.e("AdminUserActionsFragment", "userId is null. Cannot proceed.");
            return;
        }
        Log.d("AdminUserActionsFragment", "Navigated with userId: " + userId);

        setupButtons();
    }

    private void setupButtons() {
        // View User Information
        binding.btnViewUserInfo.setOnClickListener(v -> {
            AdminUserActionsFragmentDirections.ActionAdminUserActionsFragmentToViewUserInfoFragment action =
                    AdminUserActionsFragmentDirections.actionAdminUserActionsFragmentToViewUserInfoFragment(userId);
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });
        // Edit Personal Details
        binding.btnEditDetails.setOnClickListener(v -> {
            AdminUserActionsFragmentDirections.ActionAdminUserActionsFragmentToUserDetailsFragment action =
                    AdminUserActionsFragmentDirections.actionAdminUserActionsFragmentToUserDetailsFragment(userId);
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });
        // Delete User
        binding.btnDeleteUser.setOnClickListener(v -> {
            UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
            userViewModel.deleteUser(userId);
            Toast.makeText(requireContext(), "User deleted successfully!", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_adminUserActionsFragment_to_adminFragment);
        });
        // Go Back
        binding.btnGoBack.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_adminUserActionsFragment_to_adminFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
    }
}
