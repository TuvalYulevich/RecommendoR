package com.example.recommendor;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.example.recommendor.databinding.ActivityMainBinding; // Import the binding class

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    // Declare a binding instance
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Debugging: Check if the FragmentContainerView exists via the binding instance
        if (binding.navHostFragment == null) {
            Log.e(TAG, "FragmentContainerView (R.id.nav_host_fragment) is null. Check your activity_main.xml");
        } else {
            Log.d(TAG, "FragmentContainerView initialized successfully via View Binding.");
        }

        try {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            Log.d(TAG, "NavController found: " + navController);
            NavigationUI.setupActionBarWithNavController(this, navController);
            Log.d(TAG, "NavController set up successfully.");
        } catch (Exception e) {
            Log.e(TAG, "Error finding NavController: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
