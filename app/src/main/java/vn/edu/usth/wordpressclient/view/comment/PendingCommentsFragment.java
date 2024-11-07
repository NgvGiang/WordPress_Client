package vn.edu.usth.wordpressclient.view.comment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.utils.DomainManager;
import vn.edu.usth.wordpressclient.utils.SessionManager;
import vn.edu.usth.wordpressclient.view.adapter.CommentAllAdapter;
import vn.edu.usth.wordpressclient.viewmodel.CommentViewModel;

public class PendingCommentsFragment extends Fragment {
    private RecyclerView recyclerView;
    private CommentViewModel commentViewModel;
    private CommentAllAdapter adapter;
    private RelativeLayout noPendingComment;
    private String accessToken;
    private String domain;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_comments, container, false);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.pending_swipe_refresh_layout);
        accessToken = SessionManager.getInstance(getContext()).getAccessToken();
        domain = DomainManager.getInstance().getSelectedDomain();
        noPendingComment = view.findViewById(R.id.no_pending_comment);

        commentViewModel = new ViewModelProvider(requireActivity()).get(CommentViewModel.class);
        recyclerView = view.findViewById(R.id.fragment_pending_comments_rec_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CommentAllAdapter(getContext());
//        adapter = new CommentAllAdapter(getContext(), commentViewModel, this);
        recyclerView.setAdapter(adapter);
        getComments();

        commentViewModel.getComments(accessToken, domain, "hold");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                commentViewModel.getComments(accessToken, domain, "hold");
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    public void getComments() {
        noPendingComment.setVisibility(View.INVISIBLE);
        commentViewModel.getPendingCommentModelsLiveData().observe(getViewLifecycleOwner(), commentModels -> {
            adapter.setCommentCardModels(commentModels);
            if (commentModels.isEmpty()) {
                noPendingComment.setVisibility(View.VISIBLE);
            } else {
                noPendingComment.setVisibility(View.INVISIBLE);
            }
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        commentViewModel.getComments(accessToken, domain, "hold");
        adapter.notifyDataSetChanged();
    }
}