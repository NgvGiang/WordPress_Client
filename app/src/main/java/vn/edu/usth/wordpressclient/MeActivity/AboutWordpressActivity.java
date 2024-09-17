package vn.edu.usth.wordpressclient.MeActivity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.usth.wordpressclient.R;

public class AboutWordpressActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_wordpress);
        EdgeToEdge.enable(this);
    }
}