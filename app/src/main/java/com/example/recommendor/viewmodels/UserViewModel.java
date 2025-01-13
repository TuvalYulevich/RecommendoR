package com.example.recommendor.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.recommendor.models.UserModel;
import com.example.recommendor.repositories.UserRepository;

import java.util.List;

public class UserViewModel extends ViewModel {
    private final UserRepository userRepository;

    public UserViewModel() {
        userRepository = new UserRepository();
    }

    public LiveData<UserModel> getUserById(String userId) {
        return userRepository.getUserById(userId);
    }

    public LiveData<List<UserModel>> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public void deleteUser(String userId) {
        userRepository.deleteUser(userId);
    }
}
