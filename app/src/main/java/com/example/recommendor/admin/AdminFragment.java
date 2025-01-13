package com.example.recommendor.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recommendor.R;
import com.example.recommendor.adapters.UserAdapter;
import com.example.recommendor.databinding.FragmentAdminBinding; // Import the binding class
import com.example.recommendor.models.UserModel;
import com.example.recommendor.repositories.UserRepository;
import com.example.recommendor.viewmodels.UserViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class AdminFragment extends Fragment {


    private UserAdapter adapter;
    private FragmentAdminBinding binding; // Declare the binding instance
    private UserRepository userRepository; // Declare the UserRepository instance

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout using View Binding
        binding = FragmentAdminBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Use ViewModel to observe LiveData
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Observe the list of users
        userViewModel.getAllUsers().observe(getViewLifecycleOwner(), users -> {
            if (users != null && !users.isEmpty()) {
                adapter.setUsers(users); // Update the RecyclerView adapter
                binding.tvUserCount.setText("Registered Users: " + users.size());
            } else {
                adapter.setUsers(new ArrayList<>()); // Clear adapter data
                binding.tvUserCount.setText("No users found.");
            }
        });

        // Set up RecyclerView
        binding.userRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new UserAdapter(new ArrayList<>(), user -> {
            // Navigate to AdminUserActionsFragment with Safe Args
            AdminFragmentDirections.ActionAdminFragmentToAdminUserActionsFragment action =
                    AdminFragmentDirections.actionAdminFragmentToAdminUserActionsFragment(user.getId());
            Navigation.findNavController(view).navigate(action);
        });
        binding.userRecyclerView.setAdapter(adapter);

        // Set up Log Out button
        binding.btnLogout.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_adminFragment_to_welcomeFragment);
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
    }
}
