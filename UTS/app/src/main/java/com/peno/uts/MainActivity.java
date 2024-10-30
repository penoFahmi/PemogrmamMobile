package com.peno.uts;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Category> categories;
    private List<Product> products;
    public static List<CartItem> cartItems = new ArrayList<>(); // Daftar keranjang bersifat static

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initCategories();
        initProducts();
        displayCategories();
        displayProducts("Mie Rantau");

        Button cartButton = findViewById(R.id.cartButton);
        cartButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void initCategories() {
        categories = new ArrayList<>();
        categories.add(new Category("Mie Rantau"));
        categories.add(new Category("Minuman"));
        categories.add(new Category("Extra"));
        categories.add(new Category("Dimsum"));
    }

    private void initProducts() {
        products = new ArrayList<>();
        products.add(new Product("Mie Telor", "Mie Telor dengan bumbu spesial", 4.50, R.drawable.mietelur, "Mie Rantau"));
        products.add(new Product("Mie Ayam", "Mie Ayam enak", 5.00, R.drawable.e, "Mie Rantau"));
        products.add(new Product("Mie Tahu", "Mie dengan potongan tahu goreng", 4.00, R.drawable.c, "Mie Rantau"));
        products.add(new Product("Nasi Goreng", "Nasi goreng lezat", 3.50, R.drawable.mietelur, "Mie Rantau"));
        products.add(new Product("Mie Rebus", "Mie rebus dengan kuah kaldu", 4.00, R.drawable.a, "Mie Rantau"));
        products.add(new Product("Es Teh Manis", "Teh manis dingin segar", 1.50, R.drawable.esteh, "Minuman"));
        products.add(new Product("Es Jeruk", "Jeruk peras segar", 2.00, R.drawable.tehes, "Minuman"));
        products.add(new Product("Air Mineral", "Air mineral kemasan", 1.00, R.drawable.airtawar, "Minuman"));
        products.add(new Product("Telur Dadar", "Telur dadar untuk pelengkap", 1.00, R.drawable.sosis, "Extra"));
        products.add(new Product("Kerupuk", "Kerupuk renyah", 0.50, R.drawable.pangsit, "Extra"));
        products.add(new Product("Dimsum Ayam", "Dimsum ayam yang lezat", 3.00, R.drawable.pangsit, "Dimsum"));
        products.add(new Product("Dimsum Udang", "Dimsum udang segar", 3.50, R.drawable.esteh, "Dimsum"));
    }

    private void displayCategories() {
        LinearLayout categoryLayout = findViewById(R.id.categoryLayout);
        categoryLayout.removeAllViews(); // Bersihkan tampilan sebelumnya

        for (Category category : categories) {
            Button categoryButton = (Button) LayoutInflater.from(this).inflate(R.layout.category_button, categoryLayout, false);
            categoryButton.setText(category.getName());
            categoryButton.setOnClickListener(view -> displayProducts(category.getName()));
            categoryLayout.addView(categoryButton);
        }
    }

    private void displayProducts(String selectedCategory) {
        LinearLayout productLayout = findViewById(R.id.productLayout);
        productLayout.removeAllViews();

        for (Product product : products) {
            if (product.getCategory().equals(selectedCategory)) {
                LinearLayout productCard = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.product_card, productLayout, false);

                TextView productName = productCard.findViewById(R.id.productName);
                TextView productDescription = productCard.findViewById(R.id.productDescription);
                TextView productPrice = productCard.findViewById(R.id.productPrice);
                ImageView productImage = productCard.findViewById(R.id.productImage);
                Button addToCartButton = productCard.findViewById(R.id.addToCartButton);

                productName.setText(product.getName());
                productDescription.setText(product.getDescription());
                productPrice.setText(String.format("RP %.2f", product.getPrice()));
                productImage.setImageResource(product.getImageResourceId());

                addToCartButton.setOnClickListener(view -> addToCart(product));
                productLayout.addView(productCard);
            }
        }
    }

    private void addToCart(Product product) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getName().equals(product.getName())) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        cartItems.add(new CartItem(product, 1));
    }
}
