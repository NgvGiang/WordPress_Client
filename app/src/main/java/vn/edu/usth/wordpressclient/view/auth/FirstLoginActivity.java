package vn.edu.usth.wordpressclient.view.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.utils.SessionManager;
import vn.edu.usth.wordpressclient.view.ChooseYourWeb;
import vn.edu.usth.wordpressclient.view.Dont_have_site_activity;
import vn.edu.usth.wordpressclient.viewmodel.AuthViewModel;
import vn.edu.usth.wordpressclient.viewmodel.WebViewModel;

public class FirstLoginActivity extends AppCompatActivity {
    Button login_signup;
    TextView enter_site_address;
    private SessionManager session;
    WebViewModel webViewModel;
    AuthViewModel authViewModel;
    ProgressBar progressBar;
    @Override
    protected void onStart(){
        super.onStart();
        if (session.isLoggedIn()) {
            moveToMainActivity();
        }
    }
    private void moveToMainActivity() {
        webViewModel.fetchSites(session.getAccessToken());
        webViewModel.getSiteNumber().observe(this,siteNumber->{
            Intent intent;
            if(siteNumber == 0){
                intent = new Intent(FirstLoginActivity.this, Dont_have_site_activity.class);
                startActivity(intent);
                Log.i("FirstLoginActivity.this","DONT HAVE ANY SITE");
            }else{
                Log.i("FirstLoginActivity.this", "Num site: " + siteNumber);
                intent = new Intent(FirstLoginActivity.this, ChooseYourWeb.class);
                startActivity(intent);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);


        progressBar.setVisibility(View.VISIBLE);

        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith("myapp://oauth2redirect")) {
            Log.d("SignUpActivity", "onResume called with URI: " + uri);

            String authorizationCode = uri.getQueryParameter("code");

            if (authorizationCode != null) {

                getAccessTokenByAuthorizationCode(authorizationCode);
                getIntent().setData(null);
                Log.i("SignUpActivity", "Authorization Code: " + authorizationCode);
            } else {
                Log.e("SignUpActivity", "Authorization code is null");
            }
        } else {
            Log.d("SignUpActivity", "No URI found");
        }


    }//end onResume
    private void getAccessTokenByAuthorizationCode(String authorizationCode) {
        authViewModel.getAccessTokenByAuthorizationCode(authorizationCode);
        authViewModel.getAccessTokenLiveData().observe(this, accessToken -> {
                    if (accessToken != null) {
                        session.createLoginSession(accessToken);
                        moveToMainActivity();
                    } else {
                        Log.e("SignUpActivity", "Access token is null");
                    }
                });

    };//end getAccessTokenByAuthorizationCode
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_login);

        session = SessionManager.getInstance(getApplicationContext());

        webViewModel = new ViewModelProvider(this).get(WebViewModel.class);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        login_signup = findViewById(R.id.login_signup_btn);
        enter_site_address = findViewById(R.id.enter_site_address_btn);
        progressBar = findViewById(R.id.progress_bar);

        login_signup.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
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

