package com.peno.uts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CartActivity extends AppCompatActivity {

    private LinearLayout cartLayout;
    private TextView totalTextView;
    private Button payButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartLayout = findViewById(R.id.cartLayout);
        totalTextView = findViewById(R.id.totalTextView);
        payButton = findViewById(R.id.payButton);

        displayCartItems();
        updateTotal();

        payButton.setOnClickListener(view -> {
            if (MainActivity.cartItems.isEmpty()) {
                Toast.makeText(this, "Keranjang kosong, tidak ada yang bisa dibayar!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(CartActivity.this, InvoiceActivity.class);
                startActivity(intent);
            }
        });
    }

    private void displayCartItems() {
        cartLayout.removeAllViews(); // Bersihkan tampilan sebelumnya

        if (MainActivity.cartItems.isEmpty()) {
            TextView emptyMessage = new TextView(this);
            emptyMessage.setText("Keranjang Anda kosong");
            emptyMessage.setTextSize(18);
            emptyMessage.setPadding(16, 16, 16, 16);
            cartLayout.addView(emptyMessage);
            payButton.setVisibility(View.GONE); // Sembunyikan tombol bayar jika keranjang kosong
        } else {
            payButton.setVisibility(View.VISIBLE); // Tampilkan tombol bayar jika ada item

            for (CartItem item : MainActivity.cartItems) {
                LinearLayout itemLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.cart_item, cartLayout, false);

                ImageView productImage = itemLayout.findViewById(R.id.productImage);
                TextView productName = itemLayout.findViewById(R.id.productName);
                TextView productPrice = itemLayout.findViewById(R.id.productPrice);
                TextView quantityTextView = itemLayout.findViewById(R.id.quantityTextView);
                Button addButton = itemLayout.findViewById(R.id.addButton);
                Button minusButton = itemLayout.findViewById(R.id.minusButton);

                productImage.setImageResource(item.getProduct().getImageResourceId());
                productName.setText(item.getProduct().getName());
                productPrice.setText(String.format("RP %.2f", item.getProduct().getPrice()));
                quantityTextView.setText(String.valueOf(item.getQuantity()));

                // Tambah jumlah produk
                addButton.setOnClickListener(v -> {
                    item.setQuantity(item.getQuantity() + 1);
                    quantityTextView.setText(String.valueOf(item.getQuantity()));
                    updateTotal();
                });

                // Kurangi jumlah produk
                minusButton.setOnClickListener(v -> {
                    if (item.getQuantity() > 0) {
                        item.setQuantity(item.getQuantity() - 1);
                        quantityTextView.setText(String.valueOf(item.getQuantity()));
                        updateTotal();
                    }
                });

                cartLayout.addView(itemLayout);
            }
        }
    }

    private void updateTotal() {
        double total = 0.0;
        for (CartItem item : MainActivity.cartItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        totalTextView.setText(String.format("Total: RP %.2f", total));
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayCartItems(); // Perbarui tampilan saat kembali ke aktivitas
    }
}
