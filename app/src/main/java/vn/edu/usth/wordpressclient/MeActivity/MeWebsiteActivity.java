package vn.edu.usth.wordpressclient.MeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.usth.wordpressclient.R;

public class MeWebsiteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_website);
        EdgeToEdge.enable(this);

        LinearLayout username_btn = findViewById(R.id.user_name_btn);
        LinearLayout profile_btn = findViewById(R.id.my_profile);
        LinearLayout account_settings_btn = findViewById(R.id.account_settings);
        LinearLayout app_settings_btn = findViewById(R.id.app_settings);
        LinearLayout help_btn = findViewById(R.id.help);
        LinearLayout share_wordpress_btn = findViewById(R.id.share_wordpress);
        LinearLayout wp_admin_btn = findViewById(R.id.wp_admin);
        LinearLayout log_out_btn = findViewById(R.id.log_out);

        username_btn.setOnClickListener(view -> startActivity(new Intent(this, UsernameActivity.class)));
        profile_btn.setOnClickListener(view -> startActivity(new Intent(this, MyProfileActivity.class)));
        account_settings_btn.setOnClickListener(view -> startActivity(new Intent(this, AccountSettingsActivity.class)));
        app_settings_btn.setOnClickListener(view -> startActivity(new Intent(this, AppSettingsActivity.class)));
        help_btn.setOnClickListener(view -> startActivity(new Intent(this, HelpActivity.class)));
        share_wordpress_btn.setOnClickListener(view -> Toast.makeText(this, "This function is under development", Toast.LENGTH_SHORT).show());
        wp_admin_btn.setOnClickListener(view -> Toast.makeText(this, "This function is under development", Toast.LENGTH_SHORT).show());
        log_out_btn.setOnClickListener(view -> Toast.makeText(this, "This function is under development", Toast.LENGTH_SHORT).show());

    }

}

