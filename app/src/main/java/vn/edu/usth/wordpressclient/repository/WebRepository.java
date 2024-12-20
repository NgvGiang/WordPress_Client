package vn.edu.usth.wordpressclient.repository;

import android.app.UiModeManager;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.model.WebCardModel;
import vn.edu.usth.wordpressclient.utils.QueueManager;

public class WebRepository {
    private static WebRepository instance;
    private final Context context;
    private final MutableLiveData<Boolean> updateTitleStatus = new MutableLiveData<>();
    private WebRepository(Context context) {
        this.context = context.getApplicationContext();
    }
    public static synchronized WebRepository getInstance(Context context){
        if (instance == null){
            instance = new WebRepository(context);
        }
        return instance;
    }
    //this is the function declaration, which is call from WebViewModel
    public void fetchSites(String accessToken, MutableLiveData<ArrayList<WebCardModel>> webModelsLiveData,MutableLiveData<Integer> siteNumber) {
        String url = "https://public-api.wordpress.com/rest/v1.1/me/sites";
        StringRequest fetchSitesRequest = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        ArrayList<WebCardModel> webModels = new ArrayList<>();
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray sitesArray = jsonResponse.getJSONArray("sites");
                        int sitesArrayLength = sitesArray.length();
                        siteNumber.setValue(sitesArrayLength);
                        // Handle the JSON response here
                        for (int i =0;i<sitesArrayLength;i++){
                            JSONObject sitesArrayJSONObject = sitesArray.getJSONObject(i);
                            String siteTitle = sitesArrayJSONObject.getString("name");
                            String siteDomain = sitesArrayJSONObject.getString("URL");
                            String urlIcon;
                            UiModeManager modeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
                            if (sitesArrayJSONObject.has("icon")){
                                urlIcon = sitesArrayJSONObject.getJSONObject("icon").getString("img");

                            }else {
                                if(modeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES){
                                    //night mode on
                                    urlIcon = "https://img.icons8.com/?size=100&id=53372&format=png&color=ffffff";
                                }else{
                                    urlIcon = "https://img.icons8.com/?size=100&id=53372&format=png&color=000000";
                                }
                            }

                            webModels.add(new WebCardModel(urlIcon, siteDomain, siteTitle));

                        }
                        webModelsLiveData.setValue(webModels);
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

    public void updateSiteTitle(String accessToken, String domain, String newTitle){
        String url = "https://public-api.wordpress.com/wp/v2/sites/" +domain + "/settings";
        StringRequest updateTitleRequest = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                },
                error -> {
                    VolleyLog.d("volley", "Error: " + error.getMessage());
                    error.printStackTrace();
                }
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
            @Override
            public String getBodyContentType(){
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String,String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title", newTitle);
                return params;
            }
        };
        QueueManager.getInstance(context).addToRequestQueue(updateTitleRequest);
    }
}
