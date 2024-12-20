package vn.edu.usth.wordpressclient.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.usth.wordpressclient.R;

public class Welcome_to_WordPress extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome_to_word_press);

        Button create_blog_btn = findViewById(R.id.createBlogButton);
        Button set_host_btn = findViewById(R.id.addSelfHostButton);
        TextView skip_btn = findViewById(R.id.skipButton);

        set_host_btn.setOnClickListener(view -> startActivity(new Intent(this, Login_to_existing.class)));
        skip_btn.setOnClickListener(view -> startActivity(new Intent(this, Dont_have_site_activity.class)));
    }
}