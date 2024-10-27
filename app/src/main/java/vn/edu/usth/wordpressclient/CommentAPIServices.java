package vn.edu.usth.wordpressclient;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import vn.edu.usth.wordpressclient.models.Comment;
import vn.edu.usth.wordpressclient.models.CommentDetailCallback;
import vn.edu.usth.wordpressclient.models.GetCommentsCallback;
import vn.edu.usth.wordpressclient.models.QueueManager;

public class CommentAPIServices {
    public static void getAllCommentsFromUser(Context context, String domain, int perPage, int currentPage, List<Comment> commentList, GetCommentsCallback callback) {
        final int[] callsCompleted = {0};
        final int totalCalls = 2;
        Runnable checkCompletion = () -> {
            if (callsCompleted[0] == totalCalls) {
                callback.onSuccess(commentList);
            }
        };
        getApprovedCommentFromUser(context, domain, perPage, currentPage, new GetCommentsCallback() {
            @Override
            public void onSuccess(List<Comment> newComments) {
                commentList.addAll(newComments);
                callsCompleted[0]++;
                checkCompletion.run();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                callsCompleted[0]++;
                checkCompletion.run();
            }
        });

        getPendingCommentFromUser(context, domain, perPage, currentPage, new GetCommentsCallback() {
            @Override
            public void onSuccess(List<Comment> newComments) {
                commentList.addAll(newComments);
                callsCompleted[0]++;
                checkCompletion.run();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                callsCompleted[0]++;
                checkCompletion.run();
            }
        });
        callback.onSuccess(commentList);
    }

    public static void getApprovedCommentFromUser(Context context, String domain, int perPage, int currentPage, GetCommentsCallback callback) {
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/comments?per_page=" + perPage + "&page=" + currentPage + "&status=approve";
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
        QueueManager.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

    public static void getApprovedCommentFromMe(Context context, String domain, int perPage, int currentPage, Long myId, GetCommentsCallback callback) {
        SessionManager sessionManagement = SessionManager.getInstance(context);
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/comments?per_page=" + perPage + "&page=" + currentPage + "&status=approve" + "&author=" + myId;
        Log.i("my url", url);
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
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sessionManagement.getAccessToken());
                return headers;
            }
        };;
        QueueManager.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

    public static void getPendingCommentFromMe(Context context, String domain, int perPage, int currentPage, Long myId, GetCommentsCallback callback) {
        SessionManager sessionManagement = SessionManager.getInstance(context);
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/comments?per_page=" + perPage + "&page=" + currentPage + "&status=approve" + "&author=" + myId + "&status=hold";
        Log.i("my url", url);
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
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sessionManagement.getAccessToken());
                return headers;
            }
        };;
        QueueManager.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

    public static void getSpamCommentFromUser(Context context, String domain, int perPage, int currentPage, GetCommentsCallback callback) {
        SessionManager sessionManagement = SessionManager.getInstance(context);
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/comments?per_page=" + perPage + "&page=" + currentPage + "&status=spam";
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
        QueueManager.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

    public static void getPendingCommentFromUser(Context context, String domain, int perPage, int currentPage, GetCommentsCallback callback) {
        SessionManager sessionManagement = SessionManager.getInstance(context);
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/comments?per_page=" + perPage + "&page=" + currentPage + "&status=hold";
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
        QueueManager.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

    public static void getTrashedCommentFromUser(Context context, String domain, int perPage, int currentPage, GetCommentsCallback callback) {
        SessionManager sessionManagement = SessionManager.getInstance(context);
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/comments?per_page=" + perPage + "&page=" + currentPage + "&status=trash";
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
        QueueManager.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }
    public static void getApprovedCommentFromUser(Context context, String domain, int perPage, int currentPage, Long author, GetCommentsCallback callback) {
        SessionManager sessionManagement = SessionManager.getInstance(context);
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/comments?per_page=" + perPage + "&page=" + currentPage + "&author_exclude=" + author;
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
        };
        QueueManager.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

    public static void getPendingCommentFromUser(Context context, String domain, int perPage, int currentPage, Long author, GetCommentsCallback callback) {
        SessionManager sessionManagement = SessionManager.getInstance(context);
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/comments?per_page=" + perPage + "&page=" + currentPage + "&author_exclude=" + author + "&status=hold";
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
        };
        QueueManager.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

    public static void updateCommentStatus(Context context, String domain, Long id, String status, CommentDetailCallback callback) {
        SessionManager sessionManagement = SessionManager.getInstance(context);
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
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        callback.onSuccess();
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
        };
        QueueManager.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void replyComment(Context context, String domain, String content, Long parent, Long post, GetCommentsCallback callback) {
        SessionManager sessionManagement = SessionManager.getInstance(context);
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/comments/";
        Log.i("url", url);
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
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<Comment> comments = new ArrayList<>();
                        try {
                            long commentId = response.getLong("id");
                            long postId = response.getLong("post");
                            long parentId = response.getLong("parent");
                            long authorId = response.getLong("author");
                            String authorName = response.getString("author_name");
                            String authorUrl = response.getString("author_url");
                            String date = response.getString("date");
                            String content = response.getJSONObject("content").getString("rendered");
                            String link = response.getString("link");
                            String status = response.getString("status");
                            String authorAvatar = response.getJSONObject("author_avatar_urls").getString("48");
                            Comment comment = new Comment(commentId, postId, parentId, authorId, authorName, authorUrl, date, content, link, status, authorAvatar);
                            comments.add(comment);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        callback.onSuccess(comments);
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
        };
        QueueManager.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void deleteComment(Context context, String userdomain, Long commentId, GetCommentsCallback callback) {
        SessionManager sessionManagement = SessionManager.getInstance(context);
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + userdomain + "/comments/" + commentId +"?force=true";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

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
        };
        QueueManager.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void editComment(Context context, String domain, Long id, String content, CommentDetailCallback callback) {
        SessionManager sessionManagement = SessionManager.getInstance(context);
        String url = "https://public-api.wordpress.com/wp/v2/sites/" + domain + "/comments/" + id;
        Log.i("url", url);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("content", content);
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
                            Log.i("response", response.getString("content"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        callback.onSuccess();
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
        };
        QueueManager.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
