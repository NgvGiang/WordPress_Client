package vn.edu.usth.wordpressclient.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.utils.QueueManager;

public class AuthRepository {
    private static AuthRepository instance;
    private final Context context;
    private AuthRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    public static synchronized AuthRepository getInstance(Context context) {
        if (instance == null) {
            instance = new AuthRepository(context);
        }
        return instance;
    }
    public void getAccessTokenByAuthorizationCode(String authorizationCode, MutableLiveData<String> accessTokenLiveData) {
        String tokenUrl = "https://public-api.wordpress.com/oauth2/token";
        StringRequest tokenRequest = new StringRequest(
                Request.Method.POST,
                tokenUrl,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String accessToken = jsonResponse.getString("access_token");
                        Log.i("SignUpActivity", "Access Token: " + accessToken);
                        accessTokenLiveData.setValue(accessToken);
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
            protected Map<String,String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("client_id", context.getString(R.string.clientId));
                params.put("client_secret", context.getString(R.string.CLIENT_SECRET));
                params.put("code", authorizationCode);
                params.put("grant_type", "authorization_code");
                params.put("redirect_uri", context.getString(R.string.redirectUri));
                return params;
            }
        };
        QueueManager.getInstance(context).addToRequestQueue(tokenRequest);
    };//end getAccessTokenByAuthorizationCode
}
