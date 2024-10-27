package vn.edu.usth.wordpressclient.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.usth.wordpressclient.R;

public class Create_new_site extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_new_site);

        Button confirm_btn = findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(v -> startActivity(new Intent(this, UserWebManagement.class)));
    }
}