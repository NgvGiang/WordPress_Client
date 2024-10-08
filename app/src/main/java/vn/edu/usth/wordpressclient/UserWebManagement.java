package vn.edu.usth.wordpressclient;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import vn.edu.usth.wordpressclient.MeActivity.MeWebsiteActivity;
import vn.edu.usth.wordpressclient.MeActivity.UsernameActivity;

public class UserWebManagement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_web_management);
        EdgeToEdge.enable(this);

        //mapping
        LinearLayout userTitle = findViewById(R.id.user_title);
        RelativeLayout postsRow = findViewById(R.id.posts_row);
        RelativeLayout pagesRow = findViewById(R.id.pages_row);
        RelativeLayout mediaRow = findViewById(R.id.media_row);
        RelativeLayout commentRow = findViewById(R.id.comment_row);

        RelativeLayout meRow = findViewById(R.id.me_row);
        RelativeLayout siteSettingRow = findViewById(R.id.site_setting_row);
        RelativeLayout adminRow = findViewById(R.id.admin_row);
        FloatingActionButton fab = findViewById(R.id.fab);

        //clicking
        userTitle.setOnClickListener(view -> startActivity(new Intent(this, Choose_your_web.class)));
        postsRow.setOnClickListener(view -> startActivity(new Intent(this, PostsActivity.class)));
        pagesRow.setOnClickListener(view -> startActivity(new Intent(this, PagesActivity.class)));
        mediaRow.setOnClickListener(view -> startActivity(new Intent(this, WordPress_media.class)));
        commentRow.setOnClickListener(view -> startActivity(new Intent(this, CommentActivity.class)));

        meRow.setOnClickListener(view ->  startActivity(new Intent(this, MeWebsiteActivity.class)));
        siteSettingRow.setOnClickListener(view ->  startActivity(new Intent(this, UsernameActivity.class)));
        adminRow.setOnClickListener(view -> Toast.makeText(this, getString(R.string.under_dev), Toast.LENGTH_SHORT).show());

        fab.setOnClickListener(view -> Toast.makeText(this, getString(R.string.under_dev), Toast.LENGTH_SHORT).show());

    }
}
