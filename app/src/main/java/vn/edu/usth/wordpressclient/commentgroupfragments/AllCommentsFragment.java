package vn.edu.usth.wordpressclient.commentgroupfragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import vn.edu.usth.wordpressclient.CommentRecyclerViewAdapter;
import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.models.Comment;
import vn.edu.usth.wordpressclient.models.CommentAPIServices;
import vn.edu.usth.wordpressclient.models.CommentsCallback;

public class AllCommentsFragment extends Fragment {
    String userDomain;
    List<Comment> comments;
    RecyclerView recyclerView;
    CommentRecyclerViewAdapter commentRecyclerViewAdapter;
    private static final int PER_PAGE = 5;
    private int currentPage = 1;
    private boolean isLastPage = false;
    private boolean isLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_comments, container, false);

        if (savedInstanceState == null) {
            comments = new ArrayList<>();
        } else {
            comments = savedInstanceState.getParcelableArrayList("comments");
        }
        recyclerView = view.findViewById(R.id.fragment_all_comments_rec_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager != null && linearLayoutManager.findLastVisibleItemPosition() == comments.size() - 1) {
                    if (!isLoading && !isLastPage) {
                        loadMoreComments();
                    }
                }
            }
        });
        commentRecyclerViewAdapter = new CommentRecyclerViewAdapter(comments, getContext());
        recyclerView.setAdapter(commentRecyclerViewAdapter);
        if (savedInstanceState != null) {
            comments = savedInstanceState.getParcelableArrayList("comments");
            currentPage = savedInstanceState.getInt("currentPage");
            isLastPage = savedInstanceState.getBoolean("isLastPage");
            commentRecyclerViewAdapter.notifyDataSetChanged();
        } else {
            // Load comments only if not restored
            loadInitialComments();
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save current state
//        if (isLastPage) {
//
//        }
        outState.putParcelableArrayList("comments", new ArrayList<>(comments));
        outState.putInt("currentPage", currentPage);
        outState.putBoolean("isLastPage", isLastPage);
    }

    private void loadMoreComments() {
        isLoading = true;
        CommentAPIServices.getAllCommentsFromUser(getContext(), "peppermint777.wordpress.com", PER_PAGE, currentPage, new CommentsCallback() {
            @Override
            public void onSuccess(List<Comment> newComments) {
                comments.addAll(newComments);
                commentRecyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
                if (newComments.size() < 5) {
                    isLastPage = true;
                } else {
                    currentPage++;
                }
            }

            @Override
            public void onError(String error) {
                isLoading = false;
            }
        });
    }

    private void loadInitialComments() {
        isLoading = true;
        CommentAPIServices.getAllCommentsFromUser(getContext(), "peppermint777.wordpress.com", PER_PAGE, currentPage, new CommentsCallback() {
            @Override
            public void onSuccess(List<Comment> newComments) {
                comments.addAll(newComments);
                commentRecyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
                if (newComments.size() < 5) {
                    isLastPage = true;
                } else {
                    currentPage++;
                }
            }

            @Override
            public void onError(String error) {
                isLoading = false;
            }
        });
    }
}