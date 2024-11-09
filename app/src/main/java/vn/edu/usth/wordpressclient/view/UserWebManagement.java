package vn.edu.usth.wordpressclient.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.utils.DomainManager;
import vn.edu.usth.wordpressclient.utils.SessionManager;
import vn.edu.usth.wordpressclient.view.MeActivity.MeWebsiteActivity;
import vn.edu.usth.wordpressclient.view.MeActivity.UsernameActivity;
import vn.edu.usth.wordpressclient.view.comment.CommentActivity;
import vn.edu.usth.wordpressclient.view.media.MediaActivity;
import vn.edu.usth.wordpressclient.view.pages.PagesActivity;
import vn.edu.usth.wordpressclient.view.posts.PostsActivity;
import vn.edu.usth.wordpressclient.viewmodel.UserViewModel;
import vn.edu.usth.wordpressclient.viewmodel.WebViewModel;

public class UserWebManagement extends AppCompatActivity {
    RelativeLayout postsRow,pagesRow,mediaRow,commentRow,meRow,siteSettingRow,adminRow;
    ImageView chooseSites,siteImage,me_icon;
    TextView title,domain,dialogTitle,dialogMessage;
    private WebViewModel webViewModel;
    String accessToken,domainString,userName,accountName,avatarUrl;
    UserViewModel userViewModel;
    Intent intentMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_web_management);
        EdgeToEdge.enable(this);
        webViewModel = new ViewModelProvider(this).get(WebViewModel.class);
        Intent intent = getIntent();
        accessToken = SessionManager.getInstance(this).getAccessToken();
        domainString = DomainManager.getInstance().getSelectedDomain();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        String titleString = intent.getStringExtra("title");
        String imgUrl = intent.getStringExtra("imgUrl");
//        String avatarUrl = intent.getStringExtra("avatar");
        title = findViewById(R.id.title);
        domain = findViewById(R.id.domain);
        siteImage = findViewById(R.id.user_pfp);
        title.setText(titleString);
        domain.setText(domainString);
        me_icon=findViewById(R.id.me_icon);

        Picasso.get()
                .load(imgUrl)
                .placeholder(R.drawable.compass)
                .error(R.drawable.compass)
                .into(siteImage);
        userViewModel.getUserInfo(accessToken);
        userViewModel.getUserInfoLiveData().observe(this,user ->{
            try {
                intentMe = new Intent(UserWebManagement.this, MeWebsiteActivity.class);

                avatarUrl = user.getString("avatar_URL");
                userName = user.getString("display_name");
                accountName = user.getString("username");
                intentMe.putExtra("username", userName);
                intentMe.putExtra("account", accountName);
                intentMe.putExtra("avatar", avatarUrl);
                Picasso.get()
                .load(avatarUrl)
                .placeholder(R.drawable.compass)
                .error(R.drawable.compass)
                .into(me_icon);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        siteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(findViewById(android.R.id.content), "This function should be for changing the site picture", Snackbar.LENGTH_SHORT).show();
            }
        });
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeTitleDialog();
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

    private void showChangeTitleDialog() {

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog, null);
        EditText input = dialogView.findViewById(R.id.dialog_input);
        dialogTitle = dialogView.findViewById(R.id.dialog_title);
        dialogMessage = dialogView.findViewById(R.id.dialog_message);
        dialogTitle.setText("Change Title");

        input.setText(title.getText().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(UserWebManagement.this);
        builder.setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    if (!title.getText().toString().equals(input.getText().toString())){
                        webViewModel.updateSiteTitle(accessToken,domainString,input.getText().toString());
                        title.setText(input.getText().toString());
                        Snackbar.make(findViewById(android.R.id.content), "Title updated", Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("CANCEL", (dialog, which) -> {
                    dialog.dismiss();
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(UserWebManagement.this, R.drawable.overflow_menu_bg));
        alertDialog.show();
    }


}
