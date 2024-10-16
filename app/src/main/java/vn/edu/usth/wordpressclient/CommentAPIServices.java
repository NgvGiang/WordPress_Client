package vn.edu.usth.wordpressclient;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import vn.edu.usth.wordpressclient.models.Comment;
import vn.edu.usth.wordpressclient.models.CommentsCallback;

public class CommentAPIServices {
    public static void getAllCommentsFromUser(Context context, String domain, int perPage, int currentPage, CommentsCallback callback) {
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/comments?per_page=" + perPage + "&page=" + currentPage;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        int size = response.length();
                        List<Comment> newWaveOfcomments = new ArrayList<>();
                        IntStream.range(0, size).forEach(index -> {
                            try {
                                long commentId = response.getJSONObject(index).getLong("id");
                                long postId = response.getJSONObject(index).getLong("post");
                                long parentId = response.getJSONObject(index).getLong("parent");
                                long authorId = response.getJSONObject(index).getLong("author");
                                String authorName = response.getJSONObject(index).getString("author_name");
                                String authorUrl = response.getJSONObject(index).getString("author_url");
                                String date = response.getJSONObject(index).getString("date");
                                String content = response.getJSONObject(index).getJSONObject("content").getString("rendered");
                                String link = response.getJSONObject(index).getString("link");
                                String status = response.getJSONObject(index).getString("status");
                                String authorAvatar = response.getJSONObject(index).getJSONObject("author_avatar_urls").getString("48");
                                Comment comment = new Comment(commentId, postId, parentId, authorId, authorName, authorUrl, date, content, link, status, authorAvatar);
                                newWaveOfcomments.add(comment);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        callback.onSuccess(newWaveOfcomments);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    public static void getApprovedCommentFromUser(Context context, String domain, int perPage, int currentPage, CommentsCallback callback) {
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/comments?per_page=" + perPage + "&page=" + currentPage + "&status=approve";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        int size = response.length();
                        List<Comment> newWaveOfcomments = new ArrayList<>();
                        IntStream.range(0, size).forEach(index -> {
                            try {
                                long commentId = response.getJSONObject(index).getLong("id");
                                long postId = response.getJSONObject(index).getLong("post");
                                long parentId = response.getJSONObject(index).getLong("parent");
                                long authorId = response.getJSONObject(index).getLong("author");
                                String authorName = response.getJSONObject(index).getString("author_name");
                                String authorUrl = response.getJSONObject(index).getString("author_url");
                                String date = response.getJSONObject(index).getString("date");
                                String content = response.getJSONObject(index).getJSONObject("content").getString("rendered");
                                String link = response.getJSONObject(index).getString("link");
                                String status = response.getJSONObject(index).getString("status");
                                String authorAvatar = response.getJSONObject(index).getJSONObject("author_avatar_urls").getString("48");
                                Comment comment = new Comment(commentId, postId, parentId, authorId, authorName, authorUrl, date, content, link, status, authorAvatar);
                                newWaveOfcomments.add(comment);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        callback.onSuccess(newWaveOfcomments);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    public static void getSpamCommentFromUser(Context context, String domain, int perPage, int currentPage, CommentsCallback callback) {
        SessionManagement sessionManagement = new SessionManagement(context);
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/comments?per_page=" + perPage + "&page=" + currentPage + "&status=spam";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        int size = response.length();
                        List<Comment> newWaveOfcomments = new ArrayList<>();
                        IntStream.range(0, size).forEach(index -> {
                            try {
                                long commentId = response.getJSONObject(index).getLong("id");
                                long postId = response.getJSONObject(index).getLong("post");
                                long parentId = response.getJSONObject(index).getLong("parent");
                                long authorId = response.getJSONObject(index).getLong("author");
                                String authorName = response.getJSONObject(index).getString("author_name");
                                String authorUrl = response.getJSONObject(index).getString("author_url");
                                String date = response.getJSONObject(index).getString("date");
                                String content = response.getJSONObject(index).getJSONObject("content").getString("rendered");
                                String link = response.getJSONObject(index).getString("link");
                                String status = response.getJSONObject(index).getString("status");
                                String authorAvatar = response.getJSONObject(index).getJSONObject("author_avatar_urls").getString("48");
                                Comment comment = new Comment(commentId, postId, parentId, authorId, authorName, authorUrl, date, content, link, status, authorAvatar);
                                newWaveOfcomments.add(comment);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        callback.onSuccess(newWaveOfcomments);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sessionManagement.getAccessToken());
                return headers;
            }
        };;
        requestQueue.add(jsonArrayRequest);
    }

    public static void getPendingCommentFromUser(Context context, String domain, int perPage, int currentPage, CommentsCallback callback) {
        SessionManagement sessionManagement = new SessionManagement(context);
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/comments?per_page=" + perPage + "&page=" + currentPage + "&status=pending";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        int size = response.length();
                        List<Comment> newWaveOfcomments = new ArrayList<>();
                        IntStream.range(0, size).forEach(index -> {
                            try {
                                long commentId = response.getJSONObject(index).getLong("id");
                                long postId = response.getJSONObject(index).getLong("post");
                                long parentId = response.getJSONObject(index).getLong("parent");
                                long authorId = response.getJSONObject(index).getLong("author");
                                String authorName = response.getJSONObject(index).getString("author_name");
                                String authorUrl = response.getJSONObject(index).getString("author_url");
                                String date = response.getJSONObject(index).getString("date");
                                String content = response.getJSONObject(index).getJSONObject("content").getString("rendered");
                                String link = response.getJSONObject(index).getString("link");
                                String status = response.getJSONObject(index).getString("status");
                                String authorAvatar = response.getJSONObject(index).getJSONObject("author_avatar_urls").getString("48");
                                Comment comment = new Comment(commentId, postId, parentId, authorId, authorName, authorUrl, date, content, link, status, authorAvatar);
                                newWaveOfcomments.add(comment);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        callback.onSuccess(newWaveOfcomments);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sessionManagement.getAccessToken());
                return headers;
            }
        };;
        requestQueue.add(jsonArrayRequest);
    }

    public static void getTrashedCommentFromUser(Context context, String domain, int perPage, int currentPage, CommentsCallback callback) {
        SessionManagement sessionManagement = new SessionManagement(context);
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/comments?per_page=" + perPage + "&page=" + currentPage + "&status=trash";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        int size = response.length();
                        List<Comment> newWaveOfcomments = new ArrayList<>();
                        IntStream.range(0, size).forEach(index -> {
                            try {
                                long commentId = response.getJSONObject(index).getLong("id");
                                long postId = response.getJSONObject(index).getLong("post");
                                long parentId = response.getJSONObject(index).getLong("parent");
                                long authorId = response.getJSONObject(index).getLong("author");
                                String authorName = response.getJSONObject(index).getString("author_name");
                                String authorUrl = response.getJSONObject(index).getString("author_url");
                                String date = response.getJSONObject(index).getString("date");
                                String content = response.getJSONObject(index).getJSONObject("content").getString("rendered");
                                String link = response.getJSONObject(index).getString("link");
                                String status = response.getJSONObject(index).getString("status");
                                String authorAvatar = response.getJSONObject(index).getJSONObject("author_avatar_urls").getString("48");
                                Comment comment = new Comment(commentId, postId, parentId, authorId, authorName, authorUrl, date, content, link, status, authorAvatar);
                                newWaveOfcomments.add(comment);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        callback.onSuccess(newWaveOfcomments);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sessionManagement.getAccessToken());
                return headers;
            }
        };;
        requestQueue.add(jsonArrayRequest);
    }
    public static void getUnrepliedCommentFromUser(Context context, String domain, int perPage, int currentPage, CommentsCallback callback) {
        SessionManagement sessionManagement = new SessionManagement(context);
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/comments?per_page=" + perPage + "&page=" + currentPage + "&status=unreply";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        int size = response.length();
                        List<Comment> newWaveOfcomments = new ArrayList<>();
                        IntStream.range(0, size).forEach(index -> {
                            try {
                                long commentId = response.getJSONObject(index).getLong("id");
                                long postId = response.getJSONObject(index).getLong("post");
                                long parentId = response.getJSONObject(index).getLong("parent");
                                long authorId = response.getJSONObject(index).getLong("author");
                                String authorName = response.getJSONObject(index).getString("author_name");
                                String authorUrl = response.getJSONObject(index).getString("author_url");
                                String date = response.getJSONObject(index).getString("date");
                                String content = response.getJSONObject(index).getJSONObject("content").getString("rendered");
                                String link = response.getJSONObject(index).getString("link");
                                String status = response.getJSONObject(index).getString("status");
                                String authorAvatar = response.getJSONObject(index).getJSONObject("author_avatar_urls").getString("48");
                                Comment comment = new Comment(commentId, postId, parentId, authorId, authorName, authorUrl, date, content, link, status, authorAvatar);
                                newWaveOfcomments.add(comment);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        callback.onSuccess(newWaveOfcomments);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sessionManagement.getAccessToken());
                return headers;
            }
        };;
        requestQueue.add(jsonArrayRequest);
    }

    public static void updateCommentStatus(Context context, String domain, String status, String id) {
        SessionManagement sessionManagement = new SessionManagement(context);
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/comments/" + id;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        )
    }
}
