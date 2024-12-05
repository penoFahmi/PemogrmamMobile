package com.peno.learninputcontrol;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;

    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;

    private Boolean isFemale;
    private CheckBox mobileCheckBox;
    private CheckBox webCheckBox;
    private CheckBox desktopCheckBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mobileCheckBox = findViewById(R.id.mobliCB);
        webCheckBox = findViewById(R.id.webCB);
        desktopCheckBox = findViewById(R.id.desktopCB);

        nameEditText = findViewById(R.id.fullNameET);
        emailEditText = findViewById(R.id.emailET);
        phoneEditText = findViewById(R.id.phoneET);

        maleRadioButton = findViewById(R.id.maleRB);
        femaleRadioButton = findViewById(R.id.femaleRB);

        maleRadioButton.setOnClickListener(view -> {
            isFemale = false;
        });
        femaleRadioButton.setOnClickListener(view -> {
            isFemale = true;
        });

        //ini untuk mengambil data dari shared preferences
        SharedPreferences preferences = getApplicationContext().getSharedPreferences
                ("PENYIMPANAN_JAROT", Context.MODE_PRIVATE);
        String name = preferences.getString("NAMA", "");
        String email = preferences.getString("EMAIL", "");
        String phone = preferences.getString("PHONE", "");
        isFemale = preferences.getBoolean("IS_FEMALE", false);
        String courses = preferences.getString("COURSES", "");
        if (courses.contains("Mobile Programming")) {
            mobileCheckBox.setChecked(true);
        }
        if (courses.contains("Web Programming")) {
            webCheckBox.setChecked(true);
            }
        if (courses.contains("Desktop Programming")) {
            desktopCheckBox.setChecked(true);
        }

        //ini untuk memasukkan data ke edit text
        nameEditText.setText(name);
        emailEditText.setText(email);
        phoneEditText.setText(phone);
        maleRadioButton.setChecked(!isFemale);
        femaleRadioButton.setChecked(!isFemale);
        mobileCheckBox.setChecked(courses.contains("Mobile Programming"));
        webCheckBox.setChecked(courses.contains("Web Programming"));
        desktopCheckBox.setChecked(courses.contains("Desktop Programming"));


    }


    public void submit(View view) {

        StringBuilder courses = new StringBuilder();
        if (mobileCheckBox.isChecked()) {
            courses.append("Mobile Programming, ");
        }
        if (webCheckBox.isChecked()) {
            courses.append("Web Programming, ");
        }
        if (desktopCheckBox.isChecked()) {
            courses.append("Desktop Programming, ");
        }


  //      String gender = (isFemale) ? "Female" : "Male";
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
  //      String message = name + " " + email + " " + phone + " " + gender;
  //      Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        //ini untuk menyimpan data ke shared preferences
        SharedPreferences preferences = getApplicationContext().getSharedPreferences
                ("PENYIMPANAN_JAROT", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        //ini untuk memasukkan data ke shared preferences
        editor.putString("NAMA", name);
        editor.putString("EMAIL", email);
        editor.putString("PHONE", phone);
        editor.putBoolean("IS_FEMALE", isFemale);
        editor.putString("COURSES", courses.toString());
        editor.apply();

    }
    public void reset(View view) {
        //ini untuk menghapus data dari shared preferences
        nameEditText.setText("");
        emailEditText.setText("");
        phoneEditText.setText("");
        maleRadioButton.setChecked(true);
        femaleRadioButton.setChecked(false);
        mobileCheckBox.setChecked(false);
        webCheckBox.setChecked(false);
        desktopCheckBox.setChecked(false);

        //ini untuk menghapus data dari shared preferences
        SharedPreferences preferences = getApplicationContext().getSharedPreferences
                ("PENYIMPANAN_JAROT", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        //ini untuk memasukkan data ke shared preferences
        editor.remove("NAMA");
        editor.remove("EMAIL");
        editor.remove("PHONE");
        editor.remove("IS_FEMALE");
        editor.remove("COURSES");
        editor.apply();

    }
}