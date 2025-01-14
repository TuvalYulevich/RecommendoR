package com.example.recommendor.user;

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

import com.example.recommendor.databinding.FragmentViewUserInfoBinding;
import com.example.recommendor.repositories.UserRepository;
import com.example.recommendor.viewmodels.UserViewModel;

public class ViewUserInfoFragment extends Fragment {

    private String userId;
    private UserRepository userRepository; // Declare the UserRepository instance
    private FragmentViewUserInfoBinding binding; // Declare the binding instance

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentViewUserInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        ViewUserInfoFragmentArgs args = ViewUserInfoFragmentArgs.fromBundle(getArguments());
        userId = args.getUserId();

        if (userId == null) {
            Log.e("ViewUserInfoFragment", "userId is null. Cannot fetch user info.");
            Toast.makeText(requireContext(), "User ID is missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Observe LiveData for user info
        userViewModel.getUserById(userId).observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                String userInfo = "Username: " + user.getUsername() + "\n" +
                        "First Name: " + user.getFirstName() + "\n" +
                        "Last Name: " + user.getLastName() + "\n" +
                        "Email: " + user.getEmail() + "\n" +
                        "Password: " + user.getPassword() + "\n" +
                        "Age: " + user.getAge();
                binding.tvUserInfo.setText(userInfo);
            } else {
                binding.tvUserInfo.setText("Failed to load user info.");
            }
        });

        binding.btnGoBack.setOnClickListener(v -> {
            ViewUserInfoFragmentDirections.ActionViewUserInfoFragmentToAdminUserActionsFragment action =
                    ViewUserInfoFragmentDirections.actionViewUserInfoFragmentToAdminUserActionsFragment(userId);
            Navigation.findNavController(view).navigate(action);
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
    }
}
