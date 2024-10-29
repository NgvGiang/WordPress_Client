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
import vn.edu.usth.wordpressclient.DomainManager;
import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.models.Comment;
import vn.edu.usth.wordpressclient.CommentAPIServices;
import vn.edu.usth.wordpressclient.models.GetCommentsCallback;

public class ApprovedCommentsFragment extends Fragment {
    String userDomain;
    List<Comment> comments;
    RecyclerView recyclerView;
    CommentRecyclerViewAdapter commentRecyclerViewAdapter;
    private static final int PER_PAGE = 20;
    private int currentPage = 1;
    private boolean isLastPageOfApprovedFragment = false;
    private boolean isLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_approved_comments, container, false);
        userDomain = DomainManager.getInstance().getSelectedDomain();

        if (savedInstanceState == null) {
            comments = new ArrayList<>();
        } else {
            comments = savedInstanceState.getParcelableArrayList("comments", Comment.class);
        }
        recyclerView = view.findViewById(R.id.fragment_approved_comments_rec_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager != null && linearLayoutManager.findLastVisibleItemPosition() == comments.size() - 1) {
                    if (!isLoading && !isLastPageOfApprovedFragment) {
                        loadMoreComments();
                    }
                }
            }
        });
        commentRecyclerViewAdapter = new CommentRecyclerViewAdapter(comments, getContext(), userDomain, this);
        recyclerView.setAdapter(commentRecyclerViewAdapter);
        if (savedInstanceState != null) {
            comments = savedInstanceState.getParcelableArrayList("comments", Comment.class);
            currentPage = savedInstanceState.getInt("currentPage");
            isLastPageOfApprovedFragment = savedInstanceState.getBoolean("isLastPage");
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
        outState.putBoolean("isLastPage", isLastPageOfApprovedFragment);
    }

    private void loadMoreComments() {
        isLoading = true;
        CommentAPIServices.getApprovedCommentFromUser(getContext(), userDomain, PER_PAGE, currentPage, new GetCommentsCallback() {
            @Override
            public void onSuccess(List<Comment> newComments) {
                comments.addAll(newComments);
                commentRecyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
                if (newComments.size() < 20) {
                    isLastPageOfApprovedFragment = true;
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
        CommentAPIServices.getApprovedCommentFromUser(getContext(), userDomain, PER_PAGE, currentPage, new GetCommentsCallback() {
            @Override
            public void onSuccess(List<Comment> newComments) {
                comments.addAll(newComments);
                commentRecyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
                if (newComments.size() < 20) {
                    isLastPageOfApprovedFragment = true;
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

    public void removeCommentAtPosition(int position) {
        Log.i("cmt size", "" + comments.size());
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

    public List<Comment> getComments() {
        return this.comments;
    }
}