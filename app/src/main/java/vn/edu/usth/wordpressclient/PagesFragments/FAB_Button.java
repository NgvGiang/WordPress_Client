package vn.edu.usth.wordpressclient.PagesFragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import vn.edu.usth.wordpressclient.PagesActivity;
import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.SessionManagement;

public class FAB_Button extends AppCompatActivity {
    SessionManagement session = new SessionManagement(this);
    String accessToken = session.getAccessToken();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fab_button);



        Toolbar toolbar = findViewById(R.id.fab_toolbar);
        setSupportActionBar(toolbar);

        // Set title
        getSupportActionBar().setTitle("");
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();

        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fab_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.publish_button) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    createPageByAPI();
                }
            });

            Intent intent = new Intent(this, PagesActivity.class );
            startActivity(intent);

            return true;
        }
        else if (item.getItemId() == R.id.save_button) {
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void createPageByAPI(){
        String Url = "https://public-api.wordpress.com/wp/v2/sites/darkhent.wordpress.com/pages";

        // Lấy dữ liệu từ EditText
        EditText editTextTitle = findViewById(R.id.fab_title);
        EditText editTextContent = findViewById(R.id.fab_content);

        // Chuyển nội dung người dùng nhập thành chuỗi
        String title = editTextTitle.getText().toString();
        String content = editTextContent.getText().toString();

        JSONObject pageData = new JSONObject();
        try {
            pageData.put("title", title);
            pageData.put("content", content);
            pageData.put("status", "public");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest pageRequest = new StringRequest(
                Request.Method.POST,
                Url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String date = jsonResponse.getString("date");
                        Log.i("page created at:", date);
                        Log.i("Creating a page", "success");
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
                return "application/raw; charset=UTF-8";
            }
            @Override
            public byte[] getBody() {
                return pageData.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                // Nếu cần cung cấp token hoặc API key, có thể thêm trong phần header
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", accessToken);  // Thêm token xác thực nếu cần
                return headers;
            }
        };

    }



}