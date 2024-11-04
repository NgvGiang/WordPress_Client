package vn.edu.usth.wordpressclient.repository;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import vn.edu.usth.wordpressclient.model.CommentCardModel;
import vn.edu.usth.wordpressclient.model.MediaCardModel;
import vn.edu.usth.wordpressclient.utils.DomainManager;
import vn.edu.usth.wordpressclient.utils.QueueManager;
import vn.edu.usth.wordpressclient.utils.SessionManager;
import vn.edu.usth.wordpressclient.viewmodel.UserViewModel;

public class CommentRepository {
    private static CommentRepository instance;
    private final Context context;

    private CommentRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    public static synchronized CommentRepository getInstance(Context context) {
        if (instance == null) {
            instance = new CommentRepository(context);
        }
        return instance;
    }

    public void getComments(int perPage, String status, MutableLiveData<ArrayList<CommentCardModel>> commentModelLiveData) {
        String accessToken = SessionManager.getInstance(context).getAccessToken();
        String domain = DomainManager.getInstance().getSelectedDomain();

        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/comments?per_page=" + perPage + "&status=" + status;
//        Log.i("url", url);

        StringRequest fetchCommentRequest = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try{
                        ArrayList<CommentCardModel> commentModels = new ArrayList<>();
                        JSONArray commentArray = new JSONArray(response);
                        int length = commentArray.length();
                        for (int i=0; i<length; i++) {
                            JSONObject commentArrayJSONObject = commentArray.getJSONObject(i);
                            int commentId = commentArrayJSONObject.getInt("id");
                            int postId = commentArrayJSONObject.getInt("post");
                            int authorId = commentArrayJSONObject.getInt("author");
                            String authorName = commentArrayJSONObject.getString("author_name");
                            String date = commentArrayJSONObject.getString("date");
                            String content = commentArrayJSONObject.getJSONObject("content").getString("rendered");
                            String link = commentArrayJSONObject.getString("link");
                            String cmtStatus = commentArrayJSONObject.getString("status");
                            String authorAvatar = commentArrayJSONObject.getJSONObject("author_avatar_urls").getString("48");
                            commentModels.add(new CommentCardModel(commentId, postId, authorId, authorName, date, content, link, cmtStatus, authorAvatar));
                        }
                        commentModelLiveData.setValue(commentModels);
                    } catch (JSONException e){
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    VolleyLog.d("volley", "Error: " + error.getMessage());
                    error.printStackTrace();
                }
        ) {
            @Override
            public Map<String,String> getHeaders(){
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };
        QueueManager.getInstance(context).addToRequestQueue(fetchCommentRequest);
    }

    public void replyComment(String domain, String content, int parent, int post, MutableLiveData<String> successLiveData) {
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/comments/";
        SessionManager session = SessionManager.getInstance(context);
        String accessToken = session.getAccessToken();
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("post", post);
            jsonBody.put("parent", parent);
            jsonBody.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.POST,
            url,
            jsonBody,
            response -> {

                try {
//                    long commentId = response.getLong("id");
//                    long postId = response.getLong("post");
//                    long parentId = response.getLong("parent");
//                    long authorId = response.getLong("author");
//                    String authorName = response.getString("author_name");
                    String date = response.getString("date");
                    String cmtContent = response.getJSONObject("content").getString("rendered");
//                    String status = response.getString("status");
//                    String authorAvatar = response.getJSONObject("author_avatar_urls").getString("48");
//                    CommentCardModel newComment = new CommentCardModel(commentId, parentId, postId, authorId, authorName, date, cmtContent, status, authorAvatar);
                    Log.i("ContentRepository", "Content created at: " + date);

                    successLiveData.postValue(cmtContent);
                } catch (JSONException e) {
                    successLiveData.postValue("");
                    throw new RuntimeException(e);
                }
            },
            error -> {
                successLiveData.postValue("");
                VolleyLog.d("volley", "Error: " + error.getMessage());
                error.printStackTrace();
            }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };
        QueueManager.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void updateCommentStatus(int id, String status, MutableLiveData<Boolean> successLiveData) {
        String domain = DomainManager.getInstance().getSelectedDomain();
        String accessToken = SessionManager.getInstance(context).getAccessToken();

        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/comments/" + id;
        Log.i("url", url);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("response", response.getString("status"));
                            successLiveData.postValue(true);
                        } catch (JSONException e) {
                            successLiveData.postValue(false);
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        successLiveData.postValue(false);
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };
        QueueManager.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void deleteComment(int id, MutableLiveData<Boolean> successLiveData) {
        String domain = DomainManager.getInstance().getSelectedDomain();
        String accessToken = SessionManager.getInstance(context).getAccessToken();
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/comments/" + id +"?force=true";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        successLiveData.postValue(true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        successLiveData.postValue(false);
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };
        QueueManager.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void getUnrepliedComment(int perPage, int author, MutableLiveData<ArrayList<CommentCardModel>> commentModelLiveData) {
        String accessToken = SessionManager.getInstance(context).getAccessToken();
        String domain = DomainManager.getInstance().getSelectedDomain();

        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/comments?per_page=" + perPage + "&author_exclude=" + author;
        Log.i("get unreplied url", url);
        StringRequest fetchCommentRequest = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try{
                        ArrayList<CommentCardModel> commentModels = new ArrayList<>();
                        JSONArray commentArray = new JSONArray(response);
                        int length = commentArray.length();
                        for (int i=0; i<length; i++) {
                            JSONObject commentArrayJSONObject = commentArray.getJSONObject(i);
                            int commentId = commentArrayJSONObject.getInt("id");
                            int postId = commentArrayJSONObject.getInt("post");
                            int authorId = commentArrayJSONObject.getInt("author");
                            String authorName = commentArrayJSONObject.getString("author_name");
                            String date = commentArrayJSONObject.getString("date");
                            String content = commentArrayJSONObject.getJSONObject("content").getString("rendered");
                            String link = commentArrayJSONObject.getString("link");
                            String cmtStatus = commentArrayJSONObject.getString("status");
                            String authorAvatar = commentArrayJSONObject.getJSONObject("author_avatar_urls").getString("48");
                            boolean hasChildren = commentArrayJSONObject.getJSONObject("_links").has("children");
                            if (!hasChildren) {
                                commentModels.add(new CommentCardModel(commentId, postId, authorId, authorName, date, content, link, cmtStatus, authorAvatar));
                            }
//                            commentModels.add(new CommentCardModel(commentId, postId, authorId, authorName, date, content, cmtStatus, authorAvatar));
                        }
                        commentModelLiveData.setValue(commentModels);
                    } catch (JSONException e){
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    VolleyLog.d("volley", "Error: " + error.getMessage());
                    error.printStackTrace();
                }
        ) {
            @Override
            public Map<String,String> getHeaders(){
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };
        QueueManager.getInstance(context).addToRequestQueue(fetchCommentRequest);
    }

    public void getPostOfComment(int id, MutableLiveData<JSONObject> postOfComment) {
        String accessToken = SessionManager.getInstance(context).getAccessToken();
        String domain = DomainManager.getInstance().getSelectedDomain();

        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/posts/" + id;
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try{
                        JSONObject post = new JSONObject(response);
                        String title = post.getJSONObject("title").getString("rendered");
                        int authorId = post.getInt("author");

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("title", title);
                        jsonObject.put("authorId", authorId);
                        postOfComment.postValue(jsonObject);
                    } catch (JSONException e){
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    VolleyLog.d("volley", "Error: " + error.getMessage());
                    error.printStackTrace();
                }
        );
        QueueManager.getInstance(context).addToRequestQueue(stringRequest);
    }
}
