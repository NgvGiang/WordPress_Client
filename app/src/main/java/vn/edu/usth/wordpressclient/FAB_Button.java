package vn.edu.usth.wordpressclient;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class FAB_Button extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fab_button);

        Toolbar toolbar = findViewById(R.id.fab_toolbar);
        setSupportActionBar(toolbar);
        // Set title
        getSupportActionBar().setTitle("");
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.vert_dot));


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fab_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.publish_button) {
            Toast.makeText(this, "Published", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (item.getItemId() == R.id.save_button) {
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

}