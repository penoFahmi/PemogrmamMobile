package com.peno.mierantau;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Mie Ayam Pedas", "Mie ayam dengan level pedas ekstra", 20000));
        productList.add(new Product("Mie Ayam Original", "Mie ayam original khas Mie Rantau", 15000));

        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);
    }
}
