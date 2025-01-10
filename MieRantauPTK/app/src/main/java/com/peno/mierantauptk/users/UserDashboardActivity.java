package com.peno.mierantauptk.users;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.peno.mierantauptk.R;

public class UserDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        FrameLayout containerFragment = findViewById(R.id.container_fragment_user);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_user);

        // Set Home as default fragment
        loadFragment(new HomeUsersFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.navigation_home_user) {
                selectedFragment = new HomeUsersFragment();
            } else if (item.getItemId() == R.id.navigation_menu_user) {
                selectedFragment = new MenuUsersFragment();
            } else if (item.getItemId() == R.id.navigation_profile_user) {
                selectedFragment = new ProfileUsersFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }

            return true; // Indikasi item berhasil dipilih
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_fragment_user, fragment)
                .commit();
    }
}