package vn.edu.usth.wordpressclient.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.utils.SessionManager;
import vn.edu.usth.wordpressclient.view.adapter.WebDomainAdapter;
import vn.edu.usth.wordpressclient.viewmodel.UserViewModel;
import vn.edu.usth.wordpressclient.viewmodel.WebViewModel;

public class ChooseYourWeb extends AppCompatActivity {
    TextView displayName,acc_name;
    ImageView avatar;
    WebDomainAdapter adapter;
    RecyclerView recyclerView;
    private SessionManager session;
    private WebViewModel webViewModel;
    private UserViewModel userViewModel;
    SwipeRefreshLayout swipeRefreshLayout ;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_your_web);
        EdgeToEdge.enable(this);
        session = SessionManager.getInstance(this);
        String accessToken = session.getAccessToken();
        Button create_site_btn = findViewById(R.id.create_site_btn);
        create_site_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(findViewById(android.R.id.content), "Try JetPack app", Snackbar.LENGTH_SHORT).show();
            }
        });
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
//        progressBar = findViewById(R.id.progress_bar);

        recyclerView = findViewById(R.id.web_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new WebDomainAdapter(this);
        recyclerView.setAdapter(adapter);
        displayName = findViewById(R.id.display_name);
        acc_name = findViewById(R.id.acc_name);
        avatar=findViewById(R.id.profile_pic);
        webViewModel = new ViewModelProvider(this).get(WebViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        fetchAndObserveData(accessToken);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchAndObserveData(accessToken);
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        String accessToken = session.getAccessToken();
        fetchAndObserveData(accessToken);
    }


    private void fetchAndObserveData(String accessToken) {
        webViewModel.getWebModelsLiveData().observe(this, webModels -> {
            adapter.setWebModels(webModels);
            adapter.notifyDataSetChanged();
        });
        webViewModel.fetchSites(accessToken);

        userViewModel.getUserInfo(accessToken);
        userViewModel.getUserInfoLiveData().observe(this, JSONLiveData -> {
            try {
                displayName.setText(JSONLiveData.getString("display_name"));
                acc_name.setText(JSONLiveData.getString("username"));
                String path = JSONLiveData.getString("avatar_URL");
                Picasso.get()
                        .load(path)
                        .placeholder(R.drawable.blank_avatar)
                        .error(R.drawable.blank_avatar)
                        .fit()
                        .into(avatar);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }





}
