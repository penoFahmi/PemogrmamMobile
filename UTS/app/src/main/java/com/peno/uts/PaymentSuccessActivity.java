package com.peno.uts;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        TextView successMessage = findViewById(R.id.successMessage);
        successMessage.setText("Pembayaran Berhasil!");
    }
}
