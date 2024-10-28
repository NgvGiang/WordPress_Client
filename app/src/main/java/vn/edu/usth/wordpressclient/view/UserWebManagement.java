package vn.edu.usth.wordpressclient.view;

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

import vn.edu.usth.wordpressclient.view.comment.CommentActivity;
import vn.edu.usth.wordpressclient.utils.DomainManager;
import vn.edu.usth.wordpressclient.MeActivity.MeWebsiteActivity;
import vn.edu.usth.wordpressclient.MeActivity.UsernameActivity;
import vn.edu.usth.wordpressclient.view.media.MediaActivity;
import vn.edu.usth.wordpressclient.view.pages.PagesActivity;
import vn.edu.usth.wordpressclient.view.posts.PostsActivity;
import vn.edu.usth.wordpressclient.R;

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
    DomainManager domainManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_web_management);
        EdgeToEdge.enable(this);

        Intent intent = getIntent();
//        String domainString = intent.getStringExtra("domain");
        domainManager = DomainManager.getInstance();
        String domainString = domainManager.getSelectedDomain();
        Log.i("Domain:",domainString);
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

        chooseSites.setOnClickListener(v -> {
            //need to create new destination, not ChooseYourWeb.class
//            Intent intentChooseWeb = new Intent(UserWebManagement.this, ChooseYourWeb.class);
//
//            startActivity(intentChooseWeb);
            finish();
        });
        postsRow.setOnClickListener(v -> {
            Intent intentPost = new Intent(UserWebManagement.this, PostsActivity.class);

            startActivity(intentPost);
        });

        pagesRow.setOnClickListener(v -> {
            Intent intentPage = new Intent(UserWebManagement.this, PagesActivity.class);

            startActivity(intentPage);
        });



        mediaRow.setOnClickListener(v -> {
            Intent intentMedia = new Intent(UserWebManagement.this, MediaActivity.class);

            startActivity(intentMedia);
        });


        commentRow.setOnClickListener(v -> {
            Intent intentComment = new Intent(UserWebManagement.this, CommentActivity.class);

            startActivity(intentComment);
        });

        meRow.setOnClickListener(v -> {
            Intent intentMe = new Intent(UserWebManagement.this, MeWebsiteActivity.class);

            startActivity(intentMe);
        });
        siteSettingRow.setOnClickListener(v -> {
            Intent intentSetting = new Intent(UserWebManagement.this, UsernameActivity.class);

            startActivity(intentSetting);
        });

        adminRow.setOnClickListener(view ->{
            String authUrl = "https://"+domainString+"/wp-admin/";
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl));
            startActivity(browserIntent);
        });


    }
}