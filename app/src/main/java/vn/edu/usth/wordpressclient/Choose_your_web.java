package vn.edu.usth.wordpressclient;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Choose_your_web extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_your_web); // Set your layout

        Button create_site_btn = findViewById(R.id.create_site_btn);
        create_site_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Creating site, please wait", Toast.LENGTH_SHORT).show();
            }
        });

        //data
        ArrayList<Web_card_model> webModels = new ArrayList<>();
        webModels.add(new Web_card_model(R.drawable.compass, "Example Site 1", "www.example1.com"));
        webModels.add(new Web_card_model(R.drawable.compass, "Example Site 2", "www.example2.com"));
        webModels.add(new Web_card_model(R.drawable.compass, "Example Site 3", "www.example3.com"));
        webModels.add(new Web_card_model(R.drawable.compass, "Example Site 4", "www.example4.com"));
        webModels.add(new Web_card_model(R.drawable.compass, "Example Site 5", "www.example5.com"));
        webModels.add(new Web_card_model(R.drawable.compass, "Example Site 6", "www.example6.com"));
        webModels.add(new Web_card_model(R.drawable.compass, "Example Site 7", "www.example7.com"));
        webModels.add(new Web_card_model(R.drawable.compass, "Example Site 8", "www.example8.com"));


        //set up
        RecyclerView recyclerView = findViewById(R.id.web_recycler_view);
        Web_domain_adapter adapter = new Web_domain_adapter(this, webModels);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
