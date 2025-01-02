package com.peno.learnasynctaskloader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private EditText queryEditText;
    private Button searchButton;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        queryEditText = findViewById(R.id.queryET);
        searchButton = findViewById(R.id.searchB);
        resultTextView = findViewById(R.id.resultTV);

        searchButton.setOnClickListener(v -> {

            String query = queryEditText.getText().toString();

            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (connectivityManager != null) {
                networkInfo = connectivityManager.getActiveNetworkInfo();
            }
            if (networkInfo != null && networkInfo.isConnected() & !query.isEmpty()) {
                resultTextView.setText("Loading...");
                Bundle bundle = new Bundle();
                bundle.putString("query", query);
                LoaderManager.getInstance(this).restartLoader(0, bundle, this);
            } else {
                if (query.isEmpty()) {
                    resultTextView.setText("Please enter a search term");
                }
                else {
                resultTextView.setText("No network connection found");
            }
        });
        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }

    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String query = "";
        if (args != null) {
            query = args.getString("query");
        }
        return new BookLoader(this, query);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            int i = 0;
            String title = null;
            String authors = null;
            while (i < jsonArray.length()) {
                JSONObject book = jsonArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                try {
                    title = volumeInfo.getString("title");
                    authors = volumeInfo.getString("authors");
                } catch (Exception e) {
                    e.printStackTrace();
                }i++;
            }

            if (title != null && authors != null) {
                resultTextView.setText(title + "by" + authors);
            } else {
                resultTextView.setText("No Results Found");
            }

        } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}