package vn.edu.usth.wordpressclient.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import vn.edu.usth.wordpressclient.model.MediaCardModel;
import vn.edu.usth.wordpressclient.utils.SessionManager;
import vn.edu.usth.wordpressclient.utils.QueueManager;

public class ContentRepository {
    private static ContentRepository instance;
    private final Context context;

    private ContentRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    public static synchronized ContentRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ContentRepository(context);
        }
        return instance;
    }

    //this is the function declaration, which is call from ContentViewModel
    public void createContent(String endpoint, String domain, String title, String content, String status, String date, MutableLiveData<Boolean> successLiveData) {
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/" + endpoint;
        SessionManager session = SessionManager.getInstance(context);
        String accessToken = session.getAccessToken();

        JSONObject contentData = new JSONObject();
        try {
            contentData.put("title", title);
            contentData.put("content", content);
            contentData.put("status", status);
            if (date != null && !date.isEmpty()) {
                contentData.put("date", date + ":00");
            } else {
                Calendar calendar = Calendar.getInstance();
                date = String.format("%d-%02d-%02dT%02d:%02d:%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
                contentData.put("date", date);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest contentRequest = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String responseDate = jsonResponse.getString("date");
                        Log.i("ContentRepository", "Content created at: " + responseDate);
                        successLiveData.postValue(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        successLiveData.postValue(false);
                    }
                },
                error -> {
                    VolleyLog.d("volley", "Error: " + error.getMessage());
                    error.printStackTrace();
                    successLiveData.postValue(false);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=UTF-8";
            }

            @Override
            public byte[] getBody() {
                return contentData.toString().getBytes(StandardCharsets.UTF_8);
            }
        };

        contentRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        QueueManager.getInstance(context).addToRequestQueue(contentRequest);
    }

    public void fetchMediaUrls(String accessToken, String domain, MutableLiveData<ArrayList<MediaCardModel>> mediaModelsLiveData){
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain +"/media";
        StringRequest fetchMediaUrlsRequest = new StringRequest(
            Request.Method.GET,
            url,
            response -> {
                try{
                    ArrayList<MediaCardModel> mediaModels = new ArrayList<>();
                    JSONArray mediaArray = new JSONArray(response);
                    for (int i=0;i<mediaArray.length(); i++) {
                        JSONObject mediaUrlsArrayJSONObject = mediaArray.getJSONObject(i);
                        String sourceUrl = mediaUrlsArrayJSONObject.getString("source_url");
                        mediaModels.add(new MediaCardModel(sourceUrl));
                    }
                    mediaModelsLiveData.setValue(mediaModels);
                } catch (JSONException e){
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
        QueueManager.getInstance(context).addToRequestQueue(fetchMediaUrlsRequest);
    }

//    public void fetchContent(String domain,String endpoint,String status) {
//        //https://public-api.wordpress.com/wp/v2/sites/giangtestsite.wordpress.com/pages/?status=publish example url
//        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/" + endpoint + "/" + "?status=" + status;
//        String accessToken = SessionManager.getInstance(context).getAccessToken();
//        StringRequest fetchContentRequest = new StringRequest(
//                Request.Method.GET,
//                url,
//                response -> {
//                    try {
//                        JSONArray responseArray = new JSONArray(response);
//                        // Handle the response and update the LiveData
//
//                        contentLiveData.postValue(contentList);
//
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//                },
//                error -> {
//
//                }
//        ){};
//    }
}
