package vn.edu.usth.wordpressclient.commentgroupfragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.models.Comment;

public class AllCommentsFragment extends Fragment {
    private String userDomain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_comments, container, false);

//        String url = "https://public-api.wordpress.com/wp/v2/sites/" + userDomain + "/comments";
        String url = "https://public-api.wordpress.com/wp/v2/sites/peppermint777.wordpress.com/comments";

        final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                List<Comment> comments = (List<Comment>) msg.obj;
//                comments.stream().forEach(comment -> {
//                        Toast.makeText(getActivity(), comment.getContent(), Toast.LENGTH_SHORT).show();
//                });
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int size = response.length();
                                List<Comment> comments = new ArrayList<>();
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
                                        comments.add(comment);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                                Message msg = handler.obtainMessage();
                                msg.obj = comments;
                                handler.sendMessage(msg);
                            }
                        }).start();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
        return view;
    }
}