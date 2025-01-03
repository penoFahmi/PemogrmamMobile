package com.peno.mierantau;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, new HomeFragment())
                .commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, new HomeFragment())
                            .commit();
                    return true;
                case R.id.nav_produk:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, new ProdukFragment())
                            .commit();
                    return true;
                case R.id.nav_pembelian:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, new PembelianFragment())
                            .commit();
                    return true;
                case R.id.nav_profile:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, new ProfileFragment())
                            .commit();
                    return true;
            }
            return false;
        });
    }
}
