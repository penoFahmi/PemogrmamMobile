package com.peno.uts;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private ArrayList<Product> cart;
    private double totalAmount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        cart = getIntent().getParcelableArrayListExtra("cart");

        displayCartItems();
        calculateTotal();
    }

    private void displayCartItems() {
        LinearLayout cartLayout = findViewById(R.id.cartLayout);

        for (Product product : cart) {
            TextView cartItem = new TextView(this);
            cartItem.setText(String.format("%s - $%.2f", product.getName(), product.getPrice()));
            cartLayout.addView(cartItem);
        }
    }

    private void calculateTotal() {
        for (Product product : cart) {
            totalAmount += product.getPrice();
        }

        TextView totalText = findViewById(R.id.totalAmount);
        totalText.setText(String.format("Total: $%.2f", totalAmount));
    }
}
