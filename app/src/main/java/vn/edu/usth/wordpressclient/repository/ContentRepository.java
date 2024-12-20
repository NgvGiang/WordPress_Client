package vn.edu.usth.wordpressclient.repository;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.text.HtmlCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.loader.content.CursorLoader;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.model.MediaCardModel;
import vn.edu.usth.wordpressclient.model.ContentCardModel;
import vn.edu.usth.wordpressclient.utils.SessionManager;
import vn.edu.usth.wordpressclient.utils.QueueManager;
import vn.edu.usth.wordpressclient.utils.SingleLiveEvent;
import vn.edu.usth.wordpressclient.view.media.RetrofitClient;
import vn.edu.usth.wordpressclient.view.media.WordPressApi;

public class ContentRepository {
    private static ContentRepository instance;
    private final Context context;
    private WordPressApi apiService;

    private ContentRepository(Context context) {
        this.context = context.getApplicationContext();
        this.apiService = RetrofitClient.getClient().create(WordPressApi.class);
    }

    public static synchronized ContentRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ContentRepository(context);
        }
        return instance;
    }

    //this is the function declaration, which is call from ContentViewModel
    public void createContent(String endpoint, String domain, String title, String content, String status, String date, SingleLiveEvent<Boolean> successLiveData) {
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
                        successLiveData.setValue(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        successLiveData.setValue(false);
                    }
                },
                error -> {
                    VolleyLog.d("volley", "Error: " + error.getMessage());
                    error.printStackTrace();
                    successLiveData.setValue(false);
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


    //MEDIA
    public void fetchMediaUrls(String accessToken, String domain, MutableLiveData<ArrayList<MediaCardModel>> mediaModelsLiveData){
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain +"/media" + "?per_page=100";
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

    public void uploadImageToWordPress(Uri fileUri, String accessToken, View rootview, SingleLiveEvent<Boolean> successLiveData) {
        String filePath = getRealPathFromURI(fileUri);
        if (filePath == null) {
            Snackbar.make(rootview, "Unable to get file path", Snackbar.LENGTH_SHORT).show();
            return;
        }
        File file = new File(filePath);

        // Chuẩn bị Multipart cho ảnh
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // Chuẩn bị mô tả ảnh
        Call<ResponseBody> call = apiService.uploadImage(accessToken, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    successLiveData.setValue(true);
                    Log.d("Upload", "Success: " + response.message());
                } else {
                    successLiveData.setValue(false);
                    Log.e("Upload", "Failure: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Snackbar.make(rootview, "Upload error: " + t.getMessage(), Snackbar.LENGTH_LONG).show();
                successLiveData.setValue(false);
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(context, uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        if(cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String result = cursor.getString(column_index);
            cursor.close();
            return result;
        }
        return null;
    }

    public void fetchContent(String domain, String endpoint, String status, MutableLiveData<ArrayList<ContentCardModel>> livedata) {
        //https://public-api.wordpress.com/wp/v2/sites/giangtestsite.wordpress.com/pages/?status=publish example url
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/" + endpoint + "/" + "?status=" + status+"&per_page=20";
        String accessToken = SessionManager.getInstance(context).getAccessToken();
        StringRequest fetchContentRequest = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        JSONArray responseArray = new JSONArray(response);
                        ArrayList<ContentCardModel> contentModels = new ArrayList<>();
                        for (int i = 0; i < responseArray.length(); i++) {
                            JSONObject postObject = responseArray.getJSONObject(i);
                            String title = postObject.getJSONObject("title").getString("rendered").replace("&nbsp;"," ");
                            String tempDate = postObject.getString("date");
                            long dateMillis = LocalDateTime.parse(tempDate, DateTimeFormatter.ISO_DATE_TIME)
                                    .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                            String formattedDate = DateUtils.getRelativeTimeSpanString(dateMillis, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
                            int id = postObject.getInt("id");
                            String content = HtmlCompat.fromHtml(postObject.getJSONObject("content").getString("rendered"), HtmlCompat.FROM_HTML_MODE_LEGACY).toString().replace("&nbsp;", "");
                            String link = postObject.getString("link");
                            contentModels.add(new ContentCardModel(id, title, content, formattedDate,link));

                        }
                        livedata.setValue(contentModels);

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
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };
        QueueManager.getInstance(context).addToRequestQueue(fetchContentRequest);
    }

    public void deleteContent(String endpoint, String domain, int id, SingleLiveEvent<Boolean> successLiveData){
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/" + endpoint + "/" + id + "?force=true";
        String accessToken = SessionManager.getInstance(context).getAccessToken();

        StringRequest deleteRequest = new StringRequest(
                Request.Method.DELETE,
                url,
                response -> {
                    successLiveData.setValue(true);
                    Log.i("ContentRepository", "Content deleted successfully");
                },
                error -> {
                    VolleyLog.d("volley", "Error: " + error.getMessage());
                    error.printStackTrace();
                    successLiveData.setValue(false);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        deleteRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        QueueManager.getInstance(context).addToRequestQueue(deleteRequest);
    }
    public void restoreContent(String endpoint, String domain, int id, SingleLiveEvent<Boolean> successLiveData) {
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/" + endpoint + "/" + id;
        String accessToken = SessionManager.getInstance(context).getAccessToken();

        StringRequest restoreRequest = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    successLiveData.setValue(true);
                    Log.i("ContentRepository", "Content restored successfully");
                },
                error -> {
                    VolleyLog.d("volley", "Error: " + error.getMessage());
                    error.printStackTrace();
                    successLiveData.setValue(false);
                }
        ) {
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
                params.put("status", "draft");
                return params;
            }

        };

        restoreRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        QueueManager.getInstance(context).addToRequestQueue(restoreRequest);
    }
    public void publishContent(String endpoint, String domain, int id, SingleLiveEvent<Boolean> successLiveData) {
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/" + endpoint + "/" + id;
        String accessToken = SessionManager.getInstance(context).getAccessToken();

        StringRequest restoreRequest = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    successLiveData.setValue(true);
                },
                error -> {
                    VolleyLog.d("volley", "Error: " + error.getMessage());
                    error.printStackTrace();
                    successLiveData.setValue(false);
                }
        ) {
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
                params.put("status", "publish");
                return params;
            }

        };

        restoreRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        QueueManager.getInstance(context).addToRequestQueue(restoreRequest);
    }
    public void trashContent(String endpoint, String domain, int id, SingleLiveEvent<Boolean> successLiveData){
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/" + endpoint + "/" + id ;
        String accessToken = SessionManager.getInstance(context).getAccessToken();

        StringRequest deleteRequest = new StringRequest(
                Request.Method.DELETE,
                url,
                response -> {
                    successLiveData.setValue(true);
                    Log.i("ContentRepository", "Content deleted successfully");
                },
                error -> {
                    VolleyLog.d("volley", "Error: " + error.getMessage());
                    error.printStackTrace();
                    successLiveData.setValue(false);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        deleteRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        QueueManager.getInstance(context).addToRequestQueue(deleteRequest);
    }

    public void editContent(String endpoint, String domain, int id, String title, String content, String status, String date, SingleLiveEvent<Boolean> successLiveData) {
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/" + endpoint + "/" + id;
        String accessToken = SessionManager.getInstance(context).getAccessToken();

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
                    successLiveData.setValue(true);
                    Log.i("ContentRepository", "Content edited successfully");

                },
                error -> {
                    VolleyLog.d("volley", "Error: " + error.getMessage());
                    error.printStackTrace();
                    successLiveData.setValue(false);
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
}
