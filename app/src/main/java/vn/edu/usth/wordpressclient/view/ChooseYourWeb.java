package vn.edu.usth.wordpressclient.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.utils.SessionManager;
import vn.edu.usth.wordpressclient.model.WebCardModel;
import vn.edu.usth.wordpressclient.view.adapter.WebDomainAdapter;
import vn.edu.usth.wordpressclient.utils.QueueManager;
import vn.edu.usth.wordpressclient.viewmodel.WebViewModel;

public class ChooseYourWeb extends AppCompatActivity {
    TextView displayName,acc_name;
    ImageView avatar;
    ArrayList<WebCardModel> webModels = new ArrayList<>();
    WebDomainAdapter adapter;
    RecyclerView recyclerView;
    private SessionManager session;
    private WebViewModel webViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session = SessionManager.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_your_web);
        EdgeToEdge.enable(this);
//        SessionManager session = new SessionManager(this);
        String accessToken = session.getAccessToken();
        Button create_site_btn = findViewById(R.id.create_site_btn);
        create_site_btn.setOnClickListener(view -> startActivity(new Intent(this, Create_new_site.class)));

        recyclerView = findViewById(R.id.web_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new WebDomainAdapter(this, webModels);
        adapter = new WebDomainAdapter(this);
        recyclerView.setAdapter(adapter);
        displayName = findViewById(R.id.display_name);
        acc_name = findViewById(R.id.acc_name);
        avatar=findViewById(R.id.profile_pic);
        webViewModel = new ViewModelProvider(this).get(WebViewModel.class);
        webViewModel.getWebModelsLiveData().observe(this,webModels->{
            adapter.setWebModels(webModels);
            adapter.notifyDataSetChanged();
        });
        webViewModel.fetchSites(accessToken);


//        fetchSites(accessToken);
        getUserInfo(accessToken);

//        WebDomainAdapter adapter = new WebDomainAdapter(this, webModels);
//        recyclerView.setAdapter(adapter);
//        webModels.add(new WebCardModel(R.drawable.compass, "Example Site 1", "www.example1.com"));
//        webModels.add(new WebCardModel(R.drawable.compass, "Example Site 2", "www.example2.com"));
//        webModels.add(new WebCardModel(R.drawable.compass, "Example Site 3", "www.example3.com"));
//        webModels.add(new WebCardModel(R.drawable.compass, "Example Site 4", "www.example4.com"));
//        webModels.add(new WebCardModel("https://img.icons8.com/?size=100&id=53372&format=png&color=000000", "Example Site 5", "www.example5.com"));
//        webModels.add(new WebCardModel("https://img.icons8.com/?size=100&id=53372&format=png&color=000000", "Example Site 6", "www.example6.com"));
//        webModels.add(new WebCardModel("https://img.icons8.com/?size=100&id=53372&format=png&color=000000", "Example Site 7", "www.example7.com"));
//        webModels.add(new WebCardModel("https://img.icons8.com/?size=100&id=53372&format=png&color=000000", "Example Site 8", "www.example8.com"));


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
        QueueManager.getInstance(this).addToRequestQueue(fetchSitesRequest);
    }

}
