package vn.edu.usth.wordpressclient.view.comment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.stream.IntStream;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.model.CommentCardModel;
import vn.edu.usth.wordpressclient.utils.DomainManager;
import vn.edu.usth.wordpressclient.utils.SessionManager;
import vn.edu.usth.wordpressclient.view.adapter.CommentAllAdapter;
import vn.edu.usth.wordpressclient.viewmodel.CommentViewModel;

public class AllCommentsFragment extends Fragment {
    private RecyclerView recyclerView;
    private CommentViewModel commentViewModel;
    private RelativeLayout noAllComment;
    private CommentAllAdapter adapter;
    private String accessToken;
    private String domain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_comments, container, false);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        String accessToken = SessionManager.getInstance(getContext()).getAccessToken();
        String domain = DomainManager.getInstance().getSelectedDomain();
        noAllComment = view.findViewById(R.id.no_all_comment);

        commentViewModel = new ViewModelProvider(requireActivity()).get(CommentViewModel.class);
        recyclerView = view.findViewById(R.id.fragment_all_comments_rec_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CommentAllAdapter(getContext());
//        adapter = new CommentAllAdapter(getContext(), commentViewModel, this);
        recyclerView.setAdapter(adapter);
        getComments();

        commentViewModel.getComments(accessToken, domain, "all");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                commentViewModel.getComments(accessToken, domain,"all");
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    public void getComments() {
        noAllComment.setVisibility(View.INVISIBLE);
        commentViewModel.getAllCommentModelsLiveData().observe(getViewLifecycleOwner(), commentModels -> {
            adapter.setCommentCardModels(commentModels);
            if (commentModels.isEmpty()) {
                noAllComment.setVisibility(View.VISIBLE);
            } else {
                noAllComment.setVisibility(View.INVISIBLE);
            }
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        commentViewModel.getComments(accessToken, domain,"all");
        adapter.notifyDataSetChanged();
    }
}