package com.example.recommendor.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.recommendor.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewUserInfoFragment extends Fragment {

    private String userId;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_user_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        userId = requireArguments().getString("userId");
        if (userId == null) {
            Log.e("ViewUserInfoFragment", "userId is null. Cannot fetch user info.");
            Toast.makeText(requireContext(), "User ID is missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        TextView userInfoTextView = view.findViewById(R.id.tvUserInfo);

        db.collection("users").document(userId).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        String userInfo = "Username: " + snapshot.getString("username") + "\n" +
                                "First Name: " + snapshot.getString("firstName") + "\n" +
                                "Last Name: " + snapshot.getString("lastName") + "\n" +
                                "Email: " + snapshot.getString("email") + "\n" +
                                "Date of Birth: " + snapshot.getString("dateOfBirth");
                        userInfoTextView.setText(userInfo);
                    }
                })
                .addOnFailureListener(e -> Log.e("ViewUserInfo", "Failed to fetch user info", e));

        view.findViewById(R.id.btnGoBack).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("userId", userId);
            Navigation.findNavController(view).navigate(R.id.action_ViewUserInfoFragment_to_adminUserActionsFragment, bundle);
        });
    }
}
