package vn.edu.usth.wordpressclient.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import vn.edu.usth.wordpressclient.view.MeActivity.HelpActivity;
import vn.edu.usth.wordpressclient.R;

public class Sign_Up_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RelativeLayout google_btn = findViewById(R.id.google_button);
        Button continue_btn = findViewById(R.id.continue_login_btn);

        // Set the title
        getSupportActionBar().setTitle(getString(R.string.title_get_started));

        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        //buttons
        continue_btn.setOnClickListener(view -> startActivity(new Intent(this, sign_up_choose_email.class)));

        google_btn.setOnClickListener(View -> Snackbar.make(findViewById(android.R.id.content),getString(R.string.under_dev), Snackbar.LENGTH_SHORT).show());
    }

    //info icon
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.info_icon) {
            // Handle click
            startActivity(new Intent(this, HelpActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
