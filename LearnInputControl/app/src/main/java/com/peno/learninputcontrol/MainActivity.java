package com.peno.learninputcontrol;

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


        String gender = (isFemale) ? "Female" : "Male";
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String message = name + " " + email + " " + phone + " " + gender;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}