package com.peno.uts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentSuccessActivity extends AppCompatActivity {

    private ImageView checkmarkImageView;
    private TextView successMessage;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        checkmarkImageView = findViewById(R.id.checkmarkImageView);
        successMessage = findViewById(R.id.successMessage);
        continueButton = findViewById(R.id.continueButton);

        // Menampilkan animasi centang
        checkmarkImageView.setVisibility(View.VISIBLE);
        checkmarkImageView.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in));

        // Menangani klik tombol "Pesan Lagi"
        continueButton.setOnClickListener(view -> {
            Intent intent = new Intent(PaymentSuccessActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Tutup PaymentSuccessActivity
        });
    }
}
