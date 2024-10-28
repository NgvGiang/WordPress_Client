package vn.edu.usth.wordpressclient.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import vn.edu.usth.wordpressclient.MeActivity.HelpActivity;
import vn.edu.usth.wordpressclient.R;


public class Login_to_existing extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_to_existing);
        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Set title
        getSupportActionBar().setTitle(getString(R.string.title_login));
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        Button continue_login = findViewById(R.id.continue_login_btn);
        TextView enter_site_address = findViewById(R.id.find_site_btn);

        enter_site_address.setOnClickListener(view -> Snackbar.make(findViewById(android.R.id.content), "Can't find it bro :)", Snackbar.LENGTH_SHORT).show());
        continue_login.setOnClickListener(view -> startActivity(new Intent(this, Choose_your_web.class)));

    }

    // info icon
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.info_icon) {
            startActivity(new Intent(this, HelpActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

