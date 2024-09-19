package vn.edu.usth.wordpressclient;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.edu.usth.wordpressclient.MeActivity.MeWebsiteActivity;

public class Dont_have_site_activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dont_have_site);

        Button add_new_page = findViewById(R.id.add_new_site_btn);
        LinearLayout acc_n_setting_btn = findViewById(R.id.acc_n_setting_btn);

        add_new_page.setOnClickListener(view -> startActivity(new Intent(this, Create_new_site.class )));
        acc_n_setting_btn.setOnClickListener(view -> startActivity(new Intent(this, MeWebsiteActivity.class )));
    }
}