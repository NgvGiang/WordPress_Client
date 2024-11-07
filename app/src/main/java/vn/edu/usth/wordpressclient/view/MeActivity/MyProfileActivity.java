package vn.edu.usth.wordpressclient.view.MeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import vn.edu.usth.wordpressclient.R;

public class MyProfileActivity extends AppCompatActivity {
    TextView username, account,midName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        EdgeToEdge.enable(this);
        Toolbar toolbar = findViewById(R.id.toolbar_my_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.my_profile));
        Intent intentProfile = getIntent();
        String usernameString = intentProfile.getStringExtra("username");

        username = findViewById(R.id.first_name_status);
        account = findViewById(R.id.display_name_status);
        midName = findViewById(R.id.last_name_status);
        midName.setVisibility(View.GONE);
        username.setText(usernameString);
        account.setText(usernameString);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

    }
}