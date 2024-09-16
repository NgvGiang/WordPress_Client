package vn.edu.usth.wordpressclient;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

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


        //clicking
        userTitle.setOnClickListener(view -> startActivity(new Intent(this, Choose_your_web.class)));
        postsRow.setOnClickListener(view -> startActivity(new Intent(this, PostsActivity.class)));
        pagesRow.setOnClickListener(view -> startActivity(new Intent(this, PagesActivity.class)));
        mediaRow.setOnClickListener(view -> startActivity(new Intent(this, WordPress_media.class)));
        commentRow.setOnClickListener(view -> startActivity(new Intent(this, CommentActivity.class)));

        meRow.setOnClickListener(view -> Toast.makeText(this, "nah", Toast.LENGTH_SHORT).show());
        siteSettingRow.setOnClickListener(view -> Toast.makeText(this, "nah", Toast.LENGTH_SHORT).show());
        adminRow.setOnClickListener(view -> Toast.makeText(this, "nah", Toast.LENGTH_SHORT).show());
    }
}
