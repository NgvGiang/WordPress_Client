package vn.edu.usth.wordpressclient;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class UserWebManagement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_web_management);

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
        userTitle.setOnClickListener(view -> showToast("User Title under development"));
        postsRow.setOnClickListener(view -> showToast("Posts Row under development"));
        pagesRow.setOnClickListener(view -> showToast("Pages Row under development"));
        mediaRow.setOnClickListener(view -> showToast("Media Row under development"));
        commentRow.setOnClickListener(view -> showToast("Comments Row under development"));

        meRow.setOnClickListener(view -> showToast("Me Row under development"));
        siteSettingRow.setOnClickListener(view -> showToast("Site Settings Row under development"));
        adminRow.setOnClickListener(view -> showToast("Admin Row under development"));

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
