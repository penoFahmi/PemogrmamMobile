package com.peno.learnasynctaskloader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
        setContentView(R.layout.activity_main);

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
            if (networkInfo != null && networkInfo.isConnected() && !query.isEmpty()) {
                resultTextView.setText("Loading...");
                Bundle bundle = new Bundle();
                bundle.putString("query", query);
                LoaderManager.getInstance(this).restartLoader(0, bundle, this);
            } else {
                resultTextView.setText(query.isEmpty() ? "Please enter a search term" : "No network connection found");
            }
        });

        // Initialize the Loader
        if (LoaderManager.getInstance(this).getLoader(0) != null) {
            LoaderManager.getInstance(this).initLoader(0, null, this);
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String query = "";
        if (args != null) {query = args.getString("query");}
        return new BookLoader(this, query);
    }


    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            int i = 0;
            StringBuilder resultBuilder = new StringBuilder();
            while (i < jsonArray.length()) {
                JSONObject book = jsonArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                String title = volumeInfo.optString("title", "No Title");
                JSONArray authorsArray = volumeInfo.optJSONArray("authors");

                String authors = "Unknown Author";
                if (authorsArray != null) {
                    authors = authorsArray.join(", ").replace("\"", "");
                }

                resultBuilder.append(title).append(" by ").append(authors).append("\n");
                i++;
            }

            resultTextView.setText(resultBuilder.length() > 0 ? resultBuilder.toString() : "No Results Found");

        } catch (JSONException e) {
            resultTextView.setText("Error parsing results");
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        // No action needed
    }
}
