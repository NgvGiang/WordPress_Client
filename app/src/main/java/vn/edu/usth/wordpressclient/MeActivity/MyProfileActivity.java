package vn.edu.usth.wordpressclient.MeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.SessionManager;
import vn.edu.usth.wordpressclient.models.QueueManager;


public class MyProfileActivity extends AppCompatActivity {
    LinearLayout firstNameBtn, lastNameBtn, displayNameBtn, aboutMe;
    private String domain;
    TextView firstName, lastName, displayName;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        EdgeToEdge.enable(this);


        session = SessionManager.getInstance(this);
        String accessToken = session.getAccessToken();

        firstNameBtn = findViewById(R.id.firstname_btn);
        lastNameBtn = findViewById(R.id.lastname_btn);
        displayNameBtn = findViewById(R.id.displayname_btn);
        aboutMe = findViewById(R.id.aboutme_btn);

        firstName = findViewById(R.id.first_name_status);
        lastName = findViewById(R.id.last_name_status);
        displayName = findViewById(R.id.display_name_status);

        Log.d("INTENT", "domain: " + domain);

        Toolbar toolbar = findViewById(R.id.toolbar_my_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getString(R.string.my_profile));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        getUserInfo(accessToken);
    }

    private void getUserInfo(String accessToken){
        String url = "https://public-api.wordpress.com/rest/v1.1/me/settings/";
        StringRequest fetchUserInfoReq = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        JSONObject jsonRes = new JSONObject(response);
                        firstName.setText(jsonRes.getString("first_name"));
                        lastName.setText(jsonRes.getString("last_name"));
                        displayName.setText(jsonRes.getString("display_name"));
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
        QueueManager.getInstance(this).addToRequestQueue(fetchUserInfoReq);
    }
//    private void updateUserInfo(accessToken){
//        String url = "https://public-api.wordpress.com/rest/v1.1/me/settings/";
//
//    }
}