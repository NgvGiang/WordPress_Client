package vn.edu.usth.wordpressclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class FirstLoginActivity extends AppCompatActivity {

    Button login_signup;
    TextView enter_site_address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_login);


        login_signup = findViewById(R.id.login_signup_btn);
        enter_site_address = findViewById(R.id.enter_site_address_btn);


        login_signup.setOnClickListener(view -> startActivity(new Intent(this, Sign_Up_Page.class)));
        enter_site_address.setOnClickListener(view -> startActivity(new Intent(this, Login_to_existing.class)));

    }
}

