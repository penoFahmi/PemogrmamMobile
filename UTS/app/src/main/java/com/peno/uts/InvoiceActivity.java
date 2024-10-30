package com.peno.uts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class InvoiceActivity extends AppCompatActivity {

    private TextView totalInvoiceTextView;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        totalInvoiceTextView = findViewById(R.id.totalInvoiceTextView);
        confirmButton = findViewById(R.id.confirmButton);

        double total = calculateTotal();
        totalInvoiceTextView.setText(String.format("Total Pembayaran: RP %.2f", total));

        confirmButton.setOnClickListener(view -> {
            // Mengosongkan keranjang setelah konfirmasi pembayaran
            MainActivity.cartItems.clear();
            Intent intent = new Intent(InvoiceActivity.this, PaymentSuccessActivity.class);
            startActivity(intent);
            finish(); // Tutup InvoiceActivity
        });
    }

    private double calculateTotal() {
        double total = 0.0;
        for (CartItem item : MainActivity.cartItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }
}
