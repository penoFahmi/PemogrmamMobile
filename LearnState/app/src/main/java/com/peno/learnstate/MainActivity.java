package com.peno.learnstate;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText orderEditText;
    private Integer order;
    private TextView totalPaymentTextView;
    private static final int TICKET_PRICE = 50000;
    private Integer totalPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        orderEditText = findViewById(R.id.orderET);
        totalPaymentTextView = findViewById((R.id.totalPaymentTV));
        order = 0;
        // Mengambil variable yang disimpan
        if (savedInstanceState != null) {
            order = savedInstanceState.getInt("ORDER");
        }
        totalPayment = order * TICKET_PRICE;
        orderEditText.setText(order.toString());
        totalPaymentTextView.setText("Rp." + totalPayment.toString());

    }

    public void plusOrder(View view) {
        order +=1;
        totalPayment = order * TICKET_PRICE;
        orderEditText.setText(order.toString());
        totalPaymentTextView.setText("Rp." + totalPayment);
    }

    public void minusOrder(View view) {
        order -=1;
        totalPayment = order * TICKET_PRICE;
        orderEditText.setText(order.toString());
        totalPaymentTextView.setText("Rp." + totalPayment);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Menyimpan variable yang penting, Supaya tidak di reset ketika orientasi
        // layar berubah
        outState.putInt("ORDER", order);
    }
}