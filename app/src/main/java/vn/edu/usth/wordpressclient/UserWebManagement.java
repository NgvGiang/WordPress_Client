package vn.edu.usth.wordpressclient;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import vn.edu.usth.wordpressclient.MeActivity.MeWebsiteActivity;
import vn.edu.usth.wordpressclient.MeActivity.UsernameActivity;

public class UserWebManagement extends AppCompatActivity {
    RelativeLayout postsRow;
    RelativeLayout pagesRow;
    RelativeLayout mediaRow;
    RelativeLayout commentRow;
    RelativeLayout meRow;
    RelativeLayout siteSettingRow;
    RelativeLayout adminRow;
    ImageView chooseSites;
    TextView title,domain;
    ImageView siteImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_web_management);
        EdgeToEdge.enable(this);

        Intent intent = getIntent();
        String domainString = intent.getStringExtra("domain");
        String titleString = intent.getStringExtra("title");
        String imgUrl = intent.getStringExtra("imgUrl");
        title = findViewById(R.id.title);
        domain = findViewById(R.id.domain);
        siteImage = findViewById(R.id.user_pfp);
        title.setText(titleString);
        domain.setText(domainString);
        Picasso.get()
                .load(imgUrl)
                .placeholder(R.drawable.compass)
                .error(R.drawable.compass)
                .into(siteImage);
        siteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserWebManagement.this, "This function should be for changing the site picture", Toast.LENGTH_SHORT).show();
            }
        });
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserWebManagement.this, "This function should be for changing the title", Toast.LENGTH_SHORT).show();
            }
        });
        domain.setOnClickListener(v -> { //done
            String url = "https://"+domainString;
            Intent domainIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(domainIntent);
        });


        //content zone
        //call api from domain
        //mapping
        postsRow = findViewById(R.id.posts_row);
        pagesRow = findViewById(R.id.pages_row);
        mediaRow = findViewById(R.id.media_row);
        commentRow = findViewById(R.id.comment_row);
        meRow = findViewById(R.id.me_row);
        siteSettingRow = findViewById(R.id.site_setting_row);
        adminRow = findViewById(R.id.admin_row);
        chooseSites = findViewById(R.id.collapse_icon);

        //clicking
        // need to putExtra the domain to the next activity
        chooseSites.setOnClickListener(v -> {
            //need to create new destination, not Choose_your_web.class
            Intent intentChooseWeb = new Intent(UserWebManagement.this, Choose_your_web.class);

            startActivity(intentChooseWeb);
        });
        postsRow.setOnClickListener(v -> {
            Intent intentPost = new Intent(UserWebManagement.this, PostsActivity.class);
            intentPost.putExtra("domain",domainString);
            startActivity(intentPost);
        });

        pagesRow.setOnClickListener(v -> {
            Intent intentPage = new Intent(UserWebManagement.this, PagesActivity.class);
            intentPage.putExtra("domain",domainString);
            startActivity(intentPage);
        });



        mediaRow.setOnClickListener(v -> {
            Intent intentMedia = new Intent(UserWebManagement.this, WordPress_media.class);
            intentMedia.putExtra("domain",domainString);
            startActivity(intentMedia);
        });


        commentRow.setOnClickListener(v -> {
            Intent intentComment = new Intent(UserWebManagement.this, CommentActivity.class);
            intentComment.putExtra("domain",domainString);
            startActivity(intentComment);
        });

        meRow.setOnClickListener(v -> {
            Intent intentMe = new Intent(UserWebManagement.this, MeWebsiteActivity.class);
            intentMe.putExtra("domain",domainString);
            startActivity(intentMe);
        });
        siteSettingRow.setOnClickListener(v -> {
            Intent intentSetting = new Intent(UserWebManagement.this, UsernameActivity.class);
            intentSetting.putExtra("domain",domainString);
            startActivity(intentSetting);
        });

        adminRow.setOnClickListener(view ->{
            String authUrl = "https://"+domainString+"/wp-admin/";
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl));
            startActivity(browserIntent);
        });


    }
}
