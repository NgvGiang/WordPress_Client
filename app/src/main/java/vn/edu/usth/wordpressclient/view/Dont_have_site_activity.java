package vn.edu.usth.wordpressclient.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import vn.edu.usth.wordpressclient.view.MeActivity.MeWebsiteActivity;
import vn.edu.usth.wordpressclient.R;

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

        add_new_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(findViewById(android.R.id.content), "Try JetPack App", Snackbar.LENGTH_SHORT).show();
            }
        });
        acc_n_setting_btn.setOnClickListener(view -> startActivity(new Intent(this, MeWebsiteActivity.class )));
    }
}