package vn.edu.usth.wordpressclient.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import vn.edu.usth.wordpressclient.utils.QueueManager;

public class UserRepository {
    private static UserRepository instance;
    private final Context context;
    private UserRepository(Context context) {
        this.context = context.getApplicationContext();
    }
    public static synchronized UserRepository getInstance(Context context){
        if(instance == null){
            instance= new UserRepository(context);
        }
        return instance;
    }
    public void getUserInfo(String accessToken, MutableLiveData<JSONObject> userInfoLiveData){
        String url = "https://public-api.wordpress.com/rest/v1.1/me";
        StringRequest fetchSitesRequest = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        userInfoLiveData.setValue(jsonResponse);
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
        QueueManager.getInstance(context).addToRequestQueue(fetchSitesRequest);
    }

    
}
