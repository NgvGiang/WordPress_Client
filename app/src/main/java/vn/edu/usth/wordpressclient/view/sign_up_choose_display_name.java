package vn.edu.usth.wordpressclient.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.usth.wordpressclient.R;

public class sign_up_choose_display_name extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_choose_display_name);

        Button create_account = findViewById(R.id.doneButton);

        create_account.setOnClickListener(view -> startActivity(new Intent(this, Welcome_to_WordPress.class)));



    }
}