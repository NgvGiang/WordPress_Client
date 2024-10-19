package vn.edu.usth.wordpressclient.MeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.SessionManagement;
import vn.edu.usth.wordpressclient.models.MySingleton;

public class MeWebsiteActivity extends AppCompatActivity {
    LinearLayout username_btn, profile_btn, account_settings_btn, app_settings_btn,
            help_btn, share_wordpress_btn, wp_admin_btn, log_out_btn;
    private String domain;
    TextView displayName, accountName;
    ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_website);
        EdgeToEdge.enable(this);
        SessionManagement sessionManagement = new SessionManagement(this);

        SessionManagement session = new SessionManagement(this);
        String accessToken = session.getAccessToken();

        Intent intent = getIntent();
        domain = intent.getStringExtra("domain");

        profile_btn = findViewById(R.id.my_profile);
        account_settings_btn = findViewById(R.id.account_settings);
        app_settings_btn = findViewById(R.id.app_settings);
        help_btn = findViewById(R.id.help);
        share_wordpress_btn = findViewById(R.id.share_wordpress);
        wp_admin_btn = findViewById(R.id.wp_admin);
        log_out_btn = findViewById(R.id.log_out);

        displayName = findViewById(R.id.user_name);
        accountName = findViewById(R.id.account_name);

        //username_btn.setOnClickListener(view -> startActivity(new Intent(this, UsernameActivity.class)));
        profile_btn.setOnClickListener(view -> startActivity(new Intent(this, MyProfileActivity.class)));
        account_settings_btn.setOnClickListener(view -> startActivity(new Intent(this, AccountSettingsActivity.class)));
        app_settings_btn.setOnClickListener(view -> startActivity(new Intent(this, AppSettingsActivity.class)));
        help_btn.setOnClickListener(view -> startActivity(new Intent(this, HelpActivity.class)));
        share_wordpress_btn.setOnClickListener(view -> Toast.makeText(this, getString(R.string.under_dev), Toast.LENGTH_SHORT).show());
        wp_admin_btn.setOnClickListener(view -> Toast.makeText(this, getString(R.string.under_dev), Toast.LENGTH_SHORT).show());

        getUserInfo(accessToken);

        //done logout
        log_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManagement.logoutUser();
            }
        });//done

        Toolbar toolbar = findViewById(R.id.toolbar_activity_me);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_me));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

    }
    private void getUserInfo(String accessToken){
        String url = "https://public-api.wordpress.com/rest/v1.1/me";
        StringRequest fetchUserInfoRequest = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        displayName.setText(jsonResponse.getString("display_name"));
                        accountName.setText(jsonResponse.getString("username"));
                        String path = jsonResponse.getString("avatar_URL");
                        Picasso.get()
                                .load(path)
                                .placeholder(R.drawable.blank_avatar)
                                .error(R.drawable.blank_avatar)
                                .fit()
                                .into(avatar);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    VolleyLog.d("Volley", "Error" + error.getMessage());
                    error.printStackTrace();
                }
        ){
            @Override
            public Map<String,String> getHeaders(){
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(fetchUserInfoRequest);
    }
}