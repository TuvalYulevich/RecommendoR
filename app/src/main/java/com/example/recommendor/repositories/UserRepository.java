package com.example.recommendor.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recommendor.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final FirebaseFirestore db;

    public UserRepository() {
        db = FirebaseFirestore.getInstance();
    }

    // LiveData for a single user
    public LiveData<UserModel> getUserById(String userId) {
        MutableLiveData<UserModel> liveData = new MutableLiveData<>();
        db.collection("users").document(userId).addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                liveData.setValue(null); // Handle errors gracefully
                return;
            }
            if (snapshot != null && snapshot.exists()) {
                liveData.setValue(snapshot.toObject(UserModel.class));
            }
        });
        return liveData;
    }

    // LiveData for all users
    // LiveData for all users
    public LiveData<List<UserModel>> getAllUsers() {
        MutableLiveData<List<UserModel>> liveData = new MutableLiveData<>();
        db.collection("users").addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                liveData.setValue(null); // Handle errors gracefully
                return;
            }
            if (snapshots != null) {
                List<UserModel> users = new ArrayList<>();
                for (DocumentSnapshot doc : snapshots.getDocuments()) {
                    UserModel user = doc.toObject(UserModel.class);
                    if (user != null) {
                        user.setId(doc.getId()); // Set the Firestore document ID
                        users.add(user);
                    }
                }
                liveData.setValue(users);
            }
        });
        return liveData;
    }

    public void updateUser(String userId, UserModel user, OnCompleteListener<Void> listener) {
        db.collection("users").document(userId).set(user).addOnCompleteListener(listener);
    }
    public void deleteUser(String userId) {
        db.collection("users").document(userId).delete();
    }
}
