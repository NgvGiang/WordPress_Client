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

public class ApprovedCommentsFragment extends Fragment {
    private RecyclerView recyclerView;
    private CommentViewModel commentViewModel;
    private CommentAllAdapter adapter;
    private RelativeLayout noApprovedComment;
    private String accessToken;
    private String domain;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_approved_comments, container, false);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.approved_swipe_refresh_layout);
        accessToken = SessionManager.getInstance(getContext()).getAccessToken();
        domain = DomainManager.getInstance().getSelectedDomain();
        noApprovedComment = view.findViewById(R.id.no_approved_comment);

        recyclerView = view.findViewById(R.id.fragment_approved_comments_rec_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CommentAllAdapter(getContext());
        recyclerView.setAdapter(adapter);
        commentViewModel = new ViewModelProvider(requireActivity()).get(CommentViewModel.class);
        getComments();

        commentViewModel.getComments("approve");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                commentViewModel.getComments("approve");
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    public void getComments() {
        commentViewModel.getApprovedCommentModelsLiveData().observe(getViewLifecycleOwner(), commentModels -> {
            adapter.setCommentCardModels(commentModels);
            if (commentModels.isEmpty()) {
                noApprovedComment.setVisibility(View.VISIBLE);
            } else {
                noApprovedComment.setVisibility(View.INVISIBLE);
            }
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        commentViewModel.getComments("approve");
        adapter.notifyDataSetChanged();
    }
}