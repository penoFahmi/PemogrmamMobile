package com.peno.uts;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InvoiceActivity extends AppCompatActivity {

    private TextView totalInvoiceTextView, storeNameTextView, storeAddressTextView, purchaseDateTextView, customerNameTextView;
    private LinearLayout productListLayout;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        // Inisialisasi View
        totalInvoiceTextView = findViewById(R.id.totalInvoiceTextView);
        storeNameTextView = findViewById(R.id.storeNameTextView);
        storeAddressTextView = findViewById(R.id.storeAddressTextView);
        purchaseDateTextView = findViewById(R.id.purchaseDateTextView);
        customerNameTextView = findViewById(R.id.customerNameTextView);
        productListLayout = findViewById(R.id.productListLayout);
        confirmButton = findViewById(R.id.confirmButton);

        // Set Nama Toko dan Alamat
        storeNameTextView.setText("Nama Toko: Mie Rantau");
        storeAddressTextView.setText("Alamat: Jl. Merdeka No.123");

        // Set Tanggal Pembelian
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        purchaseDateTextView.setText("Tanggal Pembelian: " + currentDate);

        // Set Nama Pembeli (Contoh Nama, Bisa Ditambah Fungsi Login)
        String customerName = "Nama Pembeli: Customer"; // Ubah sesuai dengan data login
        customerNameTextView.setText(customerName);

        // Tampilkan Produk dalam Keranjang
        displayCartItems();

        // Hitung Total Pembayaran
        double total = calculateTotal();
        totalInvoiceTextView.setText(String.format("Total Pembayaran: RP %.2f", total));

        // Tombol Konfirmasi Pembayaran
        confirmButton.setOnClickListener(view -> {
            // Mengosongkan keranjang setelah konfirmasi pembayaran
            MainActivity.cartItems.clear();
            Intent intent = new Intent(InvoiceActivity.this, PaymentSuccessActivity.class);
            startActivity(intent);
            finish(); // Tutup InvoiceActivity
        });
    }

    private void displayCartItems() {
        for (CartItem item : MainActivity.cartItems) {
            View productItemView = LayoutInflater.from(this).inflate(R.layout.product_item, productListLayout, false);

            TextView productNameTextView = productItemView.findViewById(R.id.productNameTextView);
            TextView productQuantityTextView = productItemView.findViewById(R.id.productQuantityTextView);
            TextView productPriceTextView = productItemView.findViewById(R.id.productPriceTextView);

            productNameTextView.setText(item.getProduct().getName());
            productQuantityTextView.setText("Jumlah: " + item.getQuantity());
            productPriceTextView.setText("Harga: RP " + (item.getProduct().getPrice() * item.getQuantity()));

            productListLayout.addView(productItemView);
        }
    }

    private double calculateTotal() {
        double total = 0.0;
        for (CartItem item : MainActivity.cartItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }
}
