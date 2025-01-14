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
import com.example.recommendor.databinding.FragmentUserDetailsBinding;
import com.example.recommendor.models.UserModel;
import com.example.recommendor.repositories.UserRepository;
import com.example.recommendor.viewmodels.UserViewModel;

public class UserDetailsFragment extends Fragment {

    private String userId;
    private UserRepository userRepository;
    private FragmentUserDetailsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userRepository = new UserRepository();
        UserDetailsFragmentArgs args = UserDetailsFragmentArgs.fromBundle(getArguments());
        userId = args.getUserId();

        if (userId == null) {
            Log.e("UserDetailsFragment", "userId is null. Cannot fetch user details.");
            return;
        }

        // Use LiveData to observe user data
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUserById(userId).observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.etFirstName.setText(user.getFirstName());
                binding.etLastName.setText(user.getLastName());
                binding.etUsername.setText(user.getUsername());
                binding.etAge.setText(user.getAge());
            } else {
                Log.e("UserDetailsFragment", "Failed to fetch user details.");
            }
        });

        binding.btnSave.setOnClickListener(v -> {
            UserModel updatedUser = new UserModel();
            updatedUser.setId(userId); // Ensure ID is set
            updatedUser.setFirstName(binding.etFirstName.getText().toString());
            updatedUser.setLastName(binding.etLastName.getText().toString());
            updatedUser.setUsername(binding.etUsername.getText().toString());
            updatedUser.setAge(binding.etAge.getText().toString());

            userRepository.updateUser(userId, updatedUser, task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(requireContext(), "User updated successfully.", Toast.LENGTH_SHORT).show();
                    UserDetailsFragmentDirections.ActionUserDetailsFragmentToAdminUserActionsFragment action =
                            UserDetailsFragmentDirections.actionUserDetailsFragmentToAdminUserActionsFragment(userId);
                    Navigation.findNavController(view).navigate(action);
                } else {
                    Toast.makeText(requireContext(), "Failed to update user.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.btnGoBack.setOnClickListener(v -> {
            UserDetailsFragmentDirections.ActionUserDetailsFragmentToAdminUserActionsFragment action =
                    UserDetailsFragmentDirections.actionUserDetailsFragmentToAdminUserActionsFragment(userId);
            Navigation.findNavController(view).navigate(action);
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
