package com.peno.lokale;

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

    private EditText orderET1, orderET2, orderET3;
    private Integer order1, order2, order3;
    private TextView totalPaymentTV;
    private static final int ITEM1_PRICE = 30000; // Price for item 1
    private static final int ITEM2_PRICE = 30000; // Price for item 2
    private static final int ITEM3_PRICE = 20000; // Price for item 3
    private Integer totalPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize views
        orderET1 = findViewById(R.id.orderET1);
        orderET2 = findViewById(R.id.orderET2);
        orderET3 = findViewById(R.id.orderET3);
        totalPaymentTV = findViewById(R.id.totalPaymentTV);

        // Initialize quantities
        order1 = order2 = order3 = 0;

        // Restore saved instance state if available
        if (savedInstanceState != null) {
            order1 = savedInstanceState.getInt("ORDER1");
            order2 = savedInstanceState.getInt("ORDER2");
            order3 = savedInstanceState.getInt("ORDER3");
        }

        // Update UI
        orderET1.setText(order1.toString());
        orderET2.setText(order2.toString());
        orderET3.setText(order3.toString());
        updateTotal();

        // Handle padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Method to increment order quantity for item 1
    public void plusButton1(View view) {
        order1++;
        orderET1.setText(order1.toString());
        updateTotal();
    }

    // Method to decrement order quantity for item 1
    public void minusButton1(View view) {
        if (order1 > 0) {
            order1--;
            orderET1.setText(order1.toString());
            updateTotal();
        }
    }

    // Similar methods for item 2
    public void plusButton2(View view) {
        order2++;
        orderET2.setText(order2.toString());
        updateTotal();
    }

    public void minusButton2(View view) {
        if (order2 > 0) {
            order2--;
            orderET2.setText(order2.toString());
            updateTotal();
        }
    }

    // Similar methods for item 3
    public void plusButton3(View view) {
        order3++;
        orderET3.setText(order3.toString());
        updateTotal();
    }

    public void minusButton3(View view) {
        if (order3 > 0) {
            order3--;
            orderET3.setText(order3.toString());
            updateTotal();
        }
    }

    // Calculate and update the total payment
    private void updateTotal() {
        totalPayment = (order1 * ITEM1_PRICE) + (order2 * ITEM2_PRICE) + (order3 * ITEM3_PRICE);
        totalPaymentTV.setText("Rp." + totalPayment.toString());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save order quantities to retain them during configuration changes
        outState.putInt("ORDER1", order1);
        outState.putInt("ORDER2", order2);
        outState.putInt("ORDER3", order3);
    }
}
