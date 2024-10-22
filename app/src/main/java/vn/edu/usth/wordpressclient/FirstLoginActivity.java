package vn.edu.usth.wordpressclient;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import vn.edu.usth.wordpressclient.models.MySingleton;

public class FirstLoginActivity extends AppCompatActivity {
    Button login_signup;
    TextView enter_site_address;
    private SessionManagement session;

    @Override
    protected void onStart(){
        super.onStart();
        session = SessionManagement.getInstance(this);
//        SessionManagement session = new SessionManagement(this);
//        SessionManagement session = SessionManagement.getInstance(this);
        if (session.isLoggedIn()) {
            moveToMainActivity();
            finish();
        }
    }

    private void moveToMainActivity() {
//        SessionManagement session = new SessionManagement(this);
//        SessionManagement session = SessionManagement.getInstance(this);
        String accessToken = session.getAccessToken();
        String url = "https://public-api.wordpress.com/rest/v1.1/me/sites";
        StringRequest getSiteRequest = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray sitesArray = jsonResponse.getJSONArray("sites");
                        int numberOfSites = sitesArray.length();
                        Log.i("FirstLoginActivity.this", "Sites array: " + sitesArray);
                        if (numberOfSites == 0) {
                            Intent intent = new Intent(FirstLoginActivity.this, Dont_have_site_activity.class);
                            startActivity(intent);
                            Log.i("FirstLoginActivity.this","DONT HAVE ANY SITE");
                        }else{
                            Log.i("FirstLoginActivity.this", "Num site: " + numberOfSites);
                            Intent intent = new Intent(FirstLoginActivity.this, Choose_your_web.class);
                            startActivity(intent);
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    VolleyLog.d("volley", "Error: " + error.getMessage());
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
        MySingleton.getInstance(this).addToRequestQueue(getSiteRequest);
//        Volley.newRequestQueue(this).add(getSiteRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith("myapp://oauth2redirect")) {
            Log.d("SignUpActivity", "onResume called with URI: " + uri);

            String authorizationCode = uri.getQueryParameter("code");

            if (authorizationCode != null) {
                getAccessTokenByAuthorizationCode(authorizationCode);
                Log.i("SignUpActivity", "Authorization Code: " + authorizationCode);
            } else {
                Log.e("SignUpActivity", "Authorization code is null");
            }
        } else {
            Log.d("SignUpActivity", "No URI found");
        }
    }//end onResume

    private void getAccessTokenByAuthorizationCode(String authorizationCode) {
        String tokenUrl = "https://public-api.wordpress.com/oauth2/token";
        StringRequest tokenRequest = new StringRequest(
                Request.Method.POST,
                tokenUrl,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String accessToken = jsonResponse.getString("access_token");
                        Log.i("SignUpActivity", "Access Token: " + accessToken);
                        onLoginSuccess(accessToken);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    VolleyLog.d("volley", "Error: " + error.getMessage());
                    error.printStackTrace();
                }
        ){
            @Override
            public String getBodyContentType(){
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("client_id", "107157");
                params.put("client_secret", getString(R.string.CLIENT_SECRET));
                params.put("code", authorizationCode);
                params.put("grant_type", "authorization_code");
                params.put("redirect_uri", "myapp://oauth2redirect");
                return params;
            }
        };
//            Volley.newRequestQueue(this).add(tokenRequest);
            MySingleton.getInstance(this).addToRequestQueue(tokenRequest);
    };//end getAccessTokenByAuthorizationCode

    private void onLoginSuccess(String accessToken) {
//        SessionManagement session = new SessionManagement(this);
        session.createLoginSession(accessToken);
        moveToMainActivity();
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_login);


        login_signup = findViewById(R.id.login_signup_btn);
        enter_site_address = findViewById(R.id.enter_site_address_btn);

        login_signup.setOnClickListener(view -> {
            String authUrl = "https://public-api.wordpress.com/oauth2/authorize"
                    + "?client_id=" + getString(R.string.clientId)
                    + "&redirect_uri=" + getString(R.string.redirectUri)
                    + "&response_type=code"
                    + "&scope=global";
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl));
            startActivity(browserIntent);
        });

    }//end onCreate
}

