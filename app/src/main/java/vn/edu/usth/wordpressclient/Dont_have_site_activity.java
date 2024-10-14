package vn.edu.usth.wordpressclient;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

import vn.edu.usth.wordpressclient.MeActivity.MeWebsiteActivity;

public class Dont_have_site_activity extends AppCompatActivity {
    TextView email;
    ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dont_have_site);
        email= findViewById(R.id.email);
        avatar = findViewById(R.id.avatar);
        Button add_new_page = findViewById(R.id.add_new_site_btn);
        LinearLayout acc_n_setting_btn = findViewById(R.id.acc_n_setting_btn);

        add_new_page.setOnClickListener(view -> startActivity(new Intent(this, Create_new_site.class )));
        acc_n_setting_btn.setOnClickListener(view -> startActivity(new Intent(this, MeWebsiteActivity.class )));
    }
}