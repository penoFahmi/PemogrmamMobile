package com.peno.mierantauptk.admin;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.peno.mierantauptk.R;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        FrameLayout containerFragment = findViewById(R.id.container_fragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set Home as default fragment
        loadFragment(new HomeFragment());

//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            Fragment selectedFragment = null;
//
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    selectedFragment = new HomeFragment();
//                    break;
//                case R.id.navigation_menu:
//                    selectedFragment = new MenuFragment();
//                    break;
//                case R.id.navigation_orders:
//                    selectedFragment = new KasirFragment();
//                    break;
//                case R.id.navigation_users:
//                    selectedFragment = new UsersFragment();
//                    break;
//                case R.id.navigation_profile:
//                    selectedFragment = new ProfileFragment();
//                    break;
//                default:
//                    return false; // Item tidak dikenal
//            }
//
//            if (selectedFragment != null) {
//                loadFragment(selectedFragment);
//            }
//
//            return true; // Indikasi item berhasil dipilih
//        });


        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.navigation_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.navigation_menu) {
                selectedFragment = new MenuFragment();
            } else if (item.getItemId() == R.id.navigation_orders) {
                selectedFragment = new KasirFragment();
//            } else if (item.getItemId() == R.id.navigation_users) {
//                selectedFragment = new UsersFragment();
            } else if (item.getItemId() == R.id.navigation_riwayat) {
                selectedFragment = new RiwayatFragment();
            } else if (item.getItemId() == R.id.navigation_profile) {
                selectedFragment = new ProfileFragment();
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
                .replace(R.id.container_fragment, fragment)
                .commit();
    }
}
