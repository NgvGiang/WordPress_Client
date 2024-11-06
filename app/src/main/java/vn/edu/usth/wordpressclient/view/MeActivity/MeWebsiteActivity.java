package vn.edu.usth.wordpressclient.view.MeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.utils.SessionManager;

public class MeWebsiteActivity extends AppCompatActivity {
    SessionManager session;
    ImageView avatar;
    TextView userName, accountName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_website);
        EdgeToEdge.enable(this);
//        SessionManager sessionManagement = new SessionManager(this);
        session = SessionManager.getInstance(this);
        LinearLayout username_btn = findViewById(R.id.user_name_btn);
        LinearLayout profile_btn = findViewById(R.id.my_profile);
        LinearLayout account_settings_btn = findViewById(R.id.account_settings);
        LinearLayout app_settings_btn = findViewById(R.id.app_settings);
        LinearLayout help_btn = findViewById(R.id.help);
        LinearLayout share_wordpress_btn = findViewById(R.id.share_wordpress);
        LinearLayout wp_admin_btn = findViewById(R.id.wp_admin);
        LinearLayout log_out_btn = findViewById(R.id.log_out);
        userName = findViewById(R.id.user_name);
        accountName = findViewById(R.id.account_name);
        avatar = findViewById(R.id.ic_profile_placeholder);
        Intent intent = getIntent();

        userName.setText(intent.getStringExtra("username"));
        String account = "@" + intent.getStringExtra("account"); // Change to "account"
        accountName.setText(account);
        String imgUrl = intent.getStringExtra("avatar"); // Change to "avatar"
        Picasso.get()
                .load(imgUrl)
                .placeholder(R.drawable.compass)
                .error(R.drawable.compass)
                .into(avatar);
        username_btn.setOnClickListener(view -> startActivity(new Intent(this, UsernameActivity.class)));
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentProfile = new Intent(MeWebsiteActivity.this, MyProfileActivity.class);
                intentProfile.putExtra("username", userName.getText().toString());
                intentProfile.putExtra("account", accountName.getText().toString());
                startActivity(intentProfile);
            }
        });
        account_settings_btn.setOnClickListener(view -> startActivity(new Intent(this, AccountSettingsActivity.class)));
        app_settings_btn.setOnClickListener(view -> startActivity(new Intent(this, AppSettingsActivity.class)));
        help_btn.setOnClickListener(view -> startActivity(new Intent(this, HelpActivity.class)));
        share_wordpress_btn.setOnClickListener(view -> Snackbar.make(findViewById(android.R.id.content), getString(R.string.under_dev), Snackbar.LENGTH_SHORT).show());
        wp_admin_btn.setOnClickListener(view -> Snackbar.make(findViewById(android.R.id.content), getString(R.string.under_dev), Snackbar.LENGTH_SHORT).show());

        //done logout
        log_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
            }
        });//done

        Toolbar toolbar = findViewById(R.id.toolbar_activity_me);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_me));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }
}

