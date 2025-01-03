package com.peno.mierantau;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private ViewPager2 promoViewPager;
    private RecyclerView recyclerViewProduk;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        promoViewPager = view.findViewById(R.id.promoViewPager);
        recyclerViewProduk = view.findViewById(R.id.recyclerViewProduk);

        setupPromoSlider();
        setupProdukRecycler();

        return view;
    }

    private void setupPromoSlider() {
        List<Integer> promoImages = Arrays.asList(
                R.drawable.promo1, R.drawable.promo2, R.drawable.promo3
        );

        PromoAdapter promoAdapter = new PromoAdapter(promoImages);
        promoViewPager.setAdapter(promoAdapter);

        // Auto-slide
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int currentPage = 0;

            @Override
            public void run() {
                if (currentPage == promoImages.size()) {
                    currentPage = 0;
                }
                promoViewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    private void setupProdukRecycler() {
        recyclerViewProduk.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Ambil data dari SQLite
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        List<Produk> produkList = dbHelper.getAllProduk();

        ProdukAdapter produkAdapter = new ProdukAdapter(produkList);
        recyclerViewProduk.setAdapter(produkAdapter);
    }
}

