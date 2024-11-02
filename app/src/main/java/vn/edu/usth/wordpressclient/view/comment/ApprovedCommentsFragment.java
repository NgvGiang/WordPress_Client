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

import vn.edu.usth.wordpressclient.R;
import vn.edu.usth.wordpressclient.utils.DomainManager;
import vn.edu.usth.wordpressclient.utils.SessionManager;
import vn.edu.usth.wordpressclient.view.adapter.CommentAllAdapter;
import vn.edu.usth.wordpressclient.viewmodel.CommentViewModel;

public class ApprovedCommentsFragment extends Fragment {
    private RecyclerView recyclerView;
    private CommentViewModel commentViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_approved_comments, container, false);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.approved_swipe_refresh_layout);
        String accessToken = SessionManager.getInstance(getContext()).getAccessToken();
        String domain = DomainManager.getInstance().getSelectedDomain();

        recyclerView = view.findViewById(R.id.fragment_approved_comments_rec_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CommentAllAdapter adapter = new CommentAllAdapter(getContext());
        recyclerView.setAdapter(adapter);
        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);
        commentViewModel.getCommentModelsLiveData().observe(getViewLifecycleOwner(), commentModels -> {
            adapter.setCommentCardModels(commentModels);
            adapter.notifyDataSetChanged();
        });
        commentViewModel.getComments(accessToken, domain, "approve");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                commentViewModel.getComments(accessToken, domain, "approve");
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }
}