package vn.edu.usth.wordpressclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class FirstLoginActivity extends AppCompatActivity {

    Button login_signup;
    Button enter_site_address;
    Button dark_light_btn;
    private boolean isDarkMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_login);

        login_signup = findViewById(R.id.login_signup_btn);
        enter_site_address = findViewById(R.id.enter_site_address_btn);
        dark_light_btn = findViewById(R.id.dark_light_btn);

        login_signup.setOnClickListener(view -> startActivity(new Intent(this, Sign_Up_Page.class)));
        enter_site_address.setOnClickListener(view -> startActivity(new Intent(this, Login_to_existing.class)));

        // Set up the toggle button click listener
        dark_light_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDarkMode) {
                    // Switch to Light Mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    // Switch to Dark Mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                isDarkMode = !isDarkMode; // Update mode state
            }
        });
    }
}

