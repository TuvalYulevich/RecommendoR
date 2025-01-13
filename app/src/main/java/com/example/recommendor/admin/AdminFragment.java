package com.example.recommendor.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recommendor.R;
import com.example.recommendor.adapters.UserAdapter;
import com.example.recommendor.models.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AdminFragment extends Fragment {

    private FirebaseFirestore db;
    private UserAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("FragmentLifecycle", "Fragment loaded: AdminFragment");

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        Log.d("AdminFragment", "FirebaseFirestore initialized: " + (db != null));

        RecyclerView recyclerView = view.findViewById(R.id.userRecyclerView);
        TextView noUsersTextView = view.findViewById(R.id.tvNoUsers);
        TextView userCountTextView = view.findViewById(R.id.tvUserCount);

        // Listen to user data changes and update the count
        db.collection("users").addSnapshotListener((snapshots, error) -> {
            if (error != null) {
                Log.e("AdminFragment", "Failed to fetch user count", error);
                return;
            }
            if (snapshots != null) {
                int userCount = snapshots.size();
                userCountTextView.setText("Registered Users: " + userCount);
            }
        });

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        Query query = db.collection("users").orderBy("username", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        adapter = new UserAdapter(options, user -> {
            // Navigate to AdminUserActionsFragment with userId
            Bundle bundle = new Bundle();
            bundle.putString("userId", user.getId());
            Navigation.findNavController(view).navigate(R.id.action_adminFragment_to_adminUserActionsFragment, bundle);
        });

        recyclerView.setAdapter(adapter);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                noUsersTextView.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        });

        view.findViewById(R.id.btnLogout).setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_adminFragment_to_welcomeFragment);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
