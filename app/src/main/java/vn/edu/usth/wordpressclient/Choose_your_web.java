package vn.edu.usth.wordpressclient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.edu.usth.wordpressclient.models.MySingleton;

public class Choose_your_web extends AppCompatActivity {
    TextView displayName,acc_name;
    ImageView avatar;
    ArrayList<Web_card_model> webModels = new ArrayList<>();
    Web_domain_adapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_your_web); // Set your layout
        EdgeToEdge.enable(this);
        SessionManagement session = new SessionManagement(this);
        String accessToken = session.getAccessToken();
        Button create_site_btn = findViewById(R.id.create_site_btn);
        create_site_btn.setOnClickListener(view -> startActivity(new Intent(this, Create_new_site.class)));

        recyclerView = findViewById(R.id.web_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Web_domain_adapter(this, webModels);
        recyclerView.setAdapter(adapter);
        displayName = findViewById(R.id.display_name);
        acc_name = findViewById(R.id.acc_name);
        avatar=findViewById(R.id.profile_pic);
        fetchSites(accessToken);
        getUserInfo(accessToken);

//        Web_domain_adapter adapter = new Web_domain_adapter(this, webModels);
//        recyclerView.setAdapter(adapter);
//        webModels.add(new Web_card_model(R.drawable.compass, "Example Site 1", "www.example1.com"));
//        webModels.add(new Web_card_model(R.drawable.compass, "Example Site 2", "www.example2.com"));
//        webModels.add(new Web_card_model(R.drawable.compass, "Example Site 3", "www.example3.com"));
//        webModels.add(new Web_card_model(R.drawable.compass, "Example Site 4", "www.example4.com"));
//        webModels.add(new Web_card_model("https://img.icons8.com/?size=100&id=53372&format=png&color=000000", "Example Site 5", "www.example5.com"));
//        webModels.add(new Web_card_model("https://img.icons8.com/?size=100&id=53372&format=png&color=000000", "Example Site 6", "www.example6.com"));
//        webModels.add(new Web_card_model("https://img.icons8.com/?size=100&id=53372&format=png&color=000000", "Example Site 7", "www.example7.com"));
//        webModels.add(new Web_card_model("https://img.icons8.com/?size=100&id=53372&format=png&color=000000", "Example Site 8", "www.example8.com"));




    }

    private void getUserInfo(String accessToken){
        String url = "https://public-api.wordpress.com/rest/v1.1/me";
        StringRequest fetchSitesRequest = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        // Handle the JSON response here
                        displayName.setText(jsonResponse.getString("display_name"));
                        acc_name.setText(jsonResponse.getString("username"));
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
        MySingleton.getInstance(this).addToRequestQueue(fetchSitesRequest);
    }
    private void fetchSites(String accessToken){
        String url = "https://public-api.wordpress.com/rest/v1.1/me/sites";
        StringRequest fetchSitesRequest = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray sitesArray = jsonResponse.getJSONArray("sites");
                        // Handle the JSON response here
                        for (int i =0;i<sitesArray.length();i++){
                            JSONObject sitesArrayJSONObject = sitesArray.getJSONObject(i);
                            String siteTitle = sitesArrayJSONObject.getString("name");
                            String siteDomain = sitesArrayJSONObject.getString("URL");
                            String urlIcon;
                            Log.i("Name: ",siteTitle);
                            Log.i("Domain: ",siteDomain);
                            if (sitesArrayJSONObject.has("icon")){
                                urlIcon = sitesArrayJSONObject.getJSONObject("icon").getString("img");
                            }else{
                                urlIcon = "https://img.icons8.com/?size=100&id=53372&format=png&color=000000";
                            }
                            webModels.add(new Web_card_model(urlIcon, siteDomain, siteTitle));

                            adapter.notifyDataSetChanged();
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
//                headers.put("Authorization", "Bearer " + "cDkEb*pBTQI#Vg3dXv9C&6mkvKxOEUyIkLgEgrKxWxc*s#0OfJP&r$ll1neX$K#5");
                return headers;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(fetchSitesRequest);

    }
}
