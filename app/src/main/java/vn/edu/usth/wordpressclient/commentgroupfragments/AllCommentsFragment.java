package vn.edu.usth.wordpressclient.commentgroupfragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import vn.edu.usth.wordpressclient.CommentRecyclerViewAdapter;
import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.models.Comment;
import vn.edu.usth.wordpressclient.CommentAPIServices;
import vn.edu.usth.wordpressclient.models.GetCommentsCallback;

public class AllCommentsFragment extends Fragment {
    String userDomain;
    List<Comment> comments;
    RecyclerView recyclerView;
    CommentRecyclerViewAdapter commentRecyclerViewAdapter;
    private static final int PER_PAGE = 100;
    private int currentPage = 1;
    private boolean isLastPageOfAllFragment = false;
    private boolean isLoading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            userDomain = getArguments().getString("domain");
        }
    }

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
                    Log.i("outer if", "outer if");
                    Log.i("isLoading", String.valueOf(isLoading));
                    Log.i("isLastPage", String.valueOf(isLastPageOfAllFragment));

                    if (!isLoading && !isLastPageOfAllFragment) {
                        Log.i("inner if", "inner if");
                        loadMoreComments();
                    }
                }
            }
        });
        commentRecyclerViewAdapter = new CommentRecyclerViewAdapter(comments, getContext(), userDomain, this);
        recyclerView.setAdapter(commentRecyclerViewAdapter);
        if (savedInstanceState != null) {
            comments = savedInstanceState.getParcelableArrayList("comments");
            currentPage = savedInstanceState.getInt("currentPage");
            isLastPageOfAllFragment = savedInstanceState.getBoolean("isLastPage");
            commentRecyclerViewAdapter.notifyDataSetChanged();
        } else {
            loadInitialComments();
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("comments", new ArrayList<>(comments));
        outState.putInt("currentPage", currentPage);
        outState.putBoolean("isLastPage", isLastPageOfAllFragment);
    }

    private void loadMoreComments() {
        isLoading = true;
        List<Comment> commentList = new ArrayList<>();
        CommentAPIServices.getAllCommentsFromUser(getContext(), userDomain, PER_PAGE, currentPage, commentList, new GetCommentsCallback() {
            @Override
            public void onSuccess(List<Comment> newComments) {
                comments.addAll(newComments);
                comments.sort(((o1, o2) -> {
                    LocalDateTime d1 = LocalDateTime.parse(o1.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                    LocalDateTime d2 = LocalDateTime.parse(o2.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                    return d2.compareTo(d1);
                }));
                commentRecyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
                if (newComments.size() < 100) {
                    isLastPageOfAllFragment = true;
                } else {
                    currentPage++;
                    isLastPageOfAllFragment = false;
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
        List<Comment> commentList = new ArrayList<>();
        CommentAPIServices.getAllCommentsFromUser(getContext(), userDomain, PER_PAGE, currentPage, commentList, new GetCommentsCallback() {
            @Override
            public void onSuccess(List<Comment> newComments) {
                Log.i("cmt list size", "" + newComments.size());
                comments.addAll(newComments);
                comments.sort(((o1, o2) -> {
                    LocalDateTime d1 = LocalDateTime.parse(o1.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                    LocalDateTime d2 = LocalDateTime.parse(o2.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                    return d2.compareTo(d1);
                }));
                commentRecyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
                if (newComments.size() < 100) {
                    Log.i("check last page", "last page confirm!");
                    isLastPageOfAllFragment = true;
                } else {
                    Log.i("check", "checked");
                    currentPage++;
                    isLastPageOfAllFragment = false;
                }
            }

            @Override
            public void onError(String error) {
                isLoading = false;
            }
        });
    }

    public List<Comment> getComments() {
        return this.comments;
    }

    public void removeCommentAtPosition(int position) {
        comments.remove(position);
        commentRecyclerViewAdapter.notifyItemRemoved(position);

    }

    public void removeCommentBaseOnId(long id) {
        int index = IntStream.range(0, comments.size()).filter(i -> comments.get(i).getId() == id).findFirst().getAsInt();
        comments.remove(index);
        commentRecyclerViewAdapter.notifyItemRemoved(index);
    }

    public void addComment(Comment comment) {
        comment.setStatus("approved");
        comments.add(comment);
        comments.sort(((o1, o2) -> {
            LocalDateTime d1 = LocalDateTime.parse(o1.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            LocalDateTime d2 = LocalDateTime.parse(o2.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            return d2.compareTo(d1);
        }));
        commentRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void changeStatus(Comment comment, int position) {
        comments.set(position, comment);
        commentRecyclerViewAdapter.notifyItemChanged(position);
    }
}