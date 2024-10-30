package com.peno.uts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
        setContentView(R.layout.activity_main); // Pastikan ini adalah layout yang benar
//ini kalau di comment jalan tapi product dan category tidak tampil jika tidak di comment app keluar forclose
        initCategories();
        initProducts();
        displayCategories();
        displayProducts("Mie Rantau");

        // Inisialisasi tombol
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
        products = new ArrayList<>()
        ;products.add(new Product("Mie Rebus", "Mie rebus dengan kuah kaldu", 18000.00, R.drawable.mierebus, "Mie Rantau"));
        products.add(new Product("Kwetiau Ayam Bakso Rantau", "Kwetiau dengan ayam dan bakso khas Rantau", 19000.00, R.drawable.kwitiauayambaksorantau, "Mie Rantau"));
        products.add(new Product("Mie Ayam Rantau Bakso", "Mie ayam dengan tambahan bakso khas Rantau", 24000.00, R.drawable.mieayamrantaubakso, "Mie Rantau"));
        products.add(new Product("Mie Ayam Rantau Manis", "Mie ayam khas Rantau dengan kuah manis", 19000.00, R.drawable.mieayamrantaumanis, "Mie Rantau"));
        products.add(new Product("Mie Ayam Yamin Rantau", "Mie ayam yamin dengan cita rasa khas Rantau", 15000.00, R.drawable.mieayamyaminrantau, "Mie Rantau"));
        products.add(new Product("Mie Doang", "Mie polos tanpa tambahan lainnya", 1000.00, R.drawable.miedoang, "Mie Rantau"));
        products.add(new Product("Mie Paket Berdua", "Paket mie untuk dua orang dengan tambahan topping", 40000.00, R.drawable.miepaketberdua, "Mie Rantau"));
        products.add(new Product("Mie Rantau Jumbo", "Mie porsi jumbo dengan tambahan topping", 20000.00, R.drawable.mierantaujumbo, "Mie Rantau"));
        products.add(new Product("Mie Rantau Original", "Mie dengan cita rasa khas original dari Rantau", 14000.00, R.drawable.mierantauoriginal, "Mie Rantau"));
        products.add(new Product("Mie Rantau Pool", "Mie Rantau dengan kuah melimpah", 24000.00, R.drawable.mierantaupool, "Mie Rantau"));


        products.add(new Product("Es Teh Manis", "Teh manis dingin segar", 4000.00, R.drawable.esteh, "Minuman"));
        products.add(new Product("Es Jeruk", "Jeruk peras segar", 7000.00, R.drawable.esjeruk, "Minuman"));
        products.add(new Product("Air Mineral", "Air mineral kemasan", 5000.00, R.drawable.airtawar, "Minuman"));
        products.add(new Product("Es Dawet", "Minuman es dengan campuran cendol dan santan", 10000.00, R.drawable.esdawet, "Minuman"));
        products.add(new Product("Es Limau", "Minuman es dengan rasa jeruk limau segar", 8000.00, R.drawable.eslimau, "Minuman"));


        products.add(new Product("Telur Mata Sapi", "Telur Mata Sapi Setengah Matang", 50000.00, R.drawable.telurmatasapi, "Extra"));
        products.add(new Product("Pangsit Goreng", "Pangsit Goreng Khas Rantau", 3000.50, R.drawable.pangsitgoreng, "Extra"));
        products.add(new Product("Sosis", "Sosis Gurih", 6000.50, R.drawable.sosis, "Extra"));
        products.add(new Product("Ayam Tabur", "Ayam Tabur Tanpa Tulang Khas Rantau", 10000.00, R.drawable.ayamtabur, "Extra"));
        products.add(new Product("Pangsit Besar", "Pangsit yang Berisikan Ayam Tabur", 8000.50, R.drawable.pangsit, "Extra"));

        products.add(new Product("Cheezy Crumble", "Dimsum ayam yang lezat", 13000.00, R.drawable.cheezycrumble, "Dimsum"));
        products.add(new Product("Dimsum Udang", "Dimsum udang segar", 15000.00, R.drawable.dimsumudang, "Dimsum"));
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
