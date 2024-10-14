package vn.edu.usth.wordpressclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Choose_your_web extends AppCompatActivity {
    TextView username,email;
    ImageView avatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_your_web); // Set your layout
        EdgeToEdge.enable(this);

        Button create_site_btn = findViewById(R.id.create_site_btn);
        create_site_btn.setOnClickListener(view -> startActivity(new Intent(this, Create_new_site.class)));

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
        email = findViewById(R.id.acc_name);
        username=findViewById(R.id.user_name);
        avatar=findViewById(R.id.profile_pic);
//        email.setText("Test Email");
//        username.setText("Test Username");
//        Picasso.get().
//                load("https://1.gravatar.com/avatar/75ee090ecda1c3d0d749dce3408fad21c7843790fafd7a2264f2e1b1a8c3dbd7?s=96&d=identicon")
//                .placeholder(R.drawable.blank_avatar)
//                .error(R.drawable.blank_avatar)
//                .fit()
//                .into(avatar);


    }

    private void getUserInfo(String accessToken){}
}
