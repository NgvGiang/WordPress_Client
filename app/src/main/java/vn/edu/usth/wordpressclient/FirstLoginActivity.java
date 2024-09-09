package vn.edu.usth.wordpressclient;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class FirstLoginActivity extends AppCompatActivity {

    TextView comment_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_web_mangement);

        comment_btn = findViewById(R.id.comments_btn);

        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a toast message or handle the click event here
                Toast.makeText(FirstLoginActivity.this, "Comment button clicked!", Toast.LENGTH_SHORT).show();
            };
        });
    };
}