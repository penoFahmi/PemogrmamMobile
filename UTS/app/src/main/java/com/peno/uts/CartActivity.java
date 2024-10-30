package com.peno.uts;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        LinearLayout cartLayout = findViewById(R.id.cartLayout);
        for (CartItem item : MainActivity.cartItems) {
            TextView textView = new TextView(this);
            textView.setText(item.getProduct().getName() + " x " + item.getQuantity());
            cartLayout.addView(textView);
        }
    }
}
