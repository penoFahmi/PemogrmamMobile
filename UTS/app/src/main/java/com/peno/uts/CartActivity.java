package com.peno.uts;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CartActivity extends AppCompatActivity {

    private int quantity = 0;
    private TextView quantityText;
    private TextView priceText;
    private static final double PRICE_PER_ITEM = 10000; // Contoh harga per item

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        quantityText = findViewById(R.id.quantity_text);
        priceText = findViewById(R.id.price_text);

        Button buttonPlus = findViewById(R.id.button_plus);
        Button buttonMinus = findViewById(R.id.button_minus);

        buttonPlus.setOnClickListener(v -> {
            quantity++;
            updateUI();
        });

        buttonMinus.setOnClickListener(v -> {
            if (quantity > 0) {
                quantity--;
                updateUI();
            }
        });
    }

    private void updateUI() {
        quantityText.setText(String.valueOf(quantity));
        double totalPrice = quantity * PRICE_PER_ITEM;
        priceText.setText("Total Harga: Rp " + totalPrice);
    }
}
