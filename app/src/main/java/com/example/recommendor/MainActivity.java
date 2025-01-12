package com.example.recommendor;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.fragment.app.FragmentContainerView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Debugging: Check if the FragmentContainerView exists
        FragmentContainerView container = findViewById(R.id.nav_host_fragment);
        if (container == null) {
            Log.e(TAG, "FragmentContainerView (R.id.nav_host_fragment) is null. Check your activity_main.xml");
        } else {
            Log.d(TAG, "FragmentContainerView initialized successfully.");
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
