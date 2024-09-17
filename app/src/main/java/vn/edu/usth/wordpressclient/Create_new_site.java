package vn.edu.usth.wordpressclient;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Create_new_site extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_new_site);

        Button confirm_btn = findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(v -> startActivity(new Intent(this,UserWebManagement.class)));
    }
}