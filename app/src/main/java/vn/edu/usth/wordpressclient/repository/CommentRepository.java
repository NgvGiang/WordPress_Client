package vn.edu.usth.wordpressclient.repository;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
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
import vn.edu.usth.wordpressclient.utils.QueueManager;
import vn.edu.usth.wordpressclient.utils.SessionManager;

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

    public void getComments(String accessToken, String domain, int perPage, String status, MutableLiveData<ArrayList<CommentCardModel>> commentModelLiveData) {
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/comments?per_page=" + perPage + "&status=" + status;

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
                            long commentId = commentArrayJSONObject.getLong("id");
                            long postId = commentArrayJSONObject.getLong("post");
                            long parentId = commentArrayJSONObject.getLong("parent");
                            long authorId = commentArrayJSONObject.getLong("author");
                            String authorName = commentArrayJSONObject.getString("author_name");
                            String date = commentArrayJSONObject.getString("date");
                            String content = commentArrayJSONObject.getJSONObject("content").getString("rendered");
                            String cmtStatus = commentArrayJSONObject.getString("status");
                            String authorAvatar = commentArrayJSONObject.getJSONObject("author_avatar_urls").getString("48");
                            commentModels.add(new CommentCardModel(commentId, parentId, postId, authorId, authorName, date, content, cmtStatus, authorAvatar));
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
}
